package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BookQuote
import com.example.ui.components.QuoteCard

@Composable
fun QuotesScreen(
    quotes: List<BookQuote>,
    onOpenAddQuoteDialog: () -> Unit,
    onDeleteQuote: (BookQuote) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTag by remember { mutableStateOf("全部") }
    val tags = listOf("全部", "哲思", "经典", "诗意", "感悟", "生活")

    val filteredQuotes = if (selectedTag == "全部") {
        quotes
    } else {
        quotes.filter { it.themeTag == selectedTag }
    }

    Box(modifier = modifier.fillMaxSize().background(Color(0xFF1A1C1E))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FormatQuote,
                        contentDescription = "Quotes",
                        tint = Color(0xFFD0E4FF),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "佳句卡片盒",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE2E2E6)
                    )
                }

                Button(
                    onClick = onOpenAddQuoteDialog,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD0E4FF),
                        contentColor = Color(0xFF003258)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("btn_add_quote")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("辑录", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tag filter bar
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(tags) { tag ->
                    val isSelected = tag == selectedTag
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                if (isSelected) Color(0xFFD0E4FF) else Color(0xFF2E3033)
                            )
                            .clickable { selectedTag = tag }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .testTag("quote_tag_$tag")
                    ) {
                        Text(
                            text = tag,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color(0xFF003258) else Color(0xFFE2E2E6)
                        )
                    }
                }
            }

            if (filteredQuotes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "暂无相关佳句笔记",
                            fontSize = 15.sp,
                            color = Color(0xFFC4C6CF)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "点击右上角「辑录」随时记录触动心灵的文字",
                            fontSize = 12.sp,
                            color = Color(0xFFC4C6CF).copy(alpha = 0.7f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 30.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(filteredQuotes, key = { it.id }) { quote ->
                        QuoteCard(
                            quote = quote,
                            onDeleteClick = onDeleteQuote
                        )
                    }
                }
            }
        }
    }
}
