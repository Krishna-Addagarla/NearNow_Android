package com.example.drift.ui.screens.profileSetUp

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow

/* ---------------------------------------------------------------
   STEP 3 / 4 — "Pick six, max"
   Uppercase mono interest tags, multi-select with a max of 6.
   Uses Accompanist FlowRow for simple, dependency-light wrapping.
   If you don't have Accompanist, swap FlowRow for a LazyVerticalGrid.
   --------------------------------------------------------------- */
private val availableInterests = listOf(
    "COFFEE", "PHOTOGRAPHY", "MUSIC", "RUNNING", "BOARDS",
    "FOOD", "CYCLING", "TREKKING", "ART", "TRAVEL"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UserInterestsScreen(
    maxSelectable: Int = 6,
    onContinueClick: (selected: List<String>) -> Unit = {}
) {
    var selected by remember { mutableStateOf(setOf("COFFEE", "MUSIC", "FOOD")) }

    ProfileSetupStepShell(
        stepNumber = 3,
        totalSteps = 4,
        stepLabel = "STEP 03 / 04",
        title = "Pick six,\nmax",
        primaryButtonText = "CONTINUE",
        primaryEnabled = selected.isNotEmpty(),
        onPrimaryClick = { onContinueClick(selected.toList()) }
    ) {
        Column {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                availableInterests.forEach { interest ->
                    InterestTag(
                        label = interest,
                        isSelected = interest in selected,
                        onClick = {
                            selected = when {
                                interest in selected -> selected - interest
                                selected.size < maxSelectable -> selected + interest
                                else -> selected // limit reached, ignore tap
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "%02d / %02d SELECTED".format(selected.size, maxSelectable),
                color = NearNowColors.Signal,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                letterSpacing = 1.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun InterestTag(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = label,
        color = if (isSelected) NearNowColors.Ink else NearNowColors.Slate,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        letterSpacing = 0.5.sp,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isSelected) NearNowColors.Signal
                else NearNowColors.FieldFill
            )
            .border(
                width = 1.dp,
                color = if (isSelected) NearNowColors.Signal else NearNowColors.Slate.copy(alpha = 0.25f),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun UserInterestsScreenPreview(){
    UserInterestsScreen(

    )
}
