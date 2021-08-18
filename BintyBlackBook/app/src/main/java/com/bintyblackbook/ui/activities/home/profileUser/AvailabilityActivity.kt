package com.bintyblackbook.ui.activities.home.profileUser

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.CheckAvailabilityAdapter
import com.bintyblackbook.adapters.HorizontalCalendarAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.AvailabilityData
import com.bintyblackbook.model.Slot
import com.bintyblackbook.models.AvailabilityModel
import com.bintyblackbook.models.HorizontalCalendarModel
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.AvailabilityViewModel
import kotlinx.android.synthetic.main.activity_availability.*
import kotlinx.android.synthetic.main.activity_availability.rlBack
import kotlinx.android.synthetic.main.activity_availability.rlNext
import kotlinx.android.synthetic.main.activity_availability.rlPrevious
import kotlinx.android.synthetic.main.activity_availability.rvTime
import java.util.*
import kotlin.collections.ArrayList

class AvailabilityActivity : BaseActivity() {

    lateinit var availabilityViewModel: AvailabilityViewModel
    private val calendar = Calendar.getInstance()
    private var currentMonth = 0
    var arrayList = ArrayList<Slot>()
    private var itemPos = 0

    var list= ArrayList<AvailabilityData>()
    var checkAvailabilityAdapter:CheckAvailabilityAdapter?=null

    var user_id=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_availability)

        availabilityViewModel= AvailabilityViewModel(this)
        user_id= intent?.getStringExtra("user_id").toString()
        setOnClicks()

        setAdapter()

        getAvailabilityList()

        horizontalCalendarSet()


   /*     arrayList.add(AvailabilityModel("12:00 AM", false))
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
        arrayList.add(AvailabilityModel("11:00 PM", false))*/


    }

    private fun getAvailabilityList() {
        availabilityViewModel.getAvailableSlots(getSecurityKey(context)!!, getUser(context)!!.authKey,user_id)

        availabilityViewModel.availableSlotsLiveData.observe(this, androidx.lifecycle.Observer {
            arrayList.addAll(it.data[0].slots)
            checkAvailabilityAdapter?.notifyDataSetChanged()
        })
    }

    private fun setAdapter() {
        rvTime.adapter = CheckAvailabilityAdapter(this)
        checkAvailabilityAdapter=CheckAvailabilityAdapter(this)
        checkAvailabilityAdapter?.arrayList=arrayList
    }

    private fun setOnClicks() {
        rlBack.setOnClickListener {
            finish()
        }

        rlAdd.setOnClickListener {
            startActivity(Intent(this,SetAvailabilityActivity::class.java))
        }
    }

    private fun horizontalCalendarSet() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvDate.layoutManager = layoutManager

        val arrayList = ArrayList<HorizontalCalendarModel>()
        arrayList.add(HorizontalCalendarModel("Sun", "Jan 01", false))
        arrayList.add(HorizontalCalendarModel("Mon", "Jan 02", false))
        arrayList.add(HorizontalCalendarModel("Tue", "Jan 03", false))
        arrayList.add(HorizontalCalendarModel("Wed", "Jan 04", false))
        arrayList.add(HorizontalCalendarModel("Thu", "Jan 05", false))
        arrayList.add(HorizontalCalendarModel("Fri", "Jan 06", false))
        arrayList.add(HorizontalCalendarModel("Sat", "Jan 07", false))
        arrayList.add(HorizontalCalendarModel("Sun", "Jan 08", false))
        arrayList.add(HorizontalCalendarModel("Mon", "Jan 09", false))
        arrayList.add(HorizontalCalendarModel("Tue", "Jan 10", false))
        arrayList.add(HorizontalCalendarModel("Wed", "Jan 11", false))
        arrayList.add(HorizontalCalendarModel("Thu", "Jan 12", false))
        arrayList.add(HorizontalCalendarModel("Fri", "Jan 13", false))
        arrayList.add(HorizontalCalendarModel("Sat", "Jan 14", false))
        arrayList.add(HorizontalCalendarModel("Sun", "Jan 15", false))
        arrayList.add(HorizontalCalendarModel("Mon", "Jan 16", false))
        arrayList.add(HorizontalCalendarModel("Tue", "Jan 17", false))
        arrayList.add(HorizontalCalendarModel("Wed", "Jan 18", false))
        arrayList.add(HorizontalCalendarModel("Thu", "Jan 19", false))
        arrayList.add(HorizontalCalendarModel("Fri", "Jan 20", false))
        arrayList.add(HorizontalCalendarModel("Sat", "Jan 21", false))

        val horizontalCalendarAdapter = HorizontalCalendarAdapter(this, arrayList)
        rvDate.adapter = horizontalCalendarAdapter

        rvDate.setRecyclerListener {
            itemPos = it.adapterPosition
        }

        rlPrevious.setOnClickListener {
            if (itemPos <= arrayList.size-1) {
                rvDate.smoothScrollToPosition(0)
            }
        }



        rlNext.setOnClickListener {
            if (itemPos >= 0) {
                rvDate.smoothScrollToPosition(arrayList.size-1)
            }
        }

    }

    /*private fun horizontalCalendarSet() {
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
    }*/
}