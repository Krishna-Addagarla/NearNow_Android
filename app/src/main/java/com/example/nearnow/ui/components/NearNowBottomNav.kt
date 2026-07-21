package com.example.nearnow.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.example.nearnow.ui.animations.NearNowMotion
import com.example.nearnow.ui.theme.*

@Composable
fun NearNowBottomNav(
    selectedTab: String,
    onTabClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf("Nearby", "Invites", "Chats", "Me")
    val haptic = LocalHapticFeedback.current

    val targetIndex = tabs.indexOf(selectedTab).toFloat()
    val animatedIndex by animateFloatAsState(
        targetValue = targetIndex,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "active_tab_index"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .height(60.dp)
            .drawBehind {
                val width = size.width
                val height = size.height
                val tabWidth = width / tabs.size
                val cx = tabWidth * (animatedIndex + 0.5f)

                // Draw the wave background
                val curveWidth = 84.dp.toPx()
                val curveHeight = 12.dp.toPx()
                val startX = cx - curveWidth / 2
                val endX = cx + curveWidth / 2

                val path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(startX, 0f)
                    cubicTo(
                        x1 = startX + curveWidth * 0.25f, y1 = 0f,
                        x2 = cx - curveWidth * 0.2f, y2 = curveHeight,
                        x3 = cx, y3 = curveHeight
                    )
                    cubicTo(
                        x1 = cx + curveWidth * 0.2f, y1 = curveHeight,
                        x2 = endX - curveWidth * 0.25f, y2 = 0f,
                        x3 = endX, y3 = 0f
                    )
                    lineTo(width, 0f)
                    lineTo(width, height)
                    lineTo(0f, height)
                    close()
                }

                drawPath(
                    path = path,
                    color = CardWhite
                )

                // Draw the top stroke
                val strokePath = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(startX, 0f)
                    cubicTo(
                        x1 = startX + curveWidth * 0.25f, y1 = 0f,
                        x2 = cx - curveWidth * 0.2f, y2 = curveHeight,
                        x3 = cx, y3 = curveHeight
                    )
                    cubicTo(
                        x1 = cx + curveWidth * 0.2f, y1 = curveHeight,
                        x2 = endX - curveWidth * 0.25f, y2 = 0f,
                        x3 = endX, y3 = 0f
                    )
                    lineTo(width, 0f)
                }

                drawPath(
                    path = strokePath,
                    color = SoftGray,
                    style = Stroke(width = 1.dp.toPx())
                )
            }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { index, tab ->
                val isSelected = tab == selectedTab

                val icon = when (tab) {
                    "Nearby" -> Icons.Default.LocationOn
                    "Invites" -> Icons.Default.Send
                    "Chats" -> Icons.Default.MailOutline
                    else -> Icons.Default.Person
                }

                // Spring scale animation
                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1.2f else 1.0f,
                    animationSpec = NearNowMotion.BouncySpring,
                    label = "icon_scale"
                )

                // Color fade animation
                val tintColor by animateColorAsState(
                    targetValue = if (isSelected) Mango else TextMuted,
                    animationSpec = tween(NearNowMotion.Quick),
                    label = "icon_tint"
                )

                // Offset animation to slide the icon up when active
                val offsetY by animateDpAsState(
                    targetValue = if (isSelected) (-12).dp else 0.dp,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    ),
                    label = "icon_offset"
                )

                // Dot alpha animation
                val dotAlpha by animateFloatAsState(
                    targetValue = if (isSelected) 1f else 0f,
                    animationSpec = tween(NearNowMotion.Quick),
                    label = "dot_alpha"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            if (!isSelected) {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                onTabClick(tab)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.offset(y = offsetY)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) Mango.copy(alpha = 0.08f) else Color.Transparent
                                )
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = tab,
                                tint = tintColor,
                                modifier = Modifier
                                    .size(24.dp)
                                    .graphicsLayer(
                                        scaleX = scale,
                                        scaleY = scale
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Active indicator dot
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .graphicsLayer(alpha = dotAlpha)
                                .background(Mango, CircleShape)
                        )
                    }
                }
            }
        }
    }
}

