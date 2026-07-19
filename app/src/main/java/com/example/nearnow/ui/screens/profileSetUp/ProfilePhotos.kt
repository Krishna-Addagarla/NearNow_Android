package com.example.nearnow.ui.screens.profileSetUp

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nearnow.ui.animations.bounceClick
import com.example.nearnow.ui.theme.*

/* ---------------------------------------------------------------
   STEP 2 / 4 — "Show, don't tell"
   6-slot numbered photo grid. Slot 1 is always the map avatar/cover.
   --------------------------------------------------------------- */
@Composable
fun ProfilePhotosScreen(
    onContinueClick: (filledSlots: List<Boolean>) -> Unit = {},
    onLaterClick: () -> Unit = {}
) {
    var slots by remember { mutableStateOf(listOf(true, false, false, false, false, false)) }

    ProfileSetupStepShell(
        stepNumber = 2,
        totalSteps = 4,
        stepLabel = "STEP 02 / 04",
        title = "Show, don't\ntell",
        primaryButtonText = "CONTINUE",
        primaryEnabled = slots.any { it },
        onPrimaryClick = { onContinueClick(slots) },
        secondaryButtonText = "LATER",
        onSecondaryClick = onLaterClick
    ) {
        Column {
            PhotoSlotGrid(
                slots = slots,
                onSlotClick = { index ->
                    slots = slots.toMutableList().also { it[index] = !it[index] }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "← MORE NAPKINS WITH A PHOTO",
                color = TextSecondary,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun PhotoSlotGrid(
    slots: List<Boolean>,
    onSlotClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(slots.size) { index ->
            PhotoSlot(
                index = index,
                isFilled = slots[index],
                isCoverSlot = index == 0,
                onClick = { onSlotClick(index) }
            )
        }
    }
}

@Composable
private fun PhotoSlot(
    index: Int,
    isFilled: Boolean,
    isCoverSlot: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isFilled) MangoLight.copy(alpha = 0.3f)
                else FieldFill
            )
            .border(
                width = 1.5.dp,
                color = if (isFilled) Mango else SoftGray,
                shape = RoundedCornerShape(16.dp)
            )
            .bounceClick { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (isFilled) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "%02d".format(index + 1),
                    color = TextPrimary,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                if (isCoverSlot) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Cover photo",
                        tint = MangoDeep,
                        modifier = Modifier.height(14.dp)
                    )
                }
            }
        } else {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add photo",
                tint = TextMuted
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePhotosScreenPreview() {
    ProfilePhotosScreen(
        onContinueClick = {},
        onLaterClick = {}
    )
}
