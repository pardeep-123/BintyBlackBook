package com.bintyblackbook.ui.activities.home.eventCalender

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.applandeo.materialcalendarview.EventDay
import com.bintyblackbook.R
import com.bintyblackbook.base.BaseActivity
import kotlinx.android.synthetic.main.activity_calender.*
import kotlinx.android.synthetic.main.activity_calender.btnSubmit
import kotlinx.android.synthetic.main.activity_set_user_availability.*
import java.util.*


class CalenderActivity : BaseActivity() {

    var selected_date=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calender)

        setOnClicks()

        calendarView.setOnDayClickListener {
            val clickedDayCalendar: Calendar = it.calendar
            selected_date= clickedDayCalendar.time.toString()
            //date format Fri Jan 22 00:00:00 GMT+05:30 2021
            Log.i("selectedDate",selected_date)
        }


        val a = Calendar.getInstance()

    }

    private fun setOnClicks() {
        rlBack.setOnClickListener {
            finish()
        }

        btnSubmit.setOnClickListener {
            finish()
        }

    }
}