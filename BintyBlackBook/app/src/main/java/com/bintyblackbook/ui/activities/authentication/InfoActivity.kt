package com.bintyblackbook.ui.activities.authentication

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
import com.bintyblackbook.util.AppConstant
import com.bintyblackbook.util.ImagePickerUtility
import com.bintyblackbook.util.MyUtils
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.android.synthetic.main.activity_info.rbNo
import kotlinx.android.synthetic.main.business_signup_layout.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class InfoActivity : ImagePickerUtility() {

    private val myCalendar = Calendar.getInstance()
    private lateinit var date: DatePickerDialog.OnDateSetListener

    override fun selectedImage(imagePath: File?) {

    }

    override fun selectedVideoUri(videoUri: Uri?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyUtils.fullscreen(this)
        setContentView(R.layout.activity_info)

        setSpinnerBusinessCategory()
        aboutMeTypingTimeScroll()

        headingText.text = getString(R.string.info)
        clickHandles()

        date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateLabel()
        }

    }

    private fun clickHandles() {
        iv_back.setOnClickListener {
            finish()
        }

        civ_profile.setOnClickListener {
            getImage(this,0,false)
        }

        edtSetAvailability.setOnClickListener {
            datePicker()
        }

        riv_video.setOnClickListener {
            getImage(this,0,true)
        }

        riv_Picture.setOnClickListener {
            getImage(this,0,false)
        }

        btnSubmit.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        rgService.setOnCheckedChangeListener { group, checkedId ->

            if(checkedId==R.id.rbNo){
                tvServiceType.visibility=View.GONE
                edtServiceType.visibility=View.GONE
                tvAvailability.visibility=View.GONE
                edtSetAvailability.visibility=View.GONE
            }
            else{
                tvServiceType.visibility=View.VISIBLE
                edtServiceType.visibility=View.VISIBLE
                tvAvailability.visibility=View.VISIBLE
                edtSetAvailability.visibility=View.VISIBLE
            }
        }

        rgSwap.setOnCheckedChangeListener { group, checkedId ->


        }

    }

    private fun datePicker() {
        DatePickerDialog(
            this, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        ).show()
    }

    private fun updateDateLabel() {
        val dateFormat = "dd/MM/yy" //In which you need put here
        val sdf = SimpleDateFormat(dateFormat, Locale.US)
        edtSetAvailability.setText(sdf.format(myCalendar.time))
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