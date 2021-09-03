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

}