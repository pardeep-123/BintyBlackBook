package com.bintyblackbook.ui.activities.home.promote

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bintyblackbook.R
import com.bintyblackbook.adapters.AdapterPromoteBusiness
import com.bintyblackbook.adapters.EventCalenderPagerAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.ui.fragments.PromoteBusinessFragment
import com.bintyblackbook.ui.fragments.PromoteEventFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_promote_business.*
import kotlinx.android.synthetic.main.toolbar.*

class PromoteBusinessActivity : BaseActivity(), View.OnClickListener {
    var fragList: ArrayList<String> = ArrayList()
    var adapterPromoteBusiness: AdapterPromoteBusiness? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promote_business)
        headingText.text = "PROMOTE YOUR BUSINESS"
        iv_back.setOnClickListener(this)

        setTabLayout()

              TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                  tab.text = fragList[position]
              }.attach()

//        adapterPromoteBusiness = AdapterPromoteBusiness(context)
//        rvPromoteBusiness.adapter = adapterPromoteBusiness
        //adapterItemClick()
    }

    private fun setTabLayout() {
        val adapter = EventCalenderPagerAdapter(supportFragmentManager, this)
        fragList.clear()
        fragList.add("Promote Business")
        fragList.add("Promote Events")
        adapter.addFragment(PromoteBusinessFragment(),"Promote Business")
        adapter.addFragment(PromoteEventFragment(),"Promote Events")
        viewPager.adapter = adapter
    }

    private fun adapterItemClick(){
        adapterPromoteBusiness?.onItemClick = {pos: Int ->
            startActivity(Intent(this,PromotePayActivity::class.java))
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_back -> {
                finish()
            }
        }
    }
}