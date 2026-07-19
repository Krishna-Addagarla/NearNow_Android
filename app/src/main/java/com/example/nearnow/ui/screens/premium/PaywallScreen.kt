package com.example.nearnow.ui.screens.premium

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nearnow.data.local.model.PremiumTier
import com.example.nearnow.ui.components.NearNowPrimaryButton
import com.example.nearnow.ui.theme.*

@Composable
fun PaywallScreen(
    tiers: List<PremiumTier> = PremiumTier.mockTiers,
    onBackClick: () -> Unit = {},
    onStartTrialClick: (PremiumTier) -> Unit = {}
) {
    var selectedTier by remember { mutableStateOf(tiers.firstOrNull { it.isRecommended } ?: tiers.first()) }

    // Premium light warm gradient background
    val premiumGradient = Brush.verticalGradient(
        colors = listOf(
            Cream,
            MangoLight.copy(alpha = 0.15f),
            Cream
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(premiumGradient)
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Back button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(40.dp)
                        .shadow(2.dp, CircleShape, spotColor = ShadowColor, ambientColor = ShadowColor)
                        .clip(CircleShape)
                        .background(CardWhite)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Sub-headline: NEARNOW PLUS
            Text(
                text = "NEARNOW PLUS",
                color = MangoDeep,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.5.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Title
            Text(
                text = "Wider range.\nDeeper signal.",
                color = TextPrimary,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                lineHeight = 38.sp
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Plus Benefits Feature List
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val benefits = listOf(
                    "5km radius, not 2km",
                    "Unlimited daily requests",
                    "See who viewed you",
                    "Go invisible on the map"
                )

                benefits.forEach { benefit ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "⚡",
                            color = Mango,
                            fontSize = 14.sp
                        )
                        Text(
                            text = benefit,
                            color = TextPrimary,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Pricing Tiers Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                tiers.forEach { tier ->
                    val isSelected = tier.id == selectedTier.id
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .shadow(
                                elevation = if (isSelected) 6.dp else 2.dp,
                                shape = RoundedCornerShape(20.dp),
                                spotColor = ShadowColor,
                                ambientColor = ShadowColor
                            )
                            .clip(RoundedCornerShape(20.dp))
                            .background(CardWhite)
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) Mango else SoftGray,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { selectedTier = tier }
                            .padding(vertical = 20.dp, horizontal = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = tier.name,
                            color = if (isSelected) MangoDeep else TextSecondary,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = tier.priceText,
                            color = TextPrimary,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = tier.billedDetails,
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Start Free Trial Button (Mango Primary)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NearNowPrimaryButton(
                    text = "START FREE TRIAL",
                    onClick = { onStartTrialClick(selectedTier) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "7-day free trial, cancel anytime in settings. Subscription auto-renews.",
                    color = TextMuted,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaywallScreenPreview() {
    PaywallScreen()
}
