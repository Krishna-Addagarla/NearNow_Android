package com.example.nearnow.ui.screens.chat

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nearnow.data.local.model.ChatMessage
import com.example.nearnow.data.local.model.ChatSession
import com.example.nearnow.ui.components.AvatarSize
import com.example.nearnow.ui.components.NearNowAvatar
import com.example.nearnow.ui.theme.*

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
                currentDistance = 180
            }
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Cream)
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
                            .shadow(2.dp, CircleShape, spotColor = ShadowColor, ambientColor = ShadowColor)
                            .clip(CircleShape)
                            .background(CardWhite)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Reusable avatar size SMALL
                    NearNowAvatar(
                        user = session.user,
                        size = AvatarSize.SMALL,
                        showOnlineIndicator = session.user.isOnline,
                        showStoryRing = session.user.hasActiveStory,
                        showVerifiedBadge = session.user.isVerified
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    // User Details (No debugging metrics layout)
                    Column {
                        Text(
                            text = session.user.name,
                            color = TextPrimary,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (meetupModeActive) "• GRACE PERIOD ACTIVE" else "• LIVE PROXIMITY",
                            color = if (meetupModeActive) Coral else Teal,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                ProximityTicker(distance = currentDistance, meetupModeActive = meetupModeActive)

                // Scrolling Chat messages
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp)
                ) {
                    item {
                        Text(
                            text = "TODAY 4:22 PM",
                            color = TextMuted,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }

                    items(messages) { message ->
                        MessageBubble(message = message)
                    }

                    // ETA suggestion card helper at the end
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        if (!meetupModeActive) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Coral.copy(alpha = 0.08f))
                                    .border(1.5.dp, Coral, RoundedCornerShape(16.dp))
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
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            // Display active meetup indicator card
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Teal.copy(alpha = 0.08f))
                                    .border(1.5.dp, Teal, RoundedCornerShape(16.dp))
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
                                        color = Teal,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold
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
                        .background(CardWhite)
                        .border(1.dp, SoftGray)
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
                            .background(FieldFill)
                            .border(1.dp, SoftGray, RoundedCornerShape(24.dp))
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        BasicTextField(
                            value = textState,
                            onValueChange = { textState = it },
                            textStyle = TextStyle(
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontFamily = PoppinsFamily
                            ),
                            cursorBrush = SolidColor(Mango),
                            modifier = Modifier.fillMaxWidth(),
                            decorationBox = { innerTextField ->
                                if (textState.isEmpty()) {
                                    Text(
                                        text = "Message ${session.user.name}...",
                                        color = TextMuted,
                                        fontFamily = PoppinsFamily,
                                        fontSize = 15.sp
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }

                    // Send Button
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
                            .clip(CircleShape)
                            .background(Mango)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = TextPrimary
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

    val color = if (meetupModeActive || distance >= 450) Coral else Teal

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(2.dp, RoundedCornerShape(20.dp), spotColor = ShadowColor, ambientColor = ShadowColor)
            .clip(RoundedCornerShape(20.dp))
            .background(CardWhite)
            .border(1.5.dp, SoftGray, RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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
                            .background(color.copy(alpha = pulseAlpha))
                    )
                    Text(
                        text = if (meetupModeActive) "MEETUP ACTIVE" else "LIVE PROXIMITY CONNECTION",
                        color = TextPrimary,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "${distance}m",
                    color = color,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
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
                    color = SoftGray,
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
                    color = TextPrimary,
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
                color = TextSecondary,
                style = MaterialTheme.typography.bodySmall
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
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = if (message.isOutgoing) 20.dp else 4.dp,
                        bottomEnd = if (message.isOutgoing) 4.dp else 20.dp
                    )
                )
                .background(if (message.isOutgoing) Mango else SoftGray)
                .padding(14.dp)
        ) {
            Text(
                text = message.text,
                color = TextPrimary,
                fontSize = 15.sp,
                fontFamily = PoppinsFamily,
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
