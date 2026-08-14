package com.example.data.repository

import com.example.data.local.BookDao
import com.example.data.local.QuoteDao
import com.example.data.local.ReadingSessionDao
import com.example.data.model.Book
import com.example.data.model.BookQuote
import com.example.data.model.ReadingSession
import kotlinx.coroutines.flow.Flow

class BookRepository(
    private val bookDao: BookDao,
    private val quoteDao: QuoteDao,
    private val readingSessionDao: ReadingSessionDao
) {
    val allBooks: Flow<List<Book>> = bookDao.getAllBooks()
    val allQuotes: Flow<List<BookQuote>> = quoteDao.getAllQuotes()
    val allSessions: Flow<List<ReadingSession>> = readingSessionDao.getAllSessions()
    val totalReadingMinutes: Flow<Int?> = readingSessionDao.getTotalReadingMinutes()

    suspend fun insertBook(book: Book) = bookDao.insertBook(book)
    suspend fun updateBook(book: Book) = bookDao.updateBook(book)
    suspend fun deleteBook(book: Book) = bookDao.deleteBook(book)
    suspend fun updateProgress(bookId: Long, currentPage: Int) = bookDao.updateProgress(bookId, currentPage)

    suspend fun insertQuote(quote: BookQuote) = quoteDao.insertQuote(quote)
    suspend fun deleteQuote(quote: BookQuote) = quoteDao.deleteQuote(quote)

    suspend fun insertSession(session: ReadingSession) = readingSessionDao.insertSession(session)
}
