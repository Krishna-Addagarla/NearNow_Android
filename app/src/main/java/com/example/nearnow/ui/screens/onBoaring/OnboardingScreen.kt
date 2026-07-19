package com.example.nearnow.ui.screens.onBoaring

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

private val onboardingPages = listOf(
    OnboardingPage(
        pageNumber = 1,
        titleLines = listOf(
            "Distance\nhas\nurgency." to false,
            "\nAlgorithms\ndon't." to true
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
fun OnboardingScreen(
    page: OnboardingPage,
    totalPages: Int,
    onPrimaryClick: () -> Unit = {},
    onSecondaryClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
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
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

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
                lineHeight = 38.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            page.extra?.let { extraContent ->
                extraContent()
                Spacer(modifier = Modifier.height(24.dp))
            }

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
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..totalPages) {
            val isActive = i == currentPage
            Box(
                modifier = Modifier
                    .height(5.dp)
                    .width(if (isActive) 28.dp else 16.dp)
                    .clip(RoundedCornerShape(2.5.dp))
                    .background(
                        if (isActive) Mango
                        else SoftGray
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
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.5.dp,
                color = MangoLight,
                shape = RoundedCornerShape(16.dp)
            )
            .background(MangoLight.copy(alpha = 0.08f))
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${distanceMeters}m ± ${noiseMeters}m noise",
            color = TextPrimary,
            fontFamily = PoppinsFamily,
            fontWeight = FontWeight.Bold,
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
                .padding(top = 8.dp)
                .size(8.dp)
                .clip(CircleShape)
                .background(Mango)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            color = TextPrimary,
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = 22.sp,
            fontWeight = FontWeight.Medium
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