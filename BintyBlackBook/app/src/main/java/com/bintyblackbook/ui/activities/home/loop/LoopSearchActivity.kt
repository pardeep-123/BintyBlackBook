package com.bintyblackbook.ui.activities.home.loop

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.base.BaseActivity
import kotlinx.android.synthetic.main.activity_loop_search.*
import kotlinx.android.synthetic.main.toolbar.*

class LoopSearchActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loop_search)

        headingText.text = "LOOP SEARCh"
        setLoopsAdapter()

        setFriendsAdapter()
    }

    private fun setFriendsAdapter() {
        rvMyFriends.layoutManager= LinearLayoutManager(this, RecyclerView.HORIZONTAL,false)
    }

    private fun setLoopsAdapter() {
        rvLoops.layoutManager= LinearLayoutManager(this,RecyclerView.VERTICAL,false)

    }
}