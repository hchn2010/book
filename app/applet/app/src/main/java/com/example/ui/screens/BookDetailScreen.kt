package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Book

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    book: Book,
    onBack: () -> Unit,
    onFavoriteToggle: (Book) -> Unit,
    onDeleteBook: (Book) -> Unit,
    onStartZenRead: (Book) -> Unit,
    onUpdateProgressClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("藏书详情", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("btn_detail_back")) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onFavoriteToggle(book) }) {
                        Icon(
                            imageVector = if (book.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (book.isFavorite) Color(0xFFFFB4AB) else Color(0xFFC4C6CF)
                        )
                    }
                    IconButton(onClick = { onDeleteBook(book) }, modifier = Modifier.testTag("btn_delete_book")) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFFFB4AB))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A1C1E),
                    titleContentColor = Color(0xFFE2E2E6)
                )
            )
        },
        containerColor = Color(0xFF1A1C1E)
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(140.dp)
                    .height(190.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF43474E))
            ) {
                AsyncImage(
                    model = book.coverUrl,
                    contentDescription = book.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = book.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE2E2E6)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${book.author} · ${book.category}",
                fontSize = 14.sp,
                color = Color(0xFFC4C6CF)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2E3033)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("状态", fontSize = 12.sp, color = Color(0xFFC4C6CF))
                        Text(book.status, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD0E4FF))
                    }
                    Divider(modifier = Modifier.height(30.dp).width(1.dp), color = Color(0xFF43474E))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("进度", fontSize = 12.sp, color = Color(0xFFC4C6CF))
                        Text("${book.currentPage}/${book.totalPages}页", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE2E2E6))
                    }
                    Divider(modifier = Modifier.height(30.dp).width(1.dp), color = Color(0xFF43474E))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("完成度", fontSize = 12.sp, color = Color(0xFFC4C6CF))
                        Text("${book.progressPercentage}%", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD6BEE4))
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onUpdateProgressClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43474E)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("打卡进度", fontSize = 13.sp)
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

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "书籍简介与导读",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE2E2E6)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = book.description.ifBlank { "暂无简介信息..." },
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = Color(0xFFC4C6CF)
                )
            }
        }
    }
}
