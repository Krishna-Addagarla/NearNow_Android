package com.example.nearnow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.nearnow.ui.theme.CardWhite
import com.example.nearnow.ui.theme.ShadowColor

@Composable
fun NearNowCard(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(20.dp),
                clip = false,
                ambientColor = ShadowColor,
                spotColor = ShadowColor
            )
            .clip(RoundedCornerShape(20.dp))
            .background(CardWhite)
            .padding(16.dp)
    ) {
        content()
    }
}

@Composable
fun NearNowFloatingCard(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                clip = false,
                ambientColor = ShadowColor,
                spotColor = ShadowColor
            )
            .clip(RoundedCornerShape(24.dp))
            .background(CardWhite)
            .padding(16.dp)
    ) {
        content()
    }
}

@Composable
fun NearNowGlassCard(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(20.dp),
                clip = false,
                ambientColor = ShadowColor,
                spotColor = ShadowColor
            )
            .clip(RoundedCornerShape(20.dp))
            .background(CardWhite.copy(alpha = 0.85f))
            .padding(16.dp)
    ) {
        content()
    }
}
