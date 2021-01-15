package com.bintyblackbook.ui.activities.authentication

import android.annotation.SuppressLint
import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.android.synthetic.main.toolbar.*

class InfoActivity : ImagePickerUtility() {

    override fun selectedImage(imagePath: String?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyUtils.fullscreen(this)
        setContentView(R.layout.activity_info)

        setSpinnerBusinessCategory()
        aboutMeTypingTimeScroll()

        headingText.text = getString(R.string.info)

        clickHandles()

    }

    private fun clickHandles() {
        iv_back.setOnClickListener {
            finish()
        }

        riv_Picture.setOnClickListener {
            getImage(this,0)
        }

        btnSubmit.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
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