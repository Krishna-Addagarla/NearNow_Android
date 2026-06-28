package com.example.drift.ui.screens.authentication.Otp

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drift.ui.screens.authentication.Otp.OtpScreen
import kotlinx.coroutines.delay

object NearNowColors {
    val Ink = Color(0xFF0B1220)
    val Signal = Color(0xFF00E6A8)
    val Coral = Color(0xFFFF6B4A)
    val Paper = Color(0xFFF5F2EA)
    val Slate = Color(0xFF8B96A8)
    val FieldFill = Color(0xFF1A2536)
}

private const val OTP_LENGTH = 6
private const val RESEND_SECONDS = 28

@Composable
fun OtpScreen(
    phoneNumber: String = "+91 98765 43210",
    onBackClick: () -> Unit = {},
    onVerifyClick: (code: String) -> Unit = {},
    onResendClick: () -> Unit = {}
) {
    var otp by remember { mutableStateOf("") }
    var secondsLeft by remember { mutableIntStateOf(RESEND_SECONDS) }

    // Countdown timer for resend
    LaunchedEffect(secondsLeft) {
        if (secondsLeft > 0) {
            delay(1000)
            secondsLeft -= 1
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NearNowColors.Ink)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Spacer(modifier = Modifier.height(48.dp))

            // Back button
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(NearNowColors.FieldFill)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = NearNowColors.Paper
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {

                Text(
                    text = "IDENTITY · 02",
                    color = NearNowColors.Signal,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Enter the code",
                    color = NearNowColors.Paper,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Sent to $phoneNumber",
                    color = NearNowColors.Slate,
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(28.dp))

                OtpInputRow(
                    otp = otp,
                    onOtpChange = { newValue ->
                        if (newValue.length <= OTP_LENGTH && newValue.all { it.isDigit() }) {
                            otp = newValue
                        }
                    },
                    length = OTP_LENGTH
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Resend timer / link
                if (secondsLeft > 0) {
                    Text(
                        text = "RESEND IN 0:%02d".format(secondsLeft),
                        color = NearNowColors.Slate,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        letterSpacing = 1.sp
                    )
                } else {
                    Text(
                        text = "RESEND CODE",
                        color = NearNowColors.Signal,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            secondsLeft = RESEND_SECONDS
                            onResendClick()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Button(
                    onClick = { onVerifyClick(otp) },
                    enabled = otp.length == OTP_LENGTH,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NearNowColors.Signal,
                        contentColor = NearNowColors.Ink,
                        disabledContainerColor = NearNowColors.Signal.copy(alpha = 0.4f),
                        disabledContentColor = NearNowColors.Ink.copy(alpha = 0.6f)
                    )
                ) {
                    Text(
                        text = "VERIFY",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.5.sp,
                        fontSize = 15.sp
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

/**
 * Six-box OTP entry. A single invisible BasicTextField captures input;
 * the boxes are purely visual, driven by the current otp string.
 */
@Composable
private fun OtpInputRow(
    otp: String,
    onOtpChange: (String) -> Unit,
    length: Int
) {
    Box {
        // Invisible field that actually receives keyboard input
        BasicTextField(
            value = otp,
            onValueChange = onOtpChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            textStyle = TextStyle(color = Color.Transparent, fontSize = 1.sp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            cursorBrush = androidx.compose.ui.graphics.SolidColor(Color.Transparent),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
                ) {
                    for (i in 0 until length) {
                        val char = otp.getOrNull(i)?.toString() ?: ""
                        val isFilled = char.isNotEmpty()
                        val isNextToFill = i == otp.length

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(NearNowColors.FieldFill)
                                .border(
                                    width = 1.dp,
                                    color = if (isFilled || isNextToFill)
                                        NearNowColors.Signal.copy(alpha = if (isFilled) 0.8f else 0.4f)
                                    else
                                        Color.Transparent,
                                    shape = RoundedCornerShape(10.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isFilled) char else "_",
                                color = if (isFilled) NearNowColors.Signal else NearNowColors.Slate.copy(alpha = 0.4f),
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                // Render the real (invisible) text field on top to keep focus/cursor working
                innerTextField()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OtpScreenPreview (){
    OtpScreen(
        phoneNumber = "",
        onBackClick = {},
        onVerifyClick = {},
        onResendClick = {}
    )
}