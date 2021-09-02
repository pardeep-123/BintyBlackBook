package com.bintyblackbook.ui.activities.home.message

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bintyblackbook.R
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.models.EditMessageModel
import com.bintyblackbook.ui.activities.home.loop.MessageFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_event_calender.*
import kotlinx.android.synthetic.main.activity_messages.*
import kotlinx.android.synthetic.main.activity_messages.rlBack

class MessagesActivity : BaseActivity(), View.OnClickListener {

    val fragList:ArrayList<String> = ArrayList()
    lateinit var arrayList: ArrayList<EditMessageModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        setOnClicks()
        setTabLayout()
        TabLayoutMediator(tabLayoutMsg, viewPageMsg) { tab, position ->
            tab.text = fragList[position]
        }.attach()

    }

    private fun setTabLayout() {
       val adapter= MessagesPagerAdapter(supportFragmentManager,this)
        fragList.clear()
        fragList.add("Messages")
        fragList.add("Swaps")
        adapter.addFragment(MessageFragment(),"Messages")
        adapter.addFragment(SwapsFragment(),"Swaps")
        viewPageMsg.adapter=adapter
    }

    private fun setOnClicks() {
        rlBack.setOnClickListener(this)
        rlEdit.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.rlBack -> {
                finish()
            }
            R.id.rlEdit -> {
                startActivity(Intent(this,EditMessageActivity::class.java))
            }
        }
    }
}