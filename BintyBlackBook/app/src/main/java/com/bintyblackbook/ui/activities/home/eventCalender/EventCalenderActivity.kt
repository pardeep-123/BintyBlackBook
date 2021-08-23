package com.bintyblackbook.ui.activities.home.eventCalender

import android.content.Intent
import android.os.Bundle
import com.bintyblackbook.R
import com.bintyblackbook.adapters.EventCalenderPagerAdapter
import com.bintyblackbook.base.BaseActivity
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_event_calender.*

class EventCalenderActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_calender)
        setOnClicks()

        setTabLayout()


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        val adapter = EventCalenderPagerAdapter(this, supportFragmentManager, tabLayout.tabCount)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        viewPager.currentItem = 0

    }

    private fun setTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("All Events"))
        tabLayout.addTab(tabLayout.newTab().setText("Favourite"))
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
        if (mProgress != null && mProgress?.isShowing()!!) {
            mProgress?.dismiss()
        }
        super.onPause()
    }
}