package com.example.nearnow.ui.screens.onBoaring

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nearnow.ui.components.NearNowPrimaryButton
import com.example.nearnow.ui.components.NearNowSecondaryButton
import com.example.nearnow.ui.theme.*
import kotlinx.coroutines.launch

data class OnboardingPage(
    val pageNumber: Int,
    val titleLines: List<Pair<String, Boolean>>, // text, isEmphasis
    val subtitle: String,
    val extra: (@Composable () -> Unit)? = null,
    val primaryButtonText: String = "CONTINUE",
    val secondaryButtonText: String? = null
)

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit = {},
    onLocationPermissionRequested: () -> Unit = {}
) {
    val onboardingPages = listOf(
        OnboardingPage(
            pageNumber = 1,
            titleLines = listOf(
                "Distance\nhas\n" to false,
                "urgency." to true,
                "\nAlgorithms don't." to false
            ),
            subtitle = "No 40km matches. Only people close enough to actually meet — today.",
            extra = { PulseMapUrgencyVisual() }
        ),
        OnboardingPage(
            pageNumber = 2,
            titleLines = listOf(
                "We show a\n" to false,
                "band" to true,
                ", never a pin." to false
            ),
            subtitle = "No coordinates. No movement trail. Safe updates every two minutes.",
            extra = { DistanceBandBadge(distanceMeters = 180, noiseMeters = 40) }
        ),
        OnboardingPage(
            pageNumber = 3,
            titleLines = listOf(
                "One\npermission.\nThat's it." to false
            ),
            subtitle = "Location, while the app is open. Used only to compute the safety band.",
            extra = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BulletItem(text = "Show nearby people on the map")
                    BulletItem(text = "Pause chats when out of range")
                }
            },
            primaryButtonText = "CREATE ACCOUNT",
            secondaryButtonText = "LOG IN"
        )
    )

    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val scope = rememberCoroutineScope()

    HorizontalPager(state = pagerState) { pageIndex ->
        val page = onboardingPages[pageIndex]
        OnboardingScreen(
            page = page,
            totalPages = onboardingPages.size,
            onPrimaryClick = {
                if (pageIndex == onboardingPages.lastIndex) {
                    onLocationPermissionRequested()
                } else {
                    scope.launch {
                        pagerState.animateScrollToPage(pageIndex + 1)
                    }
                }
            },
            onSecondaryClick = { onFinish() }
        )
    }
}

@Composable
fun OnboardingScreen(
    page: OnboardingPage,
    totalPages: Int,
    onPrimaryClick: () -> Unit = {},
    onSecondaryClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        MangoLight.copy(alpha = 0.05f),
                        Cream
                    ),
                    radius = 1000f
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Step Counter Tag
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Coral.copy(alpha = 0.08f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "%02d / %02d".format(page.pageNumber, totalPages),
                    color = Coral,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Display Title
            Text(
                text = buildAnnotatedString {
                    page.titleLines.forEach { (text, isEmphasis) ->
                        withStyle(
                            SpanStyle(
                                color = if (isEmphasis) Mango else TextPrimary,
                                fontFamily = PoppinsFamily,
                                fontStyle = if (isEmphasis) FontStyle.Italic else FontStyle.Normal,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(text)
                        }
                    }
                },
                style = MaterialTheme.typography.displayMedium,
                lineHeight = 36.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Premium visual item slot
            page.extra?.let { extraContent ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    extraContent()
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Subtitle description text
            Text(
                text = page.subtitle,
                color = TextSecondary,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            if (page.secondaryButtonText == null) {
                PageProgressIndicator(totalPages = totalPages, currentPage = page.pageNumber)
                Spacer(modifier = Modifier.height(24.dp))
            }

            NearNowPrimaryButton(
                text = page.primaryButtonText,
                onClick = onPrimaryClick,
                modifier = Modifier.fillMaxWidth()
            )

            page.secondaryButtonText?.let { secondaryText ->
                Spacer(modifier = Modifier.height(12.dp))
                NearNowSecondaryButton(
                    text = secondaryText,
                    onClick = onSecondaryClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun PageProgressIndicator(totalPages: Int, currentPage: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..totalPages) {
            val isActive = i == currentPage
            Box(
                modifier = Modifier
                    .height(5.dp)
                    .width(if (isActive) 28.dp else 12.dp)
                    .clip(RoundedCornerShape(2.5.dp))
                    .background(
                        if (isActive) Mango
                        else SoftGray
                    )
            )
        }
    }
}

/**
 * Slide 1 Graphic: Pulse Map Urgency Visual representation
 * Animated canvas drawing real-time connection circles pulsing
 */
@Composable
private fun PulseMapUrgencyVisual() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_urgency")
    val pulseSize by infiniteTransition.animateFloat(
        initialValue = 20f,
        targetValue = 90f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = EaseOutQuad),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse"
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(24.dp))
            .background(CardWhite)
            .border(1.dp, SoftGray, RoundedCornerShape(24.dp))
    ) {
        val center = Offset(size.width / 2, size.height / 2)

        // Pulsing urgent waves
        drawCircle(
            color = Mango.copy(alpha = (1f - (pulseSize / 90f)) * 0.25f),
            radius = pulseSize,
            center = center,
            style = Stroke(width = 3.dp.toPx())
        )

        drawCircle(
            color = Teal.copy(alpha = (1f - (pulseSize / 90f)) * 0.15f),
            radius = pulseSize * 0.7f,
            center = center,
            style = Stroke(width = 2.dp.toPx())
        )

        // Map Node Self (YOU)
        drawCircle(
            color = Mango,
            radius = 16f,
            center = center
        )

        drawCircle(
            color = CardWhite,
            radius = 8f,
            center = center
        )
    }
}

/**
 * Slide 2 Graphic: Proximity radar band diagram
 * Replaces the simple text box with a gorgeous graphic
 */
@Composable
private fun DistanceBandBadge(distanceMeters: Int, noiseMeters: Int) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(24.dp))
            .background(CardWhite)
            .border(1.dp, SoftGray, RoundedCornerShape(24.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side: Radar dial canvas
            Canvas(
                modifier = Modifier
                    .size(100.dp)
                    .weight(0.45f)
            ) {
                val center = Offset(size.width / 2, size.height / 2)

                // Shaded circular band
                drawCircle(
                    color = MangoLight.copy(alpha = 0.18f),
                    radius = 42f,
                    center = center,
                    style = Stroke(width = 16f)
                )

                // Concentric inner line
                drawCircle(
                    color = SoftGray,
                    radius = 24f,
                    center = center,
                    style = Stroke(width = 2f)
                )

                // Center node
                drawCircle(
                    color = Mango,
                    radius = 6f,
                    center = center
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Right side: readout details
            Column(
                modifier = Modifier.weight(0.55f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "PROXIMITY BAND",
                    color = Teal,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "~${distanceMeters}m distance",
                    color = TextPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "±${noiseMeters}m safety buffer",
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * Slide 3: Clean Checkmark item row card
 */
@Composable
private fun BulletItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardWhite)
            .border(1.dp, SoftGray, RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(Mango.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Checked",
                tint = Mango,
                modifier = Modifier.size(14.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = text,
            color = TextPrimary,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen(
        onFinish = {},
        onLocationPermissionRequested = {}
    )
}