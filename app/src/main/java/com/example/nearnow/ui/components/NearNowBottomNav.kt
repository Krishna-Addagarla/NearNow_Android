package com.example.nearnow.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(CardWhite)
            .border(width = 1.dp, color = SoftGray)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .height(72.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEach { tab ->
            val isSelected = tab == selectedTab
            
            // Icon selection
            val icon = when (tab) {
                "Nearby" -> Icons.Default.LocationOn
                "Invites" -> Icons.Default.Send
                "Chats" -> Icons.Default.MailOutline
                else -> Icons.Default.Person
            }

            // Spring scale animation
            val scale by animateFloatAsState(
                targetValue = if (isSelected) 1.15f else 1.0f,
                animationSpec = NearNowMotion.BouncySpring,
                label = "icon_scale"
            )

            // Color fade animation
            val tintColor by animateColorAsState(
                targetValue = if (isSelected) Mango else TextMuted,
                animationSpec = tween(NearNowMotion.Quick),
                label = "icon_tint"
            )

            // Label fade / offset animation
            val labelAlpha by animateFloatAsState(
                targetValue = if (isSelected) 1.0f else 0.7f,
                animationSpec = tween(NearNowMotion.Quick),
                label = "label_alpha"
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        if (!isSelected) {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onTabClick(tab)
                        }
                    }
                    .padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Top dot indicator with slide-in/fade animation
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .background(
                            if (isSelected) Mango else Color.Transparent,
                            CircleShape
                        )
                )
                
                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(38.dp)
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
                            .size(22.dp)
                            .align(Alignment.Center)
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = tab,
                    color = tintColor,
                    fontSize = 11.sp,
                    fontFamily = PoppinsFamily,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    modifier = Modifier.graphicsLayer { alpha = labelAlpha }
                )
            }
        }
    }
}
