package com.example.data.dao

import androidx.room.*
import com.example.data.model.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM books ORDER BY lastReadTimestamp DESC")
    fun getAllBooks(): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE status = :status ORDER BY lastReadTimestamp DESC")
    fun getBooksByStatus(status: String): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE id = :id")
    fun getBookById(id: Int): Flow<Book?>

    @Query("SELECT * FROM books WHERE id = :id")
    suspend fun getBookByIdSync(id: Int): Book?

    @Query("SELECT * FROM books WHERE title LIKE '%' || :query || '%' OR author LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%' ORDER BY lastReadTimestamp DESC")
    fun searchBooks(query: String): Flow<List<Book>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<Book>)

    @Update
    suspend fun updateBook(book: Book)

    @Query("UPDATE books SET currentPage = :currentPage, lastReadTimestamp = :timestamp WHERE id = :id")
    suspend fun updateReadingProgress(id: Int, currentPage: Int, timestamp: Long = System.currentTimeMillis())

    @Delete
    suspend fun deleteBook(book: Book)

    @Query("SELECT COUNT(*) FROM books")
    suspend fun getBookCount(): Int
}
