package com.example.nearnow.ui.screens.discovery

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nearnow.data.local.model.DiscoveryUser
import com.example.nearnow.ui.components.*
import com.example.nearnow.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun ProfileStorySheet(
    user: DiscoveryUser,
    onDismiss: () -> Unit,
    onSendMessage: (String) -> Unit = {},
    onPass: () -> Unit = {}
) {
    var showStoryPlayer by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardWhite)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Header: Back/Dismiss button on left, Report flag on right
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .size(40.dp)
                    .shadow(2.dp, CircleShape, spotColor = ShadowColor, ambientColor = ShadowColor)
                    .clip(CircleShape)
                    .background(CardWhite)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Dismiss",
                    tint = TextPrimary
                )
            }

            // Custom Report Flag Icon (using Canvas to avoid missing icons)
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .shadow(2.dp, CircleShape, spotColor = ShadowColor, ambientColor = ShadowColor)
                    .clip(CircleShape)
                    .background(CardWhite)
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
                    drawRect(
                        color = TextSecondary,
                        topLeft = androidx.compose.ui.geometry.Offset(2f, 2f),
                        size = androidx.compose.ui.geometry.Size(2f, size.height - 2f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Images Swipeable Gallery
        val pagesCount = if (user.photoUrls.isEmpty()) 1 else user.photoUrls.size
        val pagerState = rememberPagerState(pageCount = { pagesCount })

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Cream),
            contentAlignment = Alignment.Center
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { pageIndex ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    NearNowAvatar(
                        user = user,
                        size = AvatarSize.XLARGE,
                        showOnlineIndicator = user.isOnline,
                        showStoryRing = user.hasActiveStory,
                        showVerifiedBadge = user.isVerified
                    )
                }
            }

            // Smooth pager indicators
            if (pagesCount > 1) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    repeat(pagesCount) { i ->
                        val isActive = pagerState.currentPage == i
                        Box(
                            modifier = Modifier
                                .size(if (isActive) 12.dp else 8.dp)
                                .clip(CircleShape)
                                .background(if (isActive) Mango else SoftGray)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            // User Details Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${user.name}, ${user.age}",
                    color = TextPrimary,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )

                if (user.isVerified) {
                    VerifiedBadge()
                }

                if (user.isOnline) {
                    OnlineBadge()
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Verified Badge & Distance Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NearNowTealChip(label = "${user.distanceMeters}M")
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Active Story Row (only appears if active story is present)
            if (user.hasActiveStory) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(16.dp), spotColor = ShadowColor, ambientColor = ShadowColor)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Cream)
                        .border(1.dp, SoftGray, RoundedCornerShape(16.dp))
                        .clickable { showStoryPlayer = true }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Small thumbnail avatar
                    NearNowAvatar(
                        user = user,
                        size = AvatarSize.SMALL,
                        showStoryRing = true
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Active story - ${user.storyTimeAgo ?: "Just now"}",
                            color = TextPrimary,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Bio / Description block
            Text(
                text = "\"${user.bio}\"",
                color = TextPrimary,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 22.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Interest Chips
            if (user.interests.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(user.interests) { interest ->
                        NearNowChip(label = interest)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Bottom Buttons
            NearNowPrimaryButton(
                text = "SEND A MESSAGE",
                onClick = { onSendMessage(user.name) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            NearNowGhostButton(
                text = "PASS",
                onClick = onPass,
                textColor = TextSecondary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))
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
 * Supports: Tap left/right navigation, swipe down to dismiss, smooth progress animations.
 */
@Composable
fun StoryPlayerOverlay(
    user: DiscoveryUser,
    onClose: () -> Unit
) {
    var progress by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

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
            .background(Color.Black.copy(alpha = (1f - (offsetY / 1000f).coerceIn(0f, 0.8f))))
            .offset(y = offsetY.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        if (dragAmount.y > 0) {
                            offsetY += dragAmount.y
                        }
                    },
                    onDragEnd = {
                        if (offsetY > 150) {
                            onClose()
                        } else {
                            offsetY = 0f
                        }
                    }
                )
            }
    ) {
        // Story Background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1A1C29),
                            Color(user.avatarColorHex.removePrefix("#").toLongOrNull(16)?.let { it or 0xFF000000L } ?: 0xFF8B96A8L).copy(alpha = 0.5f),
                            Color(0xFF0F172A)
                        )
                    )
                )
                // Tap gestures for left/right navigation
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val screenWidth = size.width
                        if (offset.x < screenWidth * 0.3f) {
                            // Tap left: Restart / Previous
                            progress = 0f
                        } else {
                            // Tap right: Skip / Close
                            onClose()
                        }
                    }
                }
        ) {
            // Simulated story content card
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(24.dp).align(Alignment.Center)
            ) {
                Text(
                    text = "⚡",
                    fontSize = 60.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "MY DAY",
                    color = Mango,
                    fontFamily = PoppinsFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = user.bio,
                    color = CardWhite,
                    fontSize = 18.sp,
                    fontFamily = PoppinsFamily,
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
                        .background(Mango, CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // User Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NearNowAvatar(
                    user = user,
                    size = AvatarSize.SMALL
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = user.name,
                    color = CardWhite,
                    fontFamily = PoppinsFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = user.storyTimeAgo ?: "",
                    color = SoftGray,
                    fontFamily = PoppinsFamily,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                // Close cross
                IconButton(onClick = onClose) {
                    Text(
                        text = "✕",
                        color = CardWhite,
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
