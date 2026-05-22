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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.instaeducate.app.data.model.Post
import com.instaeducate.app.data.model.PostType
import com.instaeducate.app.navigation.Screen
import com.instaeducate.app.ui.components.glassmorphicCard
import com.instaeducate.app.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val authViewModel = ViewModelProvider.authViewModel
    val homeViewModel = ViewModelProvider.homeViewModel

    val user by authViewModel.currentUser.collectAsState(initial = null)
    val posts by homeViewModel.posts.collectAsState()
    val downloads by homeViewModel.downloadProgress.collectAsState()

    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Coding", "Math", "AI", "Science", "Exam Prep")

    // Pomodoro Timer States
    var showTimerOverlay by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Premium Header with Streak Flame & AI Assistant button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "InstaEducate",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlueDarkPrimary
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Streaks
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(AccentOrange.copy(alpha = 0.2f))
                            .clickable { navController.navigate(Screen.Profile.route) }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocalFireDepartment,
                            contentDescription = "Streak Count",
                            tint = AccentOrange,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "${user?.learningStreak ?: 12}d",
                            color = AccentOrange,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    // Pomodoro Toggle Button
                    IconButton(
                        onClick = { showTimerOverlay = !showTimerOverlay },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(BlueDarkSecondary.copy(alpha = 0.2f))
                    ) {
                        Icon(Icons.Filled.Timer, contentDescription = "Study Timer", tint = BlueDarkSecondary)
                    }

                    // AI study assistant
                    IconButton(
                        onClick = { navController.navigate(Screen.AIAssistant.route) },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(AccentPink.copy(alpha = 0.2f))
                    ) {
                        Icon(Icons.Filled.Psychology, contentDescription = "AI Study Assistant", tint = AccentPink)
                    }
                }
            }

            // Categories horizontal feed
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    val isSelected = selectedCategory == category
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) BlueDarkPrimary else DarkSurface)
                            .clickable { selectedCategory = category }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = category,
                            color = if (isSelected) Color.White else DarkTextSecondary,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // Main Educational vertical Feed
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val filteredPosts = if (selectedCategory == "All") posts else posts.filter { it.subjectTag == selectedCategory }
                items(filteredPosts) { post ->
                    EducationalFeedCard(
                        post = post,
                        onLike = { homeViewModel.toggleLike(post.id) },
                        onSave = { homeViewModel.toggleSave(post.id) },
                        onDownload = { homeViewModel.simulateNotesDownload(post.id) },
                        downloadProgress = downloads[post.id]
                    )
                }
            }
        }

        // Pomodoro Timer Floating overlay window
        AnimatedVisibility(
            visible = showTimerOverlay,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 80.dp, start = 16.dp, end = 16.dp)
        ) {
            PomodoroTimerWidget(onClose = { showTimerOverlay = false })
        }
    }
}

