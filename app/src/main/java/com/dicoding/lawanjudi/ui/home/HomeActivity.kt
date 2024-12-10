package com.dicoding.lawanjudi.ui.home

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dicoding.lawanjudi.R
import com.dicoding.lawanjudi.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.navView.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            binding.navView.getWindowVisibleDisplayFrame(r)
            val screenHeight = binding.navView.rootView.height
            val keypadHeight = screenHeight - r.bottom

            if(keypadHeight > screenHeight * 0.15){
                binding.navView.animate()
                    .translationY(binding.navView.height.toFloat())
                    .setDuration(100)
                    .start()
            } else {
                binding.navView.animate()
                    .translationY(0f)
                    .setDuration(200)
                    .start()
            }
        }

        val navView: BottomNavigationView = binding.navView

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_home) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_education,
                R.id.navigation_create,
                R.id.navigation_history,
                R.id.navigation_setting
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}