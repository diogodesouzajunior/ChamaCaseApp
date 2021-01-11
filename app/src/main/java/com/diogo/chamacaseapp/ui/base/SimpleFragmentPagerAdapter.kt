package com.diogo.chamacaseapp.ui.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter


class SimpleFragmentPagerAdapter

constructor(fm: FragmentManager,
            private val fragmentList: List<Fragment>,
            private val titleList: List<String>): FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount() = fragmentList.size

    override fun getPageTitle(position: Int): CharSequence {
        return titleList[position]
    }
}