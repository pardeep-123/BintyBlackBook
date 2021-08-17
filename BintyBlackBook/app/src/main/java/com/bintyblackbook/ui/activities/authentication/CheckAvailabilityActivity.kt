package com.bintyblackbook.ui.activities.authentication


import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.adapters.SetAvailabilityAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.models.AvailabilityModel
import kotlinx.android.synthetic.main.fragment_availability.*

class CheckAvailabilityActivity :BaseActivity(){

    var timeSlotsList=ArrayList<AvailabilityModel>()

    var adapter:SetAvailabilityAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_availability)

        setAdapter()
    }

    private fun setAdapter() {

        timeSlotsList.add(AvailabilityModel("12:00 AM",false))
        timeSlotsList.add(AvailabilityModel("1:00 AM", false))
        timeSlotsList.add(AvailabilityModel("2:00 AM",false))
        timeSlotsList.add(AvailabilityModel("3:00 AM",false))
        timeSlotsList.add(AvailabilityModel("4:00 AM",false))
        timeSlotsList.add(AvailabilityModel("5:00 AM",false))
        timeSlotsList.add(AvailabilityModel("6:00 AM",false))
        timeSlotsList.add(AvailabilityModel("7:00 AM",false))
        timeSlotsList.add(AvailabilityModel("8:00 AM",false))
        timeSlotsList.add(AvailabilityModel("9:00 AM",false))
        timeSlotsList.add(AvailabilityModel("10:00 AM",false))
        timeSlotsList.add(AvailabilityModel("11:00 AM",false))
        timeSlotsList.add(AvailabilityModel("12:00 PM",false))
        timeSlotsList.add(AvailabilityModel("1:00 PM",false))
        timeSlotsList.add(AvailabilityModel("2:00 PM",false))
        timeSlotsList.add(AvailabilityModel("3:00 PM",false))
        timeSlotsList.add(AvailabilityModel("4:00 PM",false))
        timeSlotsList.add(AvailabilityModel("5:00 PM",false))
        timeSlotsList.add(AvailabilityModel("6:00 PM",false))
        timeSlotsList.add(AvailabilityModel("7:00 PM",false))
        timeSlotsList.add(AvailabilityModel("8:00 PM",false))
        timeSlotsList.add(AvailabilityModel("9:00 PM",false))
        timeSlotsList.add(AvailabilityModel("10:00 PM",false))
        timeSlotsList.add(AvailabilityModel("11:00 PM",false))

        rvTimeSlots.layoutManager=LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        rvTimeSlots.adapter=SetAvailabilityAdapter(context,timeSlotsList)
    }


}
