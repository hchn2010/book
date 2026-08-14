package com.example.data.local

import androidx.room.*
import com.example.data.model.BookQuote
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {
    @Query("SELECT * FROM quotes ORDER BY timestamp DESC")
    fun getAllQuotes(): Flow<List<BookQuote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: BookQuote): Long

    @Delete
    suspend fun deleteQuote(quote: BookQuote)
}
