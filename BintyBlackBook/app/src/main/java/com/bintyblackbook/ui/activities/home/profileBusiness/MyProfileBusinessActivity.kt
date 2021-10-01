package com.bintyblackbook.ui.activities.home.profileBusiness

import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_my_profile_business.*
import kotlinx.android.synthetic.main.activity_my_profile_business.rvImages
import kotlinx.android.synthetic.main.activity_my_profile_business.tvBusinessCategory
import kotlinx.android.synthetic.main.activity_my_profile_business.tvSubCategory
import kotlinx.android.synthetic.main.activity_my_profile_business.tvWebLink


class MyProfileBusinessActivity : BaseActivity(), View.OnClickListener {

    lateinit var profileViewModel: ProfileViewModel

    var horizontalImagesAdapter: HorizontalImagesAdapter? = null
    val arrayList = ArrayList<UserMedia>()
    var web_link=""

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
        tvLocation.text=it.location
        tvUserAbout.text=it.description
        tvExp.text=it.experience
        tvWebLink.text=it.websiteLink
        Glide.with(this).load(it.image).into(civ_profile)
        Glide.with(this).load(it.userMedia[0].media).into(riv1)
        if(it.userMedia.size > 0){
            arrayList.clear()
            arrayList.addAll(it.userMedia)
            horizontalImagesAdapter?.notifyDataSetChanged()
        }

        val subCategories= ArrayList<String>()
        val categories= ArrayList<String>()
        it.category.forEach {
            categories.add(it.name)
            it.subCategories.forEach {
                subCategories.add(it.name)
            }
        }
        tvSubCategory.text = TextUtils.join("# ",subCategories)
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
        horizontalImagesAdapter?.onItemClick = {media: UserMedia ->

            Glide.with(context).load(media.media).into(riv1)

        }
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