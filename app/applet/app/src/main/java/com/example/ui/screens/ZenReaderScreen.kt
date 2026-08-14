package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Stop
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZenReaderScreen(
    book: Book?,
    isTimerRunning: Boolean,
    timerElapsedSeconds: Int,
    readerPaperMode: String,
    readerFontSizeSp: Int,
    onStartTimer: () -> Unit,
    onPauseTimer: () -> Unit,
    onStopAndSaveTimer: () -> Unit,
    onSetPaperMode: (String) -> Unit,
    onChangeFontSize: (Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showControls by remember { mutableStateOf(true) }

    val bgColor = when (readerPaperMode) {
        "Parchment" -> Color(0xFF25221E)
        "White" -> Color(0xFF2B2D30)
        "ZenGreen" -> Color(0xFF1B2420)
        else -> Color(0xFF1A1C1E)
    }

    val textColor = when (readerPaperMode) {
        "Parchment" -> Color(0xFFE2D6C5)
        "White" -> Color(0xFFE2E2E6)
        "ZenGreen" -> Color(0xFFD0E6D8)
        else -> Color(0xFFE2E2E6)
    }

    val minutes = timerElapsedSeconds / 60
    val seconds = timerElapsedSeconds % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)

    val sampleChapterText = """
        在极度喧嚣的世界里，能寻得一片片刻的安宁，是一件极其奢侈的事情。
        
        梭罗曾经说过：“大多数人都在生活在平静的绝望中。”我们每天穿梭于城市的大街小巷，奔波于各种事务之间，灵魂常常追不上脚步。
        
        真正的阅读，是一场静谧的修行。当你翻开书页，外界的嘈杂仿佛被一层无形的屏障阻隔。你开始与千百年前的智者对话，倾听他们对宇宙、生命与人性的独到见解。
        
        字里行间，不仅记录着知识，更镌刻着时光的沉淀。把时间留给一本书，让思绪在墨香中安然流淌。
    """.trimIndent()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { showControls = !showControls }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            AnimatedVisibility(visible = showControls) {
                TopAppBar(
                    title = {
                        Text(
                            text = book?.title ?: "沉浸泛读",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = textColor
                            )
                        }
                    },
                    actions = {
                        Text(
                            text = formattedTime,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD0E4FF),
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Reading Content Area
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 12.dp)
            ) {
                Text(
                    text = sampleChapterText,
                    fontSize = readerFontSizeSp.sp,
                    lineHeight = (readerFontSizeSp * 1.6).sp,
                    color = textColor,
                    fontFamily = FontFamily.Serif
                )
            }

            // Bottom Control Toolbar
            AnimatedVisibility(visible = showControls) {
                Surface(
                    color = Color(0xFF2E3033).copy(alpha = 0.95f),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Timer Control Buttons
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (!isTimerRunning) {
                                Button(
                                    onClick = onStartTimer,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD0E4FF)),
                                    shape = CircleShape,
                                    modifier = Modifier.testTag("btn_timer_start")
                                ) {
                                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Start", tint = Color(0xFF003258))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("开始计时", color = Color(0xFF003258), fontWeight = FontWeight.Bold)
                                }
                            } else {
                                Button(
                                    onClick = onPauseTimer,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD6BEE4)),
                                    shape = CircleShape,
                                    modifier = Modifier.testTag("btn_timer_pause")
                                ) {
                                    Icon(imageVector = Icons.Default.Pause, contentDescription = "Pause", tint = Color(0xFF3B2948))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("暂停", color = Color(0xFF3B2948), fontWeight = FontWeight.Bold)
                                }
                            }

                            FilledTonalIconButton(
                                onClick = onStopAndSaveTimer,
                                modifier = Modifier.testTag("btn_timer_stop")
                            ) {
                                Icon(imageVector = Icons.Default.Stop, contentDescription = "Stop", tint = Color(0xFFFFB4AB))
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Font size and theme mode selector
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Font controls
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { onChangeFontSize(-2) }) {
                                    Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease Font", tint = Color(0xFFE2E2E6))
                                }
                                Text(text = "${readerFontSizeSp}sp", fontSize = 13.sp, color = Color(0xFFE2E2E6))
                                IconButton(onClick = { onChangeFontSize(2) }) {
                                    Icon(imageVector = Icons.Default.Add, contentDescription = "Increase Font", tint = Color(0xFFE2E2E6))
                                }
                            }

                            // Theme Paper Modes
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf(
                                    "Night" to Color(0xFF1A1C1E),
                                    "Parchment" to Color(0xFF25221E),
                                    "ZenGreen" to Color(0xFF1B2420)
                                ).forEach { (mode, color) ->
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(color)
                                            .clickable { onSetPaperMode(mode) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
