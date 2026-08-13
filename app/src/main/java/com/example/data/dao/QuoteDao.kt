package com.example.data.dao

import androidx.room.*
import com.example.data.model.BookQuote
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {
    @Query("SELECT * FROM book_quotes ORDER BY timestamp DESC")
    fun getAllQuotes(): Flow<List<BookQuote>>

    @Query("SELECT * FROM book_quotes WHERE bookId = :bookId ORDER BY pageNumber ASC, timestamp DESC")
    fun getQuotesForBook(bookId: Int): Flow<List<BookQuote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: BookQuote): Long

    @Delete
    suspend fun deleteQuote(quote: BookQuote)
}
