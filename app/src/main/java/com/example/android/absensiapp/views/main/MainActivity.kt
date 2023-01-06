package com.example.android.absensiapp.views.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.android.absensiapp.R
import com.example.android.absensiapp.databinding.ActivityMainBinding
import com.example.android.absensiapp.views.attendance.AttendanceFragment
import com.example.android.absensiapp.views.history.HistoryFragment
import com.example.android.absensiapp.views.profile.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.btmNavigationMain.setOnItemSelectedListener { id ->
            when (id) {
                R.id.action_attendance -> openFragment(AttendanceFragment())
                R.id.action_history -> openFragment(HistoryFragment())
                R.id.action_profile -> openFragment(ProfileFragment())
            }
        }
        openHomeFragment()
    }

    private fun openHomeFragment() {
        binding.btmNavigationMain.setItemSelected(R.id.action_attendance)
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_main, fragment)
            .addToBackStack(null)
            .commit()
    }
}