package com.dicoding.lawanjudi.ui.report

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.lawanjudi.R
import com.dicoding.lawanjudi.databinding.ActivityReportBinding
import com.dicoding.lawanjudi.model.Report
import com.dicoding.lawanjudi.ui.home.HomeActivity
import java.util.UUID

class ReportActivity : AppCompatActivity() {
    private var _binding: ActivityReportBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()

        val report = intent.getParcelableExtra<Report>(REP)

        binding?.tvReportName?.text = "Nama Pelapor: ${report?.name}"
        binding?.tvReportEmail?.text = "Email Pelapor: ${report?.email}"
        binding?.tvId?.text = "ID Laporan: ${report?.id}"
        binding?.tvContent?.text = "Konten: ${report?.content}"
        binding?.tvDesc?.text = "Deskripsi: ${report?.description ?: "Tidak ada deskripsi"}"
        binding?.tvAiIndicated?.text = "Terindikasi AI: ${if (report?.aiConfirmed == true) "Ya" else "Tidak"}"

        binding?.btnHome?.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    companion object{
        private const val REP = "report"
    }
}