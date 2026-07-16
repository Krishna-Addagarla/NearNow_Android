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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/* ---------------------------------------------------------------
   STEP 2 / 4 — "Show, don't tell"
   6-slot numbered photo grid. Slot 1 is always the map avatar/cover.
   --------------------------------------------------------------- */
@Composable
fun ProfilePhotosScreen(
    onContinueClick: (filledSlots: List<Boolean>) -> Unit = {},
    onLaterClick: () -> Unit = {}
) {
    // Local fake state: which of the 6 slots have a photo.
    // Slot 0 (the cover slot) starts filled, like the screenshot.
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
                    // Toggle fill state for demo purposes — wire to real
                    // image picker in production.
                    slots = slots.toMutableList().also { it[index] = !it[index] }
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "← MORE NAPKINS WITH A PHOTO",
                color = NearNowColors.Slate,
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                letterSpacing = 0.5.sp
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
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (isFilled) NearNowColors.Signal.copy(alpha = 0.85f)
                else NearNowColors.FieldFill
            )
            .border(
                width = 1.dp,
                color = if (isFilled) Color.Transparent else NearNowColors.Slate.copy(alpha = 0.25f),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (isFilled) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "%02d".format(index + 1),
                    color = NearNowColors.Ink,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                if (isCoverSlot) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Cover photo",
                        tint = NearNowColors.Ink,
                        modifier = Modifier.height(12.dp)
                    )
                }
            }
        } else {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add photo",
                tint = NearNowColors.Slate.copy(alpha = 0.6f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePhotosScreenPreview(){
    ProfilePhotosScreen(
        onContinueClick = {},
        onLaterClick = {}
    )
}
