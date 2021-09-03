package com.bintyblackbook.ui.activities.home

import android.os.Bundle
import androidx.lifecycle.Observer
import com.bintyblackbook.R
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.EventData
import com.bintyblackbook.util.AppConstant
import com.bintyblackbook.util.MyUtils
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.EventsViewModel
import com.bintyblackbook.viewmodel.PostsViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_event_detail.*
import kotlinx.android.synthetic.main.toolbar.*

class EventDetailActivity : BaseActivity() {

    var heartSelected = true

    var post_id:Int=0
    lateinit var eventsViewModel: EventsViewModel
    lateinit var postsViewModel: PostsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        eventsViewModel= EventsViewModel()
        postsViewModel=PostsViewModel()
        post_id= intent.getIntExtra("post_id",0)
        val heading = intent.getStringExtra(AppConstant.HEADING)

        setOnClicks()

        getEventDetail(post_id)
        if (heading != null) {
            headingText.text = heading
        }
    }

    private fun getEventDetail(postId: Int) {
        eventsViewModel.getOtherUserEvents(context,getSecurityKey(this)!!, getUser(this)?.authKey!!)

        eventsViewModel.eventsLiveData.observe(this, Observer {
            if(it.code==200){
                for(i in 0 until it.data.size){
                    if(postId== it?.data?.get(i)?.id){
                        setData(it.data[i])
                    }
                }
            }
        })
    }

    private fun setData(eventData: EventData) {
        Glide.with(context).load(eventData.image).into(img_user)
        tvName.text= eventData.name
        tvLocation.text=eventData.location
        tvTIme.text= MyUtils.getDate(eventData.date.toLong())+" " +MyUtils.getTime(eventData?.time.toLong())
        tvWebLinks.text=eventData.rsvpLink
        tvDesc.text=eventData.description
        if(eventData.isFavourite==1){
            ivHeart.setImageResource(R.drawable.fill_heart)
        }

        else{
            ivHeart.setImageResource(R.drawable.heart_new)
        }
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