package com.example.nearnow.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nearnow.data.local.model.DiscoveryUser
import com.example.nearnow.ui.components.*
import com.example.nearnow.ui.theme.*

@Composable
fun ProfileScreen(
    onBackClick: () -> Unit = {},
    onTabSelect: (String) -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    var radiusValue by remember { mutableFloatStateOf(2000f) }
    var isInvisibleMode by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    // Premium gold-themed gradient for avatar border & badges
    val goldGradient = Brush.linearGradient(
        colors = listOf(
            Mango,
            MangoLight
        )
    )

    // Dummy user for rendering avatar
    val dummyUser = remember {
        DiscoveryUser(
            id = "me",
            name = "Krish",
            age = 24,
            distanceMeters = 0,
            bio = "Android dev & UI enthusiast. Love discovering fresh coffee shops.",
            hasActiveStory = false,
            initials = "KR"
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

                // Profile Header Card
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Avatar Circle with Premium gold ring
                    NearNowAvatar(
                        user = dummyUser,
                        size = AvatarSize.XLARGE,
                        showStoryRing = true, // Premium pulse ring
                        modifier = Modifier.shadow(4.dp, CircleShape, spotColor = ShadowColor, ambientColor = ShadowColor)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // User Name & Age
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Krish",
                            color = TextPrimary,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "24",
                            color = TextSecondary,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Premium Badge Status
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(goldGradient)
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "NEARNOW PLUS MEMBER",
                            color = TextPrimary, // As per contrast rule
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Bio Box
                    Text(
                        text = "Android dev & UI enthusiast. Love discovering fresh coffee shops, minimal design systems, and chill house beats.",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 12.dp),
                        lineHeight = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Quick Statistics Row (NearNowCard style)
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
                            valueRange = 100f..50000f,
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
                            onCheckedChange = { isInvisibleMode = it },
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

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
