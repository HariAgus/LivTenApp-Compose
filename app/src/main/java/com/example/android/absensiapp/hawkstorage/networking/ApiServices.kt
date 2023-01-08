package com.example.android.absensiapp.hawkstorage.networking

object ApiServices {

    fun getLiveAttendanceServices(): AttendanceApiServices {
        return RetrofitClient.getClient()
            .create(AttendanceApiServices::class.java)
    }
}