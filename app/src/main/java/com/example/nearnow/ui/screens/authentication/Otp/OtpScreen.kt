package com.example.nearnow.ui.screens.authentication.Otp

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nearnow.data.remote.FirebaseAuthManager
import com.example.nearnow.ui.components.NearNowOtpField
import com.example.nearnow.ui.components.NearNowPrimaryButton
import com.example.nearnow.ui.theme.*
import kotlinx.coroutines.delay

private const val OTP_LENGTH = 6
private const val RESEND_SECONDS = 28

@Composable
fun OtpScreen(
    verificationId: String,
    phoneNumber: String,
    onBackClick: () -> Unit = {},
    onVerifySuccess: (token: String) -> Unit = {},
    onResendClick: () -> Unit = {}
) {
    var otp by remember { mutableStateOf("") }
    var secondsLeft by remember { mutableIntStateOf(RESEND_SECONDS) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scrollState = rememberScrollState()

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
            .background(Cream)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(48.dp))

                // Back button
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .padding(start = 16.dp)
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

                Spacer(modifier = Modifier.height(32.dp))

                Column(modifier = Modifier.padding(horizontal = 24.dp)) {

                    Text(
                        text = "IDENTITY · 02",
                        color = Mango,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Enter the code",
                        color = TextPrimary,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Sent to $phoneNumber",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    NearNowOtpField(
                        otp = otp,
                        onOtpChange = { otp = it },
                        length = OTP_LENGTH,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Resend timer / link
                    if (secondsLeft > 0) {
                        Text(
                            text = "RESEND IN 0:%02d".format(secondsLeft),
                            color = TextMuted,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = "RESEND CODE",
                            color = Mango,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
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
            }

            Column(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 24.dp)
            ) {
                NearNowPrimaryButton(
                    text = "VERIFY",
                    onClick = {
                        isLoading = true
                        FirebaseAuthManager.verifyOtpAndAuthenticate(
                            verificationId = verificationId,
                            code = otp,
                            onSuccess = { token ->
                                (context as? Activity)?.runOnUiThread {
                                    isLoading = false
                                    onVerifySuccess(token)
                                }
                            },
                            onFailure = { exception ->
                                (context as? Activity)?.runOnUiThread {
                                    isLoading = false
                                    Toast.makeText(context, "Verification failed: ${exception.localizedMessage}", Toast.LENGTH_LONG).show()
                                }
                            }
                        )
                    },
                    enabled = otp.length == OTP_LENGTH && !isLoading,
                    isLoading = isLoading,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OtpScreenPreview() {
    OtpScreen(
        verificationId = "mock-id",
        phoneNumber = "+91 98765 00001",
        onBackClick = {},
        onVerifySuccess = {},
        onResendClick = {}
    )
}