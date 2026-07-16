package com.example.drift.ui.navigation

/**
 * Defines all routes and argument mapping in NearNow app.
 */
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object PhoneNumber : Screen("phone_number")
    data class Otp(val verificationId: String, val phoneNumber: String) : Screen("otp/$verificationId")
    
    // Profile setup multi-step flow
    object ProfileSetupName : Screen("profile_setup_name")
    object ProfileSetupPhoto : Screen("profile_setup_photo")
    object ProfileSetupInterests : Screen("profile_setup_interests")
    object ProfileSetupRadius : Screen("profile_setup_radius")
    
    // Core destinations
    object DiscoveryMap : Screen("discovery_map")
    object ChatList : Screen("chat_list")
    object InviteList : Screen("invite_list")
    
    // Dynamic/Parameterized sheets or fullscreen transitions
    data class ChatCompose(val userId: String) : Screen("chat_compose/$userId")
    data class ActiveChat(val sessionId: String) : Screen("active_chat/$sessionId")
    data class PausedChat(val sessionId: String) : Screen("paused_chat/$sessionId")
    data class Report(val userId: String) : Screen("report/$userId")
    
    object InviteCreate : Screen("invite_create")
    object Paywall : Screen("paywall")
    object Profile : Screen("profile")
}
