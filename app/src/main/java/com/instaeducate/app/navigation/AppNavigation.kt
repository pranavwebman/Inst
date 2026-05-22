package com.instaeducate.app.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.instaeducate.app.ui.components.glassmorphicCard
import com.instaeducate.app.ui.screens.*

@Composable
fun AppNavigation(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Hide navigation bar on specific screens
    val showBottomBar = currentRoute != null && 
            currentRoute != Screen.Login.route && 
            currentRoute != Screen.Signup.route && 
            currentRoute != Screen.ForgotPassword.route && 
            currentRoute != Screen.Onboarding.route &&
            currentRoute != Screen.Chat.route

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                InstaEducateBottomBar(navController, currentRoute)
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically(initialOffsetY = { it * 2 }),
                exit = slideOutVertically(targetOffsetY = { it * 2 })
            ) {
                FloatingActionButton(
                    onClick = { navController.navigate(Screen.Upload.route) },
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Upload Notes or Reels", modifier = Modifier.size(28.dp))
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(Screen.Login.route) { LoginScreen(navController) }
            composable(Screen.Signup.route) { SignupScreen(navController) }
            composable(Screen.ForgotPassword.route) { ForgotPasswordScreen(navController) }
            composable(Screen.Onboarding.route) { OnboardingScreen(navController) }
            
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.Reels.route) { ReelsScreen(navController) }
            composable(Screen.Upload.route) { UploadScreen(navController) }
            composable(Screen.Communities.route) { CommunitiesScreen(navController) }
            composable(Screen.Profile.route) { ProfileScreen(navController) }
            
            composable(Screen.Chat.route) { ChatScreen(navController) }
            composable(Screen.AIAssistant.route) { AIScreen(navController) }
        }
    }
}

@Composable
fun InstaEducateBottomBar(navController: NavHostController, currentRoute: String?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(68.dp)
            .glassmorphicCard(cornerRadius = 24.dp, elevation = 12.dp, isDark = true)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                icon = Icons.Filled.Home,
                label = "Home",
                selected = currentRoute == Screen.Home.route,
                onClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )

            BottomNavItem(
                icon = Icons.Filled.Movie,
                label = "Reels",
                selected = currentRoute == Screen.Reels.route,
                onClick = {
                    navController.navigate(Screen.Reels.route) {
                        launchSingleTop = true
                    }
                }
            )

            // Space placeholder for FloatingActionButton overlap offset
            Box(modifier = Modifier.size(48.dp))

            BottomNavItem(
                icon = Icons.Filled.People,
                label = "Groups",
                selected = currentRoute == Screen.Communities.route,
                onClick = {
                    navController.navigate(Screen.Communities.route) {
                        launchSingleTop = true
                    }
                }
            )

            BottomNavItem(
                icon = Icons.Filled.Person,
                label = "Profile",
                selected = currentRoute == Screen.Profile.route,
                onClick = {
                    navController.navigate(Screen.Profile.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val contentColor = if (selected) MaterialTheme.colorScheme.primary else Color.Gray

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
