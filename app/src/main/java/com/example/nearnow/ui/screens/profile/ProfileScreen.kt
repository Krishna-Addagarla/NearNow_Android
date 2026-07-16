package com.example.nearnow.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
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
import com.example.nearnow.ui.screens.discovery.DiscoveryBottomBar
import com.example.nearnow.ui.theme.Coral
import com.example.nearnow.ui.theme.Ink
import com.example.nearnow.ui.theme.Paper
import com.example.nearnow.ui.theme.Signal
import com.example.nearnow.ui.theme.Slate

@Composable
fun ProfileScreen(
    onBackClick: () -> Unit = {},
    onTabSelect: (String) -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    var radiusValue by remember { mutableFloatStateOf(2000f) }
    var isInvisibleMode by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    // Premium gold-themed gradient for avatar border & badges
    val goldGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFFFD700), // Gold
            Color(0xFFFFA500)  // Orange Gold
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Ink)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(56.dp))

                // Profile Header Card
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Avatar Circle with Premium gold ring
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(goldGradient)
                            .padding(3.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E293B)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "KR",
                            color = Paper,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 36.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // User Name & Age
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Krish",
                            color = Paper,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp
                        )
                        Text(
                            text = "24",
                            color = Slate,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Premium Badge Status
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(goldGradient)
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "NEARNOW PLUS MEMBER",
                            color = Ink,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Bio Box
                    Text(
                        text = "Android dev & UI enthusiast. Love discovering fresh coffee shops, minimal design systems, and chill house beats.",
                        color = Slate,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Quick Statistics Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val stats = listOf(
                        Triple("12", "Sent", Coral),
                        Triple("154", "Scans", Signal),
                        Triple("4", "Chats", Color(0xFF00E6A8))
                    )

                    stats.forEach { (count, label, color) ->
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF121A2B))
                                .border(0.5.dp, Color(0xFF1E2438), RoundedCornerShape(12.dp))
                                .padding(vertical = 14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = count,
                                color = color,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = label.uppercase(),
                                color = Slate.copy(alpha = 0.8f),
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Settings & Configuration Section
                Text(
                    text = "DISCOVERY SETTINGS",
                    color = Slate,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Radius Adjustment Card
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF121A2B)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Discovery Radius",
                                color = Paper,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${radiusValue.toInt()}m",
                                color = Signal,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Slider(
                            value = radiusValue,
                            onValueChange = { radiusValue = it },
                            valueRange = 100f..50000f,
                            colors = SliderDefaults.colors(
                                thumbColor = Signal,
                                activeTrackColor = Signal,
                                inactiveTrackColor = Color(0xFF1E293B)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Invisible Mode Toggle Card
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF121A2B)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Ghost Mode",
                                color = Paper,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Go invisible on the live map",
                                color = Slate,
                                fontSize = 12.sp
                            )
                        }
                        Switch(
                            checked = isInvisibleMode,
                            onCheckedChange = { isInvisibleMode = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Paper,
                                checkedTrackColor = Signal,
                                uncheckedThumbColor = Slate,
                                uncheckedTrackColor = Color(0xFF1E293B)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Standard Setting Items List
                Text(
                    text = "ACCOUNT",
                    color = Slate,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF121A2B))
                ) {
                    val menuItems = listOf(
                        Pair(Icons.Default.Settings, "Account Settings"),
                        Pair(Icons.Default.Share, "Invite Friends"),
                        Pair(Icons.Default.Info, "Safety & Support")
                    )

                    menuItems.forEachIndexed { index, (icon, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { /* Navigate to setting detail */ }
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = icon, contentDescription = label, tint = Slate, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(text = label, color = Paper, fontSize = 15.sp, modifier = Modifier.weight(1f))
                            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Slate)
                        }
                        if (index < menuItems.lastIndex) {
                            HorizontalDivider(color = Color(0xFF1A2438), thickness = 0.5.dp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Sign Out Primary CTA
                Button(
                    onClick = onLogoutClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1E293B),
                        contentColor = Coral
                    )
                ) {
                    Text(
                        text = "LOG OUT",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Bottom Navigation Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                DiscoveryBottomBar(
                    selectedTab = "Me",
                    onTabClick = onTabSelect
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
