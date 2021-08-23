package com.bintyblackbook.ui.activities.home.bookings

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bintyblackbook.R
import com.bintyblackbook.adapters.BookingsPagerAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.util.CustomProgressDialog
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.BookingsViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import kotlinx.android.synthetic.main.activity_my_bookings.*

class MyBookingsActivity : AppCompatActivity() {
    var mProgress: CustomProgressDialog? = null
    private var mSnackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_bookings)
        mProgress = CustomProgressDialog(this)

        rlBack.setOnClickListener {
            finish()
        }

        setTabLayout()
    }

    private fun setTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("Upcoming"))
        tabLayout.addTab(tabLayout.newTab().setText("Past"))

        val adapter = BookingsPagerAdapter(this, supportFragmentManager, tabLayout.tabCount)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    fun showProgressDialog(){
        mProgress = CustomProgressDialog(this)
        mProgress?.show()
    }

    fun dismissProgressDialog(){

        mProgress?.dismiss()
    }

    fun showSnackBarMessage(msg: String) {
        try {
            mSnackBar = Snackbar.make(
                getWindow().getDecorView().getRootView(),
                msg,
                Snackbar.LENGTH_LONG
            ) //Assume "rootLayout" as the root layout of every activity.
            mSnackBar?.duration = Snackbar.LENGTH_SHORT!!
            mSnackBar?.show()
        } catch (e: Exception) {
            Log.e("TAG",e.printStackTrace().toString())
        }
    }
}