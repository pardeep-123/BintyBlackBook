package com.bintyblackbook.ui.activities.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.HorizontalImagesAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.Data
import com.bintyblackbook.model.UserMedia
import com.bintyblackbook.ui.activities.home.message.ChatActivity
import com.bintyblackbook.ui.dialogues.FragmentDialog
import com.bintyblackbook.util.AppConstant
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.LoopsViewModel
import com.bintyblackbook.viewmodel.ProfileViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_user_detail.*
import kotlinx.android.synthetic.main.toolbar.*

class UserDetailActivity : BaseActivity(), View.OnClickListener {

    lateinit var profileViewModel: ProfileViewModel
    var horizontalImagesAdapter:HorizontalImagesAdapter? = null
    var loopBtnClick = true

    var userId=""
    var showChatBtn=false
    var showChatAndUnLoopBtn=false
    lateinit var loopsViewModel: LoopsViewModel
    var arrayList= ArrayList<UserMedia>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        profileViewModel= ProfileViewModel(this)
        loopsViewModel = LoopsViewModel(this)
        getIntentData()
        init()

        getData()

        if (showChatBtn){
            btnChat.visibility = View.VISIBLE
        }

        if (showChatAndUnLoopBtn){
            btnChat.visibility = View.VISIBLE
            btnUnLoop.visibility = View.VISIBLE
            btnLoop.visibility = View.GONE
        }

        setOnClicks()

        headingText.visibility = View.GONE

    }

    private fun getIntentData() {
        showChatBtn= intent.getBooleanExtra(AppConstant.SHOW_CHAT_BTN,false)
        userId=intent.getStringExtra("user_id").toString()
        showChatAndUnLoopBtn = intent.getBooleanExtra(AppConstant.SHOW_CHAT_AND_UN_LOOP_BTN,false)
    }

    private fun setOnClicks() {
        iv_back.setOnClickListener(this)
        btnAvailability.setOnClickListener(this)
        btnChat.setOnClickListener(this)
        btnLoop.setOnClickListener(this)
        btnEvent.setOnClickListener(this)
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
        Glide.with(this).load(data?.userMedia!![0].media).into(riv1)
        tvName.text=data.firstName
        tvUserAbout.text=data.description
        tvUserExp.text=data.experience
        tvWebsiteLink.text=data.websiteLink
        tvLocation.text=data.location
        arrayList.addAll(data.userMedia)
        horizontalImagesAdapter?.notifyDataSetChanged()
    }

    private fun init(){
        rvImages.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        horizontalImagesAdapter = HorizontalImagesAdapter(this)
        rvImages.adapter = horizontalImagesAdapter
        horizontalImagesAdapter?.arrayList=arrayList
        horizontalImagesAdapterClick()
    }

    private fun horizontalImagesAdapterClick(){
        horizontalImagesAdapter?.onItemClick = {image: UserMedia ->
            Glide.with(this).load(image.media).into(riv1)
        }
    }

    override fun onClick(v: View?) {

        when(v?.id){
            R.id.iv_back ->{
                finish()
            }

            R.id.btnAvailability ->{
                val intent = Intent(this,CheckAvailabilityActivity::class.java)
                intent.putExtra("user_id",userId)
                startActivity(intent)
            }

            R.id.btnChat ->{
                val intent = Intent(this,ChatActivity::class.java)
                startActivity(intent)
            }

            R.id.btnLoop ->{
                if (loopBtnClick){
                    loopBtnClick = false
                    btnLoop.text = getString(R.string.cancel_loop_request)
                    loopsViewModel.sendLoopReq(getSecurityKey(context)!!, getUser(context)?.authKey!!,userId)

                    loopsViewModel.baseLiveData.observe(this, Observer {

                        if(it.code==200) {
                            val fragmentDialog = FragmentDialog("LoopRequest")
                            fragmentDialog.show(supportFragmentManager, "LoopDialog")
                        }
                    })

                }else{
                    loopBtnClick = true
                    btnLoop.text = getString(R.string.loop)
                    loopsViewModel.unSendLoopReq(getSecurityKey(context)!!, getUser(context)?.authKey!!,userId)
                    loopsViewModel.baseLiveData.observe(this, Observer {
                        if(it.code==200){
                            val fragmentDialog = FragmentDialog("LoopRequestCancel")
                            fragmentDialog.show(supportFragmentManager,"LoopDialog")
                        }
                    })
                }
            }

            R.id.btnEvent ->{
                val intent = Intent(this,EventActivity::class.java)
                startActivity(intent)
            }
        }
    }
}