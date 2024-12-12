package com.dicoding.lawanjudi.ui.education

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.lawanjudi.databinding.FragmentEducationBinding
import com.dicoding.lawanjudi.ui.FirebaseViewModel
import com.dicoding.lawanjudi.ui.gemini.GeminiActivity
import com.dicoding.lawanjudi.database.Result
import com.dicoding.lawanjudi.ui.adapter.ArticleAdapter
import com.dicoding.lawanjudi.ui.factory.ArticleModelFactory


class EducationFragment : Fragment() {
    private var _binding: FragmentEducationBinding? = null
    private val binding get() = _binding

    private val firebaseViewModel: FirebaseViewModel by viewModels()
    private var isArticlesSaved = false

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
        binding?.pgEducation?.visibility = View.GONE

        val factory: ArticleModelFactory = ArticleModelFactory.getInstance(requireContext())
        val viewModel: ArticleViewModel by viewModels {
            factory
        }

        val articleAdapter = ArticleAdapter()

        firebaseViewModel.getArticles()

        firebaseViewModel.articleListResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding?.pgEducation?.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding?.pgEducation?.visibility = View.GONE
                    Log.d("EducationFragment", result.data.toString())
                    if (!isArticlesSaved) {
                        viewModel.saveArticles(result.data)
                        isArticlesSaved = true
                    }
                }
                is Result.Error -> {
                    binding?.pgEducation?.visibility = View.GONE
                }
            }
        }

        viewModel.getArticles().observe(viewLifecycleOwner) { articles ->
            articleAdapter.submitList(articles)
        }

        binding?.rvEdContent?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = articleAdapter
        }

        binding?.btnAI?.setOnClickListener {
            val intent = Intent(requireContext(), GeminiActivity::class.java)
            startActivity(intent)
        }
    }
}