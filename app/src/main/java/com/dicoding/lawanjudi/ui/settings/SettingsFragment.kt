package com.dicoding.lawanjudi.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.dicoding.lawanjudi.R
import com.dicoding.lawanjudi.databinding.FragmentSettingsBinding
import com.dicoding.lawanjudi.ui.SettingsModelFactory
import com.dicoding.lawanjudi.ui.login.LoginActivity

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = SettingPreference.getInstance(requireContext().dataStore)
        val mainViewModel = ViewModelProvider(this, SettingsModelFactory(pref))[SettingViewModel::class.java]

        mainViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            AppCompatDelegate.setDefaultNightMode(if (isDarkModeActive) {
                binding?.swDarkMode?.isChecked = true
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                binding?.swDarkMode?.isChecked = false
                AppCompatDelegate.MODE_NIGHT_NO
            })
        }

        binding?.swDarkMode?.setOnCheckedChangeListener { _, isChecked ->
            mainViewModel.saveThemeSetting(isChecked)
        }

        binding?.btnLogOut?.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }
}