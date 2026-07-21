package com.example.nearnow.ui.screens.chat

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nearnow.data.local.model.ChatSession
import com.example.nearnow.data.local.model.ChatSessionStatus
import com.example.nearnow.ui.components.*
import com.example.nearnow.ui.theme.*

@Composable
fun ChatListScreen(
    sessions: List<ChatSession> = ChatSession.mockSessions,
    onBackClick: () -> Unit = {},
    onSessionClick: (ChatSession) -> Unit = {},
    onTabSelect: (String) -> Unit = {}
) {
    var selectedListTab by remember { mutableStateOf("ACTIVE") }
    var selectedBottomTab by remember { mutableStateOf("Chats") }

    val filteredSessions = remember(sessions, selectedListTab) {
        when (selectedListTab) {
            "ACTIVE" -> sessions.filter { it.status == ChatSessionStatus.ACTIVE || it.status == ChatSessionStatus.PAUSED }
            "REQUESTS" -> sessions.filter { it.status == ChatSessionStatus.REQUEST }
            "PERMANENT" -> sessions.filter { it.status == ChatSessionStatus.PERMANENT }
            else -> sessions
        }
    }

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

            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Chats",
                    color = TextPrimary,
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Horizontal Tabs row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = SoftGray)
                    .background(CardWhite)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                val listTabs = listOf("ACTIVE", "REQUESTS", "PERMANENT")
                listTabs.forEach { tabName ->
                    val isTabSelected = tabName == selectedListTab
                    val labelText = if (tabName == "REQUESTS") "REQUESTS · 3" else tabName

                    Column(
                        modifier = Modifier
                            .clickable { selectedListTab = tabName }
                            .padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = labelText,
                            color = if (isTabSelected) Mango else TextSecondary,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Box(
                            modifier = Modifier
                                .size(width = 48.dp, height = 3.dp)
                                .clip(RoundedCornerShape(1.5.dp))
                                .background(if (isTabSelected) Mango else Color.Transparent)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Chat Items list
            if (filteredSessions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    val subtitleText = when (selectedListTab) {
                        "ACTIVE" -> "Scan nearby to start live proximity chats."
                        "REQUESTS" -> "You have no incoming request messages."
                        else -> "Make connections permanent to chat anytime."
                    }
                    NearNowEmptyState(
                        emojiSymbol = "💬",
                        title = "No conversations here",
                        subtitle = subtitleText
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
                    items(filteredSessions) { session ->
                        ChatSessionRow(
                            session = session,
                            onClick = { onSessionClick(session) }
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
fun ChatSessionRow(
    session: ChatSession,
    onClick: () -> Unit
) {
    val isPaused = session.status == ChatSessionStatus.PAUSED
    val lastMessage = session.messages.lastOrNull()?.text ?: "No messages yet"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (isPaused) 0.6f else 1.0f)
            .shadow(2.dp, RoundedCornerShape(20.dp), spotColor = ShadowColor, ambientColor = ShadowColor)
            .clip(RoundedCornerShape(20.dp))
            .background(CardWhite)
            .border(1.dp, SoftGray, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Initials avatar circle replaced with NearNowAvatar
        NearNowAvatar(
            user = session.user,
            size = AvatarSize.MEDIUM,
            showOnlineIndicator = session.user.isOnline,
            showStoryRing = session.user.hasActiveStory,
            showVerifiedBadge = session.user.isVerified
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Center section: User Name and Last Message
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = session.user.name,
                color = TextPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = lastMessage,
                color = TextSecondary,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Right side: Active distance chip or Paused badge
        if (isPaused) {
            PausedBadge()
        } else {
            NearNowTealChip(label = "${session.user.distanceMeters}M")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatListScreenPreview() {
    ChatListScreen()
}
