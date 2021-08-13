package com.bintyblackbook.ui.activities.home.profileBusiness

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.bintyblackbook.R
import com.bintyblackbook.model.Data
import com.bintyblackbook.util.ImagePickerUtility
import com.bintyblackbook.util.MyUtils
import com.bintyblackbook.util.getUser
import kotlinx.android.synthetic.main.activity_edit_profile_business.*
import java.io.File

class EditProfileBusinessActivity : ImagePickerUtility() {

    override fun selectedImage(imagePath: File?) {

    }

    override fun selectedVideoUri(videoUri: Uri?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyUtils.fullscreen(this)
        setContentView(R.layout.activity_edit_profile_business)

        setSpinnerBusinessCategory()
        aboutMeTypingTimeScroll()

        clickHandles()

        setUserData(getUser(this))

    }

    private fun setUserData(user: Data?) {

        edtName.setText(user?.businessName)
        edtEmail.setText(user?.email)
        edtMobileNumber.setText(user?.phone)
        edtExperience.setText(user?.experience)
        edtLocation.setText(user?.location)
        spinnerBusinessCategory.setSelection(1)
        edtWebsiteLink.setText(user?.websiteLink)
        edtAboutMe.setText(user?.description)
        riv_video.setImageResource(R.drawable.slider)
        riv_Picture.setImageResource(R.drawable.background)

    }

    private fun clickHandles() {
        rlBack.setOnClickListener {
            finish()
        }

        civ_profile.setOnClickListener {
            getImage(this, 0, false)
        }


        riv_video.setOnClickListener {
            getImage(this, 0, true)
        }

        riv_Picture.setOnClickListener {
            getImage(this, 0, false)
        }

        btnSave.setOnClickListener {
           finish()
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun setSpinnerBusinessCategory() {
        spinnerBusinessCategory.setOnTouchListener { view, motionEvent ->
            MyUtils.hideSoftKeyboard(this)
            false
        }

        val genderSpinnerArray =
            arrayOf("Select", "Business Category", "Business Category", "Business Category")
        val spinnerAdapter =
            ArrayAdapter<String>(this, R.layout.item_spinner, R.id.tvSpinner, genderSpinnerArray)
        spinnerBusinessCategory.adapter = spinnerAdapter

        spinnerBusinessCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val tvSpinner =
                        (parent?.getChildAt(0) as View).findViewById<TextView>(R.id.tvSpinner)
                    tvSpinner.setTextColor(getColor(R.color.whiteColor))

                }

            }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun aboutMeTypingTimeScroll() {
        edtAboutMe.setOnTouchListener { view, motionEvent ->
            if (view.id == R.id.edtAboutMe) {
                view.parent.requestDisallowInterceptTouchEvent(true)
                when (motionEvent.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> view.parent.requestDisallowInterceptTouchEvent(false)
                }
            }

            false
        }
    }
}