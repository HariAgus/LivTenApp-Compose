package com.example.android.absensiapp.presentation.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.android.absensiapp.R
import com.example.android.absensiapp.ui.theme.*

@Composable
fun CardHistoryCheckInOut(
    modifier: Modifier = Modifier,
    timeCheckInOrOut: String
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = DIMENS_16dp, end = DIMENS_16dp),
        shape = RoundedCornerShape(DIMENS_12dp),
        backgroundColor = BgColor4
    ) {
        Row {
            Card(
                modifier = Modifier
                    .padding(DIMENS_8dp)
                    .weight(1f),
                backgroundColor = PrimaryColor,
                shape = RoundedCornerShape(DIMENS_12dp)
            ) {
                Column(
                    modifier = Modifier.padding(DIMENS_16dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.sample_date),
                        color = TextColorPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = TEXT_SIZE_14sp
                    )

                    Text(
                        text = stringResource(R.string.sample_month),
                        color = TextColorPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = TEXT_SIZE_14sp
                    )
                }
            }

            // Check In
            ColumTextCheckInOut(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f),
                statusCheckInOrOut = stringResource(id = R.string.check_in),
                timeCheckInOrOut = timeCheckInOrOut
            )

            // Check Out
            ColumTextCheckInOut(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f),
                statusCheckInOrOut = stringResource(id = R.string.check_out),
                timeCheckInOrOut = timeCheckInOrOut
            )
        }
    }
}

@Composable
fun ColumTextCheckInOut(
    modifier: Modifier = Modifier,
    statusCheckInOrOut: String,
    timeCheckInOrOut: String
) {
    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = statusCheckInOrOut,
            color = TextColorPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = TEXT_SIZE_14sp
        )

        Spacer(modifier = Modifier.height(DIMENS_8dp))

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = timeCheckInOrOut,
            color = TextColorBlue,
            fontWeight = FontWeight.SemiBold,
            fontSize = TEXT_SIZE_12sp
        )
    }
}

@Preview
@Composable
fun CardHistoryCheckInOutPreview() {
    CardHistoryCheckInOut(
        timeCheckInOrOut = "10.00"
    )
}