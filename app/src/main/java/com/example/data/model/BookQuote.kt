package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_quotes")
data class BookQuote(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val bookId: Int = 0,
    val bookTitle: String,
    val quoteText: String,
    val pageNumber: Int = 0,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val themeTag: String = "经典"
)
