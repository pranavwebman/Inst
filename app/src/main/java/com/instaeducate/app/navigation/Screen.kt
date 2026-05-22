package com.instaeducate.app.navigation

sealed class Screen(val route: String) {
    // Auth Routes
    object Login : Screen("login")
    object Signup : Screen("signup")
    object ForgotPassword : Screen("forgot_password")
    object Onboarding : Screen("onboarding")

    // Main App Bottom Navigation Tabs
    object Home : Screen("home")
    object Reels : Screen("reels")
    object Upload : Screen("upload")
    object Communities : Screen("communities")
    object Profile : Screen("profile")

    // Secondary Pages
    object Chat : Screen("chat")
    object AIAssistant : Screen("ai_assistant")
}
