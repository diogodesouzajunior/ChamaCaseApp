package com.diogo.chamacaseapp.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.diogo.chamacaseapp.ui.estabilishment.EstabilishmentFragment

const val RESTAURANT_SEARCH = "restaurant"
const val BAR_SEARCH = "bar"
const val CAFE_SEARCH = "cafe"

class MainAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                EstabilishmentFragment.getInstance(RESTAURANT_SEARCH)
            }
            1 -> {
                EstabilishmentFragment.getInstance(BAR_SEARCH)
            }
            else -> {
                return EstabilishmentFragment.getInstance(CAFE_SEARCH)
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Restaurantes"
            1 -> "Bares"
            else -> {
                return "Cafés"
            }
        }
    }
}