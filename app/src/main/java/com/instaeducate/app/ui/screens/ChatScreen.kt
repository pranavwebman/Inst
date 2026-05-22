package com.instaeducate.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.instaeducate.app.data.model.ChatMessage
import com.instaeducate.app.ui.components.glassmorphicCard
import com.instaeducate.app.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController) {
    val viewModel = ViewModelProvider.chatViewModel
    val messages by viewModel.messages.collectAsState()
    val isTyping by viewModel.isTyping.collectAsState()

    var textInput by remember { mutableStateOf("") }
    var isRecording by remember { mutableStateOf(false) }
    var recordTime by remember { mutableStateOf(0) }

    val coroutineScope = rememberCoroutineScope()

    // simulated voice recorder timer
    LaunchedEffect(key1 = isRecording, key2 = recordTime) {
        if (isRecording) {
            delay(1000)
            recordTime++
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Chat Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurface)
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Go back", tint = Color.White)
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(BlueDarkSecondary),
                    contentAlignment = Alignment.Center
                ) {
                    Text("T", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text("Teacher Sarah", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(
                        text = if (isTyping) "typing study notes..." else "Active now",
                        color = if (isTyping) BlueDarkTertiary else Color.Gray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Chat Messages deck
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(messages) { message ->
                    val isMe = message.senderId == "me"
                    val bubbleColor = if (isMe) BlueDarkPrimary else DarkSurface
                    val align = if (isMe) Alignment.End else Alignment.Start

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = align
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 16.dp,
                                        topEnd = 16.dp,
                                        bottomStart = if (isMe) 16.dp else 4.dp,
                                        bottomEnd = if (isMe) 4.dp else 16.dp
                                    )
                                )
                                .background(bubbleColor)
                                .padding(12.dp)
                        ) {
                            if (message.voiceNoteDuration.isNotEmpty()) {
                                // Voice player card layout
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Filled.PlayArrow, contentDescription = "Play voice note", tint = AccentOrange)
                                    Text("Voice Note (${message.voiceNoteDuration})", color = Color.White, fontSize = 13.sp)
                                }
                            } else {
                                Text(text = message.messageText, color = Color.White, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }

            // Input controller
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isRecording) {
                    // Voice Recording panel
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp)
                            .clip(RoundedCornerShape(27.dp))
                            .background(AccentRed.copy(alpha = 0.2f))
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🎤 Recording Voice... 0:${String.format("%02d", recordTime)}", color = AccentRed, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(
                            text = "Send",
                            color = AccentGreen,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                viewModel.sendVoiceNote("0:${String.format("%02d", recordTime)}")
                                isRecording = false
                                recordTime = 0
                            }
                        )
                        Text(
                            text = "Cancel",
                            color = Color.Gray,
                            modifier = Modifier.clickable {
                                isRecording = false
                                recordTime = 0
                            }
                        )
                    }
                } else {
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        label = { Text("Message study buddy...", color = DarkTextSecondary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = DarkTextPrimary,
                            unfocusedTextColor = DarkTextPrimary,
                            focusedBorderColor = BlueDarkPrimary
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    if (textInput.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                viewModel.sendMessage(textInput)
                                textInput = ""
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(BlueDarkPrimary)
                        ) {
                            Icon(Icons.Filled.Send, contentDescription = "Send", tint = Color.White)
                        }
                    } else {
                        // Mic Button
                        IconButton(
                            onClick = {
                                isRecording = true
                                recordTime = 0
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(BlueDarkSecondary)
                        ) {
                            Icon(Icons.Filled.Mic, contentDescription = "Record Voice Notes", tint = Color.White)
                        }
                    }
                }
            }
        }
    }
}
