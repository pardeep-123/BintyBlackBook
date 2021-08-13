package com.bintyblackbook.ui.activities.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.HorizontalImagesAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.Data
import com.bintyblackbook.ui.activities.home.message.ChatActivity
import com.bintyblackbook.ui.dialogues.FragmentDialog
import com.bintyblackbook.util.AppConstant
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.ProfileViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_user_detail.*
import kotlinx.android.synthetic.main.toolbar.*

class UserDetailActivity : BaseActivity() {

    lateinit var profileViewModel: ProfileViewModel
    var horizontalImagesAdapter:HorizontalImagesAdapter? = null
    var loopBtnClick = true

    var userId=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        profileViewModel= ProfileViewModel(this)

        getData()
        rvImages.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val showChatBtn = intent.getBooleanExtra(AppConstant.SHOW_CHAT_BTN,false)
        userId=intent.getStringExtra("id").toString()
        val showChatAndUnLoopBtn = intent.getBooleanExtra(AppConstant.SHOW_CHAT_AND_UN_LOOP_BTN,false)
        if (showChatBtn){
            btnChat.visibility = View.VISIBLE
        }

        if (showChatAndUnLoopBtn){
            btnChat.visibility = View.VISIBLE
            btnUnLoop.visibility = View.VISIBLE
            btnLoop.visibility = View.GONE
        }

        iv_back.setOnClickListener {
            finish()
        }

        headingText.visibility = View.GONE

        init()

        btnAvailability.setOnClickListener {
            val intent = Intent(this,CheckAvailabilityActivity::class.java)
            startActivity(intent)
        }

        btnChat.setOnClickListener {
            val intent = Intent(this,ChatActivity::class.java)
            startActivity(intent)
        }

        btnLoop.setOnClickListener {
            if (loopBtnClick){
                loopBtnClick = false
                btnLoop.text = getString(R.string.cancel_loop_request)
                val fragmentDialog = FragmentDialog("LoopRequest")
                fragmentDialog.show(supportFragmentManager,"LoopDialog")
            }else{
                loopBtnClick = true
                btnLoop.text = getString(R.string.loop)
                val fragmentDialog = FragmentDialog("LoopRequestCancel")
                fragmentDialog.show(supportFragmentManager,"LoopDialog")
            }

        }

        btnEvent.setOnClickListener {
            val intent = Intent(this,EventActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getData() {
        profileViewModel.businessUserProfile(getSecurityKey(this)!!, getUser(this)?.authKey!!,userId)
        profileViewModel.profileObservable.observe(this, Observer {
            if(it.code==200){
                setData(it?.data)
            }
        })
    }

    private fun setData(data: Data?) {
        Glide.with(this).load(data?.image).into(riv1)
        tvName.text=data?.firstName
        tvUserAbout.text=data?.description
        tvUserExp.text=data?.experience
        tvWebsiteLink.text=data?.websiteLink
        tvLocation.text=data?.location
    }

    private fun init(){
        val arrayList = ArrayList<Int>()
        arrayList.add(R.drawable.slider)
        arrayList.add(R.drawable.small2)
        arrayList.add(R.drawable.small3)
        arrayList.add(R.drawable.small4)
        arrayList.add(R.drawable.small2)
        arrayList.add(R.drawable.small3)
        arrayList.add(R.drawable.small4)

        horizontalImagesAdapter = HorizontalImagesAdapter(this)
        rvImages.adapter = horizontalImagesAdapter
       // horizontalImagesAdapter?.arrayList=arrayList
        horizontalImagesAdapterClick()
    }

    private fun horizontalImagesAdapterClick(){
        /*horizontalImagesAdapter?.onItemClick = {image: Int ->
            riv1.setImageResource(image)
        }*/
    }
}