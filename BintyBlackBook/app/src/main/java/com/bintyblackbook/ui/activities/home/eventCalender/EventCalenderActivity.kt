package com.bintyblackbook.ui.activities.home.eventCalender

import android.content.Intent
import android.os.Bundle
import com.bintyblackbook.R
import com.bintyblackbook.adapters.EventCalenderPagerAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.ui.fragments.AllEventFragment
import com.bintyblackbook.ui.fragments.FavouriteFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_event_calender.*

class EventCalenderActivity : BaseActivity() {
    var fragList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_calender)
        setOnClicks()

        setTabLayout()

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = fragList[position]
        }.attach()
    }

    private fun setTabLayout() {
        val adapter = EventCalenderPagerAdapter(supportFragmentManager, this)
        fragList.clear()
        fragList.add("All Events")
        fragList.add("Favourite")
        adapter.addFragment(AllEventFragment(),"All Events")
        adapter.addFragment(FavouriteFragment(),"Favourite")
        viewPager.adapter = adapter
    }

    private fun setOnClicks() {
        rlBack.setOnClickListener {
            finish()
        }

        rlCalender.setOnClickListener {
            startActivity(Intent(this,CalenderActivity::class.java))
        }
    }

    override fun onPause() {
        if (mProgress != null && mProgress?.isShowing!!) {
            mProgress?.dismiss()
        }
        super.onPause()
    }
}