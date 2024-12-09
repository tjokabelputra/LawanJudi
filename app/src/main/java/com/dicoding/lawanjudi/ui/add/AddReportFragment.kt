package com.dicoding.lawanjudi.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dicoding.lawanjudi.R
import com.dicoding.lawanjudi.databinding.FragmentAddReportBinding
import com.google.android.material.tabs.TabLayoutMediator


class AddReportFragment : Fragment() {
    private var _binding: FragmentAddReportBinding? = null
    val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddReportBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagerAdapter = AddPagerAdapter(requireActivity() as AppCompatActivity)
        binding?.viewPager?.adapter = pagerAdapter

        TabLayoutMediator(binding!!.tabs, binding!!.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_web,
            R.string.tab_ads
        )
    }
}