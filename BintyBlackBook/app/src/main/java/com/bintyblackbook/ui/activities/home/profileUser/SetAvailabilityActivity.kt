package com.bintyblackbook.ui.activities.home.profileUser

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.Observer
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.bintyblackbook.R
import com.bintyblackbook.adapters.SetAvailabilityAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.AllDatesModel
import com.bintyblackbook.model.AvailabilityData
import com.bintyblackbook.model.Slot
import com.bintyblackbook.model.SlotsModel
import com.bintyblackbook.timeslots.TimeSlotsInterface
import com.bintyblackbook.util.MyUtils
import com.bintyblackbook.util.MyUtils.compareDate
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.AvailabilityViewModel
import kotlinx.android.synthetic.main.activity_set_availability.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class SetAvailabilityActivity : BaseActivity() {

    var arrayList = ArrayList<SlotsModel>()
    var allDatesList = ArrayList<AllDatesModel>()
    var adapter: SetAvailabilityAdapter? = null
    lateinit var availabilityViewModel: AvailabilityViewModel
    var type = ""
    var user_id = ""
    var selectedCurrentDate = ""
    var dates_list = ArrayList<AvailabilityData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_availability)
        availabilityViewModel = AvailabilityViewModel()
        getIntentData()

        allDatesList.clear()

        setOnClicks()

    }


    private fun getIntentData() {
        type = intent.getStringExtra("type").toString()
        user_id = intent.getStringExtra("user_id").toString()
        if (type == "check_slots") {
            checkAvailability()
        }
    }

    private fun checkAvailability() {
        availabilityViewModel.getAvailableSlots(this,
            getSecurityKey(context)!!,
            getUser(context)?.authKey!!,
            user_id
        )
        availabilityViewModel.availableSlotsLiveData.observe(this, Observer {
            dates_list.clear()
            dates_list.addAll(it.data)

            // Set Up Default Calender Date
            val calendar = Calendar.getInstance()
            val currentYear = calendar[Calendar.YEAR]
            val currentMonth = calendar[Calendar.MONTH]
            val currentDay = calendar[Calendar.DAY_OF_MONTH]
            calendar.set(currentYear, currentMonth, currentDay)
            selectedCurrentDate = calendar.timeInMillis.toString()
            calenderView.setMinimumDate(calendar)
            calenderView.setDate(calendar)

            // Array List to Show Data in Slots
            allDatesList.clear()
            updateFirstArrayList(selectedCurrentDate)

            // Adding Events on Calender View
            val eventDATA: ArrayList<EventDay> = ArrayList()
            for (m in dates_list) {
                var calender11 = Calendar.getInstance()
                calender11.timeInMillis = m.date!! * 1000L
                val dateNow = Calendar.getInstance().time
                val dateSelected: Date = Date(calender11.timeInMillis)
                if (!compareDate(dateSelected, dateNow).equals("before")) {
                    eventDATA.add(EventDay(calender11, R.drawable.dots_dark))
                }

            }
            calenderView.setEvents(eventDATA)
        })

    }


    /**
     *@Filter Array list to Update The Default Array list with API Array List
     **/
    fun filterArray(): ArrayList<AllDatesModel> {
        var tempArray = allDatesList

        if (dates_list.size != 0) {
            for (m in dates_list) {
                for (n in allDatesList) {
                    // Date conversion Timestamp to String "MM/dd/yyyy"
                    var date1: String =
                        MyUtils.getDateTest(MyUtils.getDateInUTC(m.date!!.toString())!!.toLong())!!
                    var date2: String =
                        MyUtils.getDateWith((MyUtils.getDateInUTC(n.date!!.toString()))!!.toLong())!!
                    if (date1.equals(date2)) {
                        Log.e("DATA", "DATA Filterr=== DATE MATCHED")

                        for (j in m.slots) {
                            for (k in n.slotsArray) {

                                val time1 = MyUtils.getTimeTest(j.slots)
                                val time2 = MyUtils.getTimeTest(k.timeTamp!!.toLong())
                                Log.e(
                                    "DATA",
                                    "DATA Filterr TIMEEEE   === DATE MATCHED" + time1 + "Data=== " + time2
                                )
                                if (j.slots.toString().equals(k.timeTamp!!)) {
                                    k.selected = true
                                    Log.e("DATA", "DATA Filterr=== Slots MATCHED")

                                } else {
//                                        Log.e("DATA", "DATA Filterr=== Slots MATCHED")

                                }
                            }
                        }
                    }
                }
            }
        }




        return allDatesList
    }

    /**
     *@SetAdapter For Slots
     **/
    private fun setAdapter(slotList: ArrayList<SlotsModel>) {
        adapter = SetAvailabilityAdapter(this, slotList, selectedCurrentDate, object :
            TimeSlotsInterface {
            override fun onClick() {
//                checkbox_selectAll.isChecked = checkSelectedAllDate() == false
                // Update Array API and Default Array List when User Select or Un Select Any Slot
                if (dates_list.size != 0) {
                    for (m in dates_list) {
                        var date1 = MyUtils.getDateTest(m.date!!)
                        var date2 = MyUtils.getDateTest(selectedCurrentDate.toLong() / 1000)
                        if (date1.equals(date2)) {

                            for (n in slotList.indices) {
                                val slotsList = slotList[n]
                                val iterator: MutableIterator<Slot> = m.slots.iterator()
                                while (iterator.hasNext()) {
                                    val book = iterator.next()
                                    if (slotsList.timeTamp.equals(book.slots.toString())) {
                                        if (slotsList.selected == false) {
                                            m.slots.remove(book)
                                        }
                                    }
                                    iterator.remove()
                                }


                            }

                        }else{
                            //                checkbox_selectAll.isChecked = checkSelectedAllDate() == false
                            adapter!!.notifyDataSetChanged()
                        }

                    }
                }

            }
        })
        rvTime.adapter = adapter
    }

    private fun setOnClicks() {
        rlBack.setOnClickListener {
            onBackPressed()
        }

        btnSubmitDate.setOnClickListener {

            Log.e("DATAA", "DATA === " + convertTOJson())

            availabilityViewModel.uploadSlots(this,getSecurityKey(context)!!, getUser(context)?.authKey!!, convertTOJson())
        }
        // Check Box Click And Upadate Slots Array list
        checkbox_selectAll.setOnClickListener {
            Log.e("DATA", "DATA == CheckBox===" + checkbox_selectAll.isChecked)
            if (checkbox_selectAll.isChecked) {
                for (i in getSlotsArrayAccordingToDate(filterArray())) {
                    i.selected = true
                }
                adapter?.notifyDataSetChanged()
            } else {
                for (i in getSlotsArrayAccordingToDate(filterArray())) {
                    i.selected = false
                }
                adapter?.notifyDataSetChanged()
            }
        }

        // Calender Date Picker  And Upadate Slots Array and Check Box Also
        calenderView.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                val clickedDayCalendar = eventDay.calendar
                val date = clickedDayCalendar.timeInMillis

                selectedCurrentDate = date.toString()

                val dateNow = Calendar.getInstance().time
                val dateSelected: Date = clickedDayCalendar.time
                if (!compareDate(dateSelected, dateNow).equals("before")) {
                    Log.e("DATA", "DATA=== False")

                    Handler(Looper.getMainLooper()).postDelayed({
                        /* Create an Intent that will start the Menu-Activity. */
                        if (checkDate() == false) {
                            updateArrayList(selectedCurrentDate)
                            checkSelectedAllDate()
                        } else {
                            setAdapter(getSlotsArrayAccordingToDate(filterArray()))
                            checkSelectedAllDate()
                        }
                    }, 400)

                } else {



                }


            }
        })


    }


    /**
     *@Method To get Slots Array From Default Array list
     *@return ArrayList<AllDatesModel>
     **/
    fun getALLSelectedSlots(): ArrayList<AllDatesModel> {
        var tempArray = ArrayList<AllDatesModel>()
        for (m in allDatesList) {
            for (n in m.slotsArray) {
                if (n.selected == true) {
                    tempArray.add(m)
                    break
                }
            }
        }
        return tempArray
    }

    /**
     *@Method To Convert ArrayList<AllDatesModel> To JSON ARRAY For API
     *@return JSONArray
     **/
    fun convertTOJson(): JSONArray {
        var mJsonARRAY = JSONArray()
        var mArrayList = ArrayList<String>()

        for (m in getALLSelectedSlots()) {

            var mJsonObject = JSONObject()
            mJsonObject.put(
                "date",
                MyUtils.convertDateToTestTimeStamp(
                    MyUtils.getDateWithTest(m.date!!.toLong()).toString()
                )
            )
            mArrayList.clear()
            for (n in m.slotsArray) {
                if (n.selected) {
                    mArrayList.add(n.timeTamp!!)
                }

            }
            mJsonObject.put("slots", mArrayList.joinToString(separator = ","))
            mJsonARRAY.put(mJsonObject)
        }

//        }

        return mJsonARRAY
    }

    /**
     *@Method to get Slots Array from Old Date
     * @return ArrayList<SlotsModel>
     **/
    fun getSlotsArrayAccordingToDate(filterArray: ArrayList<AllDatesModel>): ArrayList<SlotsModel> {
        var tempSlotsArray = ArrayList<SlotsModel>()
        for (m in filterArray) {
            var date1 = MyUtils.getDateTest(m.date!!.toLong() / 1000)
            var date2 = MyUtils.getDateTest(selectedCurrentDate.toLong() / 1000)
            if (date2.equals(date1)) {
                tempSlotsArray.addAll(m.slotsArray)
                break
            }

        }
        return tempSlotsArray
    }

    /**
     *@Method to Check MainDates Array
     * Contain Old Date or not
     * @return Boolean
     * @True = Contain Date
     * @False = Not Contain Date or MainArrayList Size 0
     **/
    fun checkDate(): Boolean {
        if (allDatesList.size == 0) {
            return false
        } else {
            for (m in allDatesList) {
                var date1 = MyUtils.getDateTest(m.date!!.toLong() / 1000)
                var date2 = MyUtils.getDateTest(selectedCurrentDate.toLong() / 1000)
                Log.e(
                    "DATA",
                    "DATA === Select on Calender Click Date 1 ==" + date1 + "Date2 ===== " + date2
                )

                if (date1.equals(date2)) {
                    return true
                    break
                }
            }
        }
        return false
    }


    /**
     *@Method to Check MainDates Array
     * Contain All Slots Booked or not
     * @True = Not Contain Date or MainArrayList Size 0
     * @False = All Values True
     **/
    fun checkSelectedAllDate() {
        checkbox_selectAll.isChecked = true
        for (m in allDatesList) {
            Log.e("DATA", "DATA Time ===" + m.date + "Selcted Date === " + selectedCurrentDate)
            var date1 = MyUtils.getDateTest(m.date!!.toLong() / 1000)
            var date2 = MyUtils.getDateTest(selectedCurrentDate.toLong() / 1000)
            Log.e("DATA", "DATA Time After ===" + date1 + "Selcted Date === " + date2)
            if (date1.equals(date2)) {
                for (n in m.slotsArray) {
                    if (n.selected == false) {
                        checkbox_selectAll.isChecked = false
                        break
                    }
                }
            }
        }
    }

    /**
     *@Update SlotsArray When Date Change
     */
    fun updateArrayList(selectedDate: String) {
        var mAllDatesModel = AllDatesModel()
        mAllDatesModel.date = selectedDate
        mAllDatesModel.slotsArray.addAll(getArray(selectedDate.toLong()))
        allDatesList.add(mAllDatesModel)
        for (m in allDatesList) {
            var date1: String =
                MyUtils.getDateTest(MyUtils.getDateInUTC(m.date!!.toString())!!.toLong())!!
            var date2: String =
                MyUtils.getDateTest((MyUtils.getDateInUTC(selectedDate.toString()))!!.toLong())!!
            Log.e(
                "DATA",
                "DATA Time After ===" + date1 + "Afterrr" + selectedDate + "Selcted Date === " + date2 + "  " + m.date!!
            )

            if (date1.equals(date2)) {
                setAdapter(m.slotsArray)
                break
            }
        }
    }

    /**
     *@Update SlotsArray When Date Change
     */
    fun updateFirstArrayList(selectedDate: String) {

        if (dates_list.size != 0) {
            for (m in dates_list) {
                var date1 = MyUtils.getDateTest(m.date!!)
                var date2 = MyUtils.getDateTest(selectedCurrentDate.toLong() / 1000)
                Log.e("DATA", "DATA Time After ===" + date1 + "Selcted Date === " + date2 + "  " + m.date)
                if (date1.equals(date2)) {
                    var mAllDatesModel = AllDatesModel()
                    mAllDatesModel.date = selectedDate
                    mAllDatesModel.slotsArray.addAll(getArray(selectedDate.toLong()))
                    allDatesList.add(mAllDatesModel)
                } else {
                    var mAllDatesModel = AllDatesModel()
                    mAllDatesModel.date = (m.date * 1000L).toString()
                    mAllDatesModel.slotsArray.addAll(getArray(m.date * 1000L))
                    allDatesList.add(mAllDatesModel)
                }
            }

            if (checkDate() == false) {
                var mAllDatesModel = AllDatesModel()
                mAllDatesModel.date = selectedDate
                mAllDatesModel.slotsArray.addAll(getArray(selectedDate.toLong()))
                allDatesList.add(mAllDatesModel)
            }

            for (m in filterArray()) {
                var date1: String =
                    MyUtils.getDateTest(MyUtils.getDateInUTC(m.date!!.toString())!!.toLong())!!
                var date2: String =
                    MyUtils.getDateTest((MyUtils.getDateInUTC(selectedDate.toString()))!!.toLong())!!
                Log.e(
                    "DATA",
                    "DATA Time After ===" + date1 + "Afterrr" + selectedDate + "Selcted Date === " + date2 + "  " + m.date!!
                )

                if (date1.equals(date2)) {
                    setAdapter(m.slotsArray)
                    break
                }
            }
        } else {
            var mAllDatesModel = AllDatesModel()
            mAllDatesModel.date = selectedDate
            mAllDatesModel.slotsArray.addAll(getArray(selectedDate.toLong()))
            allDatesList.add(mAllDatesModel)
            setAdapter(allDatesList[0].slotsArray)
        }
    }

    /*
    * @GetSlots Array According to DATE
    * */
    fun getArray(selectedDate: Long): ArrayList<SlotsModel> {
        arrayList.clear()
        arrayList.add(SlotsModel("12:00 AM", selectedDate.toString(), false))
        arrayList.add(SlotsModel("01:00 AM", selectedDate.toString(), false))
        arrayList.add(SlotsModel("02:00 AM", selectedDate.toString(), false))
        arrayList.add(SlotsModel("03:00 AM", selectedDate.toString(), false))
        arrayList.add(SlotsModel("04:00 AM", selectedDate.toString(), false))
        arrayList.add(SlotsModel("05:00 AM", selectedDate.toString(), false))
        arrayList.add(SlotsModel("06:00 AM", selectedDate.toString(), false))
        arrayList.add(SlotsModel("07:00 AM", selectedDate.toString(), false))
        arrayList.add(SlotsModel("08:00 AM", selectedDate.toString(), false))
        arrayList.add(SlotsModel("09:00 AM", selectedDate.toString(), false))
        arrayList.add(SlotsModel("10:00 AM", selectedDate.toString(), false))
        arrayList.add(SlotsModel("11:00 AM", selectedDate.toString(), false))
        arrayList.add(SlotsModel("12:00 PM", selectedDate.toString(), false))
        arrayList.add(SlotsModel("01:00 PM", selectedDate.toString(), false))
        arrayList.add(SlotsModel("02:00 PM", selectedDate.toString(), false))
        arrayList.add(SlotsModel("03:00 PM", selectedDate.toString(), false))
        arrayList.add(SlotsModel("04:00 PM", selectedDate.toString(), false))
        arrayList.add(SlotsModel("05:00 PM", selectedDate.toString(), false))
        arrayList.add(SlotsModel("06:00 PM", selectedDate.toString(), false))
        arrayList.add(SlotsModel("07:00 PM", selectedDate.toString(), false))
        arrayList.add(SlotsModel("08:00 PM", selectedDate.toString(), false))
        arrayList.add(SlotsModel("09:00 PM", selectedDate.toString(), false))
        arrayList.add(SlotsModel("10:00 PM", selectedDate.toString(), false))
        arrayList.add(SlotsModel("11:00 PM", selectedDate.toString(), false))
        return arrayList
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}

