package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.FormatQuote
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.Book
import com.example.ui.MainViewModel
import com.example.ui.components.AddBookDialog
import com.example.ui.components.AddQuoteDialog
import com.example.ui.components.UpdateProgressDialog
import com.example.ui.screens.BookDetailScreen
import com.example.ui.screens.BookshelfScreen
import com.example.ui.screens.QuotesScreen
import com.example.ui.screens.StatsScreen
import com.example.ui.screens.ZenReaderScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PaperBeige
import com.example.ui.theme.SageSecondary

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                InkFragranceApp()
            }
        }
    }
}

@Composable
fun InkFragranceApp(mainViewModel: MainViewModel = viewModel()) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val selectedBook by mainViewModel.selectedBook.collectAsState()
    val rawBooks by mainViewModel.filteredBooks.collectAsState()

    var showAddBookDialog by remember { mutableStateOf(false) }
    var showAddQuoteDialog by remember { mutableStateOf(false) }
    var progressDialogBook by remember { mutableStateOf<Book?>(null) }
    var initialZenBook by remember { mutableStateOf<Book?>(null) }

    val navItems = listOf(
        NavTab("书房", Icons.Filled.MenuBook, Icons.Outlined.MenuBook, "nav_bookshelf"),
        NavTab("书摘", Icons.Filled.FormatQuote, Icons.Outlined.FormatQuote, "nav_quotes"),
        NavTab("静心", Icons.Filled.AutoStories, Icons.Outlined.AutoStories, "nav_zen"),
        NavTab("统计", Icons.Filled.Analytics, Icons.Outlined.Analytics, "nav_stats")
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (selectedBook == null) {
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
                                    fontWeight = if (isSelected) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal,
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
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (selectedBook != null) {
                BookDetailScreen(
                    book = selectedBook!!,
                    viewModel = mainViewModel,
                    onBackClick = { mainViewModel.selectBook(null) },
                    onOpenProgressDialog = { progressDialogBook = it },
                    onStartZenRead = { book ->
                        initialZenBook = book
                        mainViewModel.selectBook(null)
                        selectedTab = 2 // Switch to Zen Reader tab
                    }
                )
            } else {
                when (selectedTab) {
                    0 -> BookshelfScreen(
                        viewModel = mainViewModel,
                        onBookSelect = { mainViewModel.selectBook(it) },
                        onOpenAddBookDialog = { showAddBookDialog = true },
                        onOpenProgressDialog = { progressDialogBook = it },
                        onStartZenRead = { book ->
                            initialZenBook = book
                            selectedTab = 2
                        }
                    )
                    1 -> QuotesScreen(
                        viewModel = mainViewModel,
                        onOpenAddQuoteDialog = { showAddQuoteDialog = true }
                    )
                    2 -> ZenReaderScreen(
                        viewModel = mainViewModel,
                        initialBook = initialZenBook
                    )
                    3 -> StatsScreen(
                        viewModel = mainViewModel
                    )
                }
            }
        }

        // Dialogs
        if (showAddBookDialog) {
            AddBookDialog(
                onDismiss = { showAddBookDialog = false },
                onSave = { newBook ->
                    mainViewModel.saveBook(newBook)
                    showAddBookDialog = false
                }
            )
        }

        if (showAddQuoteDialog) {
            AddQuoteDialog(
                books = rawBooks,
                onDismiss = { showAddQuoteDialog = false },
                onSave = { bookTitle, quoteText, pageNumber, note, themeTag ->
                    mainViewModel.addQuote(bookTitle, quoteText, pageNumber, note, themeTag)
                    showAddQuoteDialog = false
                }
            )
        }

        progressDialogBook?.let { book ->
            UpdateProgressDialog(
                book = book,
                onDismiss = { progressDialogBook = null },
                onConfirm = { newPage ->
                    mainViewModel.updateReadingProgress(book.id, newPage)
                    progressDialogBook = null
                }
            )
        }
    }
}

private data class NavTab(
    val title: String,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val testTag: String
)
