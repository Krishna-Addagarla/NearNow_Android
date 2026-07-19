package com.example.nearnow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nearnow.ui.animations.pulsingGlowAlpha
import com.example.nearnow.ui.theme.*

@Composable
fun OnlineBadge(
    modifier: Modifier = Modifier
) {
    val glowAlpha = pulsingGlowAlpha()
    Box(
        modifier = modifier
            .size(12.dp)
            .clip(CircleShape)
            .background(Teal.copy(alpha = glowAlpha))
            .border(1.5.dp, Cream, CircleShape)
    )
}

@Composable
fun VerifiedBadge(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
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

@Composable
fun CountBadge(
    count: Int,
    modifier: Modifier = Modifier
) {
    if (count > 0) {
        Box(
            modifier = modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(Mango),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = count.toString(),
                color = CardWhite,
                fontSize = 11.sp,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
fun PausedBadge(
    modifier: Modifier = Modifier
) {
    Text(
        text = "PAUSED",
        color = Coral,
        style = MaterialTheme.typography.labelSmall,
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Coral.copy(alpha = 0.08f))
            .border(1.dp, Coral, RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}
