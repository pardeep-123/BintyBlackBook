package com.bintyblackbook.ui.activities.home.profileBusiness

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.HorizontalImagesAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.Data
import com.bintyblackbook.model.UserMedia
import com.bintyblackbook.ui.activities.home.profileUser.AvailabilityActivity
import com.bintyblackbook.ui.activities.home.profileUser.EventInProfileActivity
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
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
import kotlinx.android.synthetic.main.activity_event_detail.*
import kotlinx.android.synthetic.main.activity_my_profile_business.*
import kotlinx.android.synthetic.main.activity_my_profile_business.btnEvent
import kotlinx.android.synthetic.main.activity_my_profile_business.civ_profile
import kotlinx.android.synthetic.main.activity_my_profile_business.riv1
import kotlinx.android.synthetic.main.activity_my_profile_business.rlBack
import kotlinx.android.synthetic.main.activity_my_profile_business.rvImages
import kotlinx.android.synthetic.main.activity_my_profile_business.tvBusinessCategory
import kotlinx.android.synthetic.main.activity_my_profile_business.tvLocation
import kotlinx.android.synthetic.main.activity_my_profile_business.tvName
import kotlinx.android.synthetic.main.activity_my_profile_business.tvSubCategory
import kotlinx.android.synthetic.main.activity_my_profile_business.tvUserAbout
import kotlinx.android.synthetic.main.activity_my_profile_business.tvWebLink
import kotlinx.android.synthetic.main.activity_other_user_profile.*
import kotlinx.android.synthetic.main.activity_user_detail.*


class MyProfileBusinessActivity : BaseActivity(), View.OnClickListener {

    lateinit var profileViewModel: ProfileViewModel

    var horizontalImagesAdapter: HorizontalImagesAdapter? = null
    val arrayList = ArrayList<UserMedia>()
    var web_link=""
    private var exoplayer: SimpleExoPlayer? = null
    var response:Data?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile_business)

        profileViewModel= ProfileViewModel(this)
        setAdapter()
        getData()

        rlBack.setOnClickListener {
            onBackPressed()
        }

        setOnClicks()

    }

    private fun getData() {
        profileViewModel.businessUserProfile(getSecurityKey(this)!!, getUser(this)?.authKey!!, getUser(this)?.id.toString())

        setObservables()
    }

    private fun setOnClicks() {

        btnSetAvailability.setOnClickListener(this)
        btnEditProfile.setOnClickListener(this)
        btnEvent.setOnClickListener(this)
        tvWebLink.setOnClickListener(this)
    }

    private fun setObservables() {

        profileViewModel.profileObservable.observe(this, Observer {
            if (it.code==200){
                response=it.data
                setData(it.data)
            }
        })
    }

    private fun setData(it: Data?) {

        web_link= it?.websiteLink!!
        tvName.text= it.firstName
        if(it.location.isNullOrEmpty()){
            tvLocation.visibility=View.GONE
        }else{
            tvLocation.visibility=View.VISIBLE
            tvLocation.text=it.location
        }

        tvUserAbout.text=it.description
        tvExp.text=it.experience
        tvWebLink.setPaintFlags(tvWebLink.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        tvWebLink.text=it.websiteLink
        Glide.with(this).load(it.image).into(civ_profile)
        if(it.isPromoted==1){
            imgPromote.visibility=View.VISIBLE
            imgPromote.setImageResource(R.drawable.promote_icon)
        }else if(it.businessBlackOwned==1){
            imgPromote.visibility=View.VISIBLE
            imgPromote.setImageResource(R.drawable.dot_black_owned)
        }else{
            imgPromote.visibility=View.GONE
        }

        if(it.userMedia.size!=0){
            if(it.userMedia[0].media.endsWith(".jpg")){
                riv1.visibility=View.VISIBLE
                videoViewProfile.visibility=View.GONE
                Glide.with(this).load(it.userMedia[0].media).into(riv1)
            }else{
                riv1.visibility=View.GONE
                videoViewProfile.visibility=View.VISIBLE
                initializePlayer(it.userMedia[0].media)
            }
        }else{
            riv1.visibility=View.GONE
            videoViewProfile.visibility=View.GONE
            rvImages.visibility=View.GONE
        }


        if(it.userMedia.size > 0){
            arrayList.clear()
            arrayList.addAll(it.userMedia)
            horizontalImagesAdapter?.notifyDataSetChanged()
        }

        val subCategories= ArrayList<String>()
        val categories= ArrayList<String>()
        it.category.forEach {
            if(it.isSelected==1){
                categories.add(it.name)
            }

            it.subCategories.forEach {
                if(it.isSelected==1){
                    subCategories.add("#"+it.name)
                }

            }
        }
        tvSubCategory.text = TextUtils.join(" ,",subCategories)
        tvBusinessCategory.text= TextUtils.join(",", categories)
        if(it.services.isNullOrEmpty()){
            tvService.visibility=View.GONE
            tvServiceProvide.visibility=View.GONE
        }else{
            tvService.visibility=View.VISIBLE
            tvServiceProvide.visibility=View.VISIBLE
            tvService.text= it.services
        }
        if(it.isSwapSystem=="1"){
            tvSwapInMind.text= it.swapInMind
        }

        tvTime.text= it.operationTime
        tvWebLink.text= it.websiteLink
        txtSocialMedia.text= it.socialMediaHandles

    }

    private fun setAdapter() {

       //set adapter
        rvImages.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        horizontalImagesAdapter = HorizontalImagesAdapter(this)
        rvImages.adapter = horizontalImagesAdapter
        horizontalImagesAdapter?.arrayList=arrayList
        horizontalImagesAdapterClick()
    }

    private fun horizontalImagesAdapterClick() {
        horizontalImagesAdapter?.onItemClick = {image: UserMedia ->
            if (image.media.endsWith(".jpg")) {
                riv1.visibility=View.VISIBLE
                videoViewProfile.visibility=View.GONE
                Glide.with(this).load(image.media).into(riv1)
//photo
            } else if (image.media.endsWith(".mp4")) {
//video
                riv1.visibility=View.GONE
                videoViewProfile.visibility=View.VISIBLE
                initializePlayer(image.media)
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
        videoViewProfile.setPlayer(exoplayer)
        exoplayer?.prepare(mediaSource)
        exoplayer?.playWhenReady = true
        //mediaSession?.isActive = true

    }

    override fun onClick(v: View?) {
        when(v?.id){

            R.id.btnSetAvailability ->{
                val intent= Intent(this, AvailabilityActivity::class.java)
                intent.putExtra("user_id",response?.id.toString())
                startActivity(intent)
            }

            R.id.btnEditProfile ->{
                val intent = Intent(this, EditProfileBusinessActivity::class.java)
                startActivity(intent)
            }

            R.id.btnEvent ->{
                val intent= Intent(this,EventInProfileActivity::class.java)
                startActivity(intent)
            }

            R.id.tvWebLink ->{
                openWebPage(web_link)
            }
        }
    }



    private fun openWebPage(url: String) {

        var webpage = Uri.parse(url)
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            webpage = Uri.parse("http://$url")
        }
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    override fun onRestart() {
        super.onRestart()
        getData()
    }
}