package com.bintyblackbook.util

import android.app.Activity
import android.os.Build
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object MyUtils {

    fun fullscreen(context: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context.window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            context.window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    //--------------------------Keyboard Hide----------------------//
    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(
            Activity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            activity.currentFocus?.windowToken, 0
        )
    }

    fun getDate(timeStamp: Long): String? {
        return try {
            val sdf = SimpleDateFormat("MM/dd/yyyy")
            val netDate = Date(timeStamp * 1000L)
            sdf.format(netDate)
        } catch (ex: java.lang.Exception) {
            "xx"
        }
    }

    fun getTime(timeStamp: Long): String? {
        return try {
            val sdf = SimpleDateFormat("hh:mm a")
            val netDate = Date(timeStamp * 1000L)
            sdf.format(netDate)
        } catch (ex: java.lang.Exception) {
            "xx"
        }
    }


    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd")
        return format.format(date)
    }

    fun currentTimeToLong(time:String): String {
        val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a")
        val date = (formatter.parse(time).time /1000).toString()
        return date
    }

    fun convertDateToLong(date: String): String {

        val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date = (formatter.parse(date).time /1000).toString()
        return date
    }

    fun getCurrentDay(s: Long): String {
        val sdf = SimpleDateFormat("EE")
        val netDate = Date(s.toLong() * 1000)
        val dayOfTheWeek = sdf.format(netDate)
        return dayOfTheWeek
    }

    fun getCurrentDate(timeStamp: Long): String? {
        return try {
            val sdf = SimpleDateFormat("MMM dd")
            val netDate = Date(timeStamp * 1000L)
            sdf.format(netDate)
        } catch (ex: java.lang.Exception) {
            "xx"
        }
    }

    fun getDateInUTC(datesToConvert: String?): String? {
        var dateFormat = "MM/dd/yyyy hh:mm a"
        var dateToReturn = datesToConvert
        val sdf = SimpleDateFormat(dateFormat)
        sdf.timeZone = TimeZone.getDefault()
        var gmt: Date? = null
        val sdfOutPutToSend = SimpleDateFormat(dateFormat)
        sdfOutPutToSend.timeZone = TimeZone.getTimeZone("UTC")
        try {
            gmt = sdf.parse(datesToConvert)
            dateToReturn = (gmt!!.time / 1000).toString()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return dateToReturn
    }

    fun getTimeInUTC(timeToConvert: String?): String? {
        var dateFormat = "hh:mm aa"
        var dateToReturn = timeToConvert
        val sdf = SimpleDateFormat(dateFormat)
        sdf.timeZone = TimeZone.getDefault()
        var gmt: Date? = null
        val sdfOutPutToSend = SimpleDateFormat(dateFormat)
        sdfOutPutToSend.timeZone = TimeZone.getTimeZone("UTC")
        try {
            gmt = sdf.parse(timeToConvert)
            dateToReturn = (gmt!!.time / 1000).toString()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return dateToReturn
    }


    fun convertTimeStempToDate(timestamp: Long): String? {
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = timestamp
        val outputFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm:a")
        return outputFormat.format(cal.time)
    }

}
