package com.bintyblackbook.ui.activities.home.profile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bintyblackbook.R
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.toolbar.*

class MyProfileActivity : AppCompatActivity(), View.OnClickListener {
    val context: Context =this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        iv_back.setOnClickListener(this)
        btnSetAvailability.setOnClickListener(this)
        btnEditProfile.setOnClickListener(this)
        btnEvent.setOnClickListener(this)
        headingText.text = getString(R.string.my_profile)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_back -> {
                finish()
            }
            R.id.btnSetAvailability -> {
                startActivity(Intent(this, AvailabilityActivity::class.java))
            }
            R.id.btnEditProfile -> {
                startActivity(Intent(this, EditProfileActivity::class.java))
            }
            R.id.btnEvent ->{
                startActivity(Intent(this, EventInProfileActivity::class.java))
            }
        }
    }
}