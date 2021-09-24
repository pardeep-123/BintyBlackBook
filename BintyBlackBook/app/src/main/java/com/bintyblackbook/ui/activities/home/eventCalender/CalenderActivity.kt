package com.bintyblackbook.ui.activities.home.eventCalender

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
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


        calendarView.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                val clickedDayCalendar = eventDay.calendar
                val date = clickedDayCalendar.timeInMillis

                val dateNow = Calendar.getInstance().time
                val dateSelected: Date = clickedDayCalendar.time
                if (!MyUtils.compareDate(dateSelected, dateNow).equals("before")) {
                    val selectedCurrentDate = date.toString()
                    val intent = Intent()
                    intent.putExtra("selectedDate", selectedCurrentDate.toString())
                    setResult(RESULT_OK, intent);
                    finish()

                    Log.i("===",selectedCurrentDate)

                }
            }
        })
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