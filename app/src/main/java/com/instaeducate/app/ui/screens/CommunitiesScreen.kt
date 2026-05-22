package com.instaeducate.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.instaeducate.app.data.model.Community
import com.instaeducate.app.ui.components.glassmorphicCard
import com.instaeducate.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunitiesScreen(navController: NavController) {
    val viewModel = ViewModelProvider.communityViewModel
    val communities by viewModel.communities.collectAsState()

    var activeCommunityIndex by remember { mutableStateOf(0) }
    var userMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        if (communities.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = BlueDarkPrimary)
            }
        } else {
            val selectedCommunity = communities.getOrNull(activeCommunityIndex) ?: communities[0]

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Study Groups",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlueDarkPrimary
                )

                // Horizontal tab choices for communities
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    communities.forEachIndexed { index, community ->
                        val active = activeCommunityIndex == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (active) BlueDarkPrimary else DarkSurface)
                                .clickable { activeCommunityIndex = index }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = community.name.substringBefore(" "),
                                color = if (active) Color.White else DarkTextSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                // Active Community Dashboard Content
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .glassmorphicCard(cornerRadius = 24.dp, elevation = 8.dp, isDark = true),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(selectedCommunity.name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(selectedCommunity.description, fontSize = 13.sp, color = DarkTextSecondary)

                        // Interactive Group Poll Section
                        selectedCommunity.activePoll?.let { poll ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(DarkSurface)
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("📊 Study Poll:", fontWeight = FontWeight.Bold, color = AccentOrange, fontSize = 12.sp)
                                Text(poll.question, color = DarkTextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)

                                val totalVotes = poll.votes.sum().coerceAtLeast(1)
                                poll.options.forEachIndexed { idx, option ->
                                    val voted = poll.userVotedIndex != null
                                    val myVote = poll.userVotedIndex == idx
                                    val percentage = (poll.votes[idx] * 100) / totalVotes

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (myVote) BlueDarkSecondary else DarkBackground)
                                            .clickable(!voted) { viewModel.voteInPoll(selectedCommunity.id, idx) }
                                            .padding(10.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(option, color = Color.White, fontSize = 12.sp)
                                            if (voted) {
                                                Text("$percentage%", color = BlueDarkTertiary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Discussion board List
                        Text("💬 Live Discussions:", fontWeight = FontWeight.Bold, color = BlueDarkPrimary, fontSize = 13.sp)

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(selectedCommunity.discussionThreads) { thread ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(DarkSurface.copy(alpha = 0.5f))
                                        .padding(8.dp)
                                ) {
                                    Column {
                                        Text(thread.authorName, fontWeight = FontWeight.Bold, color = BlueDarkTertiary, fontSize = 12.sp)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(thread.content, color = DarkTextPrimary, fontSize = 13.sp)
                                    }
                                }
                            }
                        }

                        // Input panel to post new message
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = userMessage,
                                onValueChange = { userMessage = it },
                                label = { Text("Ask study group...", color = DarkTextSecondary) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = DarkTextPrimary,
                                    unfocusedTextColor = DarkTextPrimary,
                                    focusedBorderColor = BlueDarkPrimary
                                ),
                                modifier = Modifier.weight(1f)
                            )

                            Button(
                                onClick = {
                                    if (userMessage.isNotEmpty()) {
                                        viewModel.addCommentToThread(selectedCommunity.id, userMessage)
                                        userMessage = ""
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = BlueDarkPrimary)
                            ) {
                                Text("Send", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
