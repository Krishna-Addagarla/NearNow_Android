package com.example.nearnow.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nearnow.data.local.model.DiscoveryUser
import com.example.nearnow.ui.components.NearNowPrimaryButton
import com.example.nearnow.ui.theme.*

@Composable
fun ChatComposeScreen(
    user: DiscoveryUser = DiscoveryUser.mockUsers.first(),
    onBackClick: () -> Unit = {},
    onSendRequest: (String) -> Unit = {}
) {
    var messageText by remember {
        mutableStateOf("Hey ${user.name}! Also near Jubilee Hills — coffee walk sounds perfect. Which area?")
    }

    val quickOpeners = listOf(
        "ALSO NEAR JUBILEE HILLS",
        "LOVE THE COFFEE IDEA"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
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

            // Serif Title -> Poppins Bold Title
            Text(
                text = "Message ${user.name}",
                color = TextPrimary,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Info Box: matching constraints description
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CardWhite)
                    .border(1.dp, SoftGray, RoundedCornerShape(16.dp))
            ) {
                // Accent border on the left
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(80.dp)
                        .background(Mango)
                )
                
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Reaches ${user.name} now. Chat activates only if they reply while you're both in range.",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Openers Section
            Text(
                text = "QUICK OPENERS",
                color = TextMuted,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Horizontal row of openers
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                quickOpeners.forEach { opener ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(MangoLight.copy(alpha = 0.1f))
                            .border(1.5.dp, Mango, RoundedCornerShape(12.dp))
                            .clickable {
                                messageText = if (messageText.isEmpty()) {
                                    opener
                                } else {
                                    "$messageText\n$opener"
                                }
                            }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = opener,
                            color = MangoDeep,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Message composing field inside a Cream/White frame
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(FieldFill)
                    .border(1.5.dp, SoftGray, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                BasicTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    textStyle = TextStyle(
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontFamily = PoppinsFamily,
                        lineHeight = 22.sp
                    ),
                    cursorBrush = SolidColor(Mango),
                    modifier = Modifier.fillMaxSize(),
                    decorationBox = { innerTextField ->
                        if (messageText.isEmpty()) {
                            Text(
                                text = "Type your request message...",
                                color = TextMuted,
                                fontFamily = PoppinsFamily,
                                fontSize = 15.sp
                            )
                        }
                        innerTextField()
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Limit counter reads like a fuel gauge
            Text(
                text = "4 / 5 DAILY REQUESTS USED",
                color = Coral,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            // Send Request Button
            NearNowPrimaryButton(
                text = "SEND REQUEST",
                onClick = { onSendRequest(messageText) },
                enabled = messageText.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatComposeScreenPreview() {
    ChatComposeScreen()
}
