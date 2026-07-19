package com.example.nearnow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.nearnow.ui.animations.bounceClick
import com.example.nearnow.ui.theme.*

@Composable
fun NearNowChip(
    label: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Text(
        text = label,
        color = TextSecondary,
        style = MaterialTheme.typography.labelMedium,
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(SoftGray)
            .border(1.dp, SoftGray, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp)
    )
}

@Composable
fun NearNowActiveChip(
    label: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Text(
        text = label,
        color = CardWhite,
        style = MaterialTheme.typography.labelMedium,
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Mango)
            .border(1.dp, Mango, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp)
    )
}

@Composable
fun NearNowCoralChip(
    label: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Text(
        text = label,
        color = Coral,
        style = MaterialTheme.typography.labelMedium,
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Coral.copy(alpha = 0.08f))
            .border(1.dp, Coral, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp)
    )
}

@Composable
fun NearNowTealChip(
    label: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Text(
        text = label,
        color = Teal,
        style = MaterialTheme.typography.labelMedium,
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Teal.copy(alpha = 0.08f))
            .border(1.dp, Teal, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp)
    )
}
