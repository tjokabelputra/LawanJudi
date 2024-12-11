package com.dicoding.lawanjudi.ui.home

import android.graphics.Rect
import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dicoding.lawanjudi.R
import com.dicoding.lawanjudi.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!
    private var globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupBottomNavigation()
        setupKeyboardVisibilityListener()
    }

    private fun setupBottomNavigation() {
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
        binding.navView.setupWithNavController(navController)
    }

    private fun setupKeyboardVisibilityListener() {
        val navView = binding.navView
        globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            val r = Rect()
            navView.getWindowVisibleDisplayFrame(r)
            val screenHeight = navView.rootView.height
            val keypadHeight = screenHeight - r.bottom

            // If the keypad is more than 15% of screen height, it's visible
            if (keypadHeight > screenHeight * 0.15) {
                navView.animate()
                    .translationY(navView.height.toFloat())
                    .setDuration(100)
                    .start()
            } else {
                navView.animate()
                    .translationY(0f)
                    .setDuration(200)
                    .start()
            }
        }
        navView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        globalLayoutListener?.let {
            binding.navView.viewTreeObserver.removeOnGlobalLayoutListener(it)
        }
        _binding = null
    }
}