package com.example.drift.ui.navigation

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.drift.data.local.model.ChatSession
import com.example.drift.data.local.model.ChatSessionStatus
import com.example.drift.data.local.model.DiscoveryUser
import com.example.drift.ui.screens.authentication.Otp.OtpScreen
import com.example.drift.ui.screens.authentication.PhoneNumber.PhoneNumberScreen
import com.example.drift.ui.screens.chat.ActiveChatScreen
import com.example.drift.ui.screens.chat.ChatComposeScreen
import com.example.drift.ui.screens.chat.ChatListScreen
import com.example.drift.ui.screens.chat.PausedChatScreen
import com.example.drift.ui.screens.discovery.DiscoveryMapScreen
import com.example.drift.ui.screens.invitation.InviteCreateScreen
import com.example.drift.ui.screens.invitation.InviteListScreen
import com.example.drift.ui.screens.onBoaring.OnboardingScreen
import com.example.drift.ui.screens.onBoaring.SplashScreen
import com.example.drift.ui.screens.premium.PaywallScreen
import com.example.drift.ui.screens.profileSetUp.ProfilePhotosScreen
import com.example.drift.ui.screens.profileSetUp.ProfileRadiusScreen
import com.example.drift.ui.screens.profileSetUp.ProfileStepScreen
import com.example.drift.ui.screens.profileSetUp.UserInterestsScreen
import com.example.drift.ui.screens.safety.ReportScreen
import kotlinx.coroutines.delay

/**
 * Custom state-based NavController for simple Compose-only navigation
 * with full backstack and history preservation.
 */
class NavController(initialScreen: Screen = Screen.Splash) {
    var currentScreen by mutableStateOf<Screen>(initialScreen)
    private val backStack = mutableStateListOf<Screen>(initialScreen)

    fun navigate(screen: Screen) {
        // Prevent duplicate route stacking
        if (backStack.isNotEmpty() && backStack.last().route == screen.route) return
        
        backStack.add(screen)
        currentScreen = screen
    }

    fun popBackStack(): Boolean {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
            currentScreen = backStack.last()
            return true
        }
        return false
    }

    fun clearAndNavigate(screen: Screen) {
        backStack.clear()
        backStack.add(screen)
        currentScreen = screen
    }
}

@Composable
fun rememberNavController(initialScreen: Screen = Screen.Splash): NavController {
    return remember { NavController(initialScreen) }
}

