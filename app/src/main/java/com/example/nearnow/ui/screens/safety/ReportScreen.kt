package com.example.nearnow.ui.screens.safety

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nearnow.data.local.model.SafetyReportReason
import com.example.nearnow.ui.components.NearNowDestructiveButton
import com.example.nearnow.ui.components.NearNowGhostButton
import com.example.nearnow.ui.theme.*

@Composable
fun ReportScreen(
    onBackClick: () -> Unit = {},
    onReportAndBlockClick: (SafetyReportReason) -> Unit = {},
    onCancelClick: () -> Unit = {}
) {
    var selectedReason by remember { mutableStateOf(SafetyReportReason.HARASSMENT) }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(48.dp))

                // Header Back button
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

                // Title
                Text(
                    text = "Report",
                    color = TextPrimary,
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Reviewed alert box (Orange-red Coral border)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(16.dp), spotColor = ShadowColor, ambientColor = ShadowColor)
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardWhite)
                        .border(1.5.dp, Coral, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Reviewed within 24 hours. They will never know you reported them.",
                        color = TextPrimary,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // REASON label
                Text(
                    text = "REASON",
                    color = TextMuted,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Flat list styled cleanly
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(20.dp), spotColor = ShadowColor, ambientColor = ShadowColor)
                        .clip(RoundedCornerShape(20.dp))
                        .background(CardWhite)
                        .border(1.dp, SoftGray, RoundedCornerShape(20.dp))
                ) {
                    SafetyReportReason.values().forEachIndexed { index, reason ->
                        val isSelected = reason == selectedReason
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedReason = reason }
                                .padding(horizontal = 16.dp, vertical = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = reason.displayTitle,
                                    color = if (isSelected) Coral else TextPrimary,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )

                                // Tiny selection dot or circle indicator
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .border(
                                            width = 2.dp,
                                            color = if (isSelected) Coral else TextMuted,
                                            shape = CircleShape
                                        )
                                        .padding(3.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) Coral else Color.Transparent)
                                )
                            }
                        }
                        if (index < SafetyReportReason.values().size - 1) {
                            HorizontalDivider(color = SoftGray, thickness = 1.dp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Action CTA Buttons at bottom
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NearNowDestructiveButton(
                    text = "REPORT & BLOCK",
                    onClick = { onReportAndBlockClick(selectedReason) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                NearNowGhostButton(
                    text = "CANCEL",
                    onClick = onCancelClick,
                    textColor = TextSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReportScreenPreview() {
    ReportScreen()
}
