package com.example.nearnow.ui.screens.chat

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nearnow.data.local.model.ChatSession
import com.example.nearnow.ui.components.AvatarSize
import com.example.nearnow.ui.components.NearNowAvatar
import com.example.nearnow.ui.components.NearNowPrimaryButton
import com.example.nearnow.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun PausedChatScreen(
    session: ChatSession = ChatSession.mockSessions.first { it.status == com.example.nearnow.data.local.model.ChatSessionStatus.PAUSED },
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
            .background(Cream)
    ) {
        // Dimmed concentric double-ring radar visual in the center background
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val ringRadius1 = size.width * 0.2f
            val ringRadius2 = size.width * 0.35f
            val pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)

            drawCircle(
                color = SoftGray,
                radius = ringRadius2,
                center = Offset(centerX, centerY),
                style = Stroke(width = 1.5f, pathEffect = pathEffect)
            )

            drawCircle(
                color = SoftGray.copy(alpha = 0.5f),
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

                NearNowAvatar(
                    user = session.user,
                    size = AvatarSize.SMALL
                )

                Spacer(modifier = Modifier.width(12.dp))

                // User details
                Column {
                    Text(
                        text = session.user.name,
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Paused Tag
                    Box(
                        modifier = Modifier
                            .background(Coral.copy(alpha = 0.08f), RoundedCornerShape(4.dp))
                            .border(1.dp, Coral, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "PAUSED · OUT OF RANGE",
                            color = Coral,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
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
                    color = TextPrimary,
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "${session.user.name} moved out of range. This resumes the moment you're both back within 500m.",
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    lineHeight = 22.sp
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
                // Info block inside NearNowCard style
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(20.dp), spotColor = ShadowColor, ambientColor = ShadowColor)
                        .clip(RoundedCornerShape(20.dp))
                        .background(CardWhite)
                        .border(1.5.dp, SoftGray, RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "Make it permanent?",
                            color = TextPrimary,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Stay connected regardless of distance — both must agree.",
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                NearNowPrimaryButton(
                    text = if (pendingPermanentState) "PENDING APPROVAL..." else "CONNECT PERMANENTLY",
                    onClick = { pendingPermanentState = true },
                    enabled = !pendingPermanentState,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Gold lock connection celebration overlay with sparkle confetti animations
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
                    .background(Color.Black.copy(alpha = 0.95f))
                    .clickable { /* Eat clicks */ }
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                // Background Confetti Canvas
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val random = Random(42)
                    for (i in 0..30) {
                        val x = random.nextFloat() * size.width
                        val y = random.nextFloat() * size.height
                        val sizeConfetti = random.nextFloat() * 8f + 4f
                        val colorConfetti = if (random.nextBoolean()) Mango else Teal
                        drawCircle(
                            color = colorConfetti.copy(alpha = 0.6f),
                            radius = sizeConfetti,
                            center = Offset(x, y)
                        )
                    }
                }

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
                        color = CardWhite,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Proximity boundaries have been lifted! You and ${session.user.name} are now connected permanently.",
                        color = SoftGray,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    NearNowPrimaryButton(
                        text = "ENTER CHAT",
                        onClick = {
                            showCelebration = false
                            pendingPermanentState = false
                            onConnectPermanentlyClick()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
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
