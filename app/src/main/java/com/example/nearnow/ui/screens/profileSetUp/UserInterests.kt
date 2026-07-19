package com.example.nearnow.ui.screens.profileSetUp

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import com.example.nearnow.ui.components.NearNowActiveChip
import com.example.nearnow.ui.components.NearNowChip
import com.example.nearnow.ui.theme.*

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
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                availableInterests.forEach { interest ->
                    val isSelected = interest in selected
                    if (isSelected) {
                        NearNowActiveChip(
                            label = interest,
                            onClick = { selected = selected - interest }
                        )
                    } else {
                        NearNowChip(
                            label = interest,
                            onClick = {
                                if (selected.size < maxSelectable) {
                                    selected = selected + interest
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "%02d / %02d SELECTED".format(selected.size, maxSelectable),
                color = Mango,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserInterestsScreenPreview() {
    UserInterestsScreen()
}
