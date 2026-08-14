package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Book
import com.example.ui.components.BookCard
import com.example.ui.components.InkSearchBar

@Composable
fun BookshelfScreen(
    books: List<Book>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    selectedStatusTab: Int,
    onStatusTabSelected: (Int) -> Unit,
    onBookClick: (Book) -> Unit,
    onFavoriteClick: (Book) -> Unit,
    onOpenAddBookDialog: () -> Unit,
    onStartZenRead: (Book) -> Unit,
    onUpdateProgressClick: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("全部", "文学", "科幻", "哲理", "随笔", "艺术", "历史", "传记")
    val statusTabs = listOf("全部", "在读", "想读", "已读")

    Box(modifier = modifier.fillMaxSize().background(Color(0xFF1A1C1E))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Screen Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = "Bookshelf",
                        tint = Color(0xFFD0E4FF),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "墨香藏书房",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE2E2E6)
                    )
                }

                Surface(
                    color = Color(0xFF2E3033),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "共 ${books.size} 册",
                        fontSize = 12.sp,
                        color = Color(0xFFD0E4FF),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar & Filter Chips
            InkSearchBar(
                query = searchQuery,
                onQueryChange = onSearchQueryChange,
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = onCategorySelected
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Status Filter Tabs
            TabRow(
                selectedTabIndex = selectedStatusTab,
                containerColor = Color.Transparent,
                contentColor = Color(0xFFD0E4FF),
                divider = {}
            ) {
                statusTabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedStatusTab == index,
                        onClick = { onStatusTabSelected(index) },
                        text = {
                            Text(
                                text = title,
                                fontSize = 14.sp,
                                fontWeight = if (selectedStatusTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedStatusTab == index) Color(0xFFD0E4FF) else Color(0xFFC4C6CF)
                            )
                        },
                        modifier = Modifier.testTag("tab_status_$index")
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (books.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "书房静谧，尚无相关藏书",
                            fontSize = 15.sp,
                            color = Color(0xFFC4C6CF)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "点击下方按钮开启第一本书的阅读之旅",
                            fontSize = 12.sp,
                            color = Color(0xFFC4C6CF).copy(alpha = 0.7f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 80.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(books, key = { it.id }) { book ->
                        BookCard(
                            book = book,
                            onBookClick = onBookClick,
                            onFavoriteClick = onFavoriteClick,
                            onStartZenReadClick = { onStartZenRead(book) },
                            onUpdateProgressClick = { onUpdateProgressClick(book) }
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
                .padding(20.dp)
                .testTag("btn_add_book_fab")
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Book")
        }
    }
}
