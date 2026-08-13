package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.BookDao
import com.example.data.dao.QuoteDao
import com.example.data.dao.ReadingSessionDao
import com.example.data.model.Book
import com.example.data.model.BookQuote
import com.example.data.model.ReadingSession

@Database(
    entities = [Book::class, BookQuote::class, ReadingSession::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun quoteDao(): QuoteDao
    abstract fun readingSessionDao(): ReadingSessionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ink_fragrance_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
