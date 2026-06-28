package com.example.drift.ui.screens.discovery

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drift.data.local.model.DiscoveryUser
import com.example.drift.ui.theme.Coral
import com.example.drift.ui.theme.Ink
import com.example.drift.ui.theme.Paper
import com.example.drift.ui.theme.Signal
import com.example.drift.ui.theme.Slate

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
            .background(Ink)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // Leave room for bottom navigation
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
                    color = Paper,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )

                // Map View Toggle Button (Square Outline/Background)
                IconButton(
                    onClick = onToggleView,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1E293B))
                        .border(1.dp, Color(0xFF334155), RoundedCornerShape(8.dp))
                ) {
                    // Simulated Map Screen Icon using simple text/shapes
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .border(1.5.dp, Signal, RoundedCornerShape(2.dp))
                    ) {
                        // Horizontal/vertical line segments to represent a map crosshair/split
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.5.dp)
                                .background(Signal)
                                .align(Alignment.Center)
                        )
                        Spacer(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(1.5.dp)
                                .background(Signal)
                                .align(Alignment.Center)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sub-header: Count and Radius Info
            Text(
                text = "${sortedUsers.size} NEARBY · SORTED BY DISTANCE",
                color = Slate,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // LazyColumn containing nearby scannable user rows
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(sortedUsers) { user ->
                    UserListRow(
                        user = user,
                        onClick = { onUserClick(user) }
                    )
                }
            }
        }

        // Bottom Navigation Bar
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            DiscoveryBottomBar(
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
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF121A2B))
            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Initials avatar circle
        val baseColor = Color(user.avatarColorHex.removePrefix("#").toInt(16) or 0xFF000000.toInt())
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(baseColor.copy(alpha = 0.2f))
                .border(2.dp, baseColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = user.initials,
                color = baseColor,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Middle text metadata: Name/Age, Bio, and Tags
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${user.name}, ${user.age}",
                    color = Paper,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                // Conditional tags
                if (user.hasActiveStory) {
                    Box(
                        modifier = Modifier
                            .background(Signal.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                            .border(0.5.dp, Signal, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "STORY",
                            color = Signal,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                if (user.hasInvite) {
                    Box(
                        modifier = Modifier
                            .background(Coral.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                            .border(0.5.dp, Coral, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "INVITE",
                            color = Coral,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Bio description preview (trimmed)
            val bioPreview = if (user.bio.length > 28) "${user.bio.take(25)}..." else user.bio
            Text(
                text = bioPreview,
                color = Slate,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Distance on right in mono font
        Text(
            text = "${user.distanceMeters}M",
            color = Slate,
            fontFamily = FontFamily.Monospace,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Shared Discovery Bottom Bar containing 4 tabs: Nearby, Invites, Chats, Me.
 */
@Composable
fun DiscoveryBottomBar(
    selectedTab: String,
    onTabClick: (String) -> Unit
) {
    val tabs = listOf("Nearby", "Invites", "Chats", "Me")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF070B14))
            .border(width = 0.5.dp, color = Color(0xFF1A2438))
            .windowInsetsPadding(WindowInsets.navigationBars)
            .height(68.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEach { tab ->
            val isSelected = tab == selectedTab
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onTabClick(tab) }
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Circular active indicator dot or highlight
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .background(Signal, CircleShape)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                } else {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Text(
                    text = tab,
                    color = if (isSelected) Signal else Slate.copy(alpha = 0.7f),
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiscoveryListScreenPreview() {
    DiscoveryListScreen()
}
