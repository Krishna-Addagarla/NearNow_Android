package com.example.nearnow.ui.screens.profile

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nearnow.data.local.model.DiscoveryUser
import com.example.nearnow.data.remote.model.ProfileResponse
import com.example.nearnow.data.remote.model.ProfileUpsert
import com.example.nearnow.data.remote.repository.ProfileRepository
import com.example.nearnow.ui.components.*
import com.example.nearnow.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    profileRepository: ProfileRepository,
    onBackClick: () -> Unit = {},
    onTabSelect: (String) -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onProfileNotFound: () -> Unit = {}
) {
    var radiusValue by remember { mutableFloatStateOf(2000f) }
    var isInvisibleMode by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var profileState by remember { mutableStateOf<ProfileResponse?>(null) }
    var isLoadingProfile by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    // Edit Form States
    var isEditMode by remember { mutableStateOf(false) }
    var editName by remember { mutableStateOf("") }
    var editBirthYear by remember { mutableStateOf("") }
    var editBio by remember { mutableStateOf("") }
    var editPhotoUrl by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        profileRepository.getMyProfile()
            .onSuccess { profile ->
                profileState = profile
                radiusValue = profile.discoveryRadiusM.toFloat()
                isInvisibleMode = profile.isInvisible
                isLoadingProfile = false
            }
            .onFailure { err ->
                if ((err is retrofit2.HttpException && err.code() == 404) || err.message?.contains("404") == true) {
                    onProfileNotFound()
                } else {
                    errorMsg = err.localizedMessage ?: "Failed to load profile"
                    isLoadingProfile = false
                }
            }
    }

    LaunchedEffect(isEditMode) {
        if (isEditMode) {
            profileState?.let { profile ->
                editName = profile.displayName
                editBirthYear = profile.birthYear.toString()
                editBio = profile.bio ?: ""
                editPhotoUrl = profile.photoUrl ?: ""
            }
        }
    }

    // Premium gold-themed gradient for avatar border & badges
    val goldGradient = Brush.linearGradient(
        colors = listOf(
            Mango,
            MangoLight
        )
    )

    if (isLoadingProfile) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Cream),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Mango)
        }
    } else if (errorMsg != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Cream)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = errorMsg ?: "An error occurred",
                    color = Coral,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(24.dp))
                NearNowPrimaryButton(
                    text = "RETRY",
                    onClick = {
                        isLoadingProfile = true
                        errorMsg = null
                        coroutineScope.launch {
                            profileRepository.getMyProfile()
                                .onSuccess { profile ->
                                    profileState = profile
                                    radiusValue = profile.discoveryRadiusM.toFloat()
                                    isInvisibleMode = profile.isInvisible
                                    isLoadingProfile = false
                                }
                                .onFailure { err ->
                                    errorMsg = err.localizedMessage ?: "Failed to load profile"
                                    isLoadingProfile = false
                                }
                        }
                    }
                )
            }
        }
    } else {
        val profile = profileState!!
        val calculatedAge = 2026 - profile.birthYear
        val initials = if (profile.displayName.length >= 2) {
            profile.displayName.substring(0, 2).uppercase()
        } else {
            profile.displayName.uppercase()
        }
        val displayUser = remember(profile) {
            DiscoveryUser(
                id = profile.userId.toString(),
                name = profile.displayName,
                age = calculatedAge,
                distanceMeters = 0,
                bio = profile.bio ?: "",
                hasActiveStory = false,
                initials = initials,
                photoUrls = if (profile.photoUrl != null) listOf(profile.photoUrl) else emptyList()
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Cream)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 24.dp)
                ) {
                    Spacer(modifier = Modifier.height(56.dp))

                    if (!isEditMode) {
                        // --- VIEW MODE ---
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            NearNowAvatar(
                                user = displayUser,
                                size = AvatarSize.XLARGE,
                                showStoryRing = true,
                                modifier = Modifier.shadow(4.dp, CircleShape, spotColor = ShadowColor, ambientColor = ShadowColor)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = profile.displayName,
                                    color = TextPrimary,
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = calculatedAge.toString(),
                                    color = TextSecondary,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(goldGradient)
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "NEARNOW PLUS MEMBER",
                                    color = TextPrimary,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = profile.bio ?: "No bio set yet. Click Edit Profile to add one!",
                                color = TextSecondary,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 12.dp),
                                lineHeight = 20.sp
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            NearNowSecondaryButton(
                                text = "EDIT PROFILE",
                                onClick = { isEditMode = true },
                                modifier = Modifier.width(180.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        // Quick Statistics Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            val stats = listOf(
                                Triple("12", "Sent", Coral),
                                Triple("154", "Scans", Mango),
                                Triple("4", "Chats", Teal)
                            )

                            stats.forEach { (count, label, color) ->
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .shadow(2.dp, RoundedCornerShape(16.dp), spotColor = ShadowColor, ambientColor = ShadowColor)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(CardWhite)
                                        .border(1.dp, SoftGray, RoundedCornerShape(16.dp))
                                        .padding(vertical = 14.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = count,
                                        color = color,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = PoppinsFamily
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = label.uppercase(),
                                        color = TextSecondary,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        // Settings & Configuration Section
                        Text(
                            text = "DISCOVERY SETTINGS",
                            color = TextMuted,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Radius Adjustment Card
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(2.dp, RoundedCornerShape(20.dp), spotColor = ShadowColor, ambientColor = ShadowColor)
                                .clip(RoundedCornerShape(20.dp))
                                .background(CardWhite)
                                .border(1.dp, SoftGray, RoundedCornerShape(20.dp))
                                .padding(16.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Discovery Radius",
                                        color = TextPrimary,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "${radiusValue.toInt()}m",
                                        color = Mango,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Slider(
                                    value = radiusValue,
                                    onValueChange = { radiusValue = it },
                                    onValueChangeFinished = {
                                        coroutineScope.launch {
                                            val profileUpsert = ProfileUpsert(
                                                displayName = profile.displayName,
                                                birthYear = profile.birthYear,
                                                gender = profile.gender,
                                                bio = profile.bio,
                                                photoUrl = profile.photoUrl,
                                                interestedIn = profile.interestedIn,
                                                minAge = profile.minAge,
                                                maxAge = profile.maxAge,
                                                discoveryRadiusM = radiusValue.toInt()
                                            )
                                            profileRepository.updateMyProfile(profileUpsert)
                                                .onSuccess { profileState = it }
                                        }
                                    },
                                    valueRange = 100f..15000f,
                                    colors = SliderDefaults.colors(
                                        thumbColor = Mango,
                                        activeTrackColor = Mango,
                                        inactiveTrackColor = SoftGray
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Invisible Mode Toggle Card
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(2.dp, RoundedCornerShape(20.dp), spotColor = ShadowColor, ambientColor = ShadowColor)
                                .clip(RoundedCornerShape(20.dp))
                                .background(CardWhite)
                                .border(1.dp, SoftGray, RoundedCornerShape(20.dp))
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Ghost Mode",
                                        color = TextPrimary,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "Go invisible on the live map",
                                        color = TextSecondary,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                Switch(
                                    checked = isInvisibleMode,
                                    onCheckedChange = { checked ->
                                        isInvisibleMode = checked
                                        coroutineScope.launch {
                                            val profileUpsert = ProfileUpsert(
                                                displayName = profile.displayName,
                                                birthYear = profile.birthYear,
                                                gender = profile.gender,
                                                bio = profile.bio,
                                                photoUrl = profile.photoUrl,
                                                interestedIn = profile.interestedIn,
                                                minAge = profile.minAge,
                                                maxAge = profile.maxAge,
                                                discoveryRadiusM = radiusValue.toInt()
                                            )
                                            profileRepository.updateMyProfile(profileUpsert)
                                                .onSuccess { profileState = it }
                                        }
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = CardWhite,
                                        checkedTrackColor = Mango,
                                        uncheckedThumbColor = TextMuted,
                                        uncheckedTrackColor = SoftGray
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Standard Setting Items List
                        Text(
                            text = "ACCOUNT",
                            color = TextMuted,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(2.dp, RoundedCornerShape(20.dp), spotColor = ShadowColor, ambientColor = ShadowColor)
                                .clip(RoundedCornerShape(20.dp))
                                .background(CardWhite)
                                .border(1.dp, SoftGray, RoundedCornerShape(20.dp))
                        ) {
                            val menuItems = listOf(
                                Pair(Icons.Default.Settings, "Account Settings"),
                                Pair(Icons.Default.Share, "Invite Friends"),
                                Pair(Icons.Default.Info, "Safety & Support")
                            )

                            menuItems.forEachIndexed { index, (icon, label) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { /* Navigate to setting detail */ }
                                        .padding(horizontal = 16.dp, vertical = 14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(imageVector = icon, contentDescription = label, tint = TextSecondary, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(14.dp))
                                    Text(text = label, color = TextPrimary, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                                    Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null, tint = TextSecondary)
                                }
                                if (index < menuItems.lastIndex) {
                                    HorizontalDivider(color = SoftGray, thickness = 1.dp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        // Sign Out Primary CTA
                        NearNowGhostButton(
                            text = "LOG OUT",
                            onClick = onLogoutClick,
                            textColor = Coral,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                    } else {
                        // --- EDIT MODE ---
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            Text(
                                text = "EDIT DETAILS",
                                color = TextPrimary,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )

                            ProfileEditField(
                                label = "Display Name",
                                value = editName,
                                onValueChange = { editName = it },
                                placeholder = "Your display name"
                            )

                            ProfileEditField(
                                label = "Birth Year",
                                value = editBirthYear,
                                onValueChange = { editBirthYear = it },
                                placeholder = "e.g. 2002",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )

                            ProfileEditField(
                                label = "Bio",
                                value = editBio,
                                onValueChange = { editBio = it },
                                placeholder = "Tell others about yourself",
                                singleLine = false
                            )

                            ProfileEditField(
                                label = "Photo URL",
                                value = editPhotoUrl,
                                onValueChange = { editPhotoUrl = it },
                                placeholder = "Link to your avatar picture"
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                NearNowSecondaryButton(
                                    text = "CANCEL",
                                    onClick = { isEditMode = false },
                                    enabled = !isSaving,
                                    modifier = Modifier.weight(1f)
                                )

                                NearNowPrimaryButton(
                                    text = "SAVE",
                                    onClick = {
                                        val birthYearInt = editBirthYear.toIntOrNull()
                                        if (birthYearInt == null || birthYearInt < 1920 || birthYearInt > 2026) {
                                            Toast.makeText(context, "Please enter a valid birth year", Toast.LENGTH_SHORT).show()
                                            return@NearNowPrimaryButton
                                        }
                                        if (editName.trim().isEmpty()) {
                                            Toast.makeText(context, "Display Name cannot be empty", Toast.LENGTH_SHORT).show()
                                            return@NearNowPrimaryButton
                                        }
                                        isSaving = true
                                        val profileUpsert = ProfileUpsert(
                                            displayName = editName,
                                            birthYear = birthYearInt,
                                            gender = profile.gender,
                                            bio = editBio.takeIf { it.isNotEmpty() },
                                            photoUrl = editPhotoUrl.takeIf { it.isNotEmpty() },
                                            interestedIn = profile.interestedIn,
                                            minAge = profile.minAge,
                                            maxAge = profile.maxAge,
                                            discoveryRadiusM = radiusValue.toInt()
                                        )
                                        coroutineScope.launch {
                                            profileRepository.updateMyProfile(profileUpsert)
                                                .onSuccess { updatedProfile ->
                                                    profileState = updatedProfile
                                                    isEditMode = false
                                                    isSaving = false
                                                    Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                                                }
                                                .onFailure { err ->
                                                    isSaving = false
                                                    Toast.makeText(context, "Error updating profile: ${err.localizedMessage}", Toast.LENGTH_LONG).show()
                                                }
                                        }
                                    },
                                    enabled = !isSaving,
                                    isLoading = isSaving,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(48.dp))
                    }
                }

                // Bottom Navigation Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    NearNowBottomNav(
                        selectedTab = "Me",
                        onTabClick = onTabSelect
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileEditField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label.uppercase(),
            color = TextMuted,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (singleLine) 52.dp else 100.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(FieldFill)
                .border(1.dp, SoftGray, RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = if (singleLine) 0.dp else 12.dp),
            contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(
                    color = TextPrimary,
                    fontFamily = PoppinsFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                ),
                singleLine = singleLine,
                keyboardOptions = keyboardOptions,
                cursorBrush = SolidColor(Mango),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = TextMuted,
                            fontFamily = PoppinsFamily,
                            fontSize = 15.sp
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    val mockRepo = object : ProfileRepository {
        override suspend fun getMyProfile(): Result<ProfileResponse> = Result.success(
            ProfileResponse(
                id = 1,
                userId = 1,
                displayName = "Krish",
                birthYear = 2002,
                gender = "Male",
                bio = "Android dev & UI enthusiast. Love discovering fresh coffee shops.",
                photoUrl = null,
                interestedIn = emptyList(),
                minAge = 18,
                maxAge = 60,
                discoveryRadiusM = 2000,
                isInvisible = false,
                createdAt = "2026-07-21"
            )
        )
        override suspend fun updateMyProfile(profile: ProfileUpsert): Result<ProfileResponse> = Result.success(
            ProfileResponse(
                id = 1,
                userId = 1,
                displayName = profile.displayName,
                birthYear = profile.birthYear,
                gender = profile.gender,
                bio = profile.bio,
                photoUrl = profile.photoUrl,
                interestedIn = profile.interestedIn,
                minAge = profile.minAge,
                maxAge = profile.maxAge,
                discoveryRadiusM = profile.discoveryRadiusM,
                isInvisible = false,
                createdAt = "2026-07-21"
            )
        )
    }
    ProfileScreen(profileRepository = mockRepo)
}

