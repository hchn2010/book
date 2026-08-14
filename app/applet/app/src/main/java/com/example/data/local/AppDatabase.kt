package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.Book
import com.example.data.model.BookQuote
import com.example.data.model.ReadingSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                )
                .addCallback(DatabaseCallback())
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database)
                    }
                }
            }

            suspend fun populateInitialData(db: AppDatabase) {
                val bookDao = db.bookDao()
                val quoteDao = db.quoteDao()
                val sessionDao = db.readingSessionDao()

                val b1 = Book(
                    title = "瓦尔登湖",
                    author = "亨利·戴维·梭罗",
                    category = "随笔",
                    coverUrl = "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?auto=format&fit=crop&w=600&q=80",
                    status = "在读",
                    totalPages = 320,
                    currentPage = 142,
                    rating = 4.9f,
                    description = "梭罗在瓦尔登湖畔独居两年的思考与记录。文字宁静深刻，带人脱离喧嚣，寻觅生命的本质与内心的平和。",
                    isFavorite = true
                )

                val b2 = Book(
                    title = "百年孤独",
                    author = "加西亚·马尔克斯",
                    category = "文学",
                    coverUrl = "https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&w=600&q=80",
                    status = "已读",
                    totalPages = 400,
                    currentPage = 400,
                    rating = 5.0f,
                    description = "魔幻现实主义文学神作，讲述布恩迪亚家族七代人的传奇兴衰与深刻孤独。",
                    isFavorite = true
                )

                val b3 = Book(
                    title = "三体",
                    author = "刘慈欣",
                    category = "科幻",
                    coverUrl = "https://images.unsplash.com/photo-1532012197267-da84d127e765?auto=format&fit=crop&w=600&q=80",
                    status = "在读",
                    totalPages = 302,
                    currentPage = 88,
                    rating = 4.8f,
                    description = "宏大壮丽的科幻史诗，探讨文明接触、宇宙社会学与人类命运。",
                    isFavorite = false
                )

                val b4 = Book(
                    title = "艺术的故事",
                    author = "恩斯特·贡布里希",
                    category = "艺术",
                    coverUrl = "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?auto=format&fit=crop&w=600&q=80",
                    status = "想读",
                    totalPages = 688,
                    currentPage = 0,
                    rating = 4.9f,
                    description = "经典艺术史入门巨著，语言生动流畅，全景展现人类艺术的崇高演变。",
                    isFavorite = false
                )

                bookDao.insertBook(b1)
                bookDao.insertBook(b2)
                bookDao.insertBook(b3)
                bookDao.insertBook(b4)

                quoteDao.insertQuote(
                    BookQuote(
                        bookTitle = "瓦尔登湖",
                        quoteText = "我到森林里去，是因为我希望有意义地生活，只面对生活的基本事实，看看我是否能学到生活要教给我的东西。",
                        pageNumber = 12,
                        note = "清晨读到此处，心中油然而生一份坦然与澄澈。",
                        themeTag = "哲思"
                    )
                )

                quoteDao.insertQuote(
                    BookQuote(
                        bookTitle = "百年孤独",
                        quoteText = "生命中真正重要的不是你遇到了什么，而是你记住了什么，以及你是如何铭记的。",
                        pageNumber = 380,
                        note = "岁月的沉淀全在于心中的记忆。",
                        themeTag = "感悟"
                    )
                )

                sessionDao.insertSession(
                    ReadingSession(bookTitle = "瓦尔登湖", durationMinutes = 45)
                )
                sessionDao.insertSession(
                    ReadingSession(bookTitle = "三体", durationMinutes = 30)
                )
            }
        }
    }
}
