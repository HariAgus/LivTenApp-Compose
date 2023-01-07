package com.example.android.absensiapp.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.android.absensiapp.hawkstorage.HawkStorage
import com.example.android.absensiapp.presentation.login.LoginActivity
import com.example.android.absensiapp.presentation.main.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreen()
        }

        afterDelayGoToLogin()
    }

    private fun afterDelayGoToLogin() {
        Handler(Looper.getMainLooper()).postDelayed({
            checkIfLogin()
        }, 1200)
    }

    private fun checkIfLogin() {
        val isLogin = HawkStorage.instance(this).isLogin()
        if (isLogin) {
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }
    }

}