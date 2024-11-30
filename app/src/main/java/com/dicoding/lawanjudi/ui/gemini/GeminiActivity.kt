package com.dicoding.lawanjudi.ui.gemini

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.lawanjudi.databinding.ActivityGeminiBinding

class GeminiActivity : AppCompatActivity() {
    private var _binding: ActivityGeminiBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityGeminiBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }
}