package com.example.nearnow.ui.screens.invitation

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nearnow.data.local.model.InviteCategory
import com.example.nearnow.ui.components.NearNowChip
import com.example.nearnow.ui.components.NearNowCoralChip
import com.example.nearnow.ui.components.NearNowDestructiveButton
import com.example.nearnow.ui.theme.*

@Composable
fun InviteCreateScreen(
    onBackClick: () -> Unit = {},
    onPostInvite: (title: String, desc: String, category: InviteCategory, radiusMeters: Int) -> Unit = { _, _, _, _ -> }
) {
    var selectedCategory by remember { mutableStateOf(InviteCategory.COFFEE) }
    var titleText by remember { mutableStateOf("Coffee near Jubilee Hills?") }
    var descText by remember { mutableStateOf("Heading there in ~30 min. Chill vibes.") }
    var radiusValue by remember { mutableStateOf(2000f) }

    val maxCharLimit = 60
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

                // Header Back row
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
                    text = "New invite",
                    color = TextPrimary,
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Category tag selection
                Text(
                    text = "CATEGORY",
                    color = TextMuted,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    InviteCategory.values().forEach { category ->
                        val isSelected = category == selectedCategory
                        if (isSelected) {
                            NearNowCoralChip(
                                label = category.name,
                                onClick = { selectedCategory = category }
                            )
                        } else {
                            NearNowChip(
                                label = category.name,
                                onClick = { selectedCategory = category }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Title input container (60-char title limit)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "TITLE",
                        color = TextMuted,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${titleText.length}/$maxCharLimit",
                        color = if (titleText.length > maxCharLimit) Coral else TextSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(FieldFill)
                        .border(1.dp, SoftGray, RoundedCornerShape(16.dp))
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        value = titleText,
                        onValueChange = { if (it.length <= maxCharLimit) titleText = it },
                        textStyle = TextStyle(
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontFamily = PoppinsFamily,
                            fontWeight = FontWeight.Bold
                        ),
                        cursorBrush = SolidColor(Mango),
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { innerTextField ->
                            if (titleText.isEmpty()) {
                                Text(
                                    text = "Title of invite...",
                                    color = TextMuted,
                                    fontFamily = PoppinsFamily,
                                    fontSize = 15.sp
                                )
                            }
                            innerTextField()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Description input container
                Text(
                    text = "DESCRIPTION",
                    color = TextMuted,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(96.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(FieldFill)
                        .border(1.dp, SoftGray, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    BasicTextField(
                        value = descText,
                        onValueChange = { descText = it },
                        textStyle = TextStyle(
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontFamily = PoppinsFamily
                        ),
                        cursorBrush = SolidColor(Mango),
                        modifier = Modifier.fillMaxSize(),
                        decorationBox = { innerTextField ->
                            if (descText.isEmpty()) {
                                Text(
                                    text = "Details (e.g. heading there in 30 mins)...",
                                    color = TextMuted,
                                    fontFamily = PoppinsFamily,
                                    fontSize = 15.sp
                                )
                            }
                            innerTextField()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Radius Slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "RADIUS",
                        color = TextMuted,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${radiusValue.toInt()}M",
                        color = Coral,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Slider(
                    value = radiusValue,
                    onValueChange = { radiusValue = it },
                    valueRange = 100f..50000f,
                    colors = SliderDefaults.colors(
                        thumbColor = Coral,
                        activeTrackColor = Coral,
                        inactiveTrackColor = SoftGray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Bottom compose actions
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // POST INVITE primary button (Coral background)
                NearNowDestructiveButton(
                    text = "POST INVITE",
                    onClick = {
                        onPostInvite(titleText, descText, selectedCategory, radiusValue.toInt())
                    },
                    enabled = titleText.isNotEmpty() && descText.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // BOOST FOR ₹49 secondary button (outlined Coral box)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .border(1.5.dp, Coral, RoundedCornerShape(24.dp))
                        .background(Color.Transparent)
                        .clickable { /* Boost logic */ },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⚡ BOOST FOR ₹49",
                        color = Coral,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Boost makes your invite live for 12 hours and doubles visibility radius.",
                    color = TextMuted,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InviteCreateScreenPreview() {
    InviteCreateScreen()
}
