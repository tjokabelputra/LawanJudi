package com.dicoding.lawanjudi.ui.add.ads

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.dicoding.lawanjudi.database.Result
import com.dicoding.lawanjudi.databinding.FragmentAdsReportBinding
import com.dicoding.lawanjudi.ui.factory.AiModelFactory
import com.dicoding.lawanjudi.ui.add.AddViewModel
import com.dicoding.lawanjudi.ui.result.ResultActivity

class AdsReportFragment : Fragment() {
    private var _binding: FragmentAdsReportBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdsReportBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.pgAds?.visibility = View.GONE

        val factory: AiModelFactory = AiModelFactory.getInstance()
        val viewModel: AddViewModel by viewModels {
            factory
        }

        viewModel.analyzeAdsResult.observe(viewLifecycleOwner) { response ->
            when(response) {
                is Result.Loading -> {
                    binding?.pgAds?.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding?.pgAds?.visibility = View.GONE
                    val intent = Intent(requireContext(), ResultActivity::class.java)
                    intent.putExtra(ADS_RES, response.data)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    binding?.adsEditText?.text = null
                    startActivity(intent)
                }
                is Result.Error -> {
                    binding?.pgAds?.visibility = View.GONE
                    Log.e("AdsReportFragment", "Error: ${response.error}")
                }
            }
        }

        binding?.btnAdsCheck?.setOnClickListener {
            val ads = binding?.adsEditText?.text.toString().trim()
            viewModel.analyzeAds(ads)
        }
    }

    companion object{
        private const val ADS_RES = "adsResponse"
    }
}