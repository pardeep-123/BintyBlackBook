package com.bintyblackbook.ui.activities.home.profileUser

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.bintyblackbook.R
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.Data
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.ProfileViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.toolbar.*

class MyProfileActivity : BaseActivity(), View.OnClickListener {

    lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        profileViewModel= ProfileViewModel(this)

        getProfileData()

        setOnClicks()

        headingText.text = getString(R.string.my_profile)
    }

    private fun getProfileData() {
        profileViewModel.userProfile(getSecurityKey(this)!!, getUser(this)?.authKey!!, getUser(this)!!.id.toString())
        setObservables()
    }

    private fun setObservables() {
            profileViewModel.profileObservable.observe(this, Observer {
                if(it.code==200){
                    setData(it.data)
                }
            })
    }

    private fun setData(it: Data?) {
        tvUserName.text=it?.firstName
        tvEmail.text=it?.email
        tvPhoneNumber.text= it?.countryCode +" " +it?.phone
        tvAbout.text =it?.description
        runOnUiThread {
            Glide.with(this).load(it?.image).into(ivUserProfile)
        }

    }

    private fun setOnClicks() {
        iv_back.setOnClickListener(this)
        btnEditProfile.setOnClickListener(this)
        btnEvent.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_back -> {
                finish()
            }

            R.id.btnEditProfile -> {
                val intent= Intent(this,EditProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.btnEvent ->{
                startActivity(Intent(this, EventInProfileActivity::class.java))
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        getProfileData()
    }
}