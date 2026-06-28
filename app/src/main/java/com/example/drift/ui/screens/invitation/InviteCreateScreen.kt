package com.example.drift.ui.screens.invitation

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drift.data.local.model.InviteCategory
import com.example.drift.ui.theme.Coral
import com.example.drift.ui.theme.Ink
import com.example.drift.ui.theme.Paper
import com.example.drift.ui.theme.Signal
import com.example.drift.ui.theme.Slate

@Composable
fun InviteCreateScreen(
    onBackClick: () -> Unit = {},
    onPostInvite: (title: String, desc: String, category: InviteCategory, radiusMeters: Int) -> Unit = { _, _, _, _ -> }
) {
    var selectedCategory by remember { mutableStateOf(InviteCategory.COFFEE) }
    var titleText by remember { mutableStateOf("Coffee near Jubilee Hills?") }
    var descText by remember { mutableStateOf("Heading there in ~30 min. Chill vibes.") }
    var radiusValue by remember { mutableStateOf(500f) } // default 500m

    val maxCharLimit = 60

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Ink)
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
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
                text = "New invite",
                color = Paper,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Category tag selection
            Text(
                text = "CATEGORY",
                color = Slate,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                InviteCategory.values().forEach { category ->
                    val isSelected = category == selectedCategory
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) Coral else Color.Transparent)
                            .border(
                                width = 1.dp,
                                color = if (isSelected) Coral else Slate.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(6.dp)
                            )
                            .clickable { selectedCategory = category }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = category.name,
                            color = if (isSelected) Paper else Slate,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 0.5.sp
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
                    color = Slate,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "${titleText.length}/$maxCharLimit",
                    color = if (titleText.length > maxCharLimit) Coral else Slate,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1E293B))
                    .border(1.dp, Color(0xFF334155), RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                BasicTextField(
                    value = titleText,
                    onValueChange = { if (it.length <= maxCharLimit) titleText = it },
                    textStyle = TextStyle(color = Paper, fontSize = 15.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        if (titleText.isEmpty()) {
                            Text(text = "Title of invite...", color = Slate.copy(alpha = 0.5f), fontSize = 15.sp)
                        }
                        innerTextField()
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Description input container
            Text(
                text = "DESCRIPTION",
                color = Slate,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1E293B))
                    .border(1.dp, Color(0xFF334155), RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                BasicTextField(
                    value = descText,
                    onValueChange = { descText = it },
                    textStyle = TextStyle(color = Paper, fontSize = 15.sp),
                    modifier = Modifier.fillMaxSize(),
                    decorationBox = { innerTextField ->
                        if (descText.isEmpty()) {
                            Text(text = "Details (e.g. heading there in 30 mins)...", color = Slate.copy(alpha = 0.5f), fontSize = 15.sp)
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
                    color = Slate,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "${radiusValue.toInt()}M",
                    color = Coral,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Coral Slider
            Slider(
                value = radiusValue,
                onValueChange = { radiusValue = it },
                valueRange = 100f..1000f,
                colors = SliderDefaults.colors(
                    thumbColor = Coral,
                    activeTrackColor = Coral,
                    inactiveTrackColor = Color(0xFF1E293B)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            // Bottom compose actions
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // POST INVITE primary button (Coral background)
                Button(
                    onClick = {
                        onPostInvite(titleText, descText, selectedCategory, radiusValue.toInt())
                    },
                    enabled = titleText.isNotEmpty() && descText.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Coral,
                        contentColor = Ink,
                        disabledContainerColor = Coral.copy(alpha = 0.4f),
                        disabledContentColor = Ink.copy(alpha = 0.6f)
                    )
                ) {
                    Text(
                        text = "POST INVITE",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // BOOST FOR ₹49 secondary button (outlined dark box)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, Coral, RoundedCornerShape(14.dp))
                        .background(Color.Transparent)
                        .clickable { /* Boost logic */ },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⚡ BOOST FOR ₹49",
                        color = Coral,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Explainer tooltip detailing Boost benefits to address UX ambiguity
                Text(
                    text = "Boost makes your invite live for 12 hours and doubles visibility radius.",
                    color = Slate.copy(alpha = 0.6f),
                    fontSize = 10.sp,
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
