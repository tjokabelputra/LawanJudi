package com.dicoding.lawanjudi.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.dicoding.lawanjudi.R
import com.dicoding.lawanjudi.database.SettingPreference
import com.dicoding.lawanjudi.database.UserPreference
import com.dicoding.lawanjudi.database.settingsDataStore
import com.dicoding.lawanjudi.database.userDataStore
import com.dicoding.lawanjudi.databinding.FragmentSettingsBinding
import com.dicoding.lawanjudi.ui.UserViewModel
import com.dicoding.lawanjudi.ui.factory.SettingsModelFactory
import com.dicoding.lawanjudi.ui.factory.UserModelFactory
import com.dicoding.lawanjudi.ui.landing.MainActivity

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

        val settingsPref = SettingPreference.getInstance(requireContext().settingsDataStore)
        val mainViewModel = ViewModelProvider(this, SettingsModelFactory(settingsPref))[SettingViewModel::class.java]

        val userPref = UserPreference.getInstance(requireContext().userDataStore)
        val userViewModel = ViewModelProvider(this, UserModelFactory(userPref))[UserViewModel::class.java]

        mainViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            AppCompatDelegate.setDefaultNightMode(if (isDarkModeActive) {
                binding?.swDarkMode?.isChecked = true
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                binding?.swDarkMode?.isChecked = false
                AppCompatDelegate.MODE_NIGHT_NO
            })
        }

        mainViewModel.getNotificationSettings().observe(viewLifecycleOwner) { isHideNotification: Boolean ->
            binding?.swNotification?.isChecked = isHideNotification
        }

        binding?.swDarkMode?.setOnCheckedChangeListener { _, isChecked ->
            mainViewModel.saveThemeSetting(isChecked)
        }

        binding?.swNotification?.setOnCheckedChangeListener { _, isChecked ->
            mainViewModel.saveNotificationSetting(isChecked)
        }

        binding?.btnLogOut?.setOnClickListener {
            AlertDialog.Builder(this@SettingsFragment.requireContext())
                .setTitle(R.string.logout)
                .setMessage(R.string.logout_confirmation)
                .setPositiveButton(R.string.yes) { _, _ ->
                    userViewModel.deleteUser()
                    val intent = Intent(this@SettingsFragment.requireContext(), MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
                .setNegativeButton(R.string.no) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}