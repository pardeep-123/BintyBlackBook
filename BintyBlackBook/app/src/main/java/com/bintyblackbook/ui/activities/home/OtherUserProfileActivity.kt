package com.bintyblackbook.ui.activities.home

import android.content.Intent
import android.net.Uri
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
import com.bintyblackbook.ui.activities.home.message.ChatActivity
import com.bintyblackbook.ui.activities.home.message.MessagesActivity
import com.bintyblackbook.ui.dialogues.FragmentDialog
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.util.saveMsgType
import com.bintyblackbook.viewmodel.LoopsViewModel
import com.bintyblackbook.viewmodel.ProfileViewModel
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
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
    private var exoplayer: SimpleExoPlayer? = null
    var name=""
    var otherUserId=""
    var web_link=""

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
        tvWebLink.setOnClickListener(this)
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

        headingText.text= data?.firstName
        web_link=data?.websiteLink!!
        name= data.firstName
        otherUserId= data.id.toString()
        if(data.isSwapSystem=="0"){
            tvOpenToSwap.visibility=View.GONE
            tvSwapMind.visibility=View.GONE
            tvSwaps.visibility=View.GONE
        } else{
            tvOpenToSwap.visibility=View.VISIBLE
            tvSwapMind.visibility=View.VISIBLE
            tvSwaps.visibility=View.VISIBLE
            tvSwaps.text= data.swapInMind
        }

        if(data.isLoop==2){
            btnChatUser.visibility=View.VISIBLE
            btnUnLoopUser.visibility=View.VISIBLE
            btnAcceptReq.visibility=View.GONE
            btnLoopUser.visibility= View.GONE
        }else if(data.isLoop==0){
            btnChatUser.visibility=View.GONE
            btnUnLoopUser.visibility=View.GONE
            btnAcceptReq.visibility=View.VISIBLE
            btnLoopUser.visibility= View.GONE
        } else{
            btnChatUser.visibility=View.GONE
            btnUnLoopUser.visibility=View.GONE
            btnAcceptReq.visibility=View.GONE
            btnLoopUser.visibility=View.VISIBLE
        }

        tvUserName.text=data.firstName
        if(data.location.isNullOrEmpty()){
            tvUserLocation.visibility=View.GONE
        }else{
            tvUserLocation.visibility=View.VISIBLE
            tvUserLocation.text=data.location
        }

        tvOtherUserAbout.text= data.description
        val subCategories= ArrayList<String>()
        val categories= ArrayList<String>()
        data.category.forEach {
            categories.add(it.name)
            it.subCategories.forEach {
                subCategories.add("#"+it.name)
            }
        }
        tvSubCategory.text = TextUtils.join(" ,",subCategories)
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
        }else if(data.userMedia[0].media.endsWith(".mp4")){
                initializePlayer(data.userMedia[0].media)
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
                } else if (image.media.endsWith(".mp4")) {
//video
                rivUser.visibility=View.GONE
                videoView.visibility=View.VISIBLE
                initializePlayer(image.media)
            }

        }
    }

    override fun onClick(v: View?) {
        when(v?.id){

            R.id.tvWebLink ->{
                openWebPage(web_link)
            }

            R.id.btnChatUser ->{
                saveMsgType(this,0)
                val intent= Intent(this,ChatActivity::class.java)
                intent.putExtra("name",name)
                intent.putExtra("type","1")
                intent.putExtra("sender_id",otherUserId.toString())
                startActivity(intent)
            }
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
                //navigate to chat listing
                saveMsgType(this,1)
                val intent= Intent(this,ChatActivity::class.java)
                intent.putExtra("name",name)
                intent.putExtra("type","1")
                intent.putExtra("sender_id",otherUserId.toString())
                startActivity(intent)
            }
        }
    }

    private fun initializePlayer(media: String) {
        val trackSelector = DefaultTrackSelector()
        exoplayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector)
        val mediaUri = Uri.parse(media)
        val dataSourceFactory = DefaultHttpDataSourceFactory("exoplayer_video")
        val extractorsFactory: ExtractorsFactory = DefaultExtractorsFactory()
        val mediaSource: MediaSource = ExtractorMediaSource(mediaUri, dataSourceFactory, extractorsFactory, null, null)
        videoView.setPlayer(exoplayer)
        exoplayer?.prepare(mediaSource)
        exoplayer?.playWhenReady = true
        //mediaSession?.isActive = true

    }


    fun openWebPage(url: String) {
        var webpage = Uri.parse(url)
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            webpage = Uri.parse("http://$url")
        }
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

}