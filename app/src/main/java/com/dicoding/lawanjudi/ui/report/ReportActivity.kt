package com.dicoding.lawanjudi.ui.report

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.lawanjudi.R
import com.dicoding.lawanjudi.databinding.ActivityReportBinding
import com.dicoding.lawanjudi.model.Report
import com.dicoding.lawanjudi.ui.home.HomeActivity

class ReportActivity : AppCompatActivity() {
    private var _binding: ActivityReportBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()

        val report = intent.getParcelableExtra<Report>(REP)

        binding?.tvReportName?.text = getString(R.string.reporter_name, report?.name)
        binding?.tvReportEmail?.text = getString(R.string.reporter_email, report?.email)
        binding?.tvId?.text = getString(R.string.report_id, report?.id)
        binding?.tvContent?.text = getString(R.string.report_content, report?.content)
        binding?.tvDesc?.text = getString(R.string.report_cont_desc, report?.description ?: "Tidak Ada Deskripsi")
        binding?.tvAiIndicated?.text = getString(R.string.ai_indicated, if(report?.aiConfirmed == true) "Ya" else "Tidak")

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