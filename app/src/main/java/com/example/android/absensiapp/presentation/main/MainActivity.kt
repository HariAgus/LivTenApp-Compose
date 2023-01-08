package com.example.android.absensiapp.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.android.absensiapp.R
import com.example.android.absensiapp.databinding.ActivityMainBinding


@ExperimentalMaterialApi
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUpNavigation()
    }

    private fun setupUpNavigation() {
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment?
        NavigationUI.setupWithNavController(binding.btmNavigationMain, navHost?.navController!!)
    }

}