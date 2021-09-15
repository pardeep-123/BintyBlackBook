package com.bintyblackbook.ui.activities.home

import android.os.Bundle
import androidx.lifecycle.Observer
import com.bintyblackbook.R
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.Data
import com.bintyblackbook.util.AppConstant
import com.bintyblackbook.util.MyUtils
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.EventsViewModel
import com.bintyblackbook.viewmodel.ProfileViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_event_detail.*
import kotlinx.android.synthetic.main.toolbar.*

class EventDetailActivity : BaseActivity() {

    var heartSelected = true

    var user_id:Int=0
    var user_name=""
    var location=""
    var date=""
    var time=""
    var description= ""
    var image=""
    var web_link=""
    var isFavourite=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        getIntentData()
        setOnClicks()
    }

    private fun getIntentData() {
        user_id= intent.getIntExtra("user_id",0)
        user_name = intent.getStringExtra(AppConstant.HEADING).toString()
        location=intent.getStringExtra("location").toString()
        image=intent.getStringExtra("image").toString()
        time=intent.getStringExtra("time").toString()
        date= intent.getStringExtra("date").toString()
        description= intent.getStringExtra("desc").toString()
        web_link=intent.getStringExtra("web_link").toString()
        isFavourite=intent.getStringExtra("is_favourite").toString()

        setData()
    }


    private fun setData() {
        Glide.with(context).load(image).into(img_user)
        tvName.text= user_name
        tvLocation.text=location
        tvTIme.text= MyUtils.getDayDate(date.toLong())+" " +MyUtils.getTime(time.toLong())
        tvWebLinks.text= web_link
        tvDesc.text= description
        if(isFavourite=="1"){
            ivHeart.setImageResource(R.drawable.fill_heart)
        }
        else{
            ivHeart.setImageResource(R.drawable.heart_new)
        }

        headingText.text = user_name
    }

    private fun setOnClicks() {
        iv_back.setOnClickListener {
            finish()
        }
    }
}