package com.example.nearnow.ui.screens.discovery

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nearnow.data.local.model.DiscoveryUser
import com.example.nearnow.ui.components.AvatarSize
import com.example.nearnow.ui.components.NearNowAvatar
import com.example.nearnow.ui.components.NearNowBottomNav
import com.example.nearnow.ui.components.NearNowBottomSheet
import com.example.nearnow.ui.theme.*
import kotlin.math.sqrt
import kotlinx.coroutines.delay

// Cluster helper class
data class DiscoveryCluster(
    val mainUser: DiscoveryUser,
    val members: List<DiscoveryUser>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoveryMapScreen(
    onNavigateToTab: (String) -> Unit = {},
    onNavigateToChatCompose: (String) -> Unit = {},
    onNavigateToActiveChat: (String) -> Unit = {}
) {
    var showListView by remember { mutableStateOf(false) }
    var selectedUserForSheet by remember { mutableStateOf<DiscoveryUser?>(null) }
    var selectedTab by remember { mutableStateOf("Nearby") }
    var activeStoryUserForPlayer by remember { mutableStateOf<DiscoveryUser?>(null) }
    var isInvisibleMode by remember { mutableStateOf(false) }

    val clusters = remember(DiscoveryUser.mockUsers) {
        clusterUsers(DiscoveryUser.mockUsers, threshold = 0.22f)
    }

    if (showListView) {
        DiscoveryListScreen(
            users = DiscoveryUser.mockUsers,
            onUserClick = { user ->
                selectedUserForSheet = user
            },
            onToggleView = { showListView = false },
            selectedTab = selectedTab,
            onTabSelect = { tab ->
                selectedTab = tab
                onNavigateToTab(tab)
            }
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            MangoLight.copy(alpha = 0.05f),
                            Cream
                        ),
                        radius = 900f
                    )
                )
        ) {
            // 1. Concentric circles Radar grid Canvas + Animated Radar Pulse + Ambient particles
            RadarGridBackground(isInvisibleMode = isInvisibleMode)

            // 2. Main content container
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(bottom = 72.dp) // Nav bar padding
            ) {
                Spacer(modifier = Modifier.height(48.dp))

                // Header Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Nearby",
                        color = TextPrimary,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Ghost Toggle Button (Invisible Mode)
                        IconButton(
                            onClick = { isInvisibleMode = !isInvisibleMode },
                            modifier = Modifier
                                .size(44.dp)
                                .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = ShadowColor, ambientColor = ShadowColor)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isInvisibleMode) Mango else CardWhite)
                                .border(
                                    width = 1.5.dp,
                                    color = if (isInvisibleMode) Mango else SoftGray,
                                    shape = RoundedCornerShape(12.dp)
                                )
                        ) {
                            Text(
                                text = "👻",
                                fontSize = 18.sp
                            )
                        }

                        // Toggle to List View Button
                        IconButton(
                            onClick = { showListView = true },
                            modifier = Modifier
                                .size(44.dp)
                                .shadow(2.dp, RoundedCornerShape(12.dp), spotColor = ShadowColor, ambientColor = ShadowColor)
                                .clip(RoundedCornerShape(12.dp))
                                .background(CardWhite)
                                .border(1.5.dp, SoftGray, RoundedCornerShape(12.dp))
                        ) {
                            Icon(
                                imageVector = Icons.Default.List,
                                contentDescription = "List View",
                                tint = Mango
                            )
                        }
                    }
                }

                // Radar Container (Spatial Layer)
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    val canvasWidth = constraints.maxWidth.toFloat()
                    val canvasHeight = constraints.maxHeight.toFloat()
                    val centerX = canvasWidth / 2
                    val centerY = canvasHeight / 2
                    val maxRadius = minOf(canvasWidth, canvasHeight) * 0.42f

                    // Invisible Mode Status Text Overlay
                    if (isInvisibleMode) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 16.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(MangoLight.copy(alpha = 0.15f))
                                .border(1.dp, Mango, RoundedCornerShape(20.dp))
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "• GHOST MODE ACTIVE",
                                color = MangoDeep,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Render YOU in the absolute center
                    Box(
                        modifier = Modifier
                            .offset(
                                x = (centerX / 2.75f).dp - 24.dp,
                                y = (centerY / 2.75f).dp - 24.dp
                            )
                            .size(48.dp)
                            .shadow(4.dp, CircleShape, spotColor = ShadowColor, ambientColor = ShadowColor)
                            .border(
                                width = 2.dp,
                                color = if (isInvisibleMode) TextMuted else Mango,
                                shape = CircleShape
                            )
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(if (isInvisibleMode) SoftGray else MangoLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isInvisibleMode) "GHOST" else "YOU",
                            color = if (isInvisibleMode) TextMuted else TextPrimary,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Render each clustered node on the map with floating animations
                    clusters.forEachIndexed { index, cluster ->
                        val mainUser = cluster.mainUser
                        val offsetX = mainUser.mapX * maxRadius
                        val offsetY = mainUser.mapY * maxRadius

                        val xDp = ((centerX + offsetX) / 2.75f).dp - 24.dp
                        val yDp = ((centerY + offsetY) / 2.75f).dp - 24.dp

                        // Staggered entry animation
                        var animatedScale by remember { mutableStateOf(0f) }
                        LaunchedEffect(Unit) {
                            delay(index * 100L)
                            animate(
                                initialValue = 0f,
                                targetValue = 1f,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            ) { value, _ ->
                                animatedScale = value
                            }
                        }

                        // Avatar breathing animation
                        val infiniteTransition = rememberInfiniteTransition(label = "avatar_breath_$index")
                        val breathingScale by infiniteTransition.animateFloat(
                            initialValue = 0.97f,
                            targetValue = 1.03f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1800 + index * 100, easing = EaseInOutSine),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "breath"
                        )

                        DiscoveryMapNode(
                            cluster = cluster,
                            onAvatarClick = { user ->
                                if (user.hasActiveStory) {
                                    activeStoryUserForPlayer = user
                                } else {
                                    selectedUserForSheet = user
                                }
                            },
                            onLabelClick = { user ->
                                selectedUserForSheet = user
                            },
                            modifier = Modifier
                                .offset(x = xDp, y = yDp)
                                .scale(animatedScale * breathingScale)
                        )
                    }
                }

                // 8 NEARBY pill at bottom center of map
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .shadow(4.dp, RoundedCornerShape(24.dp), spotColor = ShadowColor, ambientColor = ShadowColor)
                        .clip(RoundedCornerShape(24.dp))
                        .background(CardWhite)
                        .border(1.5.dp, SoftGray, RoundedCornerShape(24.dp))
                        .padding(horizontal = 18.dp, vertical = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Pulsing Teal indicator dot
                        val infinitePulse = rememberInfiniteTransition(label = "live_pulse")
                        val pulseAlpha by infinitePulse.animateFloat(
                            initialValue = 0.3f,
                            targetValue = 1f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1000, easing = LinearEasing),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "alpha"
                        )
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Teal.copy(alpha = pulseAlpha), CircleShape)
                        )
                        Text(
                            text = "8 NEARBY - 500M RADIUS",
                            color = TextPrimary,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Bottom Navigation Bar
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                NearNowBottomNav(
                    selectedTab = selectedTab,
                    onTabClick = {
                        selectedTab = it
                        onNavigateToTab(it)
                    }
                )
            }
        }
    }

    // Modal Profile Sheet using NearNowBottomSheet component
    selectedUserForSheet?.let { user ->
        NearNowBottomSheet(
            onDismissRequest = { selectedUserForSheet = null }
        ) {
            ProfileStorySheet(
                user = user,
                onDismiss = { selectedUserForSheet = null },
                onSendMessage = {
                    selectedUserForSheet = null
                    onNavigateToChatCompose(user.id)
                },
                onPass = {
                    selectedUserForSheet = null
                }
            )
        }
    }

    // Story Player full screen simulation overlay
    activeStoryUserForPlayer?.let { user ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            StoryPlayerOverlay(
                user = user,
                onClose = {
                    activeStoryUserForPlayer = null
                    selectedUserForSheet = user
                }
            )
        }
    }
}

