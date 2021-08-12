package com.bintyblackbook.ui.activities.home

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Bundle
import com.bintyblackbook.R
import com.bintyblackbook.util.AppConstant
import com.bintyblackbook.util.ImagePickerUtility
import kotlinx.android.synthetic.main.activity_add_event.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class AddEventActivity : ImagePickerUtility() {

    private val myCalendar = Calendar.getInstance()
    /*private val mCurrentTime = Calendar.getInstance()*/
    private lateinit var date: OnDateSetListener
    private lateinit var time: TimePickerDialog.OnTimeSetListener

    override fun selectedImage(imagePath: File?) {

    }

    override fun selectedVideoUri(videoUri: Uri?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        val heading = intent.getStringExtra(AppConstant.HEADING)
        if (heading!= null){
            tvHeading.text = heading

            riv.setImageResource(R.drawable.one)
            edtEventName.setText("Colourful Fiesta")
            edtLocation.setText(getString(R.string.arizona_usa))
            edtDate.setText("20/1/2021")
            edtTime.setText("12:00 AM")
            edtDesc.setText(getString(R.string.dummy_text))
            edtMoreInfo.setText(getString(R.string.dummy_text))
        }

        rlBack.setOnClickListener {
            finish()
        }


        date = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateLabel()
        }

        time = TimePickerDialog.OnTimeSetListener { view, hour, minute ->
            myCalendar.set(Calendar.HOUR_OF_DAY, hour)
            myCalendar.set(Calendar.MINUTE, minute)
            updateTimeLabel()
        }

        riv.setOnClickListener {
            getImage(this,0,false)
        }

        edtDate.setOnClickListener {
            datePicker()
        }

        edtTime.setOnClickListener {
            timePicker()
        }

        btnSave.setOnClickListener {
            finish()
        }
    }

    private fun timePicker() {
        TimePickerDialog(
            this,
            time,
            myCalendar[Calendar.HOUR_OF_DAY],
            myCalendar[Calendar.MINUTE],
            false
        ).show()
    }

    private fun datePicker() {
        DatePickerDialog(
            this@AddEventActivity, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        ).show()
    }

    private fun updateDateLabel() {
        val dateFormat = "dd/MM/yy" //In which you need put here
        val sdf = SimpleDateFormat(dateFormat, Locale.US)
        edtDate.setText(sdf.format(myCalendar.time))
    }

    private fun updateTimeLabel() {
        val timeFormat = "hh:mm a" //In which you need put here
        val sdf = SimpleDateFormat(timeFormat, Locale.US)
        edtTime.setText(sdf.format(myCalendar.time))
    }
}