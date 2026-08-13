package com.example.data.repository

import com.example.data.dao.BookDao
import com.example.data.dao.QuoteDao
import com.example.data.dao.ReadingSessionDao
import com.example.data.model.Book
import com.example.data.model.BookQuote
import com.example.data.model.ReadingSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class BookRepository(
    private val bookDao: BookDao,
    private val quoteDao: QuoteDao,
    private val readingSessionDao: ReadingSessionDao
) {
    val allBooks: Flow<List<Book>> = bookDao.getAllBooks()
    val readingBooks: Flow<List<Book>> = bookDao.getBooksByStatus("在读")
    val wantReadBooks: Flow<List<Book>> = bookDao.getBooksByStatus("想读")
    val readBooks: Flow<List<Book>> = bookDao.getBooksByStatus("已读")
    val allQuotes: Flow<List<BookQuote>> = quoteDao.getAllQuotes()
    val allReadingSessions: Flow<List<ReadingSession>> = readingSessionDao.getAllSessions()
    val totalReadingMinutes: Flow<Int?> = readingSessionDao.getTotalReadingMinutes()

    fun getBookById(id: Int): Flow<Book?> = bookDao.getBookById(id)

    fun searchBooks(query: String): Flow<List<Book>> = bookDao.searchBooks(query)

    suspend fun insertBook(book: Book): Long = withContext(Dispatchers.IO) {
        bookDao.insertBook(book)
    }

    suspend fun updateBook(book: Book) = withContext(Dispatchers.IO) {
        bookDao.updateBook(book)
    }

    suspend fun updateProgress(bookId: Int, currentPage: Int) = withContext(Dispatchers.IO) {
        bookDao.updateReadingProgress(bookId, currentPage)
    }

    suspend fun deleteBook(book: Book) = withContext(Dispatchers.IO) {
        bookDao.deleteBook(book)
    }

    suspend fun insertQuote(quote: BookQuote) = withContext(Dispatchers.IO) {
        quoteDao.insertQuote(quote)
    }

    suspend fun deleteQuote(quote: BookQuote) = withContext(Dispatchers.IO) {
        quoteDao.deleteQuote(quote)
    }

    suspend fun recordReadingSession(bookId: Int, bookTitle: String, durationMinutes: Int) = withContext(Dispatchers.IO) {
        readingSessionDao.insertSession(
            ReadingSession(
                bookId = bookId,
                bookTitle = bookTitle,
                durationMinutes = durationMinutes
            )
        )
        // Also update last read timestamp for the book
        val currentBook = bookDao.getBookByIdSync(bookId)
        if (currentBook != null) {
            bookDao.updateBook(currentBook.copy(lastReadTimestamp = System.currentTimeMillis()))
        }
    }

    suspend fun checkAndSeedInitialData() = withContext(Dispatchers.IO) {
        if (bookDao.getBookCount() == 0) {
            val initialBooks = listOf(
                Book(
                    title = "瓦尔登湖",
                    author = "亨利·戴维·梭罗",
                    coverUrl = "https://images.unsplash.com/photo-1448375240586-882707db888b?auto=format&fit=crop&w=600&q=80",
                    category = "随笔",
                    status = "在读",
                    currentPage = 142,
                    totalPages = 320,
                    rating = 4.9f,
                    description = "一本关于宁静、自立与自然沉思的经典名著，记录梭罗在瓦尔登湖畔独居两年的生活与深邃思考。",
                    tags = "自然,哲学,经典",
                    isFavorite = true
                ),
                Book(
                    title = "百年孤独",
                    author = "加西亚·马尔克斯",
                    coverUrl = "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?auto=format&fit=crop&w=600&q=80",
                    category = "文学",
                    status = "在读",
                    currentPage = 85,
                    totalPages = 400,
                    rating = 4.9f,
                    description = "魔幻现实主义文学神作，描写布恩迪亚家族七代人的传奇故事以及马孔多镇的百年兴衰。",
                    tags = "魔幻现实主义,诺贝尔奖,文学",
                    isFavorite = true
                ),
                Book(
                    title = "明朝那些事儿",
                    author = "当年明月",
                    coverUrl = "https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&w=600&q=80",
                    category = "历史",
                    status = "已读",
                    currentPage = 520,
                    totalPages = 520,
                    rating = 4.8f,
                    description = "用通俗生动的语言讲述明朝三百年历史，全景式展示帝王将相、英雄豪杰与平民百姓的人生浮沉。",
                    tags = "历史,通俗,长篇",
                    isFavorite = false
                ),
                Book(
                    title = "三体",
                    author = "刘慈欣",
                    coverUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&w=600&q=80",
                    category = "科幻",
                    status = "在读",
                    currentPage = 210,
                    totalPages = 302,
                    rating = 4.9f,
                    description = "雨果奖获奖作品，展现人类文明与三体文明跨越光年的宏大碰撞与宇宙社会学图景。",
                    tags = "科幻,雨果奖,硬核",
                    isFavorite = true
                ),
                Book(
                    title = "月亮与六便士",
                    author = "毛姆",
                    coverUrl = "https://images.unsplash.com/photo-1532012197267-da84d127e765?auto=format&fit=crop&w=600&q=80",
                    category = "文学",
                    status = "想读",
                    currentPage = 0,
                    totalPages = 280,
                    rating = 4.7f,
                    description = "关于理想与现实、艺术与世俗选择的深刻探讨，展现主人公为了艺术追求抛弃一切的狂热。",
                    tags = "经典,艺术,人性",
                    isFavorite = false
                ),
                Book(
                    title = "人类简史",
                    author = "尤瓦尔·赫拉利",
                    coverUrl = "https://images.unsplash.com/photo-1461360370896-922624d12aa1?auto=format&fit=crop&w=600&q=80",
                    category = "历史",
                    status = "想读",
                    currentPage = 0,
                    totalPages = 440,
                    rating = 4.8f,
                    description = "从认知革命、农业革命到科学革命，颠覆性重构智人漫长发展历程的巨著。",
                    tags = "科普,历史,宏观",
                    isFavorite = false
                )
            )
            bookDao.insertBooks(initialBooks)

            // Seed quotes
            quoteDao.insertQuote(
                BookQuote(
                    bookTitle = "瓦尔登湖",
                    quoteText = "我到森林里去，是因为我希望有意义地生活，只面对生活的基本事实，看看我是否能学到生活要教给我的东西。",
                    pageNumber = 35,
                    note = "梭罗对于独居与内省的深刻感悟，让人洗涤心灵。"
                )
            )
            quoteDao.insertQuote(
                BookQuote(
                    bookTitle = "百年孤独",
                    quoteText = "生命中真正重要的不是你遇到了什么，而是你记住了什么，以及你是如何铭记的。",
                    pageNumber = 120,
                    note = "马尔克斯关于记忆与时间的隽永之句。"
                )
            )

            // Seed session
            readingSessionDao.insertSession(
                ReadingSession(
                    bookId = 1,
                    bookTitle = "瓦尔登湖",
                    durationMinutes = 45
                )
            )
            readingSessionDao.insertSession(
                ReadingSession(
                    bookId = 2,
                    bookTitle = "百年孤独",
                    durationMinutes = 30
                )
            )
        }
    }
}