@Composable
fun NavHost(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (val screen = navController.currentScreen) {
            is Screen.Splash -> {
                SplashScreen()
                // Auto transition to Onboarding slide after delay
                LaunchedEffect(Unit) {
                    delay(2000)
                    navController.navigate(Screen.Onboarding)
                }
            }
            
            is Screen.Onboarding -> {
                OnboardingScreen(
                    onFinish = {
                        navController.navigate(Screen.PhoneNumber)
                    },
                    onLocationPermissionRequested = {
                        navController.navigate(Screen.PhoneNumber)
                    }
                )
            }
            
            is Screen.PhoneNumber -> {
                PhoneNumberScreen(
                    onSendCodeClick = { countryCode, phone ->
                        navController.navigate(Screen.Otp)
                    }
                )
            }
            
            is Screen.Otp -> {
                OtpScreen(
                    phoneNumber = "+91 98765 00001",
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onVerifyClick = { code ->
                        navController.navigate(Screen.ProfileSetupName)
                    },
                    onResendClick = {}
                )
            }
            
            is Screen.ProfileSetupName -> {
                ProfileStepScreen(
                    onContinueClick = { firstName, age ->
                        navController.navigate(Screen.ProfileSetupPhoto)
                    }
                )
            }
            
            is Screen.ProfileSetupPhoto -> {
                ProfilePhotosScreen(
                    onContinueClick = { filledSlots ->
                        navController.navigate(Screen.ProfileSetupInterests)
                    },
                    onLaterClick = {
                        navController.navigate(Screen.ProfileSetupInterests)
                    }
                )
            }
            
            is Screen.ProfileSetupInterests -> {
                UserInterestsScreen(
                    maxSelectable = 6,
                    onContinueClick = { selected ->
                        navController.navigate(Screen.ProfileSetupRadius)
                    }
                )
            }
            
            is Screen.ProfileSetupRadius -> {
                ProfileRadiusScreen(
                    onStartExploringClick = { radiusMeters ->
                        navController.clearAndNavigate(Screen.DiscoveryMap)
                    }
                )
            }
            
            is Screen.DiscoveryMap -> {
                DiscoveryMapScreen(
                    onNavigateToTab = { tab ->
                        when (tab) {
                            "Chats" -> navController.navigate(Screen.ChatList)
                            "Invites" -> navController.navigate(Screen.InviteList)
                        }
                    },
                    onNavigateToChatCompose = { userId ->
                        navController.navigate(Screen.ChatCompose(userId))
                    },
                    onNavigateToActiveChat = { sessionId ->
                        navController.navigate(Screen.ActiveChat(sessionId))
                    }
                )
            }
            
            is Screen.ChatList -> {
                ChatListScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onSessionClick = { session ->
                        if (session.status == ChatSessionStatus.PAUSED) {
                            navController.navigate(Screen.PausedChat(session.id))
                        } else {
                            navController.navigate(Screen.ActiveChat(session.id))
                        }
                    },
                    onTabSelect = { tab ->
                        when (tab) {
                            "Nearby" -> navController.clearAndNavigate(Screen.DiscoveryMap)
                            "Invites" -> navController.clearAndNavigate(Screen.InviteList)
                        }
                    }
                )
            }
            
            is Screen.ChatCompose -> {
                val targetUser = DiscoveryUser.mockUsers.firstOrNull { it.id == screen.userId }
                    ?: DiscoveryUser.mockUsers.first()
                
                ChatComposeScreen(
                    user = targetUser,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onSendRequest = { text ->
                        // Request Limit Paywall Simulation: If 5th message is sent, trigger limit paywall!
                        navController.navigate(Screen.Paywall)
                    }
                )
            }
            
            is Screen.ActiveChat -> {
                val targetSession = ChatSession.mockSessions.firstOrNull { it.id == screen.sessionId }
                    ?: ChatSession.mockSessions.first()

                ActiveChatScreen(
                    session = targetSession,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onSendClick = {},
                    onEtaClick = {} // Triggers in-chat meetup mode internally
                )
            }
            
            is Screen.PausedChat -> {
                val targetSession = ChatSession.mockSessions.firstOrNull { it.id == screen.sessionId }
                    ?: ChatSession.mockSessions.first()

                PausedChatScreen(
                    session = targetSession,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onConnectPermanentlyClick = {
                        // Triggers pending transition then returns
                    }
                )
            }
            
            is Screen.InviteList -> {
                InviteListScreen(
                    onPostClick = {
                        navController.navigate(Screen.InviteCreate)
                    },
                    onInviteClick = { invite ->
                        // Show details or compose response
                    },
                    onTabSelect = { tab ->
                        when (tab) {
                            "Nearby" -> navController.clearAndNavigate(Screen.DiscoveryMap)
                            "Chats" -> navController.clearAndNavigate(Screen.ChatList)
                        }
                    }
                )
            }
            
            is Screen.InviteCreate -> {
                InviteCreateScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onPostInvite = { title, desc, cat, rad ->
                        navController.clearAndNavigate(Screen.InviteList)
                    }
                )
            }
            
            is Screen.Report -> {
                ReportScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onReportAndBlockClick = { reason ->
                        navController.clearAndNavigate(Screen.DiscoveryMap)
                    },
                    onCancelClick = {
                        navController.popBackStack()
                    }
                )
            }
            
            is Screen.Paywall -> {
                PaywallScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onStartTrialClick = { tier ->
                        // Unlock benefits and clear paywall stack
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
