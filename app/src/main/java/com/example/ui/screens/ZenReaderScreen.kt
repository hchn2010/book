package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.outlined.FormatSize
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
import com.example.ui.MainViewModel
import com.example.ui.theme.NightReaderBg
import com.example.ui.theme.PaperBeige
import com.example.ui.theme.SageSecondary
import com.example.ui.theme.WarmPaperBg
import com.example.ui.theme.ZenGreenBg

@Composable
fun ZenReaderScreen(
    viewModel: MainViewModel,
    initialBook: Book? = null,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {
    BackHandler(enabled = true, onBack = onBack)
    val isTimerRunning by viewModel.isTimerRunning.collectAsState()
    val timerElapsedSeconds by viewModel.timerElapsedSeconds.collectAsState()
    val activeBook by viewModel.activeTimerBook.collectAsState()
    val paperMode by viewModel.readerPaperMode.collectAsState()
    val fontSizeSp by viewModel.readerFontSizeSp.collectAsState()
    val allBooks by viewModel.filteredBooks.collectAsState()

    var selectedAmbientSound by remember { mutableStateOf("雨打芭蕉") }

    val currentBook = activeBook ?: initialBook ?: allBooks.firstOrNull()

    val paperBgColor = when (paperMode) {
        "Parchment" -> WarmPaperBg
        "ZenGreen" -> ZenGreenBg
        "Night" -> NightReaderBg
        else -> Color.White
    }

    val paperTextColor = when (paperMode) {
        "Night" -> Color(0xFFDCDCDC)
        else -> Color(0xFF1E252B)
    }

    val minutes = timerElapsedSeconds / 60
    val seconds = timerElapsedSeconds % 60
    val hours = minutes / 60
    val timerDisplay = String.format("%02d:%02d:%02d", hours, minutes % 60, seconds)

    val ambientSounds = listOf("风吹竹林", "雨打芭蕉", "古寺晚钟", "海浪沉吟")

    val sampleText = remember(currentBook?.title) {
        when (currentBook?.title) {
            "瓦尔登湖" -> """
                我到森林里去，是因为我希望有意义地生活，只面对生活的基本事实，看看我是否能学到生活要教给我的东西，免得在我将死之时，发现自己竟没有活过。
                
                时间不过是我在其中钓鱼的溪流。我饮用溪水，在我饮用之时，我看到了沙质的河床，意识到它是多么浅小。它浅浅的流水流走了，但永恒留了下来。
                
                我们迫切地需要看到自然未被征服的宏伟与荒凉。我们需要依靠自然的滋养，它那无穷无尽的生机与不可估量的旷野。
            """.trimIndent()

            "百年孤独" -> """
                多年以后，面对处决队，奥雷里亚诺·布恩迪亚上校将会回想起父亲带他去参观冰块的那个遥远的下午。
                
                生命中真正重要的不是你遇到了什么，而是你记住了什么，以及你是如何铭记的。
                
                过去都是假的，回忆是一条没有归途的路，以往的一切春天都无法复原，哪怕最狂热最坚贞的爱情，归根结底也不过是一种瞬息即逝的现实，唯有孤独永恒。
            """.trimIndent()

            "三体" -> """
                给岁月以文明，而不是给文明以岁月。
                
                弱小和无知不是生存的障碍，傲慢才是。
                
                宇宙就是一座黑暗森林，每个文明都是带枪的猎人，像幽灵般潜行于林间，轻轻拨开挡路的树枝，竭力不让脚步发出一点声音，连呼吸都必须小心翼翼……
            """.trimIndent()

            else -> """
                书香伴墨，静心凝神。在这个喧嚣的时代里，翻开一卷书，如同开启一场穿越时空的对话。
                
                读书并非为了向外界证明什么，而是为了给自己搭建一座避风的港湾。在文字的韵律中，听风观雨，悟道修身。
                
                静坐常思己过，闲谈莫论人非。愿你在每一页文字里，都能遇见更宁静的自己。
            """.trimIndent()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(paperBgColor)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Timer Header Widget
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            color = if (paperMode == "Night") Color(0xFF282C30) else PaperBeige,
            shadowElevation = 0.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "静心沉浸计时",
                            fontSize = 12.sp,
                            color = SageSecondary,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = currentBook?.title ?: "自由阅读",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = paperTextColor
                        )
                    }

                    // Timer controls
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isTimerRunning) {
                            IconButton(
                                onClick = { viewModel.pauseTimer() },
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(SageSecondary, CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Pause,
                                    contentDescription = "Pause",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        } else {
                            IconButton(
                                onClick = { viewModel.startTimer(currentBook) },
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(SageSecondary, CircleShape)
                                    .testTag("btn_start_timer")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Start",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        if (timerElapsedSeconds > 0) {
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = { viewModel.stopAndSaveTimer() },
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(MaterialTheme.colorScheme.error.copy(alpha = 0.8f), CircleShape)
                                    .testTag("btn_stop_timer")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Stop,
                                    contentDescription = "Stop and save",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = timerDisplay,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = SageSecondary,
                    fontFamily = FontFamily.Monospace
                )

                // Ambient sound status
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.GraphicEq,
                        contentDescription = null,
                        tint = SageSecondary.copy(alpha = 0.8f),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "白噪音：$selectedAmbientSound",
                        fontSize = 11.sp,
                        color = SageSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Reader Controls Toolbar (Paper theme + Font size + Sound)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Paper Mode Chips
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf(
                    "Parchment" to WarmPaperBg,
                    "White" to Color.White,
                    "ZenGreen" to ZenGreenBg,
                    "Night" to NightReaderBg
                ).forEach { (mode, color) ->
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (paperMode == mode) 2.dp else 1.dp,
                                color = if (paperMode == mode) SageSecondary else Color.Gray.copy(alpha = 0.4f),
                                shape = CircleShape
                            )
                            .clickable { viewModel.setPaperMode(mode) }
                            .testTag("paper_mode_$mode")
                    )
                }
            }

            // Font Size Controls
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.FormatSize,
                    contentDescription = null,
                    tint = paperTextColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(
                    onClick = { viewModel.changeFontSize(-2) },
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.width(28.dp)
                ) {
                    Text("A-", fontSize = 12.sp, color = paperTextColor)
                }
                TextButton(
                    onClick = { viewModel.changeFontSize(2) },
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.width(28.dp)
                ) {
                    Text("A+", fontSize = 12.sp, color = paperTextColor)
                }
            }
        }

        // Ambient Sound selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            ambientSounds.forEach { sound ->
                val isSelected = sound == selectedAmbientSound
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSelected) SageSecondary.copy(alpha = 0.2f) else Color.Transparent
                        )
                        .clickable { selectedAmbientSound = sound }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = sound,
                        fontSize = 11.sp,
                        color = if (isSelected) SageSecondary else paperTextColor.copy(alpha = 0.6f),
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        HorizontalDivider(color = paperTextColor.copy(alpha = 0.15f), thickness = 1.dp)

        Spacer(modifier = Modifier.height(16.dp))

        // Main Reading Parchment Text Display
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 88.dp)
        ) {
            Text(
                text = "《${currentBook?.title ?: "墨香沉浸阅读"}》",
                fontSize = (fontSizeSp + 4).sp,
                fontWeight = FontWeight.Bold,
                color = paperTextColor,
                fontFamily = FontFamily.Serif
            )

            Text(
                text = "作者：${currentBook?.author ?: "佚名"}",
                fontSize = 13.sp,
                color = paperTextColor.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            Text(
                text = sampleText,
                fontSize = fontSizeSp.sp,
                lineHeight = (fontSizeSp * 1.75f).sp,
                color = paperTextColor,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
