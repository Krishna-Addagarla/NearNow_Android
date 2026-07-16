package com.example.drift.ui.screens.chat

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drift.data.local.model.ChatSession
import com.example.drift.ui.theme.Coral
import com.example.drift.ui.theme.Ink
import com.example.drift.ui.theme.Paper
import com.example.drift.ui.theme.Signal
import com.example.drift.ui.theme.Slate
import androidx.compose.animation.core.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.draw.scale
import androidx.compose.foundation.clickable
import kotlinx.coroutines.delay

@Composable
fun PausedChatScreen(
    session: ChatSession = ChatSession.mockSessions.first { it.status == com.example.drift.data.local.model.ChatSessionStatus.PAUSED },
    onBackClick: () -> Unit = {},
    onConnectPermanentlyClick: () -> Unit = {}
) {
    var pendingPermanentState by remember { mutableStateOf(false) }
    var showCelebration by remember { mutableStateOf(false) }

    if (pendingPermanentState && !showCelebration) {
        LaunchedEffect(Unit) {
            delay(1500) // Simulate other user approving
            showCelebration = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Ink)
    ) {
        // Dimmed concentric double-ring radar visual in the center background
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val ringRadius1 = size.width * 0.2f
            val ringRadius2 = size.width * 0.35f
            val pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)

            // Outer ring
            drawCircle(
                color = Slate.copy(alpha = 0.15f),
                radius = ringRadius2,
                center = Offset(centerX, centerY),
                style = Stroke(width = 1.5f, pathEffect = pathEffect)
            )

            // Inner ring
            drawCircle(
                color = Slate.copy(alpha = 0.2f),
                radius = ringRadius1,
                center = Offset(centerX, centerY),
                style = Stroke(width = 1.5f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Header Row: Back button, Avatar, User Name, and Orange Paused Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
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

                // Avatar
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

                // User details
                Column {
                    Text(
                        text = session.user.name,
                        color = Paper,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    // Orange Paused Tag
                    Box(
                        modifier = Modifier
                            .border(1.dp, Coral, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "PAUSED · OUT OF RANGE",
                            color = Coral,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(0.35f))

            // Center Text block: Signal lost
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Signal lost",
                    color = Paper,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "${session.user.name} moved out of range. This resumes the moment you're both back within 500m.",
                    color = Slate,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.weight(0.45f))

            // Bottom Make it Permanent Consent Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Info block
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0F172A))
                        .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "Make it permanent?",
                            color = Paper,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Stay connected regardless of distance — both must agree.",
                            color = Slate,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        pendingPermanentState = true
                    },
                    enabled = !pendingPermanentState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Signal,
                        contentColor = Ink,
                        disabledContainerColor = Color(0xFF1E293B),
                        disabledContentColor = Slate
                    )
                ) {
                    Text(
                        text = if (pendingPermanentState) "PENDING APPROVAL..." else "CONNECT PERMANENTLY",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Gold lock connection celebration overlay
        if (showCelebration) {
            val infiniteTransition = rememberInfiniteTransition(label = "celebration")
            val scale by infiniteTransition.animateFloat(
                initialValue = 0.96f,
                targetValue = 1.04f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1200, easing = EaseInOutCubic),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "scale"
            )

            val goldGradient = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFFFD700), // Gold
                    Color(0xFFFFA500), // Orange Gold
                    Color(0xFFFF8C00)  // Dark Orange
                )
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Ink.copy(alpha = 0.96f))
                    .clickable { /* Eat clicks */ }
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .scale(scale),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val ringRadius = size.width * 0.22f
                            val center = size.width / 2

                            // Left Gold Interlocking Ring
                            drawCircle(
                                brush = goldGradient,
                                radius = ringRadius,
                                center = Offset(center - 30f, center),
                                style = Stroke(width = 10f)
                            )

                            // Right Gold Interlocking Ring
                            drawCircle(
                                brush = goldGradient,
                                radius = ringRadius,
                                center = Offset(center + 30f, center),
                                style = Stroke(width = 10f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "Connection Locked",
                        color = Paper,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Proximity boundaries have been lifted! You and ${session.user.name} are now connected permanently.",
                        color = Slate,
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    Button(
                        onClick = {
                            showCelebration = false
                            pendingPermanentState = false
                            onConnectPermanentlyClick()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFD700),
                            contentColor = Ink
                        )
                    ) {
                        Text(
                            text = "ENTER CHAT",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PausedChatScreenPreview() {
    PausedChatScreen()
}
