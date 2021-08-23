package com.bintyblackbook.ui.activities.home

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.CheckAvailabilityAdapter
import com.bintyblackbook.adapters.HorizontalCalendarAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.AvailabilityData
import com.bintyblackbook.model.Slot
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.AvailabilityViewModel
import kotlinx.android.synthetic.main.activity_check_availability.rlBack
import kotlinx.android.synthetic.main.activity_check_availability.rlNext
import kotlinx.android.synthetic.main.activity_check_availability.rlPrevious
import kotlinx.android.synthetic.main.activity_check_availability.rvDate
import kotlinx.android.synthetic.main.activity_check_availability.rvTime
import java.util.*
import kotlin.collections.ArrayList


class CheckAvailabilityActivity : BaseActivity(), HorizontalCalendarAdapter.CalenderInterface {

    private var itemPos = 0
    var checkAvailabilityAdapter:CheckAvailabilityAdapter?=null
    var horizontalCalendarAdapter: HorizontalCalendarAdapter?=null
    lateinit var availabilityViewModel: AvailabilityViewModel
    var user_id=""
    var arrayList = ArrayList<AvailabilityData>()
    var timeList= ArrayList<Slot>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_availability)

        availabilityViewModel= AvailabilityViewModel(this)

        user_id= intent.getStringExtra("user_id").toString()

        rlBack.setOnClickListener {
            finish()
        }
        horizontalCalendarSet()
        setAdapter()

        getAvailability()

    }

    private fun getAvailability() {

        availabilityViewModel.getAvailableSlots(getSecurityKey(context)!!, getUser(context)?.authKey!!,user_id)
        availabilityViewModel.availableSlotsLiveData.observe(this, androidx.lifecycle.Observer {

            arrayList.clear()
            arrayList.addAll(it?.data!!)
            horizontalCalendarAdapter?.notifyDataSetChanged()

        })
    }

    //set adapter for time slots
    private fun setAdapter() {
        checkAvailabilityAdapter=CheckAvailabilityAdapter(this)
        rvTime.adapter =checkAvailabilityAdapter

        checkAvailabilityAdapter?.arrayList=timeList
    }

    private fun horizontalCalendarSet() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvDate.layoutManager = layoutManager

        horizontalCalendarAdapter = HorizontalCalendarAdapter(this)
        rvDate.adapter = horizontalCalendarAdapter
        horizontalCalendarAdapter?.calenderInterface=this
        horizontalCalendarAdapter?.arrayList=arrayList

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

    override fun onItemClick(data: AvailabilityData, position: Int) {
        timeList.clear()
        timeList.addAll(data.slots)
        checkAvailabilityAdapter?.notifyDataSetChanged()
    }

    /* private fun horizontalCalendarSet() {
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