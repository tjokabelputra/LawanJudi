package com.dicoding.lawanjudi.ui.add

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.lawanjudi.ui.add.ads.AdsReportFragment
import com.dicoding.lawanjudi.ui.add.web.WebReportFragment

class AddPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position) {
            0 -> fragment = WebReportFragment()
            1 -> fragment = AdsReportFragment()
        }
        return fragment as Fragment
    }
}