package com.example.nearnow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nearnow.ui.theme.*

@Composable
fun NearNowTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    maxHeight: Int = 56
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(maxHeight.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(FieldFill)
            .border(1.dp, SoftGray, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp),
        contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                color = TextPrimary,
                fontSize = 15.sp,
                fontFamily = PoppinsFamily,
                fontWeight = FontWeight.Medium
            ),
            singleLine = singleLine,
            keyboardOptions = keyboardOptions,
            cursorBrush = SolidColor(Mango),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = if (singleLine) 0.dp else 12.dp),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = TextMuted,
                        fontFamily = PoppinsFamily,
                        fontSize = 15.sp
                    )
                }
                innerTextField()
            }
        )
    }
}

@Composable
fun NearNowOtpField(
    otp: String,
    onOtpChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    length: Int = 6
) {
    Box(modifier = modifier) {
        BasicTextField(
            value = otp,
            onValueChange = { newValue ->
                if (newValue.length <= length && newValue.all { it.isDigit() }) {
                    onOtpChange(newValue)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            textStyle = TextStyle(color = Color.Transparent, fontSize = 1.sp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            cursorBrush = SolidColor(Color.Transparent),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (i in 0 until length) {
                        val char = otp.getOrNull(i)?.toString() ?: ""
                        val isFilled = char.isNotEmpty()
                        val isNextToFill = i == otp.length

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(54.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(FieldFill)
                                .border(
                                    width = 2.dp,
                                    color = if (isNextToFill)
                                        Mango
                                    else if (isFilled)
                                        Mango.copy(alpha = 0.5f)
                                    else
                                        SoftGray,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isFilled) char else "_",
                                color = if (isFilled) TextPrimary else TextMuted,
                                fontFamily = PoppinsFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                innerTextField()
            }
        )
    }
}
