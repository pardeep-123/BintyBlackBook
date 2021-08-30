package com.bintyblackbook.ui.activities.home.profileUser

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bintyblackbook.R
import com.bintyblackbook.adapters.SetAvailabilityAdapter
import com.bintyblackbook.models.AvailabilityModel
import com.bintyblackbook.util.MyUtils
import kotlinx.android.synthetic.main.activity_set_availability.*
import java.util.*
import kotlin.collections.ArrayList


class SetAvailabilityActivity : AppCompatActivity() {

    lateinit var arrayList: ArrayList<AvailabilityModel>
    var adapter: SetAvailabilityAdapter?=null

    var list_dates= ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_availability)

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
            list_dates.add(MyUtils.getDate(date.toString())!!)
            Log.i("TAG", list_dates.toString())
        }
    }
}