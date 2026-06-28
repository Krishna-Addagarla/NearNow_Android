package com.example.drift.ui.screens.chat

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
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drift.data.local.model.ChatSession
import com.example.drift.data.local.model.ChatSessionStatus
import com.example.drift.ui.screens.discovery.DiscoveryBottomBar
import com.example.drift.ui.theme.Coral
import com.example.drift.ui.theme.Ink
import com.example.drift.ui.theme.Paper
import com.example.drift.ui.theme.Signal
import com.example.drift.ui.theme.Slate

@Composable
fun ChatListScreen(
    sessions: List<ChatSession> = ChatSession.mockSessions,
    onBackClick: () -> Unit = {},
    onSessionClick: (ChatSession) -> Unit = {},
    onTabSelect: (String) -> Unit = {}
) {
    var selectedListTab by remember { mutableStateOf("ACTIVE") }
    var selectedBottomTab by remember { mutableStateOf("Chats") }

    // Filter sessions based on list tab
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
            .background(Ink)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // Nav bar space
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1E293B))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Paper
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Chats",
                    color = Paper,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Horizontal Tabs row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 0.5.dp, color = Color(0xFF1A2438))
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
                            color = if (isTabSelected) Signal else Slate,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Hairline active indicator line
                        Box(
                            modifier = Modifier
                                .size(width = 48.dp, height = 2.dp)
                                .background(if (isTabSelected) Signal else Color.Transparent)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Chat Items list
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

        // Bottom Navigation Bar
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
fun ChatSessionRow(
    session: ChatSession,
    onClick: () -> Unit
) {
    val isPaused = session.status == ChatSessionStatus.PAUSED
    val lastMessage = session.messages.lastOrNull()?.text ?: "No messages yet"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (isPaused) 0.5f else 1.0f) // 50% opacity for paused chats
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF121A2B))
            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Initials avatar
        val baseColor = Color(session.user.avatarColorHex.removePrefix("#").toInt(16) or 0xFF000000.toInt())
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(baseColor.copy(alpha = 0.2f))
                .border(2.dp, baseColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = session.user.initials,
                color = baseColor,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Center section: User Name and Last Message
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = session.user.name,
                color = Paper,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = lastMessage,
                color = Slate,
                fontSize = 14.sp,
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Right side: Active distance readout in green mono or Paused tag in orange/coral outline
        if (isPaused) {
            Box(
                modifier = Modifier
                    .border(0.5.dp, Coral, RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "PAUSED",
                    color = Coral,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        } else {
            Text(
                text = "${session.user.distanceMeters}M",
                color = Signal,
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatListScreenPreview() {
    ChatListScreen()
}
