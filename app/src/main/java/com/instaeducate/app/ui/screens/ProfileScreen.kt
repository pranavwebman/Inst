package com.instaeducate.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Star
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
import com.instaeducate.app.data.model.User
import com.instaeducate.app.ui.components.glassmorphicCard
import com.instaeducate.app.ui.theme.*

@Composable
fun ProfileScreen(navController: NavController) {
    val authViewModel = ViewModelProvider.authViewModel
    val user by authViewModel.currentUser.collectAsState(initial = null)

    var amoledThemeEnabled by remember { mutableStateOf(true) }

    val leaderboard = listOf(
        Pair("1. Prof. Andrew", 4520),
        Pair("2. Elena Rostova", 3890),
        Pair("3. CodeBytes", 2120),
        Pair("4. You", user?.xpPoints ?: 1240),
        Pair("5. Meera Sen", 980)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Profile Header Widget
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .glassmorphicCard(cornerRadius = 24.dp, elevation = 8.dp, isDark = true),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(BlueDarkPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (user?.username ?: "Me").take(1),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Text(
                            text = user?.username ?: "Student User",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${user?.role?.name} • ${user?.educationLevel ?: "Self Learner"}",
                            fontSize = 13.sp,
                            color = BlueDarkTertiary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = user?.bio ?: "Scroll less, learn more. Building my programming habits on InstaEducate.",
                            fontSize = 12.sp,
                            color = DarkTextSecondary,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${user?.followersCount ?: 142}", color = Color.White, fontWeight = FontWeight.Bold)
                                Text("Followers", color = DarkTextSecondary, fontSize = 11.sp)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${user?.followingCount ?: 98}", color = Color.White, fontWeight = FontWeight.Bold)
                                Text("Following", color = DarkTextSecondary, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            // Gamified Streaks Progress card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .glassmorphicCard(cornerRadius = 24.dp, elevation = 8.dp, isDark = true),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("⚡ Streak & Level Progress", fontWeight = FontWeight.Bold, color = Color.White)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.LocalFireDepartment, contentDescription = "Streaks", tint = AccentOrange)
                                Text(
                                    text = "${user?.learningStreak ?: 12} Days",
                                    color = AccentOrange,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Progress Indicator
                        val xpProgress = ((user?.xpPoints ?: 1240) % 1000) / 1000f
                        Text(
                            text = "Level ${(user?.xpPoints ?: 1240) / 1000 + 1} (${(user?.xpPoints ?: 1240) % 1000} / 1000 XP)",
                            color = DarkTextSecondary,
                            fontSize = 12.sp
                        )
                        LinearProgressIndicator(
                            progress = xpProgress,
                            color = AccentGreen,
                            trackColor = DarkBackground,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )
                    }
                }
            }

            // Badges Achievement Grid list
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("🏆 Achievement Badges:", fontWeight = FontWeight.Bold, color = BlueDarkPrimary)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            Triple("Streak Master", "10 days streak completed", AccentOrange),
                            Triple("Quiz Champ", "Completed 5 AI quizzes", AccentPink),
                            Triple("Top Peer", "Shared premium study notes", BlueDarkTertiary)
                        ).forEach { badge ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(DarkSurface)
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(Icons.Filled.Star, contentDescription = badge.first, tint = badge.third, modifier = Modifier.size(24.dp))
                                    Text(badge.first, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    Text(badge.second, color = DarkTextSecondary, fontSize = 9.sp, lineHeight = 10.sp)
                                }
                            }
                        }
                    }
                }
            }

            // Leaderboards
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .glassmorphicCard(cornerRadius = 24.dp, elevation = 8.dp, isDark = true),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("📊 Global Leaderboard", fontWeight = FontWeight.Bold, color = Color.White)

                        leaderboard.forEach { rank ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(Icons.Filled.EmojiEvents, contentDescription = "Points rank", tint = AccentOrange, modifier = Modifier.size(16.dp))
                                    Text(rank.first, color = DarkTextPrimary, fontSize = 13.sp)
                                }
                                Text("${rank.second} XP", color = BlueDarkTertiary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }

            // Settings AMOLED Toggle options
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .glassmorphicCard(cornerRadius = 24.dp, elevation = 8.dp, isDark = true),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("AMOLED Power Savings Mode", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Enables absolute deep blacks on OLED displays", color = DarkTextSecondary, fontSize = 11.sp)
                        }
                        Switch(
                            checked = amoledThemeEnabled,
                            onCheckedChange = { amoledThemeEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = BlueDarkPrimary)
                        )
                    }
                }
            }

            // Logout Option
            item {
                Button(
                    onClick = {
                        authViewModel.logout {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Sign Out", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
