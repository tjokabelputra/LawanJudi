package com.dicoding.lawanjudi.ui.result

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dicoding.lawanjudi.R
import com.dicoding.lawanjudi.database.remote.response.AdsPredictReponse
import com.dicoding.lawanjudi.database.remote.response.WebPredictResponse
import com.dicoding.lawanjudi.databinding.ActivityResultBinding
import com.dicoding.lawanjudi.ui.factory.SettingsModelFactory
import com.dicoding.lawanjudi.ui.home.HomeActivity
import com.dicoding.lawanjudi.database.SettingPreference
import com.dicoding.lawanjudi.database.settingsDataStore
import com.dicoding.lawanjudi.model.Report
import com.dicoding.lawanjudi.ui.FirebaseViewModel
import com.dicoding.lawanjudi.database.Result
import com.dicoding.lawanjudi.database.User
import com.dicoding.lawanjudi.database.UserPreference
import com.dicoding.lawanjudi.database.userDataStore
import com.dicoding.lawanjudi.ui.report.ReportActivity
import com.dicoding.lawanjudi.ui.settings.SettingViewModel
import com.dicoding.lawanjudi.util.StringFormatter.percentFormatter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID

class ResultActivity : AppCompatActivity() {
    private var _binding: ActivityResultBinding? = null
    private val binding get() = _binding

    private val firebaseViewModel : FirebaseViewModel by viewModels()

    private lateinit var report: Report
    lateinit var user: User

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()

        binding?.pgResult?.visibility = View.GONE

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        val userPref = UserPreference.getInstance(userDataStore)

        lifecycleScope.launch {
            user = userPref.getUser().first()
        }

        val webRes: WebPredictResponse? = intent.getParcelableExtra(WEB_RES)
        val adRes: AdsPredictReponse? = intent.getParcelableExtra(ADS_RES)

        val type = if(webRes != null)  getString(R.string.tab_web) else getString(R.string.tab_ads)
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
            binding?.btnNext?.text = getString(R.string.send)
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
            binding?.btnNext?.text = getString(R.string.rep_again)
            sendNotification(false)
        }

        binding?.tvType?.text = getString(R.string.res_type, type)
        binding?.tvContent?.text = webRes?.url ?: adRes?.text

        if(webRes != null){
            binding?.tvDescContent?.text = intent.getStringExtra(DESC)
        }
        else{
            binding?.tvDesc?.visibility = View.GONE
            binding?.tvDescContent?.visibility = View.GONE
        }

        binding?.cbReport?.setOnCheckedChangeListener { _, isChecked ->
            binding?.btnNext?.text = if (isChecked) getString(R.string.send) else getString(R.string.rep_again)
        }

        firebaseViewModel.reportResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding?.pgResult?.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding?.pgResult?.visibility = View.GONE
                    sendReportNotification()
                    val intent = Intent(this, ReportActivity::class.java)
                    intent.putExtra(REP, report)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
                is Result.Error -> {
                    binding?.pgResult?.visibility = View.GONE
                }
            }
        }

        binding?.btnNext?.setOnClickListener {
            if (binding?.btnNext?.text == getString(R.string.send)) {
                report = Report(
                    id = UUID.randomUUID().toString(),
                    name = user.name,
                    email = user.email,
                    content = binding?.tvContent?.text.toString(),
                    description = if(webRes != null) intent.getStringExtra(DESC) else null,
                    aiConfirmed = webRes?.isJudiOnline == true || adRes?.isJudiOnline == true
                )
                firebaseViewModel.saveReport(report)
            } else {
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
    }

    private fun sendNotification(isSuccess: Boolean){
        val pref = SettingPreference.getInstance(settingsDataStore)
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

    private fun sendReportNotification(){
        val pref = SettingPreference.getInstance(settingsDataStore)
        val settingViewModel = ViewModelProvider(this, SettingsModelFactory(pref))[SettingViewModel::class.java]
        settingViewModel.getNotificationSettings().observe(this) { isHideNotification: Boolean ->
            if(!isHideNotification){
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_copy)
                    .setContentTitle(getString(R.string.report_success))
                    .setContentText(getString(R.string.report_success_notification))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setSubText(getString(R.string.subtext_report))

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
        private const val REP = "report"
    }
}