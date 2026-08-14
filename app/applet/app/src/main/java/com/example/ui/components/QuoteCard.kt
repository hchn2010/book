package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BookQuote

@Composable
fun QuoteCard(
    quote: BookQuote,
    onDeleteClick: (BookQuote) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("quote_card_${quote.id}"),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2E3033)
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FormatQuote,
                        contentDescription = "Quote",
                        tint = Color(0xFFD0E4FF),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF43474E))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = quote.themeTag,
                            fontSize = 11.sp,
                            color = Color(0xFFD0E4FF),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                IconButton(
                    onClick = { onDeleteClick(quote) },
                    modifier = Modifier.size(32.dp).testTag("btn_delete_quote_${quote.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFFC4C6CF),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "“${quote.quoteText}”",
                fontSize = 15.sp,
                fontStyle = FontStyle.Italic,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFFE2E2E6)
            )

            if (quote.note.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1A1C1E))
                        .padding(10.dp)
                ) {
                    Text(
                        text = "想法：${quote.note}",
                        fontSize = 12.sp,
                        color = Color(0xFFC4C6CF)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "—— 出自《${quote.bookTitle}》",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD0E4FF)
                )

                Text(
                    text = "第 ${quote.pageNumber} 页",
                    fontSize = 12.sp,
                    color = Color(0xFFC4C6CF)
                )
            }
        }
    }
}
