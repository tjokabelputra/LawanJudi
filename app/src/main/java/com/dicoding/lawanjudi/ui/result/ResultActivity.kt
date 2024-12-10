package com.dicoding.lawanjudi.ui.result

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.lawanjudi.R
import com.dicoding.lawanjudi.database.remote.response.AdsPredictReponse
import com.dicoding.lawanjudi.database.remote.response.WebPredictResponse
import com.dicoding.lawanjudi.databinding.ActivityResultBinding
import com.dicoding.lawanjudi.ui.SettingsModelFactory
import com.dicoding.lawanjudi.ui.home.HomeActivity
import com.dicoding.lawanjudi.ui.settings.SettingPreference
import com.dicoding.lawanjudi.ui.settings.SettingViewModel
import com.dicoding.lawanjudi.ui.settings.dataStore
import com.dicoding.lawanjudi.util.StringFormatter.percentFormatter

class ResultActivity : AppCompatActivity() {
    private var _binding: ActivityResultBinding? = null
    private val binding get() = _binding

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        val webRes: WebPredictResponse? = intent.getParcelableExtra(WEB_RES)
        val adRes: AdsPredictReponse? = intent.getParcelableExtra(ADS_RES)

        val type = if(webRes != null)  "Situs" else "Iklan"
        val percent = percentFormatter(webRes?.probability ?: adRes?.probability ?: 0.0)

        if(webRes?.isJudiOnline == true || adRes?.isJudiOnline == true){
            binding?.tvResTitle?.text = getString(R.string.check_success)
            binding?.tvResDesc?.text = getString(
                R.string.check_success_desc,
                type,
                type,
                percent
            )
            binding?.cbReport?.visibility = View.GONE
            binding?.btnNext?.text = "Kirim"
            sendNotification(true)
        }
        else{
            binding?.tvResTitle?.text = getString(R.string.check_failed)
            binding?.tvResDesc?.text = getString(
                R.string.check_failed_desc,
                type,
                type,
                percent
            )
            binding?.btnNext?.text = "Kembali Lapor"
            sendNotification(false)
        }

        binding?.tvType?.text = "$type: "
        binding?.tvContent?.text = webRes?.url ?: adRes?.text

        if(webRes != null){
            binding?.tvDescContent?.text = intent.getStringExtra(DESC)
        }
        else{
            binding?.tvDesc?.visibility = View.GONE
            binding?.tvDescContent?.visibility = View.GONE
        }

        binding?.cbReport?.setOnCheckedChangeListener { _, isChecked ->
            binding?.btnNext?.text = if (isChecked) "Kirim" else "Kembali Lapor"
        }

        binding?.btnNext?.setOnClickListener {
            if (binding?.btnNext?.text == "Kirim") {
                Log.d("ResultActivity", "Send action triggered")
            } else {
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
        }
    }

    private fun sendNotification(isSuccess: Boolean){
        val pref = SettingPreference.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, SettingsModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getNotificationSettings().observe(this) { isHideNotification: Boolean ->
            if(!isHideNotification){
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notif)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setSubText(getString(R.string.subtext))

                if (isSuccess) {
                    builder.setContentTitle(getString(R.string.check_success))
                        .setContentText(getString(R.string.check_success_notification))
                        .setSmallIcon(R.drawable.ic_check)
                } else {
                    builder.setContentTitle(getString(R.string.check_failed))
                        .setContentText(getString(R.string.check_failed_notification))
                        .setSmallIcon(R.drawable.ic_cross)
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(
                        CHANNEL_ID,
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    builder.setChannelId(CHANNEL_ID)
                    notificationManager.createNotificationChannel(channel)
                }

                val notification = builder.build()
                notificationManager.notify(NOTIFICATION_ID, notification)
            }
        }
    }

    companion object{
        private const val WEB_RES = "webResponse"
        private const val ADS_RES = "adsResponse"
        private const val DESC = "description"
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_01"
        private const val CHANNEL_NAME = "lawan judi channel"
    }
}