package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotes")
data class BookQuote(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookTitle: String,
    val quoteText: String,
    val pageNumber: Int = 1,
    val note: String = "",
    val themeTag: String = "经典",
    val timestamp: Long = System.currentTimeMillis()
)
