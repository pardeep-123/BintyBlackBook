package com.bintyblackbook.ui.activities.home.profileUser

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.applandeo.materialcalendarview.EventDay
import com.bintyblackbook.R
import com.bintyblackbook.adapters.SetAvailabilityAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.AvailabilityData
import com.bintyblackbook.models.AvailabilityModel
import com.bintyblackbook.util.MyUtils
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.AvailabilityViewModel
import kotlinx.android.synthetic.main.activity_set_availability.*
import kotlin.collections.ArrayList


class SetAvailabilityActivity : BaseActivity() {

    lateinit var arrayList: ArrayList<AvailabilityModel>
    var adapter: SetAvailabilityAdapter?=null
    lateinit var availabilityViewModel: AvailabilityViewModel
    var list_dates= ArrayList<String>()
    var type=""
    var user_id=""
    var dates_list= ArrayList<AvailabilityData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_availability)
        availabilityViewModel= AvailabilityViewModel(this)
        getIntentData()

        setAdapter()

        setOnClicks()

        checkbox_selectAll.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                for(i in 0 until arrayList.size) {
                    arrayList[i].selected = true
                }
                adapter?.notifyDataSetChanged()
            }
            else{
              for(i in 0 until arrayList.size){
                  arrayList[i].selected=false
              }
                adapter?.notifyDataSetChanged()
            }
        }

    }

    private fun getIntentData() {
        type= intent.getStringExtra("type").toString()
        user_id= intent.getStringExtra("user_id").toString()
        if(type.equals("check")){

            checkAvailability()
        }
    }

    private fun checkAvailability() {
        availabilityViewModel.getAvailableSlots(getSecurityKey(context)!!, getUser(context)?.authKey!!,user_id)
        availabilityViewModel.availableSlotsLiveData.observe(this, Observer {
            dates_list.clear()
            dates_list.addAll(it.data)
        })

    }

    private fun setAdapter() {
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
        arrayList.add(AvailabilityModel("09:00 AM", false))
        arrayList.add(AvailabilityModel("10:00 AM", false))
        arrayList.add(AvailabilityModel("11:00 AM", false))
        arrayList.add(AvailabilityModel("12:00 PM", false))
        arrayList.add(AvailabilityModel("01:00 PM", false))
        arrayList.add(AvailabilityModel("02:00 PM", false))
        arrayList.add(AvailabilityModel("03:00 PM", false))
        arrayList.add(AvailabilityModel("04:00 PM", false))
        arrayList.add(AvailabilityModel("05:00 PM", false))
        arrayList.add(AvailabilityModel("06:00 PM", false))
        arrayList.add(AvailabilityModel("07:00 PM", false))
        arrayList.add(AvailabilityModel("08:00 PM", false))
        arrayList.add(AvailabilityModel("09:00 PM", false))
        arrayList.add(AvailabilityModel("10:00 PM", false))
        arrayList.add(AvailabilityModel("11:00 PM", false))

        adapter=SetAvailabilityAdapter(this, arrayList)
        rvTime.adapter = adapter
    }

    private fun setOnClicks() {
        rlBack.setOnClickListener {
            finish()
        }

        btnSubmit.setOnClickListener {
            finish()
        }

        calenderView.setOnDayClickListener {
            val date= it.calendar.time
            list_dates.add(MyUtils.getDate(date.time.toLong())!!)
            Log.i("TAG", list_dates.toString())
        }
    }
}