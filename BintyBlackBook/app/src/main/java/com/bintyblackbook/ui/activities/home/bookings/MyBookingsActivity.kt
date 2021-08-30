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
    //    var mProgress: CustomProgressDialog? = null
//    private var mSnackBar: Snackbar? = null
    var fragList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_bookings)
//        mProgress = CustomProgressDialog(this)

        rlBack.setOnClickListener {
            finish()
        }

        setTabLayout()

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = fragList.get(position)
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
//        tabLayout.addTab(tabLayout.newTab().setText("Upcoming"))
//        tabLayout.addTab(tabLayout.newTab().setText("Past"))
//
//        val adapter = BookingsPagerAdapter(this, supportFragmentManager, tabLayout.tabCount)
//        viewPager.adapter = adapter
//        viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))
//
//        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                viewPager.currentItem = tab.position
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {
//
//            }
//            override fun onTabReselected(tab: TabLayout.Tab) {
//
//            }
//        })
    }

//    fun showProgressDialog(){
//        mProgress = CustomProgressDialog(this)
//        mProgress?.show()
//    }
//
//    fun dismissProgressDialog(){
//
//        mProgress?.dismiss()
//    }
//
//    fun showSnackBarMessage(msg: String) {
//        try {
//            mSnackBar = Snackbar.make(
//                getWindow().getDecorView().getRootView(),
//                msg,
//                Snackbar.LENGTH_LONG
//            ) //Assume "rootLayout" as the root layout of every activity.
//            mSnackBar?.duration = Snackbar.LENGTH_SHORT!!
//            mSnackBar?.show()
//        } catch (e: Exception) {
//            Log.e("TAG",e.printStackTrace().toString())
//        }
//    }
}