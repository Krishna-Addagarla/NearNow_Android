package com.example.nearnow.ui.animations

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring

object NearNowMotion {
    // Duration tokens
    const val Quick = 150    // Button feedback, chip toggle
    const val Standard = 300 // Card expansion, screen elements
    const val Emphasis = 500 // Screen transitions, celebrations
    const val Slow = 800     // Ambient breathing, radar pulses

    // Easing curves
    val EaseOut = CubicBezierEasing(0.0f, 0.0f, 0.2f, 1.0f)
    val EaseInOut = CubicBezierEasing(0.42f, 0.0f, 0.58f, 1.0f)
    val Bounce = CubicBezierEasing(0.34f, 1.56f, 0.64f, 1.0f)

    // Spring specifications
    val BouncySpring = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )
    
    val GentleSpring = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessLow
    )
}
