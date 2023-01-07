package com.example.android.absensiapp.presentation.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.android.absensiapp.ui.theme.DIMENS_16dp

@Composable
fun CardHistoryCheckInOut(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = DIMENS_16dp, end = DIMENS_16dp)
    ) {
    }
}

@Preview
@Composable
fun CardHistoryCheckInOutPreview() {
    CardHistoryCheckInOut()
}