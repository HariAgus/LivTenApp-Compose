package com.example.android.absensiapp.views.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.android.absensiapp.R
import com.example.android.absensiapp.hawkstorage.HawkStorage
import com.example.android.absensiapp.views.login.LoginActivity
import com.example.android.absensiapp.views.main.MainActivity
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

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
            startActivity<MainActivity>()
            finishAffinity()
        } else {
            startActivity<LoginActivity>()
            finishAffinity()
        }
    }

}