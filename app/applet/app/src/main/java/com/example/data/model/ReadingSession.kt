package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reading_sessions")
data class ReadingSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookTitle: String,
    val durationMinutes: Int,
    val dateTimestamp: Long = System.currentTimeMillis()
)
