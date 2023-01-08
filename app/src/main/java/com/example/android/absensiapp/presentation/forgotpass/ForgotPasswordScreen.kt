package com.example.android.absensiapp.presentation.forgotpass

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.android.absensiapp.R
import com.example.android.absensiapp.presentation.component.TextFieldCommon
import com.example.android.absensiapp.ui.theme.*

@Composable
fun ForgotPasswordScreen(
    text: String = "",
    onValueChange: (String) -> Unit = {},
    onClickArrowToolbar: () -> Unit = {},
    onClickForgotPassword: () -> Unit = {}
) {
    ForgotPassword(
        text = text,
        onValueChange = { onValueChange.invoke(it) },
        onClickArrowToolbar = { onClickArrowToolbar.invoke() },
        onClickForgotPassword = { onClickForgotPassword.invoke() }
    )
}

@Composable
fun ForgotPassword(
    modifier: Modifier = Modifier,
    text: String = "",
    onValueChange: (String) -> Unit = {},
    onClickArrowToolbar: () -> Unit = {},
    onClickForgotPassword: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .background(BgColor1)
            .fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.forgot_password),
                    color = TextColorPrimary
                )
            },
            backgroundColor = BgColor1,
            navigationIcon = {
                Image(
                    modifier = Modifier.clickable {
                        onClickArrowToolbar.invoke()
                    },
                    imageVector = Icons.Default.ArrowBack,
                    colorFilter = ColorFilter.tint(color = Color.White),
                    contentDescription = stringResource(R.string.image_arrow_back)
                )
            }
        )

        Spacer(modifier = Modifier.height(DIMENS_16dp))

        TextFieldCommon(
            text = text,
            hint = stringResource(id = R.string.please_field_your_email),
            leadingIcon = R.drawable.ic_email,
            onValueChange = { onValueChange.invoke(it) }
        )

        Button(
            onClick = {
                onClickForgotPassword.invoke()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(height = DIMENS_48dp)
                .padding(start = DIMENS_16dp, end = DIMENS_16dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = PrimaryColor),
            shape = RoundedCornerShape(DIMENS_12dp),
        ) {
            Text(
                text = stringResource(R.string.forgot_password),
                fontWeight = FontWeight.Medium,
                fontSize = TEXT_SIZE_12sp,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordPreview() {
    ForgotPassword()
}