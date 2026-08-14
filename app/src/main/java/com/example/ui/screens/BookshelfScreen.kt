package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Book
import com.example.ui.MainViewModel
import com.example.ui.components.GridBookItem
import com.example.ui.components.HeroCurrentlyReadingCard
import com.example.ui.components.CategoryFilterChips
import com.example.ui.components.InkSearchBar
import com.example.ui.theme.InkPrimary
import com.example.ui.theme.PaperBeige
import com.example.ui.theme.SageSecondary

@Composable
fun BookshelfScreen(
    viewModel: MainViewModel,
    onBookSelect: (Book) -> Unit,
    onOpenAddBookDialog: () -> Unit,
    onOpenProgressDialog: (Book) -> Unit,
    onStartZenRead: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedStatusTab by viewModel.selectedStatusTab.collectAsState()
    val books by viewModel.filteredBooks.collectAsState()
    val currentlyReadingBooks by viewModel.currentlyReadingBooks.collectAsState()

    var isGridView by remember { mutableStateOf(true) }

    val categories = listOf("全部", "文学", "历史", "哲学", "科幻", "随笔", "艺术")
    val statusTabs = listOf("全部藏书", "在读", "想读", "已读")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // App Brand Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "墨香书影",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = InkPrimary,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "墨香留痕 · 沉浸阅读",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        letterSpacing = 1.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(PaperBeige)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "${books.size} 藏本",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = SageSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar & Categories
            InkSearchBar(
                query = searchQuery,
                onQueryChange = { viewModel.onSearchQueryChange(it) }
            )
            CategoryFilterChips(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { viewModel.onCategorySelect(it) }
            )

            // Status Tabs
            TabRow(
                selectedTabIndex = selectedStatusTab,
                containerColor = Color.Transparent,
                contentColor = SageSecondary,
                divider = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .testTag("status_tab_row")
            ) {
                statusTabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedStatusTab == index,
                        onClick = { viewModel.onStatusTabSelect(index) },
                        text = {
                            Text(
                                text = title,
                                fontSize = 13.sp,
                                fontWeight = if (selectedStatusTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Scrollable Content
            LazyVerticalGrid(
                columns = GridCells.Fixed(if (isGridView) 2 else 1),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 88.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("book_list_grid")
            ) {
                // Hero Currently Reading Card (only when showing all/reading and has currently reading book)
                if (currentlyReadingBooks.isNotEmpty() && (selectedStatusTab == 0 || selectedStatusTab == 1) && searchQuery.isBlank() && selectedCategory == "全部") {
                    val heroBook = currentlyReadingBooks.first()
                    item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(maxLineSpan) }) {
                        Column {
                            HeroCurrentlyReadingCard(
                                book = heroBook,
                                onCardClick = { onBookSelect(heroBook) },
                                onUpdateProgressClick = { onOpenProgressDialog(heroBook) },
                                onStartZenReadClick = { onStartZenRead(heroBook) }
                            )
                            Spacer(modifier = Modifier.height(20.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "藏书阁",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = InkPrimary,
                                    fontFamily = FontFamily.Serif
                                )

                                IconButton(
                                    onClick = { isGridView = !isGridView },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = "Toggle layout",
                                        tint = SageSecondary
                                    )
                                }
                            }
                        }
                    }
                }

                if (books.isEmpty()) {
                    item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(maxLineSpan) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "暂无符合条件的书卷",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                } else {
                    items(books, key = { it.id }) { book ->
                        GridBookItem(
                            book = book,
                            onBookClick = { onBookSelect(book) },
                            onToggleFavorite = { viewModel.toggleFavorite(book) }
                        )
                    }
                }
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = onOpenAddBookDialog,
            containerColor = Color(0xFFD0E4FF),
            contentColor = Color(0xFF003258),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .testTag("fab_add_book")
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add book")
        }
    }
}
