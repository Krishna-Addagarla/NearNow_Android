package com.example.nearnow.ui.screens.profileSetUp

import androidx.compose.ui.tooling.preview.Preview


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

/* ---------------------------------------------------------------
   STEP 4 / 4 — "How close is close?"
   Radial readout of the discovery radius + a slider to adjust it.
   --------------------------------------------------------------- */
@Composable
fun ProfileRadiusScreen(
    minMeters: Int = 100,
    maxMeters: Int = 50000,
    onStartExploringClick: (radiusMeters: Int) -> Unit = {}
) {
    var radiusMeters by remember { mutableFloatStateOf(2000f) }

    ProfileSetupStepShell(
        stepNumber = 4,
        totalSteps = 4,
        stepLabel = "STEP 04 / 04 — DONE",
        title = "How close\nis close?",
        primaryButtonText = "START EXPLORING",
        onPrimaryClick = { onStartExploringClick(radiusMeters.roundToInt()) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            RadiusDial(radiusMeters = radiusMeters.roundToInt())

            Spacer(modifier = Modifier.height(24.dp))

            Slider(
                value = radiusMeters,
                onValueChange = { radiusMeters = it },
                valueRange = minMeters.toFloat()..maxMeters.toFloat(),
                colors = SliderDefaults.colors(
                    thumbColor = NearNowColors.Signal,
                    activeTrackColor = NearNowColors.Signal,
                    inactiveTrackColor = NearNowColors.FieldFill
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "${minMeters}m – ${maxMeters / 1000}km",
                color = NearNowColors.Slate,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp
            )
        }
    }
}

/**
 * Concentric dashed/solid ring with the radius value centered inside —
 * mirrors the "radar" visual language from the splash screen.
 */
@Composable
private fun RadiusDial(radiusMeters: Int) {
    Box(
        modifier = Modifier.size(180.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(180.dp)) {
            val strokeWidth = 2.dp.toPx()

            // Outer faint guide ring
            drawCircle(
                color = NearNowColors.Slate.copy(alpha = 0.15f),
                radius = size.minDimension / 2 - strokeWidth,
                style = Stroke(width = strokeWidth)
            )

            // Inner faint guide ring
            drawCircle(
                color = NearNowColors.Slate.copy(alpha = 0.1f),
                radius = (size.minDimension / 2 - strokeWidth) * 0.65f,
                style = Stroke(width = strokeWidth)
            )

            // Active Signal-colored ring representing current radius
            drawCircle(
                color = NearNowColors.Signal,
                radius = size.minDimension / 2 - strokeWidth,
                style = Stroke(width = strokeWidth * 1.5f)
            )
        }

        Text(
            text = "${radiusMeters}m",
            color = NearNowColors.Signal,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileRadiusScreenPreview(){
    ProfileRadiusScreen(

    )
}
