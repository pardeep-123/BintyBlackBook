package com.bintyblackbook.ui.activities.home.promote

import android.os.Bundle
import android.view.View
import com.bintyblackbook.R
import com.bintyblackbook.adapters.EventCalenderPagerAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.ui.fragments.PromoteBusinessFragment
import com.bintyblackbook.ui.fragments.PromoteEventFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_promote_business.*
import kotlinx.android.synthetic.main.toolbar.*

class PromoteBusinessActivity : BaseActivity(), View.OnClickListener {
    var fragList: ArrayList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promote_business)
        headingText.text = getString(R.string.promote_your_business)
        iv_back.setOnClickListener(this)

        setTabLayout()

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = fragList[position]
        }.attach()
    }

    private fun setTabLayout() {
        val adapter = EventCalenderPagerAdapter(supportFragmentManager, this)
        fragList.clear()
        fragList.add(getString(R.string.promote_business))
        fragList.add(getString(R.string.promote_events))
        adapter.addFragment(PromoteBusinessFragment(),getString(R.string.promote_business))
        adapter.addFragment(PromoteEventFragment(),getString(R.string.promote_events))
        viewPager.adapter = adapter
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_back -> {
                finish()
            }
        }
    }
}