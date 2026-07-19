package com.example.nearnow.ui.screens.discovery

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nearnow.data.local.model.DiscoveryUser
import com.example.nearnow.ui.components.*
import com.example.nearnow.ui.theme.*

@Composable
fun DiscoveryListScreen(
    users: List<DiscoveryUser> = DiscoveryUser.mockUsers,
    onUserClick: (DiscoveryUser) -> Unit = {},
    onToggleView: () -> Unit = {},
    selectedTab: String = "Nearby",
    onTabSelect: (String) -> Unit = {}
) {
    // Sort users by distance ascending
    val sortedUsers = users.sortedBy { it.distanceMeters }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(bottom = 72.dp) // Leave room for bottom navigation
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Header Row: Title & Toggle Map Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Nearby",
                    color = TextPrimary,
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold
                )

                // Map View Toggle Button
                IconButton(
                    onClick = onToggleView,
                    modifier = Modifier
                        .size(44.dp)
                        .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = ShadowColor, ambientColor = ShadowColor)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardWhite)
                        .border(1.5.dp, SoftGray, RoundedCornerShape(12.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .border(2.dp, Mango, RoundedCornerShape(4.dp))
                    ) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(Mango)
                                .align(Alignment.Center)
                        )
                        Spacer(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(2.dp)
                                .background(Mango)
                                .align(Alignment.Center)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sub-header: Count and Radius Info
            Text(
                text = "${sortedUsers.size} NEARBY · SORTED BY DISTANCE",
                color = TextSecondary,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // LazyColumn containing nearby user rows with entry animations
            if (sortedUsers.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    NearNowEmptyState(
                        emojiSymbol = "🗺️",
                        title = "No users nearby",
                        subtitle = "Try expanding your discovery radius in settings."
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(sortedUsers) { user ->
                        var visible by remember { mutableStateOf(false) }
                        LaunchedEffect(Unit) {
                            visible = true
                        }
                        
                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(animationSpec = tween(300)) + slideInVertically(initialOffsetY = { 20 }),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            UserListRow(
                                user = user,
                                onClick = { onUserClick(user) }
                            )
                        }
                    }
                }
            }
        }

        // Bottom Navigation Bar
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            NearNowBottomNav(
                selectedTab = selectedTab,
                onTabClick = onTabSelect
            )
        }
    }
}

@Composable
fun UserListRow(
    user: DiscoveryUser,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(20.dp), spotColor = ShadowColor, ambientColor = ShadowColor)
            .clip(RoundedCornerShape(20.dp))
            .background(CardWhite)
            .border(1.dp, SoftGray, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Redesigned avatar component
        NearNowAvatar(
            user = user,
            size = AvatarSize.MEDIUM,
            showOnlineIndicator = user.isOnline,
            showStoryRing = user.hasActiveStory,
            showVerifiedBadge = user.isVerified
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Middle details
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "${user.name}, ${user.age}",
                    color = TextPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                if (user.isVerified) {
                    VerifiedBadge()
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Bio description preview
            val bioPreview = if (user.bio.length > 35) "${user.bio.take(32)}..." else user.bio
            Text(
                text = bioPreview,
                color = TextSecondary,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            // User Tags Row
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (user.hasActiveStory) {
                    NearNowActiveChip(label = "STORY")
                }
                if (user.hasInvite) {
                    NearNowCoralChip(label = "INVITE")
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Distance Badge
        NearNowTealChip(label = "${user.distanceMeters}M")
    }
}

@Preview(showBackground = true)
@Composable
fun DiscoveryListScreenPreview() {
    DiscoveryListScreen()
}
