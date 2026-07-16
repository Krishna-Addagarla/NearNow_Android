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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nearnow.data.local.model.SafetyReportReason
import com.example.nearnow.ui.theme.Coral
import com.example.nearnow.ui.theme.Ink
import com.example.nearnow.ui.theme.Paper
import com.example.nearnow.ui.theme.Slate

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
            .background(Ink)
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
                            .clip(CircleShape)
                            .background(Color(0xFF1E293B))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Paper
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Serif Title
                Text(
                    text = "Report",
                    color = Paper,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Reviewed alert box (Orange-red Coral border)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Transparent)
                        .border(1.dp, Coral, RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Reviewed within 24 hours. They will never know you reported them.",
                        color = Paper,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // REASON monospaced label
                Text(
                    text = "REASON",
                    color = Slate,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Flat list, no card chrome, plain hairline-divided rows
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SafetyReportReason.values().forEach { reason ->
                        val isSelected = reason == selectedReason
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedReason = reason }
                                .padding(vertical = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = reason.displayTitle,
                                    color = if (isSelected) Coral else Paper,
                                    fontSize = 16.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )

                                // Tiny selection dot or circle indicator
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .border(
                                            width = 1.5.dp,
                                            color = if (isSelected) Coral else Slate.copy(alpha = 0.5f),
                                            shape = CircleShape
                                        )
                                        .padding(3.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) Coral else Color.Transparent)
                                )
                            }
                        }
                        HorizontalDivider(color = Color(0xFF1E293B), thickness = 0.5.dp)
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
                // REPORT & BLOCK filled button (Coral color)
                Button(
                    onClick = { onReportAndBlockClick(selectedReason) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Coral,
                        contentColor = Ink
                    )
                ) {
                    Text(
                        text = "REPORT & BLOCK",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // CANCEL button
                Text(
                    text = "CANCEL",
                    color = Slate,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .clickable { onCancelClick() }
                        .padding(vertical = 8.dp)
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
