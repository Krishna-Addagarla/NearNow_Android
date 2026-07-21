package com.example.nearnow.ui.screens.profileSetUp

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nearnow.ui.components.NearNowPrimaryButton
import com.example.nearnow.ui.components.NearNowSecondaryButton
import com.example.nearnow.ui.components.NearNowTextField
import com.example.nearnow.ui.theme.*

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
    primaryLoading: Boolean = false,
    secondaryButtonText: String? = null,
    onSecondaryClick: () -> Unit = {},
    body: @Composable () -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
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
                        color = Mango,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = title,
                        color = TextPrimary,
                        style = MaterialTheme.typography.headlineLarge,
                        lineHeight = 34.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Scrollable / flexible body content unique to each step
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    body()
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            Column(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 24.dp)
            ) {
                NearNowPrimaryButton(
                    text = primaryButtonText,
                    onClick = onPrimaryClick,
                    enabled = primaryEnabled && !primaryLoading,
                    isLoading = primaryLoading,
                    modifier = Modifier.fillMaxWidth()
                )

                secondaryButtonText?.let {
                    Spacer(modifier = Modifier.height(12.dp))
                    NearNowSecondaryButton(
                        text = it,
                        onClick = onSecondaryClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
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
            .height(5.dp)
            .clip(RoundedCornerShape(2.5.dp))
            .background(SoftGray)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = currentStep / totalSteps.toFloat())
                .height(5.dp)
                .clip(RoundedCornerShape(2.5.dp))
                .background(Mango)
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
            NearNowTextField(
                value = firstName,
                onValueChange = { firstName = it },
                placeholder = "First name"
            )

            Spacer(modifier = Modifier.height(14.dp))

            NearNowTextField(
                value = age,
                onValueChange = { if (it.length <= 2 && it.all { c -> c.isDigit() }) age = it },
                placeholder = "Age",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Last name never shown. First name + age only.",
                color = TextSecondary,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileSetupScreenPreview() {
    ProfileStepScreen(
        onContinueClick = { _, _ -> }
    )
}
