package com.dicoding.lawanjudi.ui.add.web

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.dicoding.lawanjudi.databinding.FragmentWebReportBinding
import com.dicoding.lawanjudi.ui.factory.AiModelFactory
import com.dicoding.lawanjudi.ui.add.AddViewModel
import com.dicoding.lawanjudi.database.Result
import com.dicoding.lawanjudi.ui.result.ResultActivity

class WebReportFragment : Fragment() {
    private var _binding: FragmentWebReportBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWebReportBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.pgWeb?.visibility = View.GONE

        val factory: AiModelFactory = AiModelFactory.getInstance()
        val viewModel: AddViewModel by viewModels {
            factory
        }

        viewModel.analyzeWebResult.observe(viewLifecycleOwner) { response ->
            when(response) {
                is Result.Loading -> {
                    binding?.pgWeb?.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding?.pgWeb?.visibility = View.GONE
                    val intent = Intent(requireContext(), ResultActivity::class.java)
                    intent.putExtra(WEB_RES, response.data)
                    intent.putExtra(DESC, binding?.descEditText?.text.toString())
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    binding?.urlEditText?.text = null
                    binding?.descEditText?.text = null
                    startActivity(intent)
                }
                is Result.Error -> {
                    binding?.pgWeb?.visibility = View.GONE
                    Toast.makeText(requireContext(), response.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding?.btnWebCheck?.setOnClickListener {
            val url = binding?.urlEditText?.text.toString()
            viewModel.analyzeWeb(url)
        }
    }

    companion object{
        private const val WEB_RES = "webResponse"
        private const val DESC = "description"
    }
}