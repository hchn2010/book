package com.example.data.dao

import androidx.room.*
import com.example.data.model.ReadingSession
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingSessionDao {
    @Query("SELECT * FROM reading_sessions ORDER BY dateTimestamp DESC")
    fun getAllSessions(): Flow<List<ReadingSession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: ReadingSession): Long

    @Query("SELECT SUM(durationMinutes) FROM reading_sessions")
    fun getTotalReadingMinutes(): Flow<Int?>
}
