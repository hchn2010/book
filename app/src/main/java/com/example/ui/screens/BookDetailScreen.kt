package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.Book
import com.example.ui.MainViewModel
import com.example.ui.components.QuoteCard
import com.example.ui.theme.InkPrimary
import com.example.ui.theme.PaperBeige
import com.example.ui.theme.SageSecondary
import com.example.ui.theme.SageSecondaryContainer

@Composable
fun BookDetailScreen(
    book: Book,
    viewModel: MainViewModel,
    onBackClick: () -> Unit,
    onOpenProgressDialog: (Book) -> Unit,
    onStartZenRead: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    val quotes by viewModel.quotes.collectAsState()
    val bookQuotes = remember(quotes, book.title) {
        quotes.filter { it.bookTitle == book.title }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.testTag("btn_back_detail")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = "藏书详情",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
            )

            Row {
                IconButton(onClick = { viewModel.toggleFavorite(book) }) {
                    Icon(
                        imageVector = if (book.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Favorite",
                        tint = if (book.isFavorite) SageSecondary else MaterialTheme.colorScheme.onSurface
                    )
                }
                IconButton(onClick = {
                    viewModel.deleteBook(book)
                    onBackClick()
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        // Scrollable Body
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Book Cover & Primary Info Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .width(110.dp)
                        .height(160.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(book.coverUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = book.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = book.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = InkPrimary,
                        fontFamily = FontFamily.Serif
                    )

                    Text(
                        text = book.author,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SageSecondary)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(book.category, fontSize = 11.sp, color = Color.White)
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(PaperBeige)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(book.status, fontSize = 11.sp, color = SageSecondary, fontWeight = FontWeight.Medium)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFF39C12),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${book.rating} 评分",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Reading Progress Card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = PaperBeige,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "阅读进度",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = InkPrimary
                        )
                        Text(
                            text = "${book.progressPercentage}%",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = SageSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { book.currentPage.toFloat() / book.totalPages.coerceAtLeast(1) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = SageSecondary,
                        trackColor = Color.White
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "已读第 ${book.currentPage} 页 / 共 ${book.totalPages} 页",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { onOpenProgressDialog(book) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.MenuBook,
                                contentDescription = null,
                                tint = InkPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("更新页数", fontSize = 13.sp, color = InkPrimary)
                        }

                        Button(
                            onClick = { onStartZenRead(book) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFD0E4FF),
                                contentColor = Color(0xFF003258)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoStories,
                                contentDescription = null,
                                tint = Color(0xFF003258),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("沉浸阅读", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF003258))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Book Description
            Text(
                text = "内容简评与简介",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = InkPrimary,
                fontFamily = FontFamily.Serif
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (book.description.isNotBlank()) book.description else "暂无该书详细简介。",
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Book Quotes
            Text(
                text = "本书摘录 (${bookQuotes.size})",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = InkPrimary,
                fontFamily = FontFamily.Serif
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (bookQuotes.isEmpty()) {
                Text(
                    text = "尚未为此书记录书摘，可在“书摘”页面辑录。",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    bookQuotes.forEach { quote ->
                        QuoteCard(
                            quote = quote,
                            onDeleteQuote = { viewModel.deleteQuote(quote) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
