package com.dicoding.lawanjudi.ui.landing

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dicoding.lawanjudi.databinding.ActivityMainBinding
import com.dicoding.lawanjudi.ui.factory.SettingsModelFactory
import com.dicoding.lawanjudi.ui.login.LoginActivity
import com.dicoding.lawanjudi.database.SettingPreference
import com.dicoding.lawanjudi.database.UserPreference
import com.dicoding.lawanjudi.database.settingsDataStore
import com.dicoding.lawanjudi.database.userDataStore
import com.dicoding.lawanjudi.ui.settings.SettingViewModel
import com.dicoding.lawanjudi.ui.home.HomeActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private fun applyTheme(){
        val pref = SettingPreference.getInstance(settingsDataStore)
        val settingViewModel = ViewModelProvider(this, SettingsModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this){ isDarkModeActive: Boolean ->
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        applyTheme()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val pref = UserPreference.getInstance(userDataStore)

        lifecycleScope.launch {
            val user = pref.getUser().first()
            if (user.name.isNotEmpty() && user.email.isNotEmpty()) {
                startActivity(Intent(this@MainActivity, HomeActivity::class.java))
            }
        }

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}