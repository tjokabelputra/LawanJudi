package com.dicoding.lawanjudi.ui.landing

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.dicoding.lawanjudi.databinding.ActivityMainBinding
import com.dicoding.lawanjudi.ui.SettingsModelFactory
import com.dicoding.lawanjudi.ui.login.LoginActivity
import com.dicoding.lawanjudi.ui.settings.SettingPreference
import com.dicoding.lawanjudi.ui.settings.SettingViewModel
import com.dicoding.lawanjudi.ui.settings.dataStore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        applyTheme()

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun applyTheme(){
        val pref = SettingPreference.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, SettingsModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this){ isDarkModeActive: Boolean ->
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }
}