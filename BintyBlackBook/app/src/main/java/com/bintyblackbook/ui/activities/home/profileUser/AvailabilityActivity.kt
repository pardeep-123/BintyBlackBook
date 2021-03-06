package com.bintyblackbook.ui.activities.home.profileUser

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.applandeo.materialcalendarview.EventDay
import com.bintyblackbook.R
import com.bintyblackbook.adapters.CheckAvailabilityAdapter
import com.bintyblackbook.adapters.HorizontalCalendarAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.AvailabilityData
import com.bintyblackbook.model.Slot
import com.bintyblackbook.timeslots.TimeSlotsInterface
import com.bintyblackbook.util.MyUtils
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.AvailabilityViewModel
import kotlinx.android.synthetic.main.activity_availability.*
import kotlinx.android.synthetic.main.activity_availability.rlBack
import kotlinx.android.synthetic.main.activity_availability.rlNext
import kotlinx.android.synthetic.main.activity_availability.rlPrevious
import kotlinx.android.synthetic.main.activity_availability.rvDate
import kotlinx.android.synthetic.main.activity_availability.rvTime
import java.util.*
import kotlin.collections.ArrayList

class AvailabilityActivity : BaseActivity(), HorizontalCalendarAdapter.CalenderInterface {

    lateinit var availabilityViewModel: AvailabilityViewModel
    var arrayList = ArrayList<AvailabilityData>()
    private var itemPos = 0
    var timeList= ArrayList<Slot>()
    var list= ArrayList<AvailabilityData>()
    var checkAvailabilityAdapter:CheckAvailabilityAdapter?=null
    var horizontalCalendarAdapter: HorizontalCalendarAdapter?=null

    var user_id=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_availability)

        availabilityViewModel= AvailabilityViewModel()
        user_id= intent?.getStringExtra("user_id").toString()
        setOnClicks()

        horizontalCalendarSet()
        setAdapter()

        getAvailabilityList()

    }

    private fun getAvailabilityList() {
        availabilityViewModel.getAvailableSlots(this,getSecurityKey(context)!!, getUser(context)!!.authKey,user_id)

        availabilityViewModel.availableSlotsLiveData.observe(this, androidx.lifecycle.Observer {
            arrayList.clear()
            arrayList.addAll(it.data)
            /**
             * add current date and future date
             */

           /* for(m in it.data){
                val calender11 = Calendar.getInstance()
                calender11.timeInMillis = m.date!! * 1000L
                val dateNow = Calendar.getInstance().time
                val dateSelected: Date = Date(calender11.timeInMillis)
                if (!MyUtils.compareDate(dateSelected, dateNow).equals("before")) {
                    arrayList.add(m)
                }
            }*/

            horizontalCalendarAdapter?.notifyDataSetChanged()
        })
    }

    private fun setAdapter() {
        checkAvailabilityAdapter=CheckAvailabilityAdapter(this,object :TimeSlotsInterface{
            override fun onClick() {

            }

        })
        rvTime.adapter =checkAvailabilityAdapter

        checkAvailabilityAdapter?.arrayList=timeList
    }

    private fun setOnClicks() {
        rlBack.setOnClickListener {
            finish()
        }

        rlAdd.setOnClickListener {
            val intent= Intent(this,SetAvailabilityActivity::class.java)
            intent.putExtra("user_id",user_id)
            intent.putExtra("type","check_slots")
            startActivity(intent)
            finish()
        }
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