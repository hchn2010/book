package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Book

@Composable
fun AddBookDialog(
    onDismiss: () -> Unit,
    onConfirm: (Book) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("文学") }
    var totalPages by remember { mutableStateOf("300") }
    var status by remember { mutableStateOf("在读") }
    var description by remember { mutableStateOf("") }

    val categories = listOf("文学", "科幻", "哲理", "随笔", "艺术", "历史", "传记")
    val statuses = listOf("在读", "想读", "已读")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("录入新书", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("书名") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_book_title")
                )

                OutlinedTextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("作者") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_book_author")
                )

                OutlinedTextField(
                    value = totalPages,
                    onValueChange = { totalPages = it },
                    label = { Text("总页数") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_book_pages")
                )

                Text("分类选定：", fontSize = 13.sp, color = Color(0xFFC4C6CF))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    categories.take(4).forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat, fontSize = 12.sp) }
                        )
                    }
                }

                Text("阅读状态：", fontSize = 13.sp, color = Color(0xFFC4C6CF))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    statuses.forEach { st ->
                        FilterChip(
                            selected = status == st,
                            onClick = { status = st },
                            label = { Text(st, fontSize = 12.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("简介/读书初衷") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val pages = totalPages.toIntOrNull() ?: 300
                        val sampleCovers = listOf(
                            "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?auto=format&fit=crop&w=600&q=80",
                            "https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&w=600&q=80",
                            "https://images.unsplash.com/photo-1532012197267-da84d127e765?auto=format&fit=crop&w=600&q=80"
                        )
                        val newBook = Book(
                            title = title,
                            author = author.ifBlank { "佚名" },
                            category = category,
                            coverUrl = sampleCovers.random(),
                            status = status,
                            totalPages = pages,
                            currentPage = if (status == "已读") pages else 0,
                            description = description
                        )
                        onConfirm(newBook)
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
                Text("取消", color = Color(0xFFC4C6CF))
            }
        }
    )
}

@Composable
fun AddQuoteDialog(
    books: List<Book>,
    onDismiss: () -> Unit,
    onSave: (bookTitle: String, quoteText: String, page: Int, note: String, themeTag: String) -> Unit
) {
    var selectedBookTitle by remember { mutableStateOf(books.firstOrNull()?.title ?: "") }
    var quoteText by remember { mutableStateOf("") }
    var pageNumber by remember { mutableStateOf("1") }
    var note by remember { mutableStateOf("") }
    var themeTag by remember { mutableStateOf("哲思") }

    val tags = listOf("哲思", "经典", "诗意", "感悟", "生活")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("摘录佳句", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = selectedBookTitle,
                    onValueChange = { selectedBookTitle = it },
                    label = { Text("出自书籍") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_quote_book")
                )

                OutlinedTextField(
                    value = quoteText,
                    onValueChange = { quoteText = it },
                    label = { Text("金句正文") },
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth().testTag("input_quote_text")
                )

                OutlinedTextField(
                    value = pageNumber,
                    onValueChange = { pageNumber = it },
                    label = { Text("页码") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text("主题标语：", fontSize = 13.sp, color = Color(0xFFC4C6CF))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
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
                    label = { Text("随手心得 (选填)") },
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (quoteText.isNotBlank() && selectedBookTitle.isNotBlank()) {
                        val page = pageNumber.toIntOrNull() ?: 1
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
            TextButton(onClick = onDismiss) {
                Text("取消", color = Color(0xFFC4C6CF))
            }
        }
    )
}

@Composable
fun UpdateProgressDialog(
    book: Book,
    onDismiss: () -> Unit,
    onConfirm: (newPage: Int) -> Unit
) {
    var currentPage by remember { mutableStateOf(book.currentPage.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("更新阅读进度", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("《${book.title}》 （总共 ${book.totalPages} 页）")
                OutlinedTextField(
                    value = currentPage,
                    onValueChange = { currentPage = it },
                    label = { Text("已读页数") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_update_page")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val page = currentPage.toIntOrNull() ?: book.currentPage
                    onConfirm(page.coerceIn(0, book.totalPages))
                },
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
            TextButton(onClick = onDismiss) {
                Text("取消", color = Color(0xFFC4C6CF))
            }
        }
    )
}
