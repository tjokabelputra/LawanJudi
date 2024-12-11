package com.dicoding.lawanjudi.ui.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.lawanjudi.R
import com.dicoding.lawanjudi.databinding.ActivityReportDetailBinding
import com.dicoding.lawanjudi.ui.factory.ReportsModelFactory
import com.dicoding.lawanjudi.ui.history.HistoryViewModel

class ReportDetailActivity : AppCompatActivity() {

    private var _binding : ActivityReportDetailBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityReportDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()

        val id = intent.getStringExtra(ID)

        val factory: ReportsModelFactory = ReportsModelFactory.getInstance(this)
        val viewModels: HistoryViewModel by viewModels {
            factory
        }

        viewModels.getReportId(id.toString()).observe(this) { report ->
            binding?.tvReportId?.text = getString(R.string.report_id, report.id)
            binding?.tvContent?.text = getString(R.string.report_content, report.content)
            binding?.tvReportDesc?.text = getString(R.string.report_cont_desc, report.description)
            binding?.tvAiConfirmed?.text = getString(R.string.ai_indicated, if(report.aiConfirmed == true) "Ya" else "Tidak")
        }
    }

    companion object{
        private const val ID = "reportId"
    }
}