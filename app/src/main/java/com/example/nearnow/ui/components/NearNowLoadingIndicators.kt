package com.example.nearnow.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.nearnow.ui.theme.Mango
import com.example.nearnow.ui.theme.SoftGray
import com.example.nearnow.ui.animations.shimmerBackground

@Composable
fun NearNowSpinner(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    color: Color = Mango
) {
    CircularProgressIndicator(
        color = color,
        modifier = modifier.size(size),
        strokeWidth = 2.5.dp
    )
}

@Composable
fun NearNowPulseLoader(
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "pulse_loader")
    
    val scale1 by transition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot1"
    )
    val scale2 by transition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot2"
    )
    val scale3 by transition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 400, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot3"
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(modifier = Modifier.size(10.dp).scale(scale1).clip(CircleShape).background(Mango))
        Box(modifier = Modifier.size(10.dp).scale(scale2).clip(CircleShape).background(Mango.copy(alpha = 0.7f)))
        Box(modifier = Modifier.size(10.dp).scale(scale3).clip(CircleShape).background(Mango.copy(alpha = 0.4f)))
    }
}

@Composable
fun NearNowSkeletonLoader(
    modifier: Modifier = Modifier,
    height: Dp = 100.dp,
    cornerRadius: Dp = 16.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(cornerRadius))
            .background(SoftGray)
            .shimmerBackground()
    )
}
