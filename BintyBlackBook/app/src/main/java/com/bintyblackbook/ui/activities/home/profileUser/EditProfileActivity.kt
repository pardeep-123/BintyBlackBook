package com.bintyblackbook.ui.activities.home.profileUser

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import com.bintyblackbook.R
import com.bintyblackbook.model.Data
import com.bintyblackbook.util.ImagePickerUtility
import com.bintyblackbook.util.getUser
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File

class EditProfileActivity : ImagePickerUtility() {


    override fun selectedImage(imagePath: File?) {

    }

    override fun selectedVideoUri(videoUri: Uri?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        setOnClicks()

        headingText.text = getString(R.string.edit_profile)
        aboutTypingTimeScroll()

        setData(getUser(this))

    }

    private fun setData(user: Data?) {
        edtUserName.setText(user?.firstName)
        edtAbout.setText(user?.description)
        edtEmail.setText(user?.email)
        edtMobileNo.setText(user?.phone)
        if(!user?.image.isNullOrEmpty()){
            Glide.with(this).load(user?.image).into(civ_profile)
        }

    }

    private fun setOnClicks() {
        iv_back.setOnClickListener {
            finish()
        }

        btnSave.setOnClickListener {
            finish()
        }
        civ_profile.setOnClickListener {
            getImage(this,0,false)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun aboutTypingTimeScroll() {
        edtAbout.setOnTouchListener { view, motionEvent ->
            if (view.id == R.id.edtAbout) {
                view.parent.requestDisallowInterceptTouchEvent(true)
                when (motionEvent.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> view.parent.requestDisallowInterceptTouchEvent(false)
                }
            }

            false
        }
    }
}