package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.FormatQuote
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Book
import com.example.ui.MainViewModel
import com.example.ui.components.AddBookDialog
import com.example.ui.components.AddQuoteDialog
import com.example.ui.components.UpdateProgressDialog
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

data class NavTabItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

@Composable
fun MainAppScreen(viewModel: MainViewModel) {
    val books by viewModel.filteredBooks.collectAsState()
    val allBooks by viewModel.allBooks.collectAsState()
    val quotes by viewModel.quotes.collectAsState()
    val sessions by viewModel.readingSessions.collectAsState()
    val totalReadingMinutes by viewModel.totalReadingMinutes.collectAsState()

    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedStatusTab by viewModel.selectedStatusTab.collectAsState()
    val selectedBook by viewModel.selectedBook.collectAsState()

    val isTimerRunning by viewModel.isTimerRunning.collectAsState()
    val timerElapsedSeconds by viewModel.timerElapsedSeconds.collectAsState()
    val activeTimerBook by viewModel.activeTimerBook.collectAsState()
    val readerPaperMode by viewModel.readerPaperMode.collectAsState()
    val readerFontSizeSp by viewModel.readerFontSizeSp.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    var isZenReaderOpen by remember { mutableStateOf(false) }

    var showAddBookDialog by remember { mutableStateOf(false) }
    var showAddQuoteDialog by remember { mutableStateOf(false) }
    var bookToUpdateProgress by remember { mutableStateOf<Book?>(null) }

    val navItems = listOf(
        NavTabItem("书房", Icons.Filled.MenuBook, Icons.Outlined.MenuBook, "tab_bookshelf"),
        NavTabItem("金句", Icons.Filled.FormatQuote, Icons.Outlined.FormatQuote, "tab_quotes"),
        NavTabItem("里程碑", Icons.Filled.AutoGraph, Icons.Outlined.AutoGraph, "tab_stats")
    )

    Scaffold(
        bottomBar = {
            if (selectedBook == null && !isZenReaderOpen) {
                NavigationBar(
                    containerColor = Color(0xFF1A1C1E),
                    contentColor = Color(0xFFE2E2E6),
                    tonalElevation = 0.dp
                ) {
                    navItems.forEachIndexed { index, tab ->
                        val isSelected = selectedTab == index
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { selectedTab = index },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                    contentDescription = tab.title,
                                    tint = if (isSelected) Color(0xFF003258) else Color(0xFFC4C6CF)
                                )
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color(0xFFD0E4FF) else Color(0xFFC4C6CF)
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color(0xFFD0E4FF)
                            ),
                            modifier = Modifier.testTag(tab.testTag)
                        )
                    }
                }
            }
        },
        containerColor = Color(0xFF1A1C1E)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isZenReaderOpen -> {
                    ZenReaderScreen(
                        book = activeTimerBook ?: selectedBook,
                        isTimerRunning = isTimerRunning,
                        timerElapsedSeconds = timerElapsedSeconds,
                        readerPaperMode = readerPaperMode,
                        readerFontSizeSp = readerFontSizeSp,
                        onStartTimer = { viewModel.startTimer(activeTimerBook ?: selectedBook) },
                        onPauseTimer = { viewModel.pauseTimer() },
                        onStopAndSaveTimer = { viewModel.stopAndSaveTimer() },
                        onSetPaperMode = { viewModel.setPaperMode(it) },
                        onChangeFontSize = { viewModel.changeFontSize(it) },
                        onBack = { isZenReaderOpen = false }
                    )
                }
                selectedBook != null -> {
                    BookDetailScreen(
                        book = selectedBook!!,
                        onBack = { viewModel.selectBook(null) },
                        onFavoriteToggle = { viewModel.toggleFavorite(it) },
                        onDeleteBook = { viewModel.deleteBook(it) },
                        onStartZenRead = {
                            viewModel.startTimer(it)
                            isZenReaderOpen = true
                        },
                        onUpdateProgressClick = { bookToUpdateProgress = selectedBook }
                    )
                }
                else -> {
                    when (selectedTab) {
                        0 -> BookshelfScreen(
                            books = books,
                            searchQuery = searchQuery,
                            onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
                            selectedCategory = selectedCategory,
                            onCategorySelected = { viewModel.onCategorySelect(it) },
                            selectedStatusTab = selectedStatusTab,
                            onStatusTabSelected = { viewModel.onStatusTabSelect(it) },
                            onBookClick = { viewModel.selectBook(it) },
                            onFavoriteClick = { viewModel.toggleFavorite(it) },
                            onOpenAddBookDialog = { showAddBookDialog = true },
                            onStartZenRead = {
                                viewModel.startTimer(it)
                                isZenReaderOpen = true
                            },
                            onUpdateProgressClick = { bookToUpdateProgress = it }
                        )
                        1 -> QuotesScreen(
                            quotes = quotes,
                            onOpenAddQuoteDialog = { showAddQuoteDialog = true },
                            onDeleteQuote = { viewModel.deleteQuote(it) }
                        )
                        2 -> StatsScreen(
                            books = allBooks,
                            totalMinutes = totalReadingMinutes,
                            sessions = sessions
                        )
                    }
                }
            }
        }
    }

    if (showAddBookDialog) {
        AddBookDialog(
            onDismiss = { showAddBookDialog = false },
            onConfirm = { newBook ->
                viewModel.saveBook(newBook)
                showAddBookDialog = false
            }
        )
    }

    if (showAddQuoteDialog) {
        AddQuoteDialog(
            books = allBooks,
            onDismiss = { showAddQuoteDialog = false },
            onSave = { bookTitle, quoteText, page, note, tag ->
                viewModel.addQuote(bookTitle, quoteText, page, note, tag)
                showAddQuoteDialog = false
            }
        )
    }

    bookToUpdateProgress?.let { book ->
        UpdateProgressDialog(
            book = book,
            onDismiss = { bookToUpdateProgress = null },
            onConfirm = { newPage ->
                viewModel.updateReadingProgress(book.id, newPage)
                bookToUpdateProgress = null
            }
        )
    }
}