@Composable
fun EducationalFeedCard(
    post: Post,
    onLike: () -> Unit,
    onSave: () -> Unit,
    onDownload: () -> Unit,
    downloadProgress: Int?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .glassmorphicCard(cornerRadius = 24.dp, elevation = 8.dp, isDark = true),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Card Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(BlueDarkSecondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(post.authorName.take(1), color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text(post.authorName, color = DarkTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(post.authorRole.name, color = BlueDarkTertiary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(BlueDarkPrimary.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(post.subjectTag, color = BlueDarkPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Post Content Text
            Text(post.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkTextPrimary)
            Text(post.caption, fontSize = 14.sp, color = DarkTextSecondary)

            // Conditional Post Subcomponents
            when (post.type) {
                PostType.QUIZ_CARD -> {
                    post.quizCard?.let { quiz ->
                        var selectedIndex by remember { mutableStateOf<Int?>(null) }
                        var submitted by remember { mutableStateOf(false) }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(DarkSurface)
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("🧠 Quiz Card:", fontWeight = FontWeight.SemiBold, color = AccentPink, fontSize = 13.sp)
                            Text(quiz.question, color = DarkTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)

                            quiz.options.forEachIndexed { idx, option ->
                                val isSelected = selectedIndex == idx
                                val isCorrect = idx == quiz.correctIndex
                                val color = when {
                                    submitted && isCorrect -> AccentGreen
                                    submitted && isSelected && !isCorrect -> AccentRed
                                    isSelected -> BlueDarkPrimary
                                    else -> DarkBackground
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(color)
                                        .clickable(enabled = !submitted) { selectedIndex = idx }
                                        .padding(12.dp)
                                ) {
                                    Text(option, color = Color.White, fontSize = 13.sp)
                                }
                            }

                            if (!submitted && selectedIndex != null) {
                                Button(
                                    onClick = { submitted = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = BlueDarkPrimary),
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Text("Submit")
                                }
                            }

                            if (submitted) {
                                Text(
                                    text = "Explanation: ${quiz.explanation}",
                                    color = DarkTextSecondary,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }

                PostType.CODING_SNIPPET -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(AmoledBackground)
                            .padding(12.dp)
                    ) {
                        Text("💻 Kotlin Snippet:", color = BlueDarkTertiary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = post.codeSnippet,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            color = Color(0xFFA7F3D0)
                        )
                    }
                }

                PostType.NOTES_PDF -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(DarkSurface)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("📄", fontSize = 24.sp)
                            Column {
                                Text("StudyNotes_Calculus.pdf", color = DarkTextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(post.notesPdfSize, color = DarkTextSecondary, fontSize = 11.sp)
                            }
                        }

                        if (downloadProgress != null) {
                            CircularProgressIndicator(
                                progress = downloadProgress / 100f,
                                modifier = Modifier.size(24.dp),
                                color = BlueDarkPrimary
                            )
                        } else {
                            IconButton(onClick = onDownload) {
                                Icon(Icons.Filled.Download, contentDescription = "Download Notes", tint = BlueDarkPrimary)
                            }
                        }
                    }
                }
                else -> {}
            }

            // Post Action items: Likes, comments, shares, saves
            Divider(color = Color.DarkGray.copy(alpha = 0.4f), thickness = 0.8.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.clickable(onClick = onLike)
                    ) {
                        Icon(
                            imageVector = if (post.isLikedByMe) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Like Post",
                            tint = if (post.isLikedByMe) AccentRed else Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                        Text("${post.likesCount}", color = Color.Gray, fontSize = 13.sp)
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Share, contentDescription = "Share Notes", tint = Color.Gray, modifier = Modifier.size(20.dp))
                        Text("Share", color = Color.Gray, fontSize = 13.sp)
                    }
                }

                IconButton(onClick = onSave) {
                    Icon(
                        imageVector = if (post.isSavedByMe) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                        contentDescription = "Save to bookmarks",
                        tint = if (post.isSavedByMe) BlueDarkTertiary else Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun PomodoroTimerWidget(onClose: () -> Unit) {
    var totalSeconds by remember { mutableStateOf(1500) } // 25 minutes
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = totalSeconds, key2 = isRunning) {
        if (isRunning && totalSeconds > 0) {
            delay(1000)
            totalSeconds--
        }
    }

    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .glassmorphicCard(cornerRadius = 24.dp, elevation = 16.dp, isDark = true),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("⏱️ Focus Timer (Pomodoro)", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                Text(
                    text = "Close",
                    color = AccentRed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable(onClick = onClose)
                )
            }

            Text(
                text = formattedTime,
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                color = AccentOrange,
                fontFamily = FontFamily.Monospace
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { isRunning = !isRunning },
                    colors = ButtonDefaults.buttonColors(containerColor = BlueDarkPrimary)
                ) {
                    Text(if (isRunning) "Pause" else "Start Focus")
                }

                Button(
                    onClick = {
                        isRunning = false
                        totalSeconds = 1500
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkSurface)
                ) {
                    Text("Reset")
                }
            }
        }
    }
}
