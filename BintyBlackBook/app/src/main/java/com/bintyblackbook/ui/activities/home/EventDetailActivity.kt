package com.bintyblackbook.ui.activities.home

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.bintyblackbook.R
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.util.AppConstant
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.PostsViewModel
import kotlinx.android.synthetic.main.activity_event_detail.*
import kotlinx.android.synthetic.main.toolbar.*

class EventDetailActivity : BaseActivity() {

    var heartSelected = true

    var post_id=""
    lateinit var postsViewModel: PostsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        postsViewModel= PostsViewModel(this)
        val heading = intent.getStringExtra(AppConstant.HEADING)
        post_id=intent.getStringExtra("post_id").toString()

        setOnClicks()

        getEventDetail(post_id)
        if (heading != null) {
            headingText.text = heading
        }


    }

    private fun getEventDetail(postId: String) {
        postsViewModel.postDetail(getSecurityKey(context)!!, getUser(context)?.authKey!!,post_id)
        postsViewModel.addPostLiveData.observe(this, Observer {

            Log.i("TAG",it.msg)

        })
    }

    private fun setOnClicks() {
        iv_back.setOnClickListener {
            finish()
        }

        rlHeart.setOnClickListener {
            if (heartSelected) {
                heartSelected = false
                ivHeart.setImageResource(R.drawable.like)
            } else {
                heartSelected = true
                ivHeart.setImageResource(R.drawable.heart_new)
            }
        }
    }
}