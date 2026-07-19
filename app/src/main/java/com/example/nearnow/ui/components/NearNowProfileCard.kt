package com.example.nearnow.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nearnow.R
import com.example.nearnow.data.local.model.DiscoveryUser
import com.example.nearnow.ui.theme.*

@Composable
fun NearNowProfileCard(
    user: DiscoveryUser,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    mutualInterests: List<String> = emptyList()
) {
    var expanded by remember { mutableStateOf(false) }
    
    // Scale on press
    val transition = updateTransition(targetState = expanded, label = "card_expansion")
    val cardPadding by transition.animateDp(
        transitionSpec = { spring(stiffness = Spring.StiffnessLow) },
        label = "padding"
    ) { state ->
        if (state) 0.dp else 4.dp
    }
    val cardElevation by transition.animateDp(
        transitionSpec = { spring(stiffness = Spring.StiffnessLow) },
        label = "elevation"
    ) { state ->
        if (state) 8.dp else 2.dp
    }

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
        modifier = modifier
            .padding(cardPadding)
            .shadow(
                elevation = cardElevation,
                shape = RoundedCornerShape(24.dp),
                clip = false,
                ambientColor = ShadowColor,
                spotColor = ShadowColor
            )
            .clip(RoundedCornerShape(24.dp))
            .background(CardWhite)
            .clickable {
                expanded = !expanded
                onClick()
            }
    ) {
        Column {
            // Large Top Image Area (Illustrated Avatar or Gradient background)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(user.avatarColorHex.removePrefix("#").toLongOrNull(16)?.let { it or 0xFF000000L } ?: 0xFF8B96A8L).copy(alpha = 0.2f),
                                Color(user.avatarColorHex.removePrefix("#").toLongOrNull(16)?.let { it or 0xFF000000L } ?: 0xFF8B96A8L).copy(alpha = 0.05f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Large Center Avatar
                NearNowAvatar(
                    user = user,
                    size = AvatarSize.LARGE,
                    showOnlineIndicator = user.isOnline,
                    showStoryRing = user.hasActiveStory,
                    showVerifiedBadge = user.isVerified
                )

                // Distance Badge Chip in bottom-start corner
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    NearNowTealChip(label = "${user.distanceMeters}M")
                }
            }

            // Info details below
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Name & Age Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${user.name}, ${user.age}",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )

                    if (user.isVerified) {
                        VerifiedBadge()
                    }

                    if (user.isOnline) {
                        OnlineBadge()
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Bio
                Text(
                    text = user.bio,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    maxLines = 2,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Interests Chips Row
                val userInterests = user.interests
                if (userInterests.isNotEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(userInterests) { interest ->
                            val isMutual = mutualInterests.contains(interest)
                            if (isMutual) {
                                Text(
                                    text = "✨ $interest",
                                    color = MangoDeep,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(MangoLight.copy(alpha = 0.3f))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            } else {
                                Text(
                                    text = interest,
                                    color = TextSecondary,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(SoftGray)
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
