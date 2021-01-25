package com.bintyblackbook.ui.activities.home.profileBusiness

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.bintyblackbook.R
import com.bintyblackbook.ui.activities.home.HomeActivity
import com.bintyblackbook.util.ImagePickerUtility
import com.bintyblackbook.util.MyUtils
import kotlinx.android.synthetic.main.activity_edit_profile_business.*
import java.util.*

class EditProfileBusinessActivity : ImagePickerUtility() {

    override fun selectedImage(imagePath: String?) {

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

        edtName.setText(R.string.john)
        edtEmail.setText("john@gmail.com")
        edtMobileNumber.setText("9988776655")
        edtExperience.setText("2 Year")
        edtLocation.setText(R.string.arizona_usa)
        spinnerBusinessCategory.setSelection(1)
        edtWebsiteLink.setText("www.google.com")
        edtAboutMe.setText(R.string.dummy_text)
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