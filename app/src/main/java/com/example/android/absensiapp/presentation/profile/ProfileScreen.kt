package com.example.android.absensiapp.presentation.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.android.absensiapp.R
import com.example.android.absensiapp.presentation.component.ButtonCommon
import com.example.android.absensiapp.ui.theme.*

@Composable
fun ProfileScreen(onClickChangePassword: () -> Unit) {
    Profile(onClickChangePassword = { onClickChangePassword.invoke() })
}

@Composable
fun Profile(
    modifier: Modifier = Modifier,
    onClickChangePassword: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgColor1)
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

        Image(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .height(DIMENS_160dp)
                .padding(top = DIMENS_32dp),
            painter = painterResource(id = R.drawable.ic_attendance),
            contentDescription = stringResource(id = R.string.image_logo_app)
        )

        Spacer(modifier = Modifier.height(DIMENS_32dp))

        ProfileContent(
            onClickChangePassword = { onClickChangePassword.invoke() }
        )
    }
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    onClickChangePassword: () -> Unit
) {
    Card(
        modifier = modifier,
        backgroundColor = BgColor4,
        shape = RoundedCornerShape(topStart = DIMENS_32dp, topEnd = DIMENS_32dp),
        border = BorderStroke(width = DIMENS_2dp, color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(start = DIMENS_16dp, end = DIMENS_16dp)
        ) {
            Card(
                modifier = Modifier
                    .padding(DIMENS_24dp)
                    .align(Alignment.CenterHorizontally),
                border = BorderStroke(width = DIMENS_2dp, color = SecondaryColor),
                shape = RoundedCornerShape(DIMENS_80dp),
            ) {
                Image(
                    modifier = Modifier.size(DIMENS_120dp),
                    painter = painterResource(id = R.drawable.img_photo_profile),
                    contentDescription = stringResource(R.string.image_photo_profile),
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.sample_name),
                color = TextColorPrimary,
                fontSize = TEXT_SIZE_16sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.sample_email),
                color = TextColorSecond,
                fontSize = TEXT_SIZE_14sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(DIMENS_16dp))

            ButtonCommon(
                backgroundColor = PrimaryColor,
                title = stringResource(id = R.string.change_password)
            ) {
                onClickChangePassword.invoke()
            }

            Spacer(modifier = Modifier.height(DIMENS_12dp))

            ButtonCommon(
                backgroundColor = PrimaryColor,
                title = stringResource(id = R.string.change_language)
            ) {
                onClickChangePassword.invoke()
            }

            Spacer(modifier = Modifier.height(DIMENS_12dp))

            ButtonCommon(
                backgroundColor = AlertColor,
                title = stringResource(id = R.string.logout)
            ) {
                onClickChangePassword.invoke()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    ProfileScreen(onClickChangePassword = {})
}