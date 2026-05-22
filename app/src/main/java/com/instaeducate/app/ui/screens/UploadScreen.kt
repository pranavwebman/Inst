package com.instaeducate.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.instaeducate.app.data.model.PostType
import com.instaeducate.app.navigation.Screen
import com.instaeducate.app.ui.components.glassmorphicCard
import com.instaeducate.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadScreen(navController: NavController) {
    val homeViewModel = ViewModelProvider.homeViewModel

    var title by remember { mutableStateOf("") }
    var caption by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Coding") }
    var selectedType by remember { mutableStateOf(PostType.CODING_SNIPPET) }
    var codeSnippet by remember { mutableStateOf("") }

    val categories = listOf("Coding", "Math", "AI", "Science", "Exam Prep")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Share Knowledge",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = BlueDarkPrimary
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .glassmorphicCard(cornerRadius = 24.dp, elevation = 12.dp, isDark = true),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text("Select Post Type:", color = DarkTextPrimary, fontWeight = FontWeight.SemiBold)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(PostType.CODING_SNIPPET, PostType.NOTES_PDF, PostType.QUIZ_CARD).forEach { type ->
                            val active = selectedType == type
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (active) BlueDarkPrimary else DarkSurface)
                                    .clickable { selectedType = type },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when (type) {
                                        PostType.CODING_SNIPPET -> "Snippet"
                                        PostType.NOTES_PDF -> "Notes PDF"
                                        PostType.QUIZ_CARD -> "Quiz Card"
                                        else -> "Image"
                                    },
                                    color = if (active) Color.White else DarkTextSecondary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title", color = DarkTextSecondary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = DarkTextPrimary,
                            unfocusedTextColor = DarkTextPrimary,
                            focusedBorderColor = BlueDarkPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = caption,
                        onValueChange = { caption = it },
                        label = { Text("Caption or Lesson Explanation", color = DarkTextSecondary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = DarkTextPrimary,
                            unfocusedTextColor = DarkTextPrimary,
                            focusedBorderColor = BlueDarkPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    )

                    if (selectedType == PostType.CODING_SNIPPET) {
                        OutlinedTextField(
                            value = codeSnippet,
                            onValueChange = { codeSnippet = it },
                            label = { Text("Paste Source Code here", color = DarkTextSecondary) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = DarkTextPrimary,
                                unfocusedTextColor = DarkTextPrimary,
                                focusedBorderColor = BlueDarkPrimary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                        )
                    }

                    if (selectedType == PostType.NOTES_PDF) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(DarkSurface)
                                .clickable { /* Simulation of file selector */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📎 Click to select PDF from Storage", color = BlueDarkTertiary, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Text("Category:", color = DarkTextPrimary, fontWeight = FontWeight.SemiBold)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categories.take(3).forEach { category ->
                            val active = selectedCategory == category
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (active) BlueDarkSecondary else DarkSurface)
                                    .clickable { selectedCategory = category }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(category, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {
                    if (title.isNotEmpty()) {
                        homeViewModel.addPost(title, caption, selectedCategory, selectedType, codeSnippet)
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Upload.route) { inclusive = true }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BlueDarkPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Publish to Feed 🚀", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}
