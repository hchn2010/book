package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.Book
import com.example.data.model.BookQuote
import com.example.data.model.ReadingSession
import com.example.data.repository.BookRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BookRepository

    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow("全部")
    val selectedStatusTab = MutableStateFlow(0) // 0: 全部, 1: 在读, 2: 想读, 3: 已读

    // All Books & Filtered Books
    private val rawBooks: StateFlow<List<Book>>
    val allBooks: StateFlow<List<Book>> get() = rawBooks
    val filteredBooks: StateFlow<List<Book>>

    val currentlyReadingBooks: StateFlow<List<Book>>
    val wantReadBooks: StateFlow<List<Book>>
    val readBooks: StateFlow<List<Book>>
    val quotes: StateFlow<List<BookQuote>>
    val totalReadingMinutes: StateFlow<Int>
    val readingSessions: StateFlow<List<ReadingSession>>

    // Selected Book Detail
    val selectedBook = MutableStateFlow<Book?>(null)

    // Zen Timer state
    val isTimerRunning = MutableStateFlow(false)
    val timerElapsedSeconds = MutableStateFlow(0)
    val activeTimerBook = MutableStateFlow<Book?>(null)
    private var timerJob: Job? = null

    // Ambient Reader Settings
    val readerPaperMode = MutableStateFlow("Parchment") // "White", "Parchment", "ZenGreen", "Night"
    val readerFontSizeSp = MutableStateFlow(18)

    init {
        val db = AppDatabase.getDatabase(application)
        repository = BookRepository(db.bookDao(), db.quoteDao(), db.readingSessionDao())

        // Seed initial data if DB empty
        viewModelScope.launch {
            repository.checkAndSeedInitialData()
        }

        rawBooks = repository.allBooks.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        currentlyReadingBooks = repository.readingBooks.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        wantReadBooks = repository.wantReadBooks.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        readBooks = repository.readBooks.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        quotes = repository.allQuotes.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        readingSessions = repository.allReadingSessions.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        totalReadingMinutes = repository.totalReadingMinutes
            .map { it ?: 0 }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

        // Combined Filter
        filteredBooks = combine(
            rawBooks,
            searchQuery,
            selectedCategory,
            selectedStatusTab
        ) { books, query, category, statusTab ->
            books.filter { book ->
                val matchesQuery = query.isBlank() ||
                        book.title.contains(query, ignoreCase = true) ||
                        book.author.contains(query, ignoreCase = true) ||
                        book.category.contains(query, ignoreCase = true)

                val matchesCategory = category == "全部" || book.category == category

                val matchesStatus = when (statusTab) {
                    1 -> book.status == "在读"
                    2 -> book.status == "想读"
                    3 -> book.status == "已读"
                    else -> true
                }

                matchesQuery && matchesCategory && matchesStatus
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    // Actions
    fun onSearchQueryChange(newQuery: String) {
        searchQuery.value = newQuery
    }

    fun onCategorySelect(category: String) {
        selectedCategory.value = category
    }

    fun onStatusTabSelect(tabIndex: Int) {
        selectedStatusTab.value = tabIndex
    }

    fun selectBook(book: Book?) {
        selectedBook.value = book
    }

    fun updateReadingProgress(bookId: Int, newPage: Int) {
        viewModelScope.launch {
            repository.updateProgress(bookId, newPage)
            selectedBook.value?.let { current ->
                if (current.id == bookId) {
                    selectedBook.value = current.copy(currentPage = newPage, lastReadTimestamp = System.currentTimeMillis())
                }
            }
        }
    }

    fun saveBook(book: Book) {
        viewModelScope.launch {
            if (book.id == 0) {
                repository.insertBook(book)
            } else {
                repository.updateBook(book)
            }
        }
    }

    fun toggleFavorite(book: Book) {
        viewModelScope.launch {
            val updated = book.copy(isFavorite = !book.isFavorite)
            repository.updateBook(updated)
            if (selectedBook.value?.id == book.id) {
                selectedBook.value = updated
            }
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            repository.deleteBook(book)
            if (selectedBook.value?.id == book.id) {
                selectedBook.value = null
            }
        }
    }

    fun addQuote(bookTitle: String, quoteText: String, pageNumber: Int, note: String, themeTag: String) {
        viewModelScope.launch {
            val bookId = rawBooks.value.find { it.title == bookTitle }?.id ?: 0
            val quote = BookQuote(
                bookId = bookId,
                bookTitle = bookTitle,
                quoteText = quoteText,
                pageNumber = pageNumber,
                note = note,
                themeTag = themeTag
            )
            repository.insertQuote(quote)
        }
    }

    fun deleteQuote(quote: BookQuote) {
        viewModelScope.launch {
            repository.deleteQuote(quote)
        }
    }

    // Zen Timer logic
    fun startTimer(book: Book?) {
        activeTimerBook.value = book
        isTimerRunning.value = true
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isTimerRunning.value) {
                delay(1000)
                timerElapsedSeconds.value += 1
            }
        }
    }

    fun pauseTimer() {
        isTimerRunning.value = false
        timerJob?.cancel()
    }

    fun stopAndSaveTimer() {
        pauseTimer()
        val minutes = (timerElapsedSeconds.value / 60).coerceAtLeast(1)
        val book = activeTimerBook.value
        val bookId = book?.id ?: (rawBooks.value.firstOrNull()?.id ?: 1)
        val bookTitle = book?.title ?: (rawBooks.value.firstOrNull()?.title ?: "静心随手记")

        viewModelScope.launch {
            repository.recordReadingSession(bookId, bookTitle, minutes)
            timerElapsedSeconds.value = 0
            activeTimerBook.value = null
        }
    }

    fun setPaperMode(mode: String) {
        readerPaperMode.value = mode
    }

    fun changeFontSize(delta: Int) {
        readerFontSizeSp.value = (readerFontSizeSp.value + delta).coerceIn(14, 28)
    }
}
