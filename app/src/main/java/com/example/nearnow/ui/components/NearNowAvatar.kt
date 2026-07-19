package com.example.nearnow.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nearnow.R
import com.example.nearnow.data.local.model.DiscoveryUser
import com.example.nearnow.ui.animations.pulsingGlowAlpha
import com.example.nearnow.ui.theme.*

enum class AvatarSize(val sizeDp: Dp) {
    SMALL(36.dp),
    MEDIUM(48.dp),
    LARGE(72.dp),
    XLARGE(96.dp)
}

@Composable
fun NearNowAvatar(
    user: DiscoveryUser,
    modifier: Modifier = Modifier,
    size: AvatarSize = AvatarSize.MEDIUM,
    showOnlineIndicator: Boolean = false,
    showStoryRing: Boolean = false,
    showVerifiedBadge: Boolean = false
) {
    val sizeDp = size.sizeDp
    val paddingForRing = if (showStoryRing) 4.dp else 0.dp
    
    // Choose illustrated avatar drawable based on hash code of user ID
    val avatarIndex = (Math.abs(user.id.hashCode()) % 10) + 1
    val avatarResId = when (avatarIndex) {
        1 -> R.drawable.avatar_default_1
        2 -> R.drawable.avatar_default_2
        3 -> R.drawable.avatar_default_3
        4 -> R.drawable.avatar_default_4
        5 -> R.drawable.avatar_default_5
        6 -> R.drawable.avatar_default_6
        7 -> R.drawable.avatar_default_7
        8 -> R.drawable.avatar_default_8
        9 -> R.drawable.avatar_default_9
        else -> R.drawable.avatar_default_10
    }

    Box(
        modifier = modifier.size(sizeDp),
        contentAlignment = Alignment.Center
    ) {
        // 1. Story Ring (Breathing Mango Gradient)
        if (showStoryRing) {
            val infiniteTransition = rememberInfiniteTransition(label = "story_ring")
            val ringScale by infiniteTransition.animateFloat(
                initialValue = 0.96f,
                targetValue = 1.04f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1200, easing = EaseInOutSine),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "scale"
            )
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = ringScale
                        scaleY = ringScale
                    }
                    .border(
                        width = 2.5.dp,
                        brush = Brush.sweepGradient(
                            listOf(Mango, MangoLight, MangoDeep, Mango)
                        ),
                        shape = CircleShape
                    )
            )
        }

        // 2. Main Avatar Shape
        Box(
            modifier = Modifier
                .padding(paddingForRing)
                .fillMaxSize()
                .clip(CircleShape)
                .background(Color(user.avatarColorHex.removePrefix("#").toLongOrNull(16)?.let { it or 0xFF000000L } ?: 0xFF8B96A8L)),
            contentAlignment = Alignment.Center
        ) {
            // Check if user has photos.
            // Since Coil is not added in the project, we display a fallback colored circle with initials
            // if photoUrls is populated, else we show the illustrated avatar.
            val hasUploadedPhotos = try {
                // Safe check when we update model
                user.photoUrls.isNotEmpty()
            } catch (e: Exception) {
                false
            }

            if (hasUploadedPhotos) {
                // If they have uploaded photos, display initials or first photo placeholder
                Text(
                    text = user.initials,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = (sizeDp.value * 0.38f).sp
                )
            } else {
                // High-quality illustrated avatar
                Image(
                    painter = painterResource(id = avatarResId),
                    contentDescription = "Avatar for ${user.name}",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // 3. Online status (Teal dot with glow)
        if (showOnlineIndicator) {
            val glowAlpha = pulsingGlowAlpha()
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 1.dp, bottom = 1.dp)
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(Teal.copy(alpha = glowAlpha))
                    .border(1.5.dp, Cream, CircleShape)
            )
        }

        // 4. Verified Badge (Teal circle with checkmark)
        if (showVerifiedBadge) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(Teal)
                    .border(1.dp, Cream, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Verified User",
                    tint = TextPrimary,
                    modifier = Modifier.size(8.dp)
                )
            }
        }
    }
}
