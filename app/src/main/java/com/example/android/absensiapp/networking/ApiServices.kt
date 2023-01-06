package com.example.android.absensiapp.networking

object ApiServices {

    fun getLiveAttendanceServices(): AttendanceApiServices {
        return RetrofitClient
            .getClient()
            .create(AttendanceApiServices::class.java)
    }
}