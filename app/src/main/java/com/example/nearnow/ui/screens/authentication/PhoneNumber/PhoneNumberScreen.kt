package com.example.nearnow.ui.screens.authentication.PhoneNumber

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nearnow.data.remote.FirebaseAuthManager

// ---------- Color Tokens ----------
object NearNowColors {
    val Ink = Color(0xFF0B1220)
    val Signal = Color(0xFF00E6A8)
    val Coral = Color(0xFFFF6B4A)
    val Paper = Color(0xFFF5F2EA)
    val Slate = Color(0xFF8B96A8)
    val FieldFill = Color(0xFF1A2536) // subtle elevated surface for inputs
}

@Composable
fun PhoneNumberScreen(
    onSendCodeSuccess: (verificationId: String, fullPhoneNumber: String) -> Unit = { _, _ -> }
) {
    var countryCode by remember { mutableStateOf("+91") }
    var phoneNumber by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NearNowColors.Ink)
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
                Spacer(modifier = Modifier.height(64.dp))

                // Eyebrow label: "IDENTITY · 01"
                Text(
                    text = "IDENTITY · 01",
                    color = NearNowColors.Signal,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Headline
                Text(
                    text = "What's your\nnumber?",
                    color = NearNowColors.Paper,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 34.sp,
                    lineHeight = 40.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Subtitle
                Text(
                    text = "No password. No email. Just a code.",
                    color = NearNowColors.Slate,
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Country code + phone number row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(10.dp)
                ) {
                    CountryCodeField(
                        value = countryCode,
                        onValueChange = { countryCode = it },
                        modifier = Modifier.width(72.dp)
                    )

                    PhoneNumberField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Info text
                Text(
                    text = "Standard SMS rates may apply",
                    color = NearNowColors.Slate.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 16.dp)
            ) {
                Button(
                    onClick = {
                        if (phoneNumber.trim().isEmpty()) {
                            Toast.makeText(context, "Please enter a phone number", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        isLoading = true
                        val fullPhone = "$countryCode${phoneNumber.trim()}"
                        val activity = context as? Activity
                        if (activity != null) {
                            FirebaseAuthManager.sendOtp(
                                phoneNumber = fullPhone,
                                activity = activity,
                                onCodeSent = { verificationId ->
                                    isLoading = false
                                    onSendCodeSuccess(verificationId, fullPhone)
                                },
                                onVerificationFailed = { exception ->
                                    isLoading = false
                                    Toast.makeText(context, "Failed: ${exception.localizedMessage}", Toast.LENGTH_LONG).show()
                                }
                            )
                        } else {
                            isLoading = false
                            Toast.makeText(context, "Error: Context is not an Activity", Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NearNowColors.Signal,
                        contentColor = NearNowColors.Ink,
                        disabledContainerColor = NearNowColors.Signal.copy(alpha = 0.5f),
                        disabledContentColor = NearNowColors.Ink.copy(alpha = 0.5f)
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = NearNowColors.Ink,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "SEND CODE",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.5.sp,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CountryCodeField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp)),
        textStyle = androidx.compose.ui.text.TextStyle(
            color = NearNowColors.Paper,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = NearNowColors.FieldFill,
            unfocusedContainerColor = NearNowColors.FieldFill,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = NearNowColors.Signal
        )
    )
}

@Composable
private fun PhoneNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp)),
        placeholder = {
            Text(
                text = "98765 43210",
                color = NearNowColors.Slate,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        },
        textStyle = androidx.compose.ui.text.TextStyle(
            color = NearNowColors.Paper,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = NearNowColors.FieldFill,
            unfocusedContainerColor = NearNowColors.FieldFill,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = NearNowColors.Signal
        )
    )
}


@Preview(showBackground = true)
@Composable
fun PhoneNumberScreenPreview (){
    PhoneNumberScreen(
    )
}