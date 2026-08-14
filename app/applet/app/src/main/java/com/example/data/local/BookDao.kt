package com.example.data.local

import androidx.room.*
import com.example.data.model.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM books ORDER BY addedTimestamp DESC")
    fun getAllBooks(): Flow<List<Book>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book): Long

    @Update
    suspend fun updateBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    @Query("UPDATE books SET currentPage = :currentPage, status = CASE WHEN :currentPage >= totalPages THEN '已读' ELSE status END WHERE id = :bookId")
    suspend fun updateProgress(bookId: Long, currentPage: Int)
}
