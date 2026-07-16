package com.example.nearnow.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nearnow.data.local.model.ChatMessage
import com.example.nearnow.data.local.model.ChatSession
import com.example.nearnow.ui.theme.Coral
import com.example.nearnow.ui.theme.Ink
import com.example.nearnow.ui.theme.Paper
import com.example.nearnow.ui.theme.Signal
import com.example.nearnow.ui.theme.Slate
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset

@Composable
fun ActiveChatScreen(
    session: ChatSession = ChatSession.mockSessions.first(),
    onBackClick: () -> Unit = {},
    onSendClick: (String) -> Unit = {},
    onEtaClick: () -> Unit = {}
) {
    var textState by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf(*session.messages.toTypedArray()) }
    var currentDistance by remember { mutableStateOf(session.user.distanceMeters) }
    var meetupModeActive by remember { mutableStateOf(false) }

    val isOut = currentDistance > 500
    val showPausedState = isOut && !meetupModeActive

    if (showPausedState) {
        // Automatically switch rendering to PausedChatScreen inline!
        PausedChatScreen(
            session = session.copy(
                user = session.user.copy(distanceMeters = currentDistance),
                status = com.example.nearnow.data.local.model.ChatSessionStatus.PAUSED
            ),
            onBackClick = onBackClick,
            onConnectPermanentlyClick = {
                // Clicking Connect Permanently resets distance to simulate acceptance
                currentDistance = 180
            }
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Ink)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(48.dp))

                // Header Top Bar
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

                    Spacer(modifier = Modifier.width(12.dp))

                    // User Avatar
                    val avatarColor = Color(session.user.avatarColorHex.removePrefix("#").toInt(16) or 0xFF000000.toInt())
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(avatarColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = session.user.initials,
                            color = Paper,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // User details with interactive distance testing controls
                    Column {
                        Text(
                            text = session.user.name,
                            color = Paper,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "• ${currentDistance}M - ${if (meetupModeActive) "GRACE" else "LIVE"}",
                                color = if (meetupModeActive) Coral else Signal,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            // Dynamic test triggers for distance changes
                            Text(
                                text = "[ -50M ]",
                                color = Slate,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clickable {
                                        currentDistance = (currentDistance - 50).coerceAtLeast(10)
                                    }
                            )
                            Text(
                                text = "[ +50M ]",
                                color = Slate,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clickable {
                                        currentDistance = (currentDistance + 50).coerceAtMost(990)
                                    }
                            )
                        }
                    }
                }

                ProximityTicker(distance = currentDistance, meetupModeActive = meetupModeActive)

                // Scrolling Chat messages
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp)
                ) {
                    item {
                        Text(
                            text = "TODAY 4:22 PM",
                            color = Slate.copy(alpha = 0.6f),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }

                    items(messages) { message ->
                        MessageBubble(message = message)
                    }

                    // ETA suggestion pill helper at the end
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        if (!meetupModeActive) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .border(1.dp, Coral, RoundedCornerShape(10.dp))
                                    .clickable {
                                        meetupModeActive = true
                                        onEtaClick()
                                    }
                                    .padding(14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "MEETING SOON? SHARE YOUR ETA →",
                                    color = Coral,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        } else {
                            // Display active meetup indicator card
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF0F1E19))
                                    .border(1.dp, Signal, RoundedCornerShape(10.dp))
                                    .padding(14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(text = "🤝", fontSize = 16.sp)
                                    Text(
                                        text = "ETA SHARED (MEETUP MODE ENABLED)",
                                        color = Signal,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Message Input bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF070B14))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .navigationBarsPadding()
                        .imePadding(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Text Area Input Box
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(0xFF1E293B))
                            .border(1.dp, Color(0xFF334155), RoundedCornerShape(24.dp))
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        BasicTextField(
                            value = textState,
                            onValueChange = { textState = it },
                            textStyle = TextStyle(
                                color = Paper,
                                fontSize = 15.sp
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            decorationBox = { innerTextField ->
                                if (textState.isEmpty()) {
                                    Text(
                                        text = "Message ${session.user.name}...",
                                        color = Slate.copy(alpha = 0.5f),
                                        fontSize = 15.sp
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }

                    // Square Send Button
                    IconButton(
                        onClick = {
                            if (textState.isNotEmpty()) {
                                messages.add(
                                    ChatMessage(
                                        id = "m_temp_${messages.size}",
                                        senderId = "me",
                                        text = textState,
                                        timestampText = "Just now",
                                        isOutgoing = true
                                    )
                                )
                                onSendClick(textState)
                                textState = ""
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Signal)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = Ink
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProximityTicker(
    distance: Int,
    meetupModeActive: Boolean
) {
    val maxRadius = 500f // 500m base limit
    val pct = (distance / maxRadius).coerceIn(0f, 1f)

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    val color = when {
        meetupModeActive -> Coral
        distance >= 450 -> Coral
        else -> Signal
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF0F172A))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Pulsing connection status row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (meetupModeActive) Coral.copy(alpha = pulseAlpha) else Color(0xFF00E6A8).copy(alpha = pulseAlpha))
                    )
                    Text(
                        text = if (meetupModeActive) "MEETUP ACTIVE" else "LIVE PROXIMITY CONNECTION",
                        color = Paper,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 0.5.sp
                    )
                }

                Text(
                    text = "${distance}m",
                    color = color,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp
                )
            }

            // Proximity track line mapping relative distance
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
            ) {
                val trackY = size.height / 2
                val startX = 10f
                val endX = size.width - 10f
                val widthRange = endX - startX

                // Draw background track line
                drawLine(
                    color = Color(0xFF1E293B),
                    start = Offset(startX, trackY),
                    end = Offset(endX, trackY),
                    strokeWidth = 6f
                )

                // Draw active connection range line
                val matchX = startX + (widthRange * (1f - pct))
                drawLine(
                    color = color.copy(alpha = 0.7f),
                    start = Offset(startX, trackY),
                    end = Offset(matchX, trackY),
                    strokeWidth = 6f
                )

                // Draw Self node (You)
                drawCircle(
                    color = Paper,
                    radius = 8f,
                    center = Offset(startX, trackY)
                )

                // Draw Match node
                drawCircle(
                    color = color,
                    radius = 8f,
                    center = Offset(matchX, trackY)
                )
            }

            // Dynamic distance readout status subtitle
            Text(
                text = when {
                    meetupModeActive -> "Grace active · Closing in for real-world meeting"
                    distance < 200 -> "Sweet spot reached · Proximity overlap healthy"
                    distance >= 450 -> "Warning · Moving out of range soon"
                    else -> "Connected · Stable signal range"
                },
                color = Slate,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (message.isOutgoing) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 260.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (message.isOutgoing) 16.dp else 4.dp,
                        bottomEnd = if (message.isOutgoing) 4.dp else 16.dp
                    )
                )
                // Solid Signal Green for Outgoing, Slate/Grey for Incoming
                .background(if (message.isOutgoing) Signal else Color(0xFF1E293B))
                .padding(14.dp)
        ) {
            Text(
                text = message.text,
                color = if (message.isOutgoing) Ink else Paper,
                fontSize = 15.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActiveChatScreenPreview() {
    ActiveChatScreen()
}
