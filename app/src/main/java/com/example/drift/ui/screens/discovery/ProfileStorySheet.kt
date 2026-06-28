package com.example.drift.ui.screens.discovery

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drift.data.local.model.DiscoveryUser
import com.example.drift.ui.theme.Coral
import com.example.drift.ui.theme.Ink
import com.example.drift.ui.theme.Paper
import com.example.drift.ui.theme.Signal
import com.example.drift.ui.theme.Slate
import kotlinx.coroutines.delay

@Composable
fun ProfileStorySheet(
    user: DiscoveryUser,
    onDismiss: () -> Unit,
    onSendMessage: (String) -> Unit = {},
    onPass: () -> Unit = {}
) {
    var showStoryPlayer by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.85f)
            .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
            .background(Ink)
            .border(
                width = 1.dp,
                color = Color(0xFF1E293B),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Handle / Notch at top
            Box(
                modifier = Modifier
                    .size(width = 36.dp, height = 4.dp)
                    .background(Color(0xFF334155), CircleShape)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Header: Back/Dismiss button on left, Report flag on right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1E293B))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Dismiss",
                        tint = Paper
                    )
                }

                // Custom Report Flag Icon (using Canvas to avoid missing icons)
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1E293B))
                        .clickable { /* Report logic */ },
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(18.dp)) {
                        val path = Path().apply {
                            moveTo(2f, 2f)
                            lineTo(14f, 2f)
                            lineTo(12f, 7f)
                            lineTo(16f, 7f)
                            lineTo(4f, 16f)
                            lineTo(4f, 2f)
                            close()
                        }
                        drawPath(
                            path = path,
                            color = Coral
                        )
                        // Flag pole
                        drawRect(
                            color = Slate,
                            topLeft = androidx.compose.ui.geometry.Offset(2f, 2f),
                            size = androidx.compose.ui.geometry.Size(2f, size.height - 2f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // User Profile Header: Name & Age (Serif style)
            Text(
                text = "${user.name}, ${user.age}",
                color = Paper,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Row with verified status and mono distance
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // VERIFIED badge
                Box(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Signal.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "VERIFIED",
                        color = Signal,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp
                    )
                }

                // Mono Distance
                Text(
                    text = "${user.distanceMeters}M",
                    color = Signal,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Active Story Row (only appears if active story is present)
            if (user.hasActiveStory) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF131D30))
                        .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(12.dp))
                        .clickable { showStoryPlayer = true }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Story Thumbnail with Signal Green Glowing ring
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .border(2.dp, Signal, CircleShape)
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(Color(user.avatarColorHex.removePrefix("#").toInt(16) or 0xFF000000.toInt())),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user.initials,
                            color = Paper,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Active story - ${user.storyTimeAgo ?: "Just now"}",
                            color = Paper,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Bio / Description block
            Text(
                text = "\"${user.bio}\"",
                color = Paper,
                fontSize = 16.sp,
                fontStyle = FontStyle.Normal,
                lineHeight = 22.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            // Bottom Buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { onSendMessage(user.name) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Signal,
                        contentColor = Ink
                    )
                ) {
                    Text(
                        text = "SEND A MESSAGE",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "PASS",
                    color = Slate,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .clickable { onPass() }
                        .padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Custom Overlay Story Player
        AnimatedVisibility(
            visible = showStoryPlayer,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
        ) {
            StoryPlayerOverlay(
                user = user,
                onClose = { showStoryPlayer = false }
            )
        }
    }
}

/**
 * A simulation of a Snapchat-style fullscreen story player.
 */
@Composable
fun StoryPlayerOverlay(
    user: DiscoveryUser,
    onClose: () -> Unit
) {
    var progress by remember { mutableStateOf(0f) }

    // Increment progress bar to simulate story playing
    LaunchedEffect(Unit) {
        while (progress < 1.0f) {
            delay(30)
            progress += 0.01f
        }
        onClose() // Auto-close when story finishes
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable { onClose() } // Tap to dismiss
    ) {
        // Story Background (vibrant gradient representation of user's story content)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1A1C29),
                            Color(user.avatarColorHex.removePrefix("#").toInt(16) or 0xFF000000.toInt()).copy(alpha = 0.5f),
                            Color(0xFF0F172A)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Simulated story content card
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "⚡",
                    fontSize = 60.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "MY DAY",
                    color = Signal,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = user.bio,
                    color = Paper,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Serif,
                    textAlign = TextAlign.Center,
                    lineHeight = 26.sp
                )
            }
        }

        // Top Controls: Progress indicator and User Info
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            // Progress Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(Color.White.copy(alpha = 0.3f), CircleShape)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress)
                        .background(Signal, CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // User Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(user.avatarColorHex.removePrefix("#").toInt(16) or 0xFF000000.toInt())),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user.initials,
                        color = Paper,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = user.name,
                    color = Paper,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = user.storyTimeAgo ?: "",
                    color = Slate,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                // Close cross
                IconButton(onClick = onClose) {
                    Text(
                        text = "✕",
                        color = Paper,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileStorySheetPreview() {
    ProfileStorySheet(
        user = DiscoveryUser.mockUsers.first(),
        onDismiss = {}
    )
}