@Composable
fun DiscoveryMapNode(
    cluster: DiscoveryCluster,
    onAvatarClick: (DiscoveryUser) -> Unit,
    onLabelClick: (DiscoveryUser) -> Unit,
    modifier: Modifier = Modifier
) {
    val mainUser = cluster.mainUser
    val hasCluster = cluster.members.size > 1

    Column(
        modifier = modifier.width(76.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            // Main user avatar circle using reusable NearNowAvatar
            NearNowAvatar(
                user = mainUser,
                size = AvatarSize.MEDIUM,
                showOnlineIndicator = mainUser.isOnline,
                showStoryRing = mainUser.hasActiveStory,
                showVerifiedBadge = mainUser.isVerified,
                modifier = Modifier
                    .shadow(3.dp, CircleShape, spotColor = ShadowColor, ambientColor = ShadowColor)
                    .clickable { onAvatarClick(mainUser) }
            )

            // Cluster overlapping badge (+N)
            if (hasCluster) {
                Box(
                    modifier = Modifier
                        .offset(x = 6.dp, y = (-4).dp)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Mango)
                        .border(1.5.dp, Cream, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+${cluster.members.size - 1}",
                        color = CardWhite,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Username and distance details below avatar
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onLabelClick(mainUser) }
        ) {
            Text(
                text = mainUser.name,
                color = TextPrimary,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${mainUser.distanceMeters}M",
                color = if (mainUser.isOnline) Teal else TextSecondary,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun RadarGridBackground(isInvisibleMode: Boolean) {
    // 1. Animated Radar Pulse (Continuous Mango ring expanding)
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_radar")
    val pulseProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val maxRadius = minOf(size.width, size.height) * 0.42f

        // Draw background mesh grid lines
        val meshCount = 8
        for (i in 1..meshCount) {
            val fraction = i.toFloat() / meshCount
            drawLine(
                color = SoftGray.copy(alpha = 0.5f),
                start = Offset(0f, centerY * fraction * 2f),
                end = Offset(size.width, centerY * fraction * 2f),
                strokeWidth = 1f
            )
            drawLine(
                color = SoftGray.copy(alpha = 0.5f),
                start = Offset(centerX * fraction * 2f, 0f),
                end = Offset(centerX * fraction * 2f, size.height),
                strokeWidth = 1f
            )
        }

        // Draw animated expanding pulse ring
        if (!isInvisibleMode) {
            drawCircle(
                color = Mango.copy(alpha = (1f - pulseProgress) * 0.15f),
                radius = maxRadius * pulseProgress,
                center = Offset(centerX, centerY),
                style = Stroke(width = 2.dp.toPx())
            )
        }

        // Draw Concentric dashed/dotted Radar Rings
        val rings = listOf(
            maxRadius * 0.35f,
            maxRadius * 0.7f,
            maxRadius
        )

        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

        rings.forEach { radius ->
            drawCircle(
                color = MangoLight.copy(alpha = 0.15f),
                radius = radius,
                center = Offset(centerX, centerY),
                style = Stroke(
                    width = 1.5f,
                    pathEffect = pathEffect
                )
            )
        }
    }

    // Concentric Ring textual distance markers overlays
    Box(modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val width = constraints.maxWidth.toFloat()
            val height = constraints.maxHeight.toFloat()
            val centerX = width / 2
            val centerY = height / 2
            val maxRadius = minOf(width, height) * 0.42f

            val labels = listOf(
                "180M" to maxRadius * 0.35f,
                "420M" to maxRadius * 0.7f,
                "650M" to maxRadius
            )

            labels.forEach { (label, radius) ->
                val yOffset = ((centerY - radius) / 2.75f).dp - 10.dp
                val xOffset = (centerX / 2.75f).dp - 24.dp
                Text(
                    text = label,
                    color = TextMuted,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .offset(x = xOffset, y = yOffset)
                        .background(Cream)
                        .padding(horizontal = 4.dp)
                )
            }
        }
    }
}

private fun clusterUsers(users: List<DiscoveryUser>, threshold: Float): List<DiscoveryCluster> {
    val visited = mutableSetOf<String>()
    val result = mutableListOf<DiscoveryCluster>()

    for (user in users) {
        if (user.id in visited) continue
        val clusterMembers = mutableListOf(user)
        visited.add(user.id)

        for (other in users) {
            if (other.id in visited) continue
            val dx = user.mapX - other.mapX
            val dy = user.mapY - other.mapY
            val dist = sqrt((dx * dx + dy * dy).toDouble()).toFloat()

            if (dist < threshold) {
                clusterMembers.add(other)
                visited.add(other.id)
            }
        }
        result.add(DiscoveryCluster(mainUser = user, members = clusterMembers))
    }
    return result
}

private suspend fun androidx.compose.ui.input.pointer.PointerInputScope.detectTapGestures(
    onTap: () -> Unit
) {
    while (true) {
        val event = awaitPointerEventScope {
            awaitFirstDown()
        }
        onTap()
    }
}

private suspend fun androidx.compose.ui.input.pointer.AwaitPointerEventScope.awaitFirstDown() {
    do {
        val event = awaitPointerEvent()
    } while (event.changes.none { it.pressed })
}

@Preview(showBackground = true)
@Composable
fun DiscoveryMapScreenPreview() {
    DiscoveryMapScreen()
}
