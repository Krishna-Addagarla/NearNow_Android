package com.example.nearnow.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.nearnow.data.local.SessionManager
import com.example.nearnow.data.remote.repository.ProfileRepository
import com.example.nearnow.data.remote.repository.DiscoveryRepository
import com.example.nearnow.data.remote.model.ProfileUpsert
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.nearnow.data.local.model.ChatSession
import com.example.nearnow.data.local.model.ChatSessionStatus
import com.example.nearnow.data.local.model.DiscoveryUser
import com.example.nearnow.ui.screens.authentication.Otp.OtpScreen
import com.example.nearnow.ui.screens.authentication.PhoneNumber.PhoneNumberScreen
import com.example.nearnow.ui.screens.chat.ActiveChatScreen
import com.example.nearnow.ui.screens.chat.ChatComposeScreen
import com.example.nearnow.ui.screens.chat.ChatListScreen
import com.example.nearnow.ui.screens.chat.PausedChatScreen
import com.example.nearnow.ui.screens.discovery.DiscoveryMapScreen
import com.example.nearnow.ui.screens.invitation.InviteCreateScreen
import com.example.nearnow.ui.screens.invitation.InviteListScreen
import com.example.nearnow.ui.screens.onBoaring.OnboardingScreen
import com.example.nearnow.ui.screens.onBoaring.SplashScreen
import com.example.nearnow.ui.screens.premium.PaywallScreen
import com.example.nearnow.ui.screens.profile.ProfileScreen
import com.example.nearnow.ui.screens.profileSetUp.ProfilePhotosScreen
import com.example.nearnow.ui.screens.profileSetUp.ProfileRadiusScreen
import com.example.nearnow.ui.screens.profileSetUp.ProfileStepScreen
import com.example.nearnow.ui.screens.profileSetUp.UserInterestsScreen
import com.example.nearnow.ui.screens.safety.ReportScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    sessionManager: SessionManager,
    profileRepository: ProfileRepository,
    discoveryRepository: DiscoveryRepository,
    modifier: Modifier = Modifier
) {
    var profileSetupName by remember { mutableStateOf("") }
    var profileSetupAge by remember { mutableStateOf(24) }
    var profileSetupPhotos by remember { mutableStateOf<List<String>>(emptyList()) }
    var profileSetupInterests by remember { mutableStateOf<List<String>>(emptyList()) }
    AnimatedContent(
        targetState = navController.currentScreen,
        transitionSpec = {
            val isTabTransition = (initialState is Screen.DiscoveryMap || initialState is Screen.InviteList || initialState is Screen.ChatList || initialState is Screen.Profile) &&
                                  (targetState is Screen.DiscoveryMap || targetState is Screen.InviteList || targetState is Screen.ChatList || targetState is Screen.Profile)
            if (isTabTransition) {
                fadeIn(animationSpec = tween(200, easing = LinearEasing)) togetherWith fadeOut(animationSpec = tween(200, easing = LinearEasing))
            } else {
                fadeIn(animationSpec = tween(300)) + slideInHorizontally(
                    initialOffsetX = { 200 },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ) togetherWith fadeOut(animationSpec = tween(150))
            }
        },
        label = "screen_navigation"
    ) { screen ->
        Box(modifier = modifier.fillMaxSize()) {
            when (screen) {
                is Screen.Splash -> {
                    SplashScreen()
                    // Auto transition to Onboarding slide after delay
                    LaunchedEffect(Unit) {
                        delay(2000)
                        if (sessionManager.getToken() != null) {
                            navController.clearAndNavigate(Screen.DiscoveryMap)
                        } else {
                            navController.navigate(Screen.Onboarding)
                        }
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
                        onSendCodeSuccess = { verificationId, fullPhone ->
                            navController.navigate(Screen.Otp(verificationId, fullPhone))
                        }
                    )
                }
                
                is Screen.Otp -> {
                    OtpScreen(
                        verificationId = screen.verificationId,
                        phoneNumber = screen.phoneNumber,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onVerifySuccess = { token, hasProfile ->
                            sessionManager.saveToken(token)
                            if (hasProfile) {
                                navController.clearAndNavigate(Screen.DiscoveryMap)
                            } else {
                                navController.navigate(Screen.ProfileSetupName)
                            }
                        },
                        onResendClick = {}
                    )
                }
                
                is Screen.ProfileSetupName -> {
                    ProfileStepScreen(
                        onContinueClick = { firstName, age ->
                            profileSetupName = firstName
                            profileSetupAge = age.toIntOrNull() ?: 24
                            navController.navigate(Screen.ProfileSetupPhoto)
                        }
                    )
                }
                
                is Screen.ProfileSetupPhoto -> {
                    ProfilePhotosScreen(
                        onContinueClick = { filledSlots ->
                            profileSetupPhotos = filledSlots.mapIndexedNotNull { index, isFilled ->
                                if (isFilled) "mock_photo_${index + 1}" else null
                            }
                            navController.navigate(Screen.ProfileSetupInterests)
                        },
                        onLaterClick = {
                            profileSetupPhotos = emptyList()
                            navController.navigate(Screen.ProfileSetupInterests)
                        }
                    )
                }
                
                is Screen.ProfileSetupInterests -> {
                    UserInterestsScreen(
                        maxSelectable = 6,
                        onContinueClick = { selected ->
                            profileSetupInterests = selected
                            navController.navigate(Screen.ProfileSetupRadius)
                        }
                    )
                }
                
                is Screen.ProfileSetupRadius -> {
                    var isSubmitting by remember { mutableStateOf(false) }
                    val context = LocalContext.current
                    val coroutineScope = rememberCoroutineScope()
                    ProfileRadiusScreen(
                        isSubmitting = isSubmitting,
                        onStartExploringClick = { radiusMeters ->
                            isSubmitting = true
                            val birthYear = 2026 - profileSetupAge
                            val photoUrl = profileSetupPhotos.firstOrNull()
                            val profileUpsert = ProfileUpsert(
                                displayName = profileSetupName,
                                birthYear = birthYear,
                                gender = "Other",
                                bio = "Hey! I am using NearNow.",
                                photoUrl = photoUrl,
                                interestedIn = profileSetupInterests,
                                minAge = 18,
                                maxAge = 60,
                                discoveryRadiusM = radiusMeters
                            )
                            coroutineScope.launch {
                                profileRepository.updateMyProfile(profileUpsert)
                                    .onSuccess {
                                        isSubmitting = false
                                        navController.clearAndNavigate(Screen.DiscoveryMap)
                                    }
                                    .onFailure { err ->
                                        isSubmitting = false
                                        Toast.makeText(context, "Failed to create profile: ${err.localizedMessage}", Toast.LENGTH_LONG).show()
                                    }
                            }
                        }
                    )
                }
                
                is Screen.DiscoveryMap -> {
                    DiscoveryMapScreen(
                        profileRepository = profileRepository,
                        discoveryRepository = discoveryRepository,
                        onNavigateToTab = { tab ->
                            when (tab) {
                                "Chats" -> navController.navigate(Screen.ChatList)
                                "Invites" -> navController.navigate(Screen.InviteList)
                                "Me" -> navController.navigate(Screen.Profile)
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
                                "Me" -> navController.navigate(Screen.Profile)
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
                            // Request Limit Paywall Simulation
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
                            // Triggers connection approval simulation
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
                                "Me" -> navController.navigate(Screen.Profile)
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
                            navController.popBackStack()
                        }
                    )
                }
                
                is Screen.Profile -> {
                    ProfileScreen(
                        profileRepository = profileRepository,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onTabSelect = { tab ->
                            when (tab) {
                                "Nearby" -> navController.clearAndNavigate(Screen.DiscoveryMap)
                                "Invites" -> navController.clearAndNavigate(Screen.InviteList)
                                "Chats" -> navController.clearAndNavigate(Screen.ChatList)
                            }
                        },
                        onLogoutClick = {
                            sessionManager.clear()
                            navController.clearAndNavigate(Screen.PhoneNumber)
                        },
                        onProfileNotFound = {
                            navController.clearAndNavigate(Screen.ProfileSetupName)
                        }
                    )
                }
            }
        }
    }
}
