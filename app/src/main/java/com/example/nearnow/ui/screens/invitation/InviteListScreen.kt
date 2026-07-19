package com.example.nearnow.ui.screens.invitation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nearnow.data.local.model.DiscoveryInvite
import com.example.nearnow.data.local.model.InviteCategory
import com.example.nearnow.ui.components.*
import com.example.nearnow.ui.theme.*

@Composable
fun InviteListScreen(
    invites: List<DiscoveryInvite> = DiscoveryInvite.mockInvites,
    onPostClick: () -> Unit = {},
    onInviteClick: (DiscoveryInvite) -> Unit = {},
    onTabSelect: (String) -> Unit = {}
) {
    var selectedBottomTab by remember { mutableStateOf("Invites") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(bottom = 72.dp)
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Header Row: Title & Post button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Invitations",
                    color = TextPrimary,
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold
                )

                // Coral CTA button: + POST
                Box(
                    modifier = Modifier
                        .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = ShadowColor, ambientColor = ShadowColor)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Coral)
                        .clickable { onPostClick() }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "+ POST",
                        color = CardWhite,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Scrollable List of active invitations
            if (invites.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    NearNowEmptyState(
                        emojiSymbol = "✉️",
                        title = "No active invites",
                        subtitle = "Post an invitation to grab coffee or food with people nearby.",
                        actionText = "POST INVITE",
                        onAction = onPostClick
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(invites) { invite ->
                        InviteFeedCard(
                            invite = invite,
                            onClick = { onInviteClick(invite) }
                        )
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
                selectedTab = selectedBottomTab,
                onTabClick = {
                    selectedBottomTab = it
                    onTabSelect(it)
                }
            )
        }
    }
}

@Composable
fun InviteFeedCard(
    invite: DiscoveryInvite,
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
    ) {
        // Left accent strip in Coral
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(110.dp)
                .background(Coral)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Illustrated Avatar
                NearNowAvatar(
                    user = invite.user,
                    size = AvatarSize.SMALL,
                    showOnlineIndicator = invite.user.isOnline,
                    showStoryRing = invite.user.hasActiveStory,
                    showVerifiedBadge = invite.user.isVerified
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    // Title in bold
                    Text(
                        text = invite.title,
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    // Metadata row: Username • Distance (Coral) • Reply Count
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = invite.user.name,
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "•",
                            color = TextMuted,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "${invite.distanceMeters}M",
                            color = Coral,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "•",
                            color = TextMuted,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = if (invite.repliesCount == 1) "1 reply" else "${invite.repliesCount} replies",
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // scannable category icon/text tag at top-right
                val categoryLabel = when (invite.category) {
                    InviteCategory.COFFEE -> "☕ COFFEE"
                    InviteCategory.FOOD -> "🍜 FOOD"
                    InviteCategory.MOVIE -> "🎬 MOVIE"
                    InviteCategory.WALK -> "🚶 WALK"
                }
                NearNowCoralChip(label = categoryLabel)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Post Description
            Text(
                text = invite.description,
                color = TextPrimary,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Post freshness marker
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Expires in ~5h",
                    color = TextMuted,
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = invite.relativeAgeText,
                    color = TextMuted,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InviteListScreenPreview() {
    InviteListScreen()
}
