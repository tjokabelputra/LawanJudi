package com.dicoding.lawanjudi.ui.result

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.lawanjudi.database.remote.response.AdsPredictReponse
import com.dicoding.lawanjudi.database.remote.response.WebPredictResponse
import com.dicoding.lawanjudi.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private var _binding: ActivityResultBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()

        val webRes: WebPredictResponse? = intent.getParcelableExtra(WEB_RES)
        val adRes: AdsPredictReponse? = intent.getParcelableExtra(ADS_RES)

        if(webRes != null) {
            val desc = intent.getStringExtra(DESC)
            binding?.tvText?.text = webRes.url
            binding?.tvRes?.text = webRes.isJudiOnline.toString()
            binding?.tvProb?.text = webRes.probability.toString()
            binding?.tvDesc?.text = desc
        }
        else{
            binding?.tvText?.text = adRes?.text
            binding?.tvRes?.text = adRes?.isJudiOnline.toString()
            binding?.tvProb?.text = adRes?.probability.toString()
            binding?.tvDesc?.visibility = View.GONE
        }
    }

    companion object{
        private const val WEB_RES = "webResponse"
        private const val ADS_RES = "adsResponse"
        private const val DESC = "description"
    }
}