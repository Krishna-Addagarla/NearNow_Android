package com.example.drift.ui.screens.profileSetUp

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ---------- Color Tokens (shared across all profile-setup screens) ----------
object NearNowColors {
    val Ink = Color(0xFF0B1220)
    val Signal = Color(0xFF00E6A8)
    val Coral = Color(0xFFFF6B4A)
    val Paper = Color(0xFFF5F2EA)
    val Slate = Color(0xFF8B96A8)
    val FieldFill = Color(0xFF1A2536)
}

/**
 * Shared shell for every profile-setup step: progress bar, step label,
 * title, scrollable body slot, then a primary (and optional secondary)
 * button pinned to the bottom.
 */
@Composable
fun ProfileSetupStepShell(
    stepNumber: Int,
    totalSteps: Int,
    stepLabel: String, // e.g. "STEP 01 / 04"
    title: String,
    primaryButtonText: String,
    onPrimaryClick: () -> Unit,
    primaryEnabled: Boolean = true,
    secondaryButtonText: String? = null,
    onSecondaryClick: () -> Unit = {},
    body: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NearNowColors.Ink)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Spacer(modifier = Modifier.height(48.dp))

            // Top progress bar
            StepProgressBar(
                modifier = Modifier.padding(horizontal = 24.dp),
                currentStep = stepNumber,
                totalSteps = totalSteps
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text(
                    text = stepLabel,
                    color = NearNowColors.Signal,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    letterSpacing = 1.5.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = title,
                    color = NearNowColors.Paper,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    lineHeight = 31.sp
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Scrollable / flexible body content unique to each step
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                body()
            }

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Button(
                    onClick = onPrimaryClick,
                    enabled = primaryEnabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NearNowColors.Signal,
                        contentColor = NearNowColors.Ink,
                        disabledContainerColor = NearNowColors.Signal.copy(alpha = 0.35f),
                        disabledContentColor = NearNowColors.Ink.copy(alpha = 0.6f)
                    )
                ) {
                    Text(
                        text = primaryButtonText,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp,
                        fontSize = 13.sp
                    )
                }

                secondaryButtonText?.let {
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = onSecondaryClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NearNowColors.FieldFill,
                            contentColor = NearNowColors.Slate
                        )
                    ) {
                        Text(
                            text = it,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }
}

@Composable
fun StepProgressBar(
    modifier: Modifier = Modifier,
    currentStep: Int,
    totalSteps: Int
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(3.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(NearNowColors.FieldFill)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = currentStep / totalSteps.toFloat())
                .height(3.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(NearNowColors.Signal)
        )
    }
}

/* ---------------------------------------------------------------
   STEP 1 / 4 — "Who are you here?"
   Name + age inputs, with a note that last name is never shown.
   --------------------------------------------------------------- */
@Composable
fun ProfileStepScreen(
    onContinueClick: (firstName: String, age: String) -> Unit = { _, _ -> }
) {
    var firstName by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    ProfileSetupStepShell(
        stepNumber = 1,
        totalSteps = 4,
        stepLabel = "STEP 01 / 04",
        title = "Who are you\nhere?",
        primaryButtonText = "CONTINUE",
        primaryEnabled = firstName.isNotBlank() && age.isNotBlank(),
        onPrimaryClick = { onContinueClick(firstName, age) }
    ) {
        Column {
            SetupTextField(
                value = firstName,
                onValueChange = { firstName = it },
                placeholder = "First name"
            )

            Spacer(modifier = Modifier.height(10.dp))

            SetupTextField(
                value = age,
                onValueChange = { if (it.length <= 2 && it.all { c -> c.isDigit() }) age = it },
                placeholder = "Age",
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Last name never shown. First name + age only.",
                color = NearNowColors.Slate,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )
        }
    }
}

@Composable
private fun SetupTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(10.dp)),
        placeholder = {
            Text(
                text = placeholder,
                color = NearNowColors.Slate,
                fontSize = 15.sp
            )
        },
        textStyle = TextStyle(
            color = NearNowColors.Paper,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
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
fun ProfileSetupScreenPreview(){
    ProfileStepScreen(
        onContinueClick = {} as (String, String) -> Unit
    )
}
