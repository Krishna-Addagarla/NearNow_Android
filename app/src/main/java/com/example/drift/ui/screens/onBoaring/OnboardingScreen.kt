package com.example.drift.ui.screens.onBoaring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drift.ui.theme.Coral
import com.example.drift.ui.theme.Ink
import com.example.drift.ui.theme.Paper
import com.example.drift.ui.theme.Signal
import com.example.drift.ui.theme.Slate
import kotlinx.coroutines.launch


/**
 * One generic page's content. `extra` is a slot for whatever sits between
 * the headline and the subtitle — a badge, an icon, a bullet list, or
 * nothing at all (screen 1 has no extra content).
 */
data class OnboardingPage(
    val pageNumber: Int,
    val titleLines: List<Pair<String, Boolean>>, // text, isEmphasis(signal+italic)
    val subtitle: String,
    val extra: (@Composable () -> Unit)? = null,
    val primaryButtonText: String = "CONTINUE",
    val secondaryButtonText: String? = null
)

private val onboardingPages = listOf(
    OnboardingPage(
        pageNumber = 1,
        titleLines = listOf(
            "Distance\nhas\nurgency." to false,
            "Algorithms\ndon't." to true
        ),
        subtitle = "No 40km matches. Only people close enough to actually meet — today."
    ),
    OnboardingPage(
        pageNumber = 2,
        titleLines = listOf(
            "We show a " to false,
            "band" to true,
            ",\nnever a pin." to false
        ),
        subtitle = "No coordinates. No movement trail. Updates every two minutes.",
        extra = { DistanceBandBadge(distanceMeters = 182, noiseMeters = 38) }
    ),
    OnboardingPage(
        pageNumber = 3,
        titleLines = listOf(
            "One\npermission.\nThat's it." to false
        ),
        subtitle = "Location, while the app is open. Used only to compute the band above.",
        extra = {
            Column {
                BulletItem(text = "Show nearby people on the map")
                Spacer(modifier = Modifier.height(14.dp))
                BulletItem(text = "Pause chats when out of range")
            }
        },
        primaryButtonText = "ALLOW LOCATION ACCESS",
        secondaryButtonText = "NOT NOW"
    )
)

/**
 * Top-level entry point: swipeable 3-page onboarding flow.
 * Drop this directly into your nav graph.
 */
@Composable
fun OnboardingScreen(
    onFinish: () -> Unit = {},
    onLocationPermissionRequested: () -> Unit = {}
) {
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
private fun rememberCoroutineScope() = androidx.compose.runtime.rememberCoroutineScope()

/**
 * The single shared screen shell used by every onboarding page.
 */
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
            .background(Ink)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "%02d / %02d".format(page.pageNumber, totalPages),
                color = Coral,
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = buildAnnotatedString {
                    page.titleLines.forEach { (text, isEmphasis) ->
                        withStyle(
                            SpanStyle(
                                color = if (isEmphasis) Signal else Paper,
                                fontFamily = FontFamily.Serif,
                                fontStyle = if (isEmphasis) FontStyle.Italic else FontStyle.Normal,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(text)
                        }
                    }
                },
                fontSize = 36.sp,
                lineHeight = 42.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Optional extra content slot (badge / bullet list / nothing)
            page.extra?.let { extraContent ->
                extraContent()
                Spacer(modifier = Modifier.height(24.dp))
            }

            Text(
                text = page.subtitle,
                color = Slate,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            if (page.secondaryButtonText == null) {
                // Pages 1 & 2 show the dot/pill progress indicator
                PageProgressIndicator(totalPages = totalPages, currentPage = page.pageNumber)
                Spacer(modifier = Modifier.height(20.dp))
            }

            Button(
                onClick = onPrimaryClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Signal,
                    contentColor = Ink
                )
            ) {
                Text(
                    text = page.primaryButtonText,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp,
                    fontSize = 14.sp
                )
            }

            page.secondaryButtonText?.let { secondaryText ->
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = onSecondaryClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Paper.copy(alpha = 0.06f),
                        contentColor = Slate
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = Slate.copy(alpha = 0.25f)
                    )
                ) {
                    Text(
                        text = secondaryText,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ---------- Shared small components ----------

@Composable
private fun PageProgressIndicator(totalPages: Int, currentPage: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..totalPages) {
            val isActive = i == currentPage
            Box(
                modifier = Modifier
                    .height(4.dp)
                    .width(if (isActive) 28.dp else 16.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        if (isActive) Signal
                        else Slate.copy(alpha = 0.3f)
                    )
            )
        }
    }
}

@Composable
private fun DistanceBandBadge(distanceMeters: Int, noiseMeters: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 1.dp,
                color = Signal.copy(alpha = 0.35f),
                shape = RoundedCornerShape(10.dp)
            )
            .background(Signal.copy(alpha = 0.06f))
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${distanceMeters}m ± ${noiseMeters}m noise",
            color = Paper,
            fontFamily = FontFamily.Monospace,
            fontSize = 15.sp,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
private fun BulletItem(text: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .padding(top = 7.dp)
                .size(6.dp)
                .clip(CircleShape)
                .background(Signal)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            color = Paper,
            fontSize = 15.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview (){
    OnboardingScreen(
        onFinish = {},
        onLocationPermissionRequested = {}
    )
}