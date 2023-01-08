package com.example.android.absensiapp.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.android.absensiapp.ui.theme.DIMENS_12dp
import com.example.android.absensiapp.ui.theme.DIMENS_16dp
import com.example.android.absensiapp.ui.theme.DIMENS_48dp
import com.example.android.absensiapp.ui.theme.TEXT_SIZE_12sp

@Composable
fun ButtonCommon(
    backgroundColor: Color,
    title: String,
    onClick: () -> Unit
) {
    Button(
        onClick = {
            onClick.invoke()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(height = DIMENS_48dp)
            .padding(start = DIMENS_16dp, end = DIMENS_16dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
        shape = RoundedCornerShape(DIMENS_12dp),
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Medium,
            fontSize = TEXT_SIZE_12sp,
            color = Color.White,
        )
    }
}