package com.bintyblackbook.ui.activities.home.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bintyblackbook.R
import com.bintyblackbook.adapters.CheckAvailabilityAdapter
import com.bintyblackbook.models.AvailabilityModel
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import kotlinx.android.synthetic.main.activity_availability.*
import kotlinx.android.synthetic.main.calendar_item.view.*
import java.util.*

class AvailabilityActivity : AppCompatActivity() {

    private val calendar = Calendar.getInstance()
    private var currentMonth = 0
    lateinit var arrayList: ArrayList<AvailabilityModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_availability)

        rlBack.setOnClickListener {
            finish()
        }

        rlAdd.setOnClickListener {
            startActivity(Intent(this,SetAvailabilityActivity::class.java))
        }

        horizontalCalendarSet()

        arrayList = ArrayList()

        arrayList.add(AvailabilityModel("12:00 AM", false))
        arrayList.add(AvailabilityModel("01:00 AM", false))
        arrayList.add(AvailabilityModel("02:00 AM", false))
        arrayList.add(AvailabilityModel("03:00 AM", false))
        arrayList.add(AvailabilityModel("04:00 AM", false))
        arrayList.add(AvailabilityModel("05:00 AM", false))
        arrayList.add(AvailabilityModel("06:00 AM", false))
        arrayList.add(AvailabilityModel("07:00 AM", false))
        arrayList.add(AvailabilityModel("08:00 AM", false))
        arrayList.add(AvailabilityModel("-", false))
        arrayList.add(AvailabilityModel("10:00 AM", false))
        arrayList.add(AvailabilityModel("-", false))
        arrayList.add(AvailabilityModel("12:00 PM", false))
        arrayList.add(AvailabilityModel("01:00 PM", false))
        arrayList.add(AvailabilityModel("02:00 PM", false))
        arrayList.add(AvailabilityModel("03:00 PM", false))
        arrayList.add(AvailabilityModel("-", false))
        arrayList.add(AvailabilityModel("05:00 PM", false))
        arrayList.add(AvailabilityModel("-", false))
        arrayList.add(AvailabilityModel("07:00 PM", false))
        arrayList.add(AvailabilityModel("08:00 PM", false))
        arrayList.add(AvailabilityModel("-", false))
        arrayList.add(AvailabilityModel("10:00 PM", false))
        arrayList.add(AvailabilityModel("11:00 PM", false))

        rvTime.adapter = CheckAvailabilityAdapter(this, arrayList)
    }

    private fun horizontalCalendarSet() {
        // set current date to calendar and current month to currentMonth variable
        calendar.time = Date()
        currentMonth = calendar[Calendar.MONTH]

        val myCalendarViewManager = object : CalendarViewManager {
            override fun setCalendarViewResourceId(
                position: Int,
                date: Date,
                isSelected: Boolean
            ): Int {
                // set date to calendar according to position where we are
                val cal = Calendar.getInstance()
                cal.time = date

                // return item layout files, which you have created
                if (isSelected) {
                    // here we return items which are selected
                    return R.layout.calendar_selected_item
                } else {
                    // here we return items which are not selected
                    return R.layout.calendar_item
                }
            }

            override fun bindDataToCalendarView(
                holder: SingleRowCalendarAdapter.CalendarViewHolder,
                date: Date,
                position: Int,
                isSelected: Boolean
            ) {
                // bind data to calendar item views
                // using this method we can bind data to calendar view
                // good practice is if all views in layout have same IDs in all item views
                holder.itemView.tv_day_calendar_item.text = DateUtils.getDay3LettersName(date)
                holder.itemView.tv_date_calendar_item.text =
                    DateUtils.getMonth3LettersName(date) + " " + DateUtils.getDayNumber(date)

            }
        }

        // selection manager is responsible for managing selection
        val mySelectionManager = object : CalendarSelectionManager {
            override fun canBeItemSelected(position: Int, date: Date): Boolean {
                // set date to calendar according to position
                val cal = Calendar.getInstance()
                cal.time = date

                // return true if item can be selected
                return true
            }
        }

        val myCalendarChangesObserver = object : CalendarChangesObserver {
            override fun whenWeekMonthYearChanged(
                weekNumber: String,
                monthNumber: String,
                monthName: String,
                year: String,
                date: Date
            ) {
                super.whenWeekMonthYearChanged(weekNumber, monthNumber, monthName, year, date)
            }

            override fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {
                super.whenSelectionChanged(isSelected, position, date)
                DateUtils.getMonthName(date)
                DateUtils.getDayNumber(date)
                DateUtils.getDayName(date)

            }

            override fun whenCalendarScrolled(dx: Int, dy: Int) {
                super.whenCalendarScrolled(dx, dy)
            }

            override fun whenSelectionRestored() {
                super.whenSelectionRestored()
            }

            override fun whenSelectionRefreshed() {
                super.whenSelectionRefreshed()
            }
        }

        val singleRowCalendar = main_single_row_calendar.apply {
            calendarViewManager = myCalendarViewManager
            calendarChangesObserver = myCalendarChangesObserver
            calendarSelectionManager = mySelectionManager
            setDates(getFutureDatesOfCurrentMonth())
            init()
        }

        rlPrevious.setOnClickListener {
            singleRowCalendar.setDates(getDatesOfPreviousMonth())
        }

        rlNext.setOnClickListener {
            singleRowCalendar.setDates(getDatesOfNextMonth())
        }
    }

    private fun getDatesOfNextMonth(): List<Date> {
        currentMonth++ // + because we want next month
        if (currentMonth == 12) {
            // we will switch to january of next year, when we reach last month of year
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] + 1)
            currentMonth = 0 // 0 == january
        }
        return getDates(mutableListOf())
    }

    private fun getDatesOfPreviousMonth(): List<Date> {
        currentMonth-- // - because we want previous month
        if (currentMonth == -1) {
            // we will switch to december of previous year, when we reach first month of year
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] - 1)
            currentMonth = 11 // 11 == december
        }
        return getDates(mutableListOf())
    }

    private fun getFutureDatesOfCurrentMonth(): List<Date> {
        // get all next dates of current month
        currentMonth = calendar[Calendar.MONTH]
        return getDates(mutableListOf())
    }

    private fun getDates(list: MutableList<Date>): List<Date> {
        // load dates of whole month
        calendar.set(Calendar.MONTH, currentMonth)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        list.add(calendar.time)
        while (currentMonth == calendar[Calendar.MONTH]) {
            calendar.add(Calendar.DATE, +1)
            if (calendar[Calendar.MONTH] == currentMonth)
                list.add(calendar.time)
        }
        calendar.add(Calendar.DATE, -1)
        return list
    }
}