package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
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

    val allBooks: StateFlow<List<Book>>
    val quotes: StateFlow<List<BookQuote>>
    val readingSessions: StateFlow<List<ReadingSession>>
    val totalReadingMinutes: StateFlow<Int>

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("全部")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _selectedStatusTab = MutableStateFlow(0)
    val selectedStatusTab: StateFlow<Int> = _selectedStatusTab.asStateFlow()

    private val _selectedBook = MutableStateFlow<Book?>(null)
    val selectedBook: StateFlow<Book?> = _selectedBook.asStateFlow()

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    private val _timerElapsedSeconds = MutableStateFlow(0)
    val timerElapsedSeconds: StateFlow<Int> = _timerElapsedSeconds.asStateFlow()

    private val _activeTimerBook = MutableStateFlow<Book?>(null)
    val activeTimerBook: StateFlow<Book?> = _activeTimerBook.asStateFlow()

    private val _readerPaperMode = MutableStateFlow("Night")
    val readerPaperMode: StateFlow<String> = _readerPaperMode.asStateFlow()

    private val _readerFontSizeSp = MutableStateFlow(16)
    val readerFontSizeSp: StateFlow<Int> = _readerFontSizeSp.asStateFlow()

    private var timerJob: Job? = null

    init {
        val db = AppDatabase.getDatabase(application)
        repository = BookRepository(db.bookDao(), db.quoteDao(), db.readingSessionDao())

        allBooks = repository.allBooks.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        quotes = repository.allQuotes.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        readingSessions = repository.allSessions.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        totalReadingMinutes = repository.totalReadingMinutes.map { it ?: 0 }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )
    }

    val filteredBooks: StateFlow<List<Book>> = combine(
        allBooks, searchQuery, selectedCategory, selectedStatusTab
    ) { books, query, category, statusIndex ->
        books.filter { book ->
            val matchesQuery = query.isBlank() || book.title.contains(query, ignoreCase = true) || book.author.contains(query, ignoreCase = true)
            val matchesCategory = category == "全部" || book.category == category
            val matchesStatus = when (statusIndex) {
                1 -> book.status == "在读"
                2 -> book.status == "想读"
                3 -> book.status == "已读"
                else -> true
            }
            matchesQuery && matchesCategory && matchesStatus
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentlyReadingBooks: StateFlow<List<Book>> = allBooks.map { books ->
        books.filter { it.status == "在读" }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val readBooks: StateFlow<List<Book>> = allBooks.map { books ->
        books.filter { it.status == "已读" }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wantReadBooks: StateFlow<List<Book>> = allBooks.map { books ->
        books.filter { it.status == "想读" }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(query: String) { _searchQuery.value = query }
    fun onCategorySelect(category: String) { _selectedCategory.value = category }
    fun onStatusTabSelect(tabIndex: Int) { _selectedStatusTab.value = tabIndex }
    fun selectBook(book: Book?) { _selectedBook.value = book }

    fun saveBook(book: Book) {
        viewModelScope.launch { repository.insertBook(book) }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            if (_selectedBook.value?.id == book.id) _selectedBook.value = null
            repository.deleteBook(book)
        }
    }

    fun toggleFavorite(book: Book) {
        viewModelScope.launch {
            val updated = book.copy(isFavorite = !book.isFavorite)
            repository.updateBook(updated)
            if (_selectedBook.value?.id == book.id) {
                _selectedBook.value = updated
            }
        }
    }

    fun updateReadingProgress(bookId: Long, newPage: Int) {
        viewModelScope.launch {
            repository.updateProgress(bookId, newPage)
            _selectedBook.value?.let { current ->
                if (current.id == bookId) {
                    val newStatus = if (newPage >= current.totalPages) "已读" else current.status
                    _selectedBook.value = current.copy(currentPage = newPage, status = newStatus)
                }
            }
        }
    }

    fun addQuote(bookTitle: String, quoteText: String, pageNumber: Int, note: String, themeTag: String) {
        viewModelScope.launch {
            repository.insertQuote(BookQuote(bookTitle = bookTitle, quoteText = quoteText, pageNumber = pageNumber, note = note, themeTag = themeTag))
        }
    }

    fun deleteQuote(quote: BookQuote) {
        viewModelScope.launch { repository.deleteQuote(quote) }
    }

    fun startTimer(book: Book?) {
        _activeTimerBook.value = book
        _isTimerRunning.value = true
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_isTimerRunning.value) {
                delay(1000)
                _timerElapsedSeconds.value += 1
            }
        }
    }

    fun pauseTimer() {
        _isTimerRunning.value = false
        timerJob?.cancel()
    }

    fun stopAndSaveTimer() {
        pauseTimer()
        val elapsedMinutes = (_timerElapsedSeconds.value / 60).coerceAtLeast(1)
        val title = _activeTimerBook.value?.title ?: "自由读书"
        viewModelScope.launch {
            repository.insertSession(ReadingSession(bookTitle = title, durationMinutes = elapsedMinutes))
            _timerElapsedSeconds.value = 0
            _activeTimerBook.value = null
        }
    }

    fun setPaperMode(mode: String) { _readerPaperMode.value = mode }
    fun changeFontSize(delta: Int) { _readerFontSizeSp.value = (_readerFontSizeSp.value + delta).coerceIn(12, 28) }
}
