package com.dicoding.lawanjudi.ui.history

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.lawanjudi.database.User
import com.dicoding.lawanjudi.database.UserPreference
import com.dicoding.lawanjudi.database.userDataStore
import com.dicoding.lawanjudi.databinding.FragmentHistoryBinding
import com.dicoding.lawanjudi.ui.FirebaseViewModel
import com.dicoding.lawanjudi.database.Result
import com.dicoding.lawanjudi.ui.adapter.HistoryAdapter
import com.dicoding.lawanjudi.ui.factory.ReportsModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding

    private val firebaseViewModel: FirebaseViewModel by viewModels()

    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userPref = UserPreference.getInstance(requireContext().userDataStore)

        val factory: ReportsModelFactory = ReportsModelFactory.getInstance(requireContext())
        val viewModel: HistoryViewModel by viewModels {
            factory
        }

        val historyAdapter = HistoryAdapter()
        binding?.pgHistory?.visibility = View.GONE

        lifecycleScope.launch {
            user = userPref.getUser().first()
            firebaseViewModel.getReports(user.name, user.email)
        }

        firebaseViewModel.reportListResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding?.pgHistory?.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding?.pgHistory?.visibility = View.GONE
                    viewModel.addReports(result.data)
                }
                is Result.Error -> {
                    binding?.pgHistory?.visibility = View.GONE
                    Log.e("HistoryFragment", "Error: ${result.error}")
                }
            }
        }

        viewModel.getReports().observe(viewLifecycleOwner) { reports ->
            historyAdapter.submitList(reports)
        }

        binding?.rvReports?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = historyAdapter
        }
    }
}