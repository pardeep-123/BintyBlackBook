package com.bintyblackbook.ui.activities.home.profileBusiness

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
import com.bintyblackbook.ui.activities.home.profileUser.AvailabilityActivity
import com.bintyblackbook.ui.activities.home.profileUser.EventInProfileActivity
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.ProfileViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_my_profile_business.*

class MyProfileBusinessActivity : BaseActivity(), View.OnClickListener {

    lateinit var profileViewModel: ProfileViewModel

    var horizontalImagesAdapter: HorizontalImagesAdapter? = null
    val arrayList = ArrayList<UserMedia>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile_business)

        profileViewModel= ProfileViewModel(this)

        getData()

        rlBack.setOnClickListener {
            onBackPressed()
        }

        setAdapter()
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
    }

    private fun setObservables() {

        profileViewModel.profileObservable.observe(this, Observer {
            if (it.code==200){
                setData(it.data)
            }
        })
    }

    private fun setData(it: Data?) {
        tvName.text= it?.firstName
        tvLocation.text=it?.location
        tvUserAbout.text=it?.description
        tvExp.text=it?.experience
        tvWebLink.text=it?.websiteLink

        Glide.with(this).load(it?.image).into(riv1)
        if(it?.userMedia?.size!! > 0){
            arrayList.addAll(it.userMedia)
            horizontalImagesAdapter?.notifyDataSetChanged()
        }

    }

    private fun setAdapter() {

//        arrayList.add(R.drawable.slider)
//        arrayList.add(R.drawable.small2)

       //set adapter
        rvImages.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        horizontalImagesAdapter = HorizontalImagesAdapter(this)
        rvImages.adapter = horizontalImagesAdapter
        horizontalImagesAdapter?.arrayList=arrayList
        horizontalImagesAdapterClick()
    }

    private fun horizontalImagesAdapterClick() {
        horizontalImagesAdapter?.onItemClick = {image: String ->
            //riv1.setImageResource(image)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){

            R.id.btnSetAvailability ->{
                startActivity(Intent(this, AvailabilityActivity::class.java))
            }

            R.id.btnEditProfile ->{
                val intent = Intent(this, EditProfileBusinessActivity::class.java)
                startActivity(intent)
            }

            R.id.btnEvent ->{
                startActivity(Intent(this, EventInProfileActivity::class.java))
            }
        }
    }
}