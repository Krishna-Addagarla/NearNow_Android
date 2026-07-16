package com.example.drift.ui.screens.discovery

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drift.data.local.model.DiscoveryUser
import com.example.drift.ui.theme.Coral
import com.example.drift.ui.theme.Ink
import com.example.drift.ui.theme.Paper
import com.example.drift.ui.theme.Signal
import com.example.drift.ui.theme.Slate
import kotlin.math.sqrt

// Cluster helper class
data class DiscoveryCluster(
    val mainUser: DiscoveryUser,
    val members: List<DiscoveryUser>
)

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

    // Custom clustering logic for spatial mapping
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
                .background(Ink)
        ) {
            // 1. Concentric circles Radar grid Canvas
            RadarGridBackground()

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
                        color = Paper,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
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
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isInvisibleMode) Color(0xFF334155) else Color(0xFF1E293B))
                                .border(
                                    width = 1.dp,
                                    color = if (isInvisibleMode) Signal else Color(0xFF334155),
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Text(
                                text = "👻",
                                fontSize = 16.sp
                            )
                        }

                        // Toggle to List View Button
                        IconButton(
                            onClick = { showListView = true },
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF1E293B))
                                .border(1.dp, Color(0xFF334155), RoundedCornerShape(8.dp))
                        ) {
                            Icon(
                                imageVector = Icons.Default.List,
                                contentDescription = "List View",
                                tint = Signal
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
                    // Max radius is limited by width/height of the container
                    val maxRadius = minOf(canvasWidth, canvasHeight) * 0.42f

                    // Invisible Mode Status Text Overlay
                    if (isInvisibleMode) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 16.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF1E293B))
                                .border(0.5.dp, Slate, RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "• INVISIBLE MODE ACTIVE",
                                color = Slate,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
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
                            .border(
                                width = 2.dp,
                                color = if (isInvisibleMode) Slate else Color(0xFF007AFF),
                                shape = CircleShape
                            )
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(if (isInvisibleMode) Color(0xFF1E293B) else Color(0xFF0056B3)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isInvisibleMode) "GHOST" else "YOU",
                            color = if (isInvisibleMode) Slate else Paper,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = if (isInvisibleMode) 9.sp else 11.sp
                        )
                    }

                    // Render each clustered node on the map
                    clusters.forEach { cluster ->
                        val mainUser = cluster.mainUser
                        // Map coordinates (-1.0 to 1.0) to actual pixel offset from center
                        val offsetX = mainUser.mapX * maxRadius
                        val offsetY = mainUser.mapY * maxRadius

                        // Screen dp positions
                        val xDp = ((centerX + offsetX) / 2.75f).dp - 24.dp
                        val yDp = ((centerY + offsetY) / 2.75f).dp - 24.dp

                        DiscoveryMapNode(
                            cluster = cluster,
                            onAvatarClick = { user ->
                                if (user.hasActiveStory) {
                                    // Tap once: opens story
                                    activeStoryUserForPlayer = user
                                } else {
                                    // Otherwise directly opens profile
                                    selectedUserForSheet = user
                                }
                            },
                            onLabelClick = { user ->
                                // Tap name label below: directly opens messaging/profile sheet
                                selectedUserForSheet = user
                            },
                            modifier = Modifier.offset(x = xDp, y = yDp)
                        )
                    }
                }

                // 8 NEARBY pill at bottom center of map
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF1E293B))
                        .border(1.dp, Color(0xFF334155), RoundedCornerShape(20.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Green pulsing-like dot
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Signal, CircleShape)
                        )
                        Text(
                            text = "8 NEARBY - 500M RADIUS",
                            color = Paper,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            letterSpacing = 1.sp
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
                DiscoveryBottomBar(
                    selectedTab = selectedTab,
                    onTabClick = {
                        selectedTab = it
                        onNavigateToTab(it)
                    }
                )
            }
        }
    }

    // Modal Profile & Story Sheet
    selectedUserForSheet?.let { user ->
        // Custom bottom sheet trigger simulation
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
                .pointerInput(Unit) {
                    detectTapGestures { selectedUserForSheet = null }
                },
            contentAlignment = Alignment.BottomCenter
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
                    // "tapping it opens the story first, tapping again opens chat"
                    // After the story is viewed, set the sheet target to this user
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
        modifier = modifier.width(72.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            // Main user avatar circle
            val avatarColor = Color(mainUser.avatarColorHex.removePrefix("#").toInt(16) or 0xFF000000.toInt())

            Box(
                modifier = Modifier
                    .size(48.dp)
                    // Border changes depending on state: Signal green (active story), Coral (invite)
                    .border(
                        width = 2.dp,
                        color = when {
                            mainUser.hasActiveStory -> Signal
                            mainUser.hasInvite -> Coral
                            else -> Color.Transparent
                        },
                        shape = CircleShape
                    )
                    .padding(3.dp)
                    .clip(CircleShape)
                    .background(avatarColor)
                    .clickable { onAvatarClick(mainUser) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = mainUser.initials,
                    color = Paper,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Cluster overlapping badge (+N)
            if (hasCluster) {
                Box(
                    modifier = Modifier
                        .offset(x = 4.dp, y = (-4).dp)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Signal)
                        .border(1.dp, Ink, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+${cluster.members.size - 1}",
                        color = Ink,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
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
                color = Paper,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${mainUser.distanceMeters}M",
                color = if (mainUser.hasActiveStory) Signal else Slate,
                fontFamily = FontFamily.Monospace,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun RadarGridBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val maxRadius = minOf(size.width, size.height) * 0.42f

        // Draw background mesh grid lines
        val meshCount = 8
        for (i in 1..meshCount) {
            val fraction = i.toFloat() / meshCount
            // horizontal line
            drawLine(
                color = Color(0xFF1E293B).copy(alpha = 0.2f),
                start = Offset(0f, centerY * fraction * 2f),
                end = Offset(size.width, centerY * fraction * 2f),
                strokeWidth = 1f
            )
            // vertical line
            drawLine(
                color = Color(0xFF1E293B).copy(alpha = 0.2f),
                start = Offset(centerX * fraction * 2f, 0f),
                end = Offset(centerX * fraction * 2f, size.height),
                strokeWidth = 1f
            )
        }

        // Draw Concentric dashed/dotted Radar Rings
        val rings = listOf(
            maxRadius * 0.35f to "180M",
            maxRadius * 0.7f to "420M",
            maxRadius to "650M"
        )

        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

        rings.forEach { (radius, label) ->
            // Draw Circle
            drawCircle(
                color = Color(0xFF1E293B),
                radius = radius,
                center = Offset(centerX, centerY),
                style = Stroke(
                    width = 1.5f,
                    pathEffect = pathEffect
                )
            )

            // Draw label markers (e.g. "180M", "420M", "650M")
            // Plot them slightly offset to the left of the center vertical line
            // Using a simple canvas text draw or placeholder circle indicator
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
                    color = Color(0xFF475569),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .offset(x = xOffset, y = yOffset)
                        .background(Ink)
                        .padding(horizontal = 4.dp)
                )
            }
        }
    }
}

/**
 * Perform spatial distance-based clustering for mock avatars.
 */
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

// Simple gesture detector wrapper helper to avoid importing huge custom inputs
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
