package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.components.QuoteCard
import com.example.ui.theme.InkPrimary
import com.example.ui.theme.PaperBeige
import com.example.ui.theme.SageSecondary

@Composable
fun QuotesScreen(
    viewModel: MainViewModel,
    onOpenAddQuoteDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val allQuotes by viewModel.quotes.collectAsState()
    var selectedTag by remember { mutableStateOf("全部") }

    val tags = listOf("全部", "经典", "感悟", "名言", "诗意", "哲思")

    val filteredQuotes = remember(allQuotes, selectedTag) {
        if (selectedTag == "全部") allQuotes
        else allQuotes.filter { it.themeTag == selectedTag }
    }

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

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "墨香书摘",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = InkPrimary,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "辑录灵感 · 记录文字之美",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        letterSpacing = 1.sp
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
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("辑录", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tags filter
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tags.forEach { tag ->
                    val isSelected = tag == selectedTag
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                if (isSelected) Color(0xFFD0E4FF) else Color(0xFF2E3033)
                            )
                            .clickable { selectedTag = tag }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .testTag("tag_filter_$tag")
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

            Spacer(modifier = Modifier.height(12.dp))

            // Quotes list
            if (filteredQuotes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.FormatQuote,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "尚无相关书摘，点击右上方“辑录”添加",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 88.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredQuotes, key = { it.id }) { quote ->
                        QuoteCard(
                            quote = quote,
                            onDeleteQuote = { viewModel.deleteQuote(quote) }
                        )
                    }
                }
            }
        }
    }
}
