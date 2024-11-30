package com.dicoding.lawanjudi.ui.education

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dicoding.lawanjudi.databinding.FragmentEducationBinding
import com.dicoding.lawanjudi.ui.gemini.GeminiActivity


class EducationFragment : Fragment() {
    private var _binding: FragmentEducationBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEducationBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnAI?.setOnClickListener {
            val intent = Intent(requireContext(), GeminiActivity::class.java)
            startActivity(intent)
        }
    }
}