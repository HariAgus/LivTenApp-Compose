package com.example.android.absensiapp.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.android.absensiapp.R
import com.example.android.absensiapp.presentation.component.CardHistoryCheckInOut
import com.example.android.absensiapp.ui.theme.*
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.color.KalendarThemeColor
import com.himanshoe.kalendar.component.day.config.KalendarDayColors
import com.himanshoe.kalendar.model.KalendarType

@Composable
fun HistoryScreen() {
    History()
}

@Composable
fun History(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = BgColor1)
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Start)
                .fillMaxWidth()
                .padding(top = DIMENS_32dp, start = DIMENS_16dp),
            text = stringResource(id = R.string.profile),
            fontSize = TEXT_SIZE_16sp,
            color = TextColorPrimary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(DIMENS_32dp))

        CalendarHistory()

        Spacer(modifier = Modifier.height(DIMENS_32dp))

        CardHistoryCheckInOut(timeCheckInOrOut = stringResource(R.string.sample_time))
    }
}

@Composable
fun CalendarHistory(modifier: Modifier = Modifier) {
    Kalendar(
        modifier = modifier.fillMaxWidth(),
        kalendarType = KalendarType.Firey,
        kalendarThemeColor = KalendarThemeColor(
            backgroundColor = BgColor1,
            dayBackgroundColor = PrimaryColor,
            headerTextColor = Color.White
        ),
        kalendarDayColors = KalendarDayColors(
            textColor = TextColorPrimary,
            selectedTextColor = TextColorPrimary
        )
    )
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    HistoryScreen()
}