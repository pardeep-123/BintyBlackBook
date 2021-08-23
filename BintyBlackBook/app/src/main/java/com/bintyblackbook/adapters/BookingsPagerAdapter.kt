package com.bintyblackbook.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.bintyblackbook.ui.fragments.PastBookingsFragment
import com.bintyblackbook.ui.fragments.UpcomingBookingsFragment


class BookingsPagerAdapter(context: Context, fm: FragmentManager, totalTabs: Int) : FragmentPagerAdapter(fm) {

    private val mContext: Context = context
    private var mTotalTabs: Int = totalTabs

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                UpcomingBookingsFragment()
            }

            1 ->{
                PastBookingsFragment()
            }
            else -> {
                UpcomingBookingsFragment()
            }

        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return mTotalTabs
    }

}