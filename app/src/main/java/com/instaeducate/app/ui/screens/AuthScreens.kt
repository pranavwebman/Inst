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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.instaeducate.app.data.model.UserRole
import com.instaeducate.app.navigation.Screen
import com.instaeducate.app.ui.components.glassmorphicCard
import com.instaeducate.app.ui.theme.*

// Mock Global ViewModel Provider structure
object ViewModelProvider {
    val authRepository = AuthRepository()
    val postRepository = PostRepository()
    val communityRepository = CommunityRepository()
    val chatRepository = ChatRepository()
    val aiRepository = AIRepository()

    val authViewModel = com.instaeducate.app.ui.viewmodel.AuthViewModel(authRepository)
    val homeViewModel = com.instaeducate.app.ui.viewmodel.HomeViewModel(postRepository)
    val reelsViewModel = com.instaeducate.app.ui.viewmodel.ReelsViewModel(postRepository)
    val communityViewModel = com.instaeducate.app.ui.viewmodel.CommunityViewModel(communityRepository)
    val chatViewModel = com.instaeducate.app.ui.viewmodel.ChatViewModel(chatRepository)
    val aiViewModel = com.instaeducate.app.ui.viewmodel.AIViewModel(aiRepository)
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    val viewModel = ViewModelProvider.authViewModel
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DarkBackground, AmoledBackground)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "InstaEducate",
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                color = BlueDarkPrimary,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Scroll. Learn. Grow.",
                fontSize = 16.sp,
                color = DarkTextSecondary,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .glassmorphicCard(cornerRadius = 24.dp, elevation = 16.dp, isDark = true),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Login to your workspace",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DarkTextPrimary
                    )

                    errorMessage?.let {
                        Text(text = it, color = Color.Red, fontSize = 14.sp)
                    }

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email", color = DarkTextSecondary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = DarkTextPrimary,
                            unfocusedTextColor = DarkTextPrimary,
                            focusedBorderColor = BlueDarkPrimary,
                            unfocusedBorderColor = DarkTextSecondary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password", color = DarkTextSecondary) },
                        visualTransformation = PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = DarkTextPrimary,
                            unfocusedTextColor = DarkTextPrimary,
                            focusedBorderColor = BlueDarkPrimary,
                            unfocusedBorderColor = DarkTextSecondary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            viewModel.login(email, password) {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BlueDarkPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(top = 8.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Sign In", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }

                    Text(
                        text = "Forgot Password?",
                        color = BlueDarkSecondary,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable { navController.navigate(Screen.ForgotPassword.route) }
                    )
                }
            }

            Row(
                modifier = Modifier.padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Don't have an account?", color = DarkTextSecondary)
                Text(
                    text = "Sign Up",
                    fontWeight = FontWeight.Bold,
                    color = BlueDarkPrimary,
                    modifier = Modifier.clickable { navController.navigate(Screen.Signup.route) }
                )
            }
        }
    }
}

@Composable
fun SignupScreen(navController: NavController) {
    val viewModel = ViewModelProvider.authViewModel
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(DarkBackground, AmoledBackground))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Join InstaEducate",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = BlueDarkPrimary
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .glassmorphicCard(cornerRadius = 24.dp, elevation = 16.dp, isDark = true),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    errorMessage?.let {
                        Text(text = it, color = Color.Red, fontSize = 14.sp)
                    }

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username", color = DarkTextSecondary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = DarkTextPrimary,
                            unfocusedTextColor = DarkTextPrimary,
                            focusedBorderColor = BlueDarkPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email", color = DarkTextSecondary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = DarkTextPrimary,
                            unfocusedTextColor = DarkTextPrimary,
                            focusedBorderColor = BlueDarkPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password", color = DarkTextSecondary) },
                        visualTransformation = PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = DarkTextPrimary,
                            unfocusedTextColor = DarkTextPrimary,
                            focusedBorderColor = BlueDarkPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            viewModel.signup(email, username) {
                                navController.navigate(Screen.Onboarding.route)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BlueDarkPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(top = 8.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Register", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Text(
                text = "Back to Login",
                color = BlueDarkSecondary,
                modifier = Modifier.clickable { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun ForgotPasswordScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var linkSent by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(DarkBackground, AmoledBackground))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Reset Password",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = BlueDarkPrimary
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .glassmorphicCard(cornerRadius = 24.dp, elevation = 16.dp, isDark = true)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (linkSent) {
                        Text(
                            text = "A password reset link was sent to $email. Please check your inbox.",
                            color = AccentGreen,
                            fontSize = 14.sp
                        )
                    } else {
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Enter your registered Email", color = DarkTextSecondary) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = DarkTextPrimary,
                                unfocusedTextColor = DarkTextPrimary,
                                focusedBorderColor = BlueDarkPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = { linkSent = true },
                            colors = ButtonDefaults.buttonColors(containerColor = BlueDarkPrimary),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Send Link")
                        }
                    }
                }
            }

            Text(
                text = "Back to Login",
                color = BlueDarkSecondary,
                modifier = Modifier.clickable { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun OnboardingScreen(navController: NavController) {
    val viewModel = ViewModelProvider.authViewModel
    var selectedRole by remember { mutableStateOf(UserRole.STUDENT) }
    var educationLevel by remember { mutableStateOf("") }
    val selectedSubjects = remember { mutableStateListOf<String>() }

    val subjects = listOf("Coding", "Math", "Science", "AI", "Languages", "Exam Prep", "Cybersecurity")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(DarkBackground, AmoledBackground))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Setup Workspace",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = BlueDarkPrimary,
                modifier = Modifier.padding(top = 16.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 16.dp)
                    .glassmorphicCard(cornerRadius = 24.dp, elevation = 16.dp, isDark = true),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Select Your Role:", color = DarkTextPrimary, fontWeight = FontWeight.SemiBold)
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        UserRole.values().forEach { role ->
                            val selected = selectedRole == role
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (selected) BlueDarkPrimary else DarkSurface)
                                    .clickable { selectedRole = role },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = role.name,
                                    color = if (selected) Color.White else DarkTextSecondary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = educationLevel,
                        onValueChange = { educationLevel = it },
                        label = { Text("Education Level / Designation", color = DarkTextSecondary) },
                        placeholder = { Text("e.g. B.Tech Sophomore, High School Teacher", color = DarkTextSecondary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = DarkTextPrimary,
                            unfocusedTextColor = DarkTextPrimary,
                            focusedBorderColor = BlueDarkPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Subjects of Interest:", color = DarkTextPrimary, fontWeight = FontWeight.SemiBold)

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        subjects.forEach { subject ->
                            val contains = selectedSubjects.contains(subject)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (contains) BlueDarkSecondary else DarkSurface)
                                    .clickable {
                                        if (contains) selectedSubjects.remove(subject) else selectedSubjects.add(subject)
                                    }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(text = subject, color = Color.White, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {
                    viewModel.completeOnboarding(selectedRole, educationLevel, selectedSubjects) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BlueDarkPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Get Started 🚀", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

// FlowRow layout simulator helper
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    // Basic wrap flow row emulator using a Box with horizontal layouts
    Box(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = verticalArrangement) {
                Row(horizontalArrangement = horizontalArrangement) {
                    content()
                }
            }
        }
    }
}
