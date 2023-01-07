package com.example.android.absensiapp.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.android.absensiapp.R
import com.example.android.absensiapp.presentation.component.TextFieldCommon
import com.example.android.absensiapp.ui.theme.*

@Composable
fun LoginScreen(
    email: String,
    password: String,
    onValueChangeEmail: (String) -> Unit,
    onValueChangePassword: (String) -> Unit,
    onClickForgotPassword: () -> Unit,
    onClickLogin: () -> Unit
) {
    Login(
        email = email,
        password = password,
        onValueChangeEmail = { onValueChangeEmail.invoke(it) },
        onValueChangePassword = { onValueChangePassword.invoke(it) },
        onClickForgotPassword = { onClickForgotPassword.invoke() },
        onClickLogin = { onClickLogin.invoke() }
    )
}

@Composable
fun Login(
    modifier: Modifier = Modifier,
    email: String,
    password: String,
    onValueChangeEmail: (String) -> Unit,
    onValueChangePassword: (String) -> Unit,
    onClickForgotPassword: () -> Unit,
    onClickLogin: () -> Unit
) {
    Column(
        modifier = modifier
            .background(BgColor1)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(DIMENS_140dp)
                .clickable {
                    onClickForgotPassword.invoke()
                },
            painter = painterResource(id = R.drawable.ic_attendance),
            contentDescription = stringResource(id = R.string.image_logo_app)
        )

        Spacer(modifier = Modifier.height(DIMENS_80dp))

        // Text Field Email
        TextFieldCommon(
            text = email,
            hint = stringResource(id = R.string.email),
            leadingIcon = R.drawable.ic_email,
            onValueChange = { onValueChangeEmail.invoke(it) }
        )

        Spacer(modifier = Modifier.height(DIMENS_16dp))

        // Text Field Password
        TextFieldCommon(
            text = password,
            hint = stringResource(id = R.string.password),
            leadingIcon = R.drawable.ic_password,
            inputTypePassword = true,
            onValueChange = { onValueChangePassword.invoke(it) }
        )

        Spacer(modifier = Modifier.height(DIMENS_32dp))

        Text(
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = DIMENS_16dp),
            text = stringResource(id = R.string.forgot_password),
            color = PrimaryColor
        )

        Spacer(modifier = Modifier.height(DIMENS_16dp))

        Button(
            modifier = Modifier
                .height(DIMENS_48dp)
                .fillMaxWidth()
                .padding(start = DIMENS_16dp, end = DIMENS_16dp),
            colors = ButtonDefaults.buttonColors(contentColor = PrimaryColor),
            shape = RoundedCornerShape(DIMENS_12dp),
            onClick = {
                onClickLogin.invoke()
            }
        ) {
            Text(text = stringResource(id = R.string.login), color = TextColorPrimary)
        }

    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreePreview() {
    LoginScreen(
        "hariagus23@gmail.com",
        "hari2023",
        onValueChangeEmail = {},
        onValueChangePassword = {},
        onClickForgotPassword = {},
        onClickLogin = {}
    )
}