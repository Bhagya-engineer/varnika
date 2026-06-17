package com.example.ui.navigation

sealed class Screen(val route: String) {
    object Landing : Screen("landing")
    object Auth : Screen("auth")
    object Dashboard : Screen("dashboard")
    object UploadRoom : Screen("upload_room")
    object AIAnalysis : Screen("ai_analysis")
    object VirtualPreview : Screen("virtual_preview")
    object Marketplace : Screen("marketplace")
    object AssistantChat : Screen("assistant_chat")
    object Cart : Screen("cart")
    object Profile : Screen("profile")
    object Admin : Screen("admin")
}
