package com.example.android.absensiapp.presentation.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.android.absensiapp.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@Composable
fun CardDateAndTime(
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("EEEE, dd MMM yyyy")
    val timeFormat = SimpleDateFormat("HH:mm")
    val currentDate = dateFormat.format(Date())
    val currentTime = timeFormat.format(Date())

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(DIMENS_12dp),
        backgroundColor = BgColor3,
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = DIMENS_8dp, horizontal = DIMENS_24dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = currentDate,
                fontWeight = FontWeight.SemiBold,
                color = TextColorSecond,
                fontSize = TEXT_SIZE_14sp,
            )

            Spacer(modifier = Modifier.height(DIMENS_8dp))

            Text(
                text = currentTime,
                fontWeight = FontWeight.Bold,
                color = TextColorBlue,
                fontSize = TEXT_SIZE_18sp,
            )
        }
    }
}

@Preview
@Composable
fun CardDateAndTimePreview() {
    CardDateAndTime()
}