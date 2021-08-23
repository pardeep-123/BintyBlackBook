package com.bintyblackbook.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.bintyblackbook.ui.fragments.AllEventFragment
import com.bintyblackbook.ui.fragments.FavouriteFragment


class EventCalenderPagerAdapter(context: Context, fm: FragmentManager, totalTabs: Int) : FragmentPagerAdapter(fm) {

    private val mContext: Context = context
    private var mTotalTabs: Int = totalTabs

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                AllEventFragment()
            }
            1->{
                FavouriteFragment()
            }
            else -> {
              getItem(position)
            }

        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return mTotalTabs
    }

}