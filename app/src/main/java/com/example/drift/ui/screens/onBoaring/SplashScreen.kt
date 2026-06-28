package com.example.drift.ui.screens.onBoaring

import android.window.SplashScreen
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import com.example.drift.ui.theme.Ink
import com.example.drift.ui.theme.Paper
import com.example.drift.ui.theme.Signal


@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Ink),
        contentAlignment = Alignment.Center
    ) {
        // Animated radar rings in the background
        RadarRings()

        // Foreground content: title + scanning text
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = "NearNow",
                color = Paper,
                fontSize = 42.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Normal,
                style = TextStyle(textAlign = TextAlign.Center)
            )

            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(10.dp))

            ScanningLabel()
        }
    }
}

/**
 * Concentric pulsing rings, like a radar / proximity scanner.
 * Several rings expand and fade out continuously, staggered in time.
 */
@Composable
private fun RadarRings() {
    val ringCount = 3
    val infiniteTransition = rememberInfiniteTransition(label = "radar")

    // One animated progress per ring, staggered via delay
    val progresses = (0 until ringCount).map { index ->
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 3000,
                    delayMillis = index * 1000,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "ring_$index"
        )
    }

    // Static faint base rings (always visible, subtle)
    Canvas(modifier = Modifier.fillMaxSize()) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val maxRadius = size.minDimension * 0.42f

        // Static guide rings
        for (i in 1..3) {
            drawCircle(
                color = Signal.copy(alpha = 0.08f),
                radius = maxRadius * (i / 3f),
                center = center,
                style = Stroke(width = 1.5.dp.toPx())
            )
        }

        // Animated expanding/fading pulse rings
        progresses.forEach { animatedProgress ->
            val progress = animatedProgress.value
            val radius = maxRadius * 0.25f + (maxRadius * 0.75f * progress)
            val alpha = (1f - progress) * 0.35f

            drawCircle(
                color = Signal.copy(alpha = alpha),
                radius = radius,
                center = center,
                style = Stroke(width = 1.5.dp.toPx())
            )
        }
    }
}

/**
 * "SCANNING" label followed by 3 dots that pulse in sequence,
 * like a typing / loading indicator.
 */
@Composable
private fun ScanningLabel() {
    Box(contentAlignment = Alignment.Center) {
        androidx.compose.foundation.layout.Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SCANNING",
                color = Signal,
                fontSize = 14.sp,
                letterSpacing = 2.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Medium
            )

            androidx.compose.foundation.layout.Spacer(modifier = Modifier.width(6.dp))

            AnimatedDots()
        }
    }
}

@Composable
private fun AnimatedDots(dotCount: Int = 3) {
    val infiniteTransition = rememberInfiniteTransition(label = "dots")

    androidx.compose.foundation.layout.Row {
        repeat(dotCount) { index ->
            val scale by infiniteTransition.animateFloat(
                initialValue = 0.4f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 600,
                        delayMillis = index * 200,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "dot_scale_$index"
            )

            val alpha by infiniteTransition.animateFloat(
                initialValue = 0.3f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 600,
                        delayMillis = index * 200,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "dot_alpha_$index"
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .size(5.dp)
                    .scale(scale)
                    .background(
                        color = Signal.copy(alpha = alpha),
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashPreview(){
    SplashScreen()
}