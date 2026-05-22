package com.instaeducate.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.instaeducate.app.data.model.Reel
import com.instaeducate.app.ui.components.glassmorphicCard
import com.instaeducate.app.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReelsScreen(navController: NavController) {
    val viewModel = ViewModelProvider.reelsViewModel
    val reels by viewModel.reels.collectAsState()

    var selectedTag by remember { mutableStateOf("All") }
    val tags = listOf("All", "AI", "Science", "Coding", "Cybersecurity")

    val pagerState = rememberPagerState(pageCount = {
        val filtered = if (selectedTag == "All") reels else reels.filter { it.subjectTag == selectedTag }
        filtered.size
    })

    val filteredReels = if (selectedTag == "All") reels else reels.filter { it.subjectTag == selectedTag }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (filteredReels.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No reels found for category '$selectedTag'", color = Color.White)
            }
        } else {
            // Full-screen Vertical Swipe Pager
            VerticalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val reel = filteredReels[page]
                ReelPlayerItem(
                    reel = reel,
                    onLike = { viewModel.toggleLikeReel(reel.id) }
                )
            }
        }

        // Horizontal Subject Filters row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tags.forEach { tag ->
                val isSelected = selectedTag == tag
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) BlueDarkPrimary else Color.Black.copy(alpha = 0.5f))
                        .clickable { selectedTag = tag }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(tag, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ReelPlayerItem(
    reel: Reel,
    onLike: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var playSpeed by remember { mutableStateOf(1.0f) }
    var showLikeHeartAnim by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        onLike()
                        showLikeHeartAnim = true
                        coroutineScope.launch {
                            delay(800)
                            showLikeHeartAnim = false
                        }
                    }
                )
            }
    ) {
        // High fidelity video rendering mock with simulated gradients
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0F172A),
                            Color(0xFF1E1B4B),
                            Color(0xFF000000)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Mock Play State Indicator
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("📚 Playback Simulator", color = Color.White.copy(alpha = 0.4f), fontSize = 14.sp)
                Text(
                    text = "${reel.subjectTag} Lecture (Active)",
                    color = BlueDarkTertiary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Animated circular progress spinner
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = BlueDarkPrimary,
                    strokeWidth = 3.dp
                )
            }
        }

        // Left-side metadata Overlay & speed control
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 24.dp, start = 16.dp, end = 80.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(BlueDarkSecondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(reel.authorName.take(1), color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Text(reel.authorName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                Text(reel.caption, color = Color.White, fontSize = 14.sp)

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    reel.hashtags.forEach { tag ->
                        Text("#$tag", color = BlueDarkTertiary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                // Speed Switch controls (0.5x -> 1.0x -> 1.5x -> 2.0x for rapid learning)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Speed, contentDescription = "Playback Speed", tint = AccentOrange, modifier = Modifier.size(16.dp))
                    listOf(1.0f, 1.5f, 2.0f).forEach { speed ->
                        val active = playSpeed == speed
                        Text(
                            text = "${speed}x",
                            color = if (active) AccentOrange else Color.Gray,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .clickable { playSpeed = speed }
                                .padding(horizontal = 4.dp)
                        )
                    }
                }
            }
        }

        // Right side engagement vertical Actions row
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp, bottom = 40.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Like Button
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(
                        onClick = onLike,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.5f))
                    ) {
                        Icon(
                            imageVector = if (reel.isLikedByMe) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Like reel",
                            tint = if (reel.isLikedByMe) AccentRed else Color.White
                        )
                    }
                    Text("${reel.likesCount}", color = Color.White, fontSize = 12.sp)
                }

                // Save/Bookmark Button
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(Icons.Filled.Bookmark, contentDescription = "Save reel", tint = Color.White)
                }

                // Share Button
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(Icons.Filled.MusicNote, contentDescription = "Music tag", tint = Color.White)
                }
            }
        }

        // Heart Pop-up on Double Tap
        AnimatedVisibility(
            visible = showLikeHeartAnim,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = "Double tap like animation",
                tint = AccentRed,
                modifier = Modifier.size(100.dp)
            )
        }
    }
}
