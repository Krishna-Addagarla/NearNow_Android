package com.example.drift.ui.screens.invitation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drift.data.local.model.DiscoveryInvite
import com.example.drift.data.local.model.InviteCategory
import com.example.drift.ui.screens.discovery.DiscoveryBottomBar
import com.example.drift.ui.theme.Coral
import com.example.drift.ui.theme.Ink
import com.example.drift.ui.theme.Paper
import com.example.drift.ui.theme.Signal
import com.example.drift.ui.theme.Slate

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
            .background(Ink)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // Nav bar padding
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
                    color = Paper,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )

                // Coral CTA button: + POST
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Coral)
                        .clickable { onPostClick() }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "+ POST",
                        color = Paper,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Scrollable List of active invitations
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

        // Bottom Navigation Bar (Invites tab highlighted)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            DiscoveryBottomBar(
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
    // Card with Coral left border decoration
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF121A2B))
            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        // Left accent strip in Coral
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .align(Alignment.CenterVertically)
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
                // Circle initials avatar
                val userColor = Color(invite.user.avatarColorHex.removePrefix("#").toInt(16) or 0xFF000000.toInt())
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(userColor.copy(alpha = 0.2f))
                        .border(1.5.dp, userColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = invite.user.initials,
                        color = userColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    // Title in bold
                    Text(
                        text = invite.title,
                        color = Paper,
                        fontSize = 16.sp,
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
                            color = Slate,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "•",
                            color = Slate.copy(alpha = 0.5f),
                            fontSize = 12.sp
                        )
                        Text(
                            text = "${invite.distanceMeters}M",
                            color = Coral, // Distance is highlighted in Coral for invites
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "•",
                            color = Slate.copy(alpha = 0.5f),
                            fontSize = 12.sp
                        )
                        Text(
                            text = if (invite.repliesCount == 1) "1 reply" else "${invite.repliesCount} replies",
                            color = Slate,
                            fontSize = 12.sp
                        )
                    }
                }

                // UX Solution: Scannable category icon/text tag at top-right
                Box(
                    modifier = Modifier
                        .border(0.5.dp, Coral.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    val categoryLabel = when (invite.category) {
                        InviteCategory.COFFEE -> "☕ COFFEE"
                        InviteCategory.FOOD -> "🍜 FOOD"
                        InviteCategory.MOVIE -> "🎬 MOVIE"
                        InviteCategory.WALK -> "🚶 WALK"
                    }
                    Text(
                        text = categoryLabel,
                        color = Coral,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Post Description
            Text(
                text = invite.description,
                color = Paper.copy(alpha = 0.9f),
                fontSize = 14.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Post freshness marker (UX enhancement explaining 6h auto-expiry)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Expires in ~5h",
                    color = Slate.copy(alpha = 0.5f),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp
                )
                Text(
                    text = invite.relativeAgeText,
                    color = Slate.copy(alpha = 0.5f),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp
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
