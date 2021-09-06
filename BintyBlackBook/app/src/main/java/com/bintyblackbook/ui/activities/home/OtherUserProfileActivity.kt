package com.bintyblackbook.ui.activities.home

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.HorizontalImagesAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.Data
import com.bintyblackbook.model.UserMedia
import com.bintyblackbook.ui.dialogues.FragmentDialog
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.LoopsViewModel
import com.bintyblackbook.viewmodel.ProfileViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_other_user_profile.*
import kotlinx.android.synthetic.main.activity_other_user_profile.tvWebLink
import kotlinx.android.synthetic.main.activity_user_detail.rvImages
import kotlinx.android.synthetic.main.toolbar.*

class OtherUserProfileActivity: BaseActivity(), View.OnClickListener {

    var userId=""
    lateinit var profileViewModel: ProfileViewModel
    lateinit var loopsViewModel: LoopsViewModel
    var horizontalImagesAdapter:HorizontalImagesAdapter? = null
    var arrayList= ArrayList<UserMedia>()
    var loopBtnClick = true

    var loop_id=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_user_profile)

        profileViewModel= ProfileViewModel(this)
        loopsViewModel= LoopsViewModel()

        getIntentData()
        setAdapter()

        getData()

        setOnClicks()

    }

    private fun setOnClicks() {
        btnCheckAvailability.setOnClickListener(this)
        btnChatUser.setOnClickListener(this)
        btnLoopUser.setOnClickListener(this)
        btnUnLoopUser.setOnClickListener(this)
        btnUserEvent.setOnClickListener(this)
        btnSwap.setOnClickListener(this)
        iv_back.setOnClickListener {
            finish()
        }
    }

    private fun getData() {
        profileViewModel.businessUserProfile(getSecurityKey(context)!!, getUser(context)?.authKey!!,userId)
        profileViewModel.profileObservable.observe(this, Observer {

            setData(it.data)

        })
    }

    private fun setData(data: Data?) {

        if(data?.isSwapSystem=="0"){
            tvOpenToSwap.visibility=View.GONE
            tvSwapMind.visibility=View.GONE
            tvSwaps.visibility=View.GONE
        }

        else{
            tvOpenToSwap.visibility=View.VISIBLE
            tvSwapMind.visibility=View.VISIBLE
            tvSwaps.visibility=View.VISIBLE
            tvSwaps.text= data?.swapInMind
        }

        if(data?.isLoop==0){
            btnChatUser.visibility=View.GONE
            btnUnLoopUser.visibility=View.GONE
            btnLoopUser.visibility=View.VISIBLE
        }
        else{
            btnChatUser.visibility=View.VISIBLE
            btnUnLoopUser.visibility=View.VISIBLE
            btnLoopUser.visibility= View.GONE
        }

        tvUserName.text=data?.firstName
        tvUserLocation.text=data?.location
        tvOtherUserAbout.text= data?.description
        val subCategories= ArrayList<String>()
        val categories= ArrayList<String>()
        data?.category!!.forEach {
            categories.add(it.name)
            it.subCategories.forEach {
                subCategories.add(it.name)
            }
        }
        tvSubCategory.text = TextUtils.join("# ",subCategories)
        tvBusinessCategory.text= TextUtils.join(",", categories)

        //tvSubCategory.text= TextUtils.join(",")
        tvBusinessYrs.text = data.experience
        tvServices.text= data.services

        tvOperationTime.text= data.operationTime
        tvWebLink.text = data.websiteLink
        tvSocialMedia.text= data.socialMediaHandles
        Glide.with(this).load(data.image).into(civ_profile_user)

        if(data.userMedia[0].media.endsWith(".jpg")){
            Glide.with(this).load(data.userMedia[0].media).into(rivUser)
        }else if(data.userMedia[0].media.endsWith(".3gp")){

        }
        else{

        }

        if(data.userMedia.size > 0){
            arrayList.addAll(data.userMedia)
            horizontalImagesAdapter?.notifyDataSetChanged()
        }
    }

    private fun setAdapter() {
        rvImages.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        horizontalImagesAdapter = HorizontalImagesAdapter(this)
        rvImages.adapter = horizontalImagesAdapter
        horizontalImagesAdapter?.arrayList=arrayList
        horizontalImagesAdapterClick()
    }

    private fun getIntentData() {
        userId=intent.getStringExtra("user_id").toString()
    }

    private fun horizontalImagesAdapterClick(){
        horizontalImagesAdapter?.onItemClick = {image: UserMedia ->
            if (image.media.endsWith(".jpg")) {
                rivUser.visibility=View.VISIBLE
                videoView.visibility=View.GONE
                Glide.with(this).load(image.media).into(rivUser)
//photo
                } else if (image.media.endsWith(".3gp")) {
//video
                rivUser.visibility=View.GONE
                videoView.visibility=View.VISIBLE
            }

        }
    }

    override fun onClick(v: View?) {
        when(v?.id){

            R.id.btnCheckAvailability ->{
                val intent = Intent(this,CheckAvailabilityActivity::class.java)
                intent.putExtra("user_id",userId)
                startActivity(intent)
            }
            R.id.btnUserEvent ->{

                val intent= Intent(this, EventActivity::class.java)
                startActivity(intent)

            }
            R.id.btnLoopUser ->{
                if (loopBtnClick){
                    loopBtnClick = false
                    btnLoopUser.text = getString(R.string.cancel_loop_request)
                    loopsViewModel.sendLoopReq(this,getSecurityKey(context)!!, getUser(context)?.authKey!!,userId)

                    loopsViewModel.baseLiveData.observe(this, Observer {

                        if(it.code==200) {
                            val fragmentDialog = FragmentDialog("LoopRequest")
                            fragmentDialog.show(supportFragmentManager, "LoopDialog")
                        }
                    })

                }else{
                    loopBtnClick = true
                    btnLoopUser.text = getString(R.string.loop)
                    loopsViewModel.unSendLoopReq(this,getSecurityKey(context)!!, getUser(context)?.authKey!!,userId)
                    loopsViewModel.baseLiveData.observe(this, Observer {
                        if(it.code==200){
                            val fragmentDialog = FragmentDialog("LoopRequestCancel")
                            fragmentDialog.show(supportFragmentManager,"LoopDialog")
                        }
                    })
                }
            }

            R.id.btnUnLoopUser ->{
                loopsViewModel.unLoop(this,getSecurityKey(context)!!, getUser(context)?.authKey!!,userId)
                loopsViewModel.baseLiveData.observe(this, Observer {
                    if(it.code==200){
                        showAlertWithOk(it.msg)
                        btnChatUser.visibility=View.GONE
                        btnUnLoopUser.visibility=View.GONE
                        btnLoopUser.visibility=View.VISIBLE
                    }
                    else{
                        Log.i("TAG",it?.msg.toString())
                    }
                })
            }

            R.id.btnSwap ->{

            }
        }
    }
}