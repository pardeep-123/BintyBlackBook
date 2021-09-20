package com.bintyblackbook.models

import com.bintyblackbook.util.MyUtils


data class AvailabilityModel (
    val data: ArrayList<AllDatesModel> = ArrayList<AllDatesModel>(), )



data class AllDatesModel(
    var date: String? = null,
    val slotsArray: ArrayList<SlotsModel> = ArrayList<SlotsModel>(),
)
class SlotsModel{
    var time: String? = null
    var date: String? = null
    var selected: Boolean = false
    var timeTamp: String? = null

    constructor(time:String,date:String,selected:Boolean){
        this.time=time
        this.date=date
        this.selected=selected
        this.timeTamp= MyUtils.convertDateTimeToTestTimeStamp(MyUtils.getDateWithTest(date.toLong()).toString()+" "+time)

    }
}



