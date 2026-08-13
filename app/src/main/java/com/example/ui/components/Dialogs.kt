package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Book
import com.example.ui.theme.PaperBeige
import com.example.ui.theme.SageSecondary

@Composable
fun AddBookDialog(
    onDismiss: () -> Unit,
    onSave: (Book) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("文学") }
    var coverUrl by remember { mutableStateOf("") }
    var totalPagesStr by remember { mutableStateOf("300") }
    var status by remember { mutableStateOf("在读") }
    var description by remember { mutableStateOf("") }

    val categories = listOf("文学", "历史", "哲学", "科幻", "随笔", "艺术")
    val statuses = listOf("在读", "想读", "已读")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "添增藏书",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("书名") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_book_title")
                )

                OutlinedTextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("作者") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_book_author")
                )

                Text("书籍分类", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat, fontSize = 12.sp) }
                        )
                    }
                }

                Text("阅读状态", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    statuses.forEach { st ->
                        FilterChip(
                            selected = status == st,
                            onClick = { status = st },
                            label = { Text(st, fontSize = 12.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = coverUrl,
                    onValueChange = { coverUrl = it },
                    label = { Text("封面图片网址 (网络热链接)") },
                    placeholder = { Text("https://images.unsplash.com/...") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_cover_url")
                )

                OutlinedTextField(
                    value = totalPagesStr,
                    onValueChange = { totalPagesStr = it },
                    label = { Text("总页数") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_total_pages")
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("书籍简介 / 推荐语") },
                    maxLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_description")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && author.isNotBlank()) {
                        val totalPages = totalPagesStr.toIntOrNull() ?: 300
                        val defaultCover = if (coverUrl.isBlank()) {
                            "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?auto=format&fit=crop&w=600&q=80"
                        } else coverUrl

                        onSave(
                            Book(
                                title = title,
                                author = author,
                                category = category,
                                coverUrl = defaultCover,
                                status = status,
                                totalPages = totalPages,
                                currentPage = if (status == "已读") totalPages else 0,
                                description = description
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD0E4FF),
                    contentColor = Color(0xFF003258)
                ),
                modifier = Modifier.testTag("btn_confirm_add_book")
            ) {
                Text("存入书房", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        },
        containerColor = PaperBeige,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun AddQuoteDialog(
    books: List<Book>,
    onDismiss: () -> Unit,
    onSave: (bookTitle: String, quoteText: String, pageNumber: Int, note: String, themeTag: String) -> Unit
) {
    var selectedBookTitle by remember { mutableStateOf(books.firstOrNull()?.title ?: "瓦尔登湖") }
    var quoteText by remember { mutableStateOf("") }
    var pageNumStr by remember { mutableStateOf("1") }
    var note by remember { mutableStateOf("") }
    var themeTag by remember { mutableStateOf("经典") }

    val tags = listOf("经典", "感悟", "名言", "诗意", "哲思")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("记录书摘随笔", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("出处书籍：", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Column {
                    books.take(4).forEach { book ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedBookTitle = book.title }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = selectedBookTitle == book.title,
                                onClick = { selectedBookTitle = book.title }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(book.title, fontSize = 14.sp)
                        }
                    }
                }

                OutlinedTextField(
                    value = quoteText,
                    onValueChange = { quoteText = it },
                    label = { Text("书摘摘录文字 *") },
                    maxLines = 4,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_quote_text")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = pageNumStr,
                        onValueChange = { pageNumStr = it },
                        label = { Text("页码 (P.)") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_quote_page")
                    )
                }

                Text("标签主题", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    tags.forEach { tag ->
                        FilterChip(
                            selected = themeTag == tag,
                            onClick = { themeTag = tag },
                            label = { Text(tag, fontSize = 12.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("个人随笔心境 (可选)") },
                    maxLines = 2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_quote_note")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (quoteText.isNotBlank()) {
                        val page = pageNumStr.toIntOrNull() ?: 1
                        onSave(selectedBookTitle, quoteText, page, note, themeTag)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD0E4FF),
                    contentColor = Color(0xFF003258)
                ),
                modifier = Modifier.testTag("btn_confirm_add_quote")
            ) {
                Text("保存书摘", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        },
        containerColor = PaperBeige,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun UpdateProgressDialog(
    book: Book,
    onDismiss: () -> Unit,
    onConfirm: (newPage: Int) -> Unit
) {
    var currentPage by remember { mutableFloatStateOf(book.currentPage.toFloat()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("更新阅读进度", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "《${book.title}》",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    "第 ${currentPage.toInt()} 页 / 共 ${book.totalPages} 页 (${((currentPage / book.totalPages) * 100).toInt()}%)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = SageSecondary
                )

                Slider(
                    value = currentPage,
                    onValueChange = { currentPage = it },
                    valueRange = 0f..book.totalPages.toFloat(),
                    colors = SliderDefaults.colors(
                        thumbColor = SageSecondary,
                        activeTrackColor = SageSecondary
                    ),
                    modifier = Modifier.testTag("slider_progress_update")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(currentPage.toInt()) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD0E4FF),
                    contentColor = Color(0xFF003258)
                ),
                modifier = Modifier.testTag("btn_save_progress")
            ) {
                Text("更新", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        },
        containerColor = PaperBeige,
        shape = RoundedCornerShape(20.dp)
    )
}
