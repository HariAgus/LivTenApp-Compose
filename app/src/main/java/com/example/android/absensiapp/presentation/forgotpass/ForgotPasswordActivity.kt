package com.example.android.absensiapp.presentation.forgotpass

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateOf
import com.example.android.absensiapp.R
import com.example.android.absensiapp.dialog.MyDialog
import com.example.android.absensiapp.model.ForgotPasswordResponse
import com.example.android.absensiapp.hawkstorage.networking.ApiServices
import com.example.android.absensiapp.hawkstorage.networking.RetrofitClient
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException

class ForgotPasswordActivity : AppCompatActivity() {

    private var mFieldTextPassword = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ForgotPasswordScreen(
                text = mFieldTextPassword.value,
                onValueChange = {
                    mFieldTextPassword.value = it
                },
                onClickArrowToolbar = {
                    finish()
                },
                onClickForgotPassword = {
                    onClickForgotPassword()
                }
            )
        }
    }

    private fun onClickForgotPassword() {
        if (isFormValid(mFieldTextPassword.value)) {
            forgotPassToServer(mFieldTextPassword.value)
        }
    }

    private fun forgotPassToServer(email: String) {
        val forgotPasswordRequest = ForgotPasswordRequest(email = email)
        val forgotPasswordString = Gson().toJson(forgotPasswordRequest)

        MyDialog.showProgressDialog(this)

        ApiServices.getLiveAttendanceServices()
            .forgotPasswordRequest(forgotPasswordString)
            .enqueue(object : Callback<ForgotPasswordResponse> {
                override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                    MyDialog.hideDialog()
                    Log.e(TAG, "Error: ${t.message}")
                }

                override fun onResponse(
                    call: Call<ForgotPasswordResponse>,
                    response: Response<ForgotPasswordResponse>
                ) {
                    MyDialog.hideDialog()
                    if (response.isSuccessful) {
                        val message = response.body()?.message
                        MyDialog.dynamicDialog(
                            this@ForgotPasswordActivity,
                            getString(R.string.success),
                            message.toString()
                        )
                        Handler(Looper.getMainLooper()).postDelayed({
                            MyDialog.hideDialog()
                            finish()
                        }, 2000)
                    } else {
                        val errorConverter: Converter<ResponseBody, ForgotPasswordResponse> =
                            RetrofitClient
                                .getClient()
                                .responseBodyConverter(
                                    ForgotPasswordResponse::class.java,
                                    arrayOfNulls<Annotation>(0)
                                )
                        var errorResponse: ForgotPasswordResponse?
                        try {
                            response.errorBody()?.let {
                                errorResponse = errorConverter.convert(it)
                                MyDialog.dynamicDialog(
                                    this@ForgotPasswordActivity,
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

    private fun isFormValid(email: String): Boolean {
        if (email.isEmpty()) {
            Toast.makeText(this, R.string.please_field_your_email, Toast.LENGTH_SHORT).show()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, R.string.please_use_valid_email, Toast.LENGTH_SHORT).show()
        } else {
            return true
        }
        return false
    }


    companion object {
        private val TAG = ForgotPasswordActivity::class.java.simpleName
    }

}