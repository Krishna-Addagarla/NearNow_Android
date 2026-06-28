package com.example.drift.ui.screens.premium

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drift.data.local.model.PremiumTier
import com.example.drift.ui.theme.Ink
import com.example.drift.ui.theme.Paper
import com.example.drift.ui.theme.Signal
import com.example.drift.ui.theme.Slate

@Composable
fun PaywallScreen(
    tiers: List<PremiumTier> = PremiumTier.mockTiers,
    onBackClick: () -> Unit = {},
    onStartTrialClick: (PremiumTier) -> Unit = {}
) {
    var selectedTier by remember { mutableStateOf(tiers.firstOrNull { it.isRecommended } ?: tiers.first()) }

    // Premium deep forest green gradient background
    val premiumGradient = Brush.verticalGradient(
        colors = listOf(
            Ink,
            Color(0xFF0F261E), // Deep Emerald Forest Green
            Color(0xFF071410)
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
                        .clip(CircleShape)
                        .background(Color(0xFF1E293B).copy(alpha = 0.4f))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Paper
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Sub-headline: NEARNOW PLUS
            Text(
                text = "NEARNOW PLUS",
                color = Signal,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                letterSpacing = 2.5.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Serif Title
            Text(
                text = "Wider range.\nDeeper signal.",
                color = Paper,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                lineHeight = 44.sp
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
                        // Custom green bullet symbol
                        Text(
                            text = "⚡",
                            color = Signal,
                            fontSize = 14.sp
                        )
                        Text(
                            text = benefit,
                            color = Paper.copy(alpha = 0.9f),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Pricing Selectors (Horizontal row)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                tiers.forEach { tier ->
                    val isSelected = tier.id == selectedTier.id
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) Color(0xFF0D251D) else Color(0xFF1E293B).copy(alpha = 0.4f))
                            .border(
                                width = 1.5.dp,
                                color = if (isSelected) Signal else Color.Transparent,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable { selectedTier = tier }
                            .padding(vertical = 20.dp, horizontal = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = tier.name,
                            color = if (isSelected) Signal else Slate,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = tier.priceText,
                            color = Paper,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Billed details to address pricing transparency UX finding
                        Text(
                            text = tier.billedDetails,
                            color = Slate,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Start Free Trial Button (Signal Green)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { onStartTrialClick(selectedTier) },
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
                        text = "START FREE TRIAL",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Explainer trial terms below CTA button to solve trial term clarity issue
                Text(
                    text = "7-day free trial, cancel anytime in settings. Subscription auto-renews.",
                    color = Slate.copy(alpha = 0.7f),
                    fontSize = 11.sp,
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
