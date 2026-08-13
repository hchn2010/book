package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.theme.InkPrimary
import com.example.ui.theme.PaperBeige
import com.example.ui.theme.SageSecondary
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun StatsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val totalReadingMinutes by viewModel.totalReadingMinutes.collectAsState()
    val readBooks by viewModel.readBooks.collectAsState()
    val currentlyReadingBooks by viewModel.currentlyReadingBooks.collectAsState()
    val wantReadBooks by viewModel.wantReadBooks.collectAsState()
    val quotes by viewModel.quotes.collectAsState()
    val sessions by viewModel.readingSessions.collectAsState()

    val totalHours = totalReadingMinutes / 60
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    val targetGoal = 12
    val goalProgress = (readBooks.size.toFloat() / targetGoal.toFloat()).coerceIn(0f, 1f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 88.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column {
                    Text(
                        text = "阅读统计与目标",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = InkPrimary,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "积跬步以至千里 · 读书日记",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        letterSpacing = 1.sp
                    )
                }
            }

            // Stat Cards Grid
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "累计时长",
                        value = if (totalHours > 0) "${totalHours}小时" else "${totalReadingMinutes}分钟",
                        icon = Icons.Default.Timer,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "已读完毕",
                        value = "${readBooks.size} 本",
                        icon = Icons.Default.MenuBook,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "在读藏本",
                        value = "${currentlyReadingBooks.size} 本",
                        icon = Icons.Default.AutoStories,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "精选书摘",
                        value = "${quotes.size} 条",
                        icon = Icons.Default.FormatQuote,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Annual Reading Goal Card
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = PaperBeige,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("reading_goal_card")
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
                            Text(
                                text = "2026 年度阅读目标",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = InkPrimary,
                                fontFamily = FontFamily.Serif
                            )
                            Text(
                                text = "${readBooks.size} / $targetGoal 本",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = SageSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = { goalProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = SageSecondary,
                            trackColor = Color.White
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (readBooks.size >= targetGoal) "恭喜！您已达成年度阅读目标！"
                            else "距离达成目标还需阅读 ${targetGoal - readBooks.size} 本书，加油！",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Reading History Section Title
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = SageSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "沉浸阅读记录",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = InkPrimary,
                        fontFamily = FontFamily.Serif
                    )
                }
            }

            if (sessions.isEmpty()) {
                item {
                    Text(
                        text = "暂无阅读记录，点击“静心”页面开启沉浸计时。",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            } else {
                items(sessions, key = { it.id }) { session ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = PaperBeige)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "《${session.bookTitle}》",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = InkPrimary
                                )
                                Text(
                                    text = dateFormat.format(Date(session.dateTimestamp)),
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            }

                            Text(
                                text = "沉浸 ${session.durationMinutes} 分钟",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = SageSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = PaperBeige
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SageSecondary,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = InkPrimary
            )
        }
    }
}
