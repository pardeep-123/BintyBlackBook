package com.bintyblackbook.ui.activities.authentication

import android.os.Bundle
import android.util.Log
import com.bintyblackbook.R
import com.bintyblackbook.adapters.SetAvailabilityAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.models.AvailabilityModel
import kotlinx.android.synthetic.main.activity_set_availability.rvTime
import kotlinx.android.synthetic.main.activity_set_user_availability.*
import kotlinx.android.synthetic.main.fragment_availability.checkbox_select
import java.util.*
import kotlin.collections.ArrayList

class SetUserAvailabilityActivity :BaseActivity(){

    lateinit var timeSlotsList: ArrayList<AvailabilityModel>

    var adapter:SetAvailabilityAdapter?=null
    var selected_date=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_user_availability)

        rl_back.setOnClickListener {
            finish()
        }
        setAdapter()
        checkbox_select.setOnCheckedChangeListener { buttonView, isChecked ->

           /* if(isChecked){
                for(i in 0 until timeSlotsList.size) {
                    timeSlotsList[i].selected = true
                }
                adapter?.notifyDataSetChanged()
            }
            else{
                for(i in 0 until timeSlotsList.size){
                    timeSlotsList[i].selected=false
                }
                adapter?.notifyDataSetChanged()
            }*/
        }

        calenderView_user.setOnDayClickListener {
            val clickedDayCalendar: Calendar = it.calendar
            selected_date= clickedDayCalendar.time.toString()

            Log.i("selectedDate",selected_date)
        }

        setOnClicks()
    }

    private fun setOnClicks() {
        btnSubmit.setOnClickListener {
        }
    }

    private fun setAdapter() {

        timeSlotsList= ArrayList()
       /* timeSlotsList.add(AvailabilityModel("12:00 AM", false))
        timeSlotsList.add(AvailabilityModel("1:00 AM", false))
        timeSlotsList.add(AvailabilityModel("2:00 AM", false))
        timeSlotsList.add(AvailabilityModel("3:00 AM", false))
        timeSlotsList.add(AvailabilityModel("4:00 AM", false))
        timeSlotsList.add(AvailabilityModel("5:00 AM", false))
        timeSlotsList.add(AvailabilityModel("6:00 AM", false))
        timeSlotsList.add(AvailabilityModel("7:00 AM", false))
        timeSlotsList.add(AvailabilityModel("8:00 AM", false))
        timeSlotsList.add(AvailabilityModel("9:00 AM", false))
        timeSlotsList.add(AvailabilityModel("10:00 AM", false))
        timeSlotsList.add(AvailabilityModel("11:00 AM", false))
        timeSlotsList.add(AvailabilityModel("12:00 PM", false))
        timeSlotsList.add(AvailabilityModel("1:00 PM", false))
        timeSlotsList.add(AvailabilityModel("2:00 PM", false))
        timeSlotsList.add(AvailabilityModel("3:00 PM", false))
        timeSlotsList.add(AvailabilityModel("4:00 PM", false))
        timeSlotsList.add(AvailabilityModel("5:00 PM", false))
        timeSlotsList.add(AvailabilityModel("6:00 PM", false))
        timeSlotsList.add(AvailabilityModel("7:00 PM", false))
        timeSlotsList.add(AvailabilityModel("8:00 PM", false))
        timeSlotsList.add(AvailabilityModel("9:00 PM", false))
        timeSlotsList.add(AvailabilityModel("10:00 PM", false))
        timeSlotsList.add(AvailabilityModel("11:00 PM", false))

        adapter=SetAvailabilityAdapter(this, timeSlotsList)*/
        rvTime.adapter = adapter
    }
}
