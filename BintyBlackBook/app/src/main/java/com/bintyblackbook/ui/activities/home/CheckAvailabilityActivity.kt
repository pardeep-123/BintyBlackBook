package com.bintyblackbook.ui.activities.home

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.CheckAvailabilityAdapter
import com.bintyblackbook.adapters.HorizontalCalendarAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.AvailabilityData
import com.bintyblackbook.model.Slot
import com.bintyblackbook.timeslots.TimeSlotsInterface
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.AvailabilityViewModel
import com.bintyblackbook.viewmodel.BookingsViewModel
import kotlinx.android.synthetic.main.activity_check_availability.*
import kotlin.collections.ArrayList


class CheckAvailabilityActivity : BaseActivity(), HorizontalCalendarAdapter.CalenderInterface {

    private var itemPos = 0
    var checkAvailabilityAdapter: CheckAvailabilityAdapter? = null
    var horizontalCalendarAdapter: HorizontalCalendarAdapter? = null
    lateinit var availabilityViewModel: AvailabilityViewModel
    lateinit var bookingsViewModel: BookingsViewModel
    var user_id = ""
    var arrayList = ArrayList<AvailabilityData>()
    var timeList = ArrayList<Slot>()
    var screen_type = ""
    var selectedSlots=ArrayList<String>()
    var availabilityId=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_availability)
        initViewModel()

        getIntentData()
        setOnClicks()

        horizontalCalendarSet()
        setAdapter()

        getAvailability()

    }

    private fun initViewModel() {
        availabilityViewModel = AvailabilityViewModel()
        bookingsViewModel = BookingsViewModel()
    }

    private fun setOnClicks() {

        rlBack.setOnClickListener {
            finish()
        }

        btnSubmitBooking.setOnClickListener {

            val bookingSlots=TextUtils.join(",",selectedSlots)
            bookingsViewModel.addBooking(this, getSecurityKey(this)!!, getUser(this)?.authKey.toString(),
                user_id, availabilityId, bookingSlots)
            bookingsViewModel.bookingsLiveData.observe(this, Observer {
                Toast.makeText(this,it?.msg,Toast.LENGTH_SHORT).show()
                finish()
            })
        }
    }

    private fun getIntentData() {
        user_id = intent.getStringExtra("user_id").toString()
        screen_type = intent.getStringExtra("screen_type").toString()

        if ("profile" == screen_type) {
            btnSubmitBooking.visibility = View.GONE
        } else {
            btnSubmitBooking.visibility = View.VISIBLE
        }
    }

    private fun getAvailability() {

        availabilityViewModel.getAvailableSlots(this, getSecurityKey(context)!!, getUser(context)?.authKey!!, user_id)
        availabilityViewModel.availableSlotsLiveData.observe(this, androidx.lifecycle.Observer {

            if(it.data.size==0){
                tvNoSlots.visibility=View.VISIBLE
                rlCalendar.visibility=View.GONE
                rvTime.visibility=View.GONE
                btnSubmitBooking.visibility=View.GONE
            }else{
                tvNoSlots.visibility=View.GONE
                rlCalendar.visibility=View.VISIBLE
                rvTime.visibility=View.VISIBLE
                if ("profile" == screen_type) {
                    btnSubmitBooking.visibility = View.GONE
                } else {
                    btnSubmitBooking.visibility = View.VISIBLE
                }
                arrayList.clear()
                arrayList.addAll(it?.data!!)
                horizontalCalendarAdapter?.notifyDataSetChanged()
            }

        })
    }

    //set adapter for time slots
    private fun setAdapter() {
        checkAvailabilityAdapter = CheckAvailabilityAdapter(this, object : TimeSlotsInterface {
            override fun onClick() {
                selectedSlots.clear()
                for (i in timeList){
                    if(i.isSelected==true){
                        selectedSlots.add(i.slots.toString())
                    }
                }
                Log.i("selectedList",selectedSlots.toString())
            }

        })
        rvTime.adapter = checkAvailabilityAdapter
        checkAvailabilityAdapter?.arrayList = timeList
    }

    private fun horizontalCalendarSet() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvDate.layoutManager = layoutManager

        horizontalCalendarAdapter = HorizontalCalendarAdapter(this)
        rvDate.adapter = horizontalCalendarAdapter
        horizontalCalendarAdapter?.calenderInterface = this
        horizontalCalendarAdapter?.arrayList = arrayList

        rvDate.setRecyclerListener {
            itemPos = it.adapterPosition
        }

        rlPrevious.setOnClickListener {
            if (itemPos <= arrayList.size - 1) {
                rvDate.smoothScrollToPosition(0)
            }
        }

        rlNext.setOnClickListener {
            if (itemPos >= 0) {
                rvDate.smoothScrollToPosition(arrayList.size - 1)
            }
        }
    }

    override fun onItemClick(data: AvailabilityData, position: Int) {
        availabilityId= data.slots[0].availabilityId.toString()
        timeList.clear()
        timeList.addAll(data.slots)
        checkAvailabilityAdapter?.notifyDataSetChanged()
    }

}