package com.bintyblackbook.ui.activities.home.eventCalender

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bintyblackbook.R
import com.bintyblackbook.adapters.EventCalenderPagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_event_calender.*

class EventCalenderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_calender)

        rlBack.setOnClickListener {
            finish()
        }

        rlCalender.setOnClickListener {
            startActivity(Intent(this,CalenderActivity::class.java))
        }

        tabLayout.addTab(tabLayout.newTab().setText("All Events"))
        tabLayout.addTab(tabLayout.newTab().setText("Favourite"))

        val adapter = EventCalenderPagerAdapter(this, supportFragmentManager, tabLayout.tabCount)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }
}