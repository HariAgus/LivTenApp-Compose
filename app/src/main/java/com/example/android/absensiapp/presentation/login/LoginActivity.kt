package com.example.android.absensiapp.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateOf
import com.example.android.absensiapp.R
import com.example.android.absensiapp.dialog.MyDialog
import com.example.android.absensiapp.hawkstorage.HawkStorage
import com.example.android.absensiapp.model.LoginResponse
import com.example.android.absensiapp.model.User
import com.example.android.absensiapp.networking.ApiServices
import com.example.android.absensiapp.networking.RetrofitClient
import com.example.android.absensiapp.presentation.forgotpass.ForgotPasswordActivity
import com.example.android.absensiapp.presentation.main.MainActivity
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private var mEdtEmail = mutableStateOf("")
    private var mEdtPassword = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen(
                email = mEdtEmail.value,
                password = mEdtPassword.value,
                onValueChangeEmail = { mEdtEmail.value = it },
                onValueChangePassword = { mEdtPassword.value = it },
                onClickForgotPassword = { onClickForgotPassword() },
                onClickLogin = {
                    // onClickLogin()
                    val user = User(id = 1, name = "Hari Agus", email = "hariaguswidakdo@gmail.com")
                    val token = "deDzndfdkmd|zDfdjnEr"
                    HawkStorage.instance(this@LoginActivity).setUser(user)
                    HawkStorage.instance(this@LoginActivity).setToken(token)
                    gotoMain()
                }
            )
        }

    }

    private fun onClickForgotPassword() {
        startActivity(Intent(this, ForgotPasswordActivity::class.java))
    }

    private fun onClickLogin() {
        if (isFormValid(mEdtEmail.value, mEdtPassword.value)) {
            loginToServer(email = mEdtEmail.value, password = mEdtPassword.value)
        }
    }

    private fun loginToServer(email: String, password: String) {
        val loginRequest = LoginRequest(email = email, password = password, deviceName = "mobile")
        val loginRequestString = Gson().toJson(loginRequest)
        MyDialog.showProgressDialog(this)

        ApiServices.getLiveAttendanceServices()
            .loginRequest(loginRequestString)
            .enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    MyDialog.hideDialog()
                    Log.e(TAG, "Error: ${t.message}")
                }

                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    MyDialog.hideDialog()
                    if (response.isSuccessful) {
                        val user = response.body()?.user
                        val token = response.body()?.meta?.token
                        if (user != null && token != null) {
                            HawkStorage.instance(this@LoginActivity).setUser(user)
                            HawkStorage.instance(this@LoginActivity).setToken(token)
                            gotoMain()
                        }
                    } else {
                        val errorConverter: Converter<ResponseBody, LoginResponse> =
                            RetrofitClient
                                .getClient()
                                .responseBodyConverter(
                                    LoginResponse::class.java,
                                    arrayOfNulls<Annotation>(0)
                                )
                        var errorResponse: LoginResponse?
                        try {
                            response.errorBody()?.let {
                                errorResponse = errorConverter.convert(it)
                                MyDialog.dynamicDialog(
                                    this@LoginActivity,
                                    getString(R.string.failed),
                                    errorResponse?.message.toString()
                                )
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                            Log.e(TAG, "Error: ${e.message}")
                        }
                    }
                }
            })
    }

    private fun gotoMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun isFormValid(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            Toast.makeText(this, R.string.please_field_your_email, Toast.LENGTH_SHORT).show()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, R.string.please_use_valid_email, Toast.LENGTH_SHORT).show()
        } else if (password.isEmpty()) {
            Toast.makeText(this, R.string.please_field_your_password, Toast.LENGTH_SHORT).show()
        } else {
            return true
        }
        return false
    }

    companion object {
        private val TAG = LoginActivity::class.java.simpleName
    }

}