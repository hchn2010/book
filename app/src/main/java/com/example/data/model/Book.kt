package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val author: String,
    val coverUrl: String,
    val category: String,
    val status: String, // "在读", "想读", "已读"
    val currentPage: Int = 0,
    val totalPages: Int = 300,
    val rating: Float = 4.8f,
    val description: String = "",
    val lastReadTimestamp: Long = System.currentTimeMillis(),
    val tags: String = "",
    val isFavorite: Boolean = false
) {
    val progressPercentage: Int
        get() = if (totalPages > 0) ((currentPage.toFloat() / totalPages.toFloat()) * 100).toInt().coerceIn(0, 100) else 0
}
