package com.example.android.absensiapp.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.android.absensiapp.ui.theme.*

@Composable
fun TextFieldCommon(
    modifier: Modifier = Modifier,
    text: String = "",
    hint: String,
    onValueChange: (String) -> Unit = {},
) {
    TextField(
        modifier = modifier
            .padding(DIMENS_16dp)
            .fillMaxWidth()
            .height(DIMENS_48dp)
            .clip(RoundedCornerShape(DIMENS_12dp)),
        value = text,
        onValueChange = {
            onValueChange.invoke(it)
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = BgColor2,
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        placeholder = {
            Text(
                text = hint,
                fontWeight = FontWeight.SemiBold,
                color = TextColorHint,
                fontSize = TEXT_SIZE_12sp
            )
        },
        singleLine = true,
        textStyle = TextStyle(
            fontWeight = FontWeight.SemiBold,
            color = TextColorPrimary,
            fontSize = TEXT_SIZE_12sp
        ),
    )
}

@Preview
@Composable
fun TextFieldCommonPreview() {
    TextFieldCommon(hint = "Masukan Password")
}