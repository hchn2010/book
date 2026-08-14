package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val author: String,
    val category: String,
    val coverUrl: String,
    val status: String,
    val totalPages: Int = 300,
    val currentPage: Int = 0,
    val rating: Float = 4.8f,
    val description: String = "",
    val isFavorite: Boolean = false,
    val addedTimestamp: Long = System.currentTimeMillis()
) {
    val progressPercentage: Int
        get() = if (totalPages > 0) ((currentPage.toFloat() / totalPages.toFloat()) * 100).toInt().coerceIn(0, 100) else 0
}
