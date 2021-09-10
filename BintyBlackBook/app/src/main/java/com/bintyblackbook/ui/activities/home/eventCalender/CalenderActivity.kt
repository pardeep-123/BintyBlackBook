package com.bintyblackbook.ui.activities.home.eventCalender

import android.os.Bundle
import android.util.Log
import com.bintyblackbook.R
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.util.MyUtils
import kotlinx.android.synthetic.main.activity_calender.*
import kotlinx.android.synthetic.main.activity_calender.rlBack
import kotlinx.android.synthetic.main.activity_set_availability.*
import java.util.*


class CalenderActivity : BaseActivity() {

    var selected_date=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calender)

        setOnClicks()

        val calendar = Calendar.getInstance()
      //  calendar.set(2021,8,7)

        calendarView.setDate(calendar)

        calendarView.setMinimumDate(calendar)


        calendarView.setOnDayClickListener {
            selected_date = it.calendar.time.toString()

           // val selectedTimeStamp= MyUtils.convertDateToTimeStamp(selected_date)

            //date format Fri Jan 22 00:00:00 GMT+05:30 2021
            Log.i("selectedDate", selected_date)
        }
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