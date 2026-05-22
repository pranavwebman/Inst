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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Flip
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIScreen(navController: NavController) {
    val viewModel = ViewModelProvider.aiViewModel
    val messages by viewModel.aiMessages.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()

    var textInput by remember { mutableStateOf("") }
    var activeTab by remember { mutableStateOf("Chat") }

    // Flashcards state
    var currentFlashcardIdx by remember { mutableStateOf(0) }
    var flashcardFlipped by remember { mutableStateOf(false) }

    val flashcards = listOf(
        Pair("Supervised Learning", "Algorithms are trained using labeled input data (features with target solutions) to map mappings."),
        Pair("Unsupervised Learning", "Algorithms identify hidden data clusters/distributions on their own using completely unlabeled datasets."),
        Pair("Active Recall", "A highly efficient memory testing strategy where you actively retrieve target concepts instead of passively rereading.")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // AI Header
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

                Icon(Icons.Filled.AutoAwesome, contentDescription = "AI Study Assistant", tint = AccentPink, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text("AI Study Buddy", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            // Segmented Tab bar: AI Chat, Flashcards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf("Chat", "Flashcards", "Create Quiz").forEach { tab ->
                    val active = activeTab == tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (active) AccentPink else DarkSurface)
                            .clickable { activeTab = tab }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(tab, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            // Conditional view content
            when (activeTab) {
                "Chat" -> {
                    Column(modifier = Modifier.weight(1f)) {
                        // AI chatbot message thread
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(messages) { message ->
                                val isAI = message.senderId == "ai"
                                val bubbleColor = if (isAI) DarkSurface else AccentPink.copy(alpha = 0.2f)
                                val align = if (isAI) Alignment.Start else Alignment.End

                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = align
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(bubbleColor)
                                            .padding(14.dp)
                                    ) {
                                        Text(text = message.messageText, color = Color.White, fontSize = 13.sp)
                                    }
                                }
                            }

                            if (isGenerating) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(DarkSurface)
                                            .padding(14.dp)
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            CircularProgressIndicator(color = AccentPink, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                                            Text("Reasoning study solution...", color = DarkTextSecondary, fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }

                        // Chat Input controller
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .navigationBarsPadding()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = textInput,
                                onValueChange = { textInput = it },
                                label = { Text("Ask study buddy...", color = DarkTextSecondary) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = DarkTextPrimary,
                                    unfocusedTextColor = DarkTextPrimary,
                                    focusedBorderColor = AccentPink
                                ),
                                modifier = Modifier.weight(1f)
                            )

                            IconButton(
                                onClick = {
                                    if (textInput.isNotEmpty()) {
                                        viewModel.askAI(textInput)
                                        textInput = ""
                                    }
                                },
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(AccentPink)
                            ) {
                                Icon(Icons.Filled.Send, contentDescription = "Send to AI", tint = Color.White)
                            }
                        }
                    }
                }

                "Flashcards" -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        val card = flashcards[currentFlashcardIdx]

                        // Interactive Flipping Card Card layout
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .glassmorphicCard(cornerRadius = 24.dp, elevation = 12.dp, isDark = true)
                                .clickable { flashcardFlipped = !flashcardFlipped }
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(Icons.Filled.Flip, contentDescription = "Flip flashcard", tint = AccentOrange)
                                if (flashcardFlipped) {
                                    Text(text = card.second, color = Color.White, fontSize = 14.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                                } else {
                                    Text(text = card.first, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                    Text("Tap to reveal summary details", color = DarkTextSecondary, fontSize = 11.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    flashcardFlipped = false
                                    currentFlashcardIdx = (currentFlashcardIdx - 1 + flashcards.size) % flashcards.size
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = DarkSurface)
                            ) {
                                Text("Previous")
                            }

                            Button(
                                onClick = {
                                    flashcardFlipped = false
                                    currentFlashcardIdx = (currentFlashcardIdx + 1) % flashcards.size
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentPink)
                            ) {
                                Text("Next Card")
                            }
                        }
                    }
                }

                "Create Quiz" -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Draft AI Quiz", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Specify a study topic and the Assistant will structure a custom multi-choice test question immediately.", fontSize = 13.sp, color = DarkTextSecondary, textAlign = androidx.compose.ui.text.style.TextAlign.Center)

                        Button(
                            onClick = {
                                activeTab = "Chat"
                                viewModel.askAI("Generate a quiz for Kotlin algorithms")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentPink),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text("Generate Kotlin Quiz 🧠", fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                activeTab = "Chat"
                                viewModel.askAI("Generate a quiz for machine learning embeddings")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkSurface),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text("Generate Machine Learning Quiz 🤖", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
