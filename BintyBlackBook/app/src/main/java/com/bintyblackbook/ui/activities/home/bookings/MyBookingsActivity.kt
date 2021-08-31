package com.bintyblackbook.ui.activities.home.bookings

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bintyblackbook.R
import com.bintyblackbook.adapters.BookingsPagerAdapter

import com.bintyblackbook.base.BaseActivity

import com.bintyblackbook.ui.fragments.PastBookingsFragment
import com.bintyblackbook.ui.fragments.UpcomingBookingsFragment

import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_my_bookings.*


class MyBookingsActivity : BaseActivity() {

    var fragList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_bookings)

        rlBack.setOnClickListener {
            finish()
        }

        setTabLayout()

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = fragList[position]
        }.attach()
    }

    private fun setTabLayout() {
        val adapter = BookingsPagerAdapter(supportFragmentManager, this)
        fragList.clear()
        fragList.add("Upcoming")
        fragList.add("Past")
        adapter.addFragment(UpcomingBookingsFragment(),"Upcoming")
        adapter.addFragment(PastBookingsFragment(),"Past")
        viewPager.adapter = adapter

    }

    override fun onPause() {
        if(mProgress!=null && mProgress?.isShowing!!){
            mProgress?.dismiss()
        }
        super.onPause()
    }
}