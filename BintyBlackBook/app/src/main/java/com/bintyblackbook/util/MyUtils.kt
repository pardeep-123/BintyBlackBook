package com.bintyblackbook.util

import android.app.Activity
import android.os.Build
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

object MyUtils {

    private const val SECOND_MILLIS = 1000
    private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private const val DAY_MILLIS = 24 * HOUR_MILLIS

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

    fun getDateWith(timeStamp: Long): String? {
        return try {
            val sdf = SimpleDateFormat("MM/dd/yyyy")

            val netDate = Date(timeStamp)
//            netDate.timeZone = TimeZone.getDefault()
            sdf.format(netDate)
        } catch (ex: java.lang.Exception) {
            "xx"
        }
    }

    fun getDateWithTest(timeStamp: Long): String? {
        return try {
            val sdf = SimpleDateFormat("MM/dd/yyyy")

            val netDate = Date(timeStamp)
//            netDate.timeZone = TimeZone.getDefault()
            sdf.format(netDate)
        } catch (ex: java.lang.Exception) {
            "xx"
        }
    }


    fun getDateWithTimeStamp(timeStamp: Long): Long? {
        try {
            val sdf = SimpleDateFormat("MM/dd/yyyy")

            val netDate = Date(timeStamp * 1000)
//            netDate.timeZone = TimeZone.getDefault()
            return netDate.time
        } catch (ex: java.lang.Exception) {
            "xx"
        }
        return null
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

    private fun getDate(ourDate: String): String? {
        var ourDate: String? = ourDate
        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            val value = formatter.parse(ourDate)
            val dateFormatter = SimpleDateFormat("MM-dd-yyyy HH:mm") //this format changeable
            dateFormatter.timeZone = TimeZone.getDefault()
            ourDate = dateFormatter.format(value)
            //Log.d("ourDate", ourDate);
        } catch (e: Exception) {
            ourDate = "00-00-0000 00:00"
        }
        return ourDate
    }


    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd")
        return format.format(date)
    }

    fun currentTimeToLong(time: String): String {
        val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a")
        val date = (formatter.parse(time).time / 1000).toString()
        return date
    }

    fun convertDateToLong(dateInString: String): String {


        val spf = SimpleDateFormat("dd/MM/yyyy")
        spf.timeZone = TimeZone.getTimeZone("Etc/UTC")
        val newDate = spf.parse(dateInString)
        Log.e("DATA", "DATe== " + newDate!!.time / 1000)

        return (newDate.time / 1000).toString()
    }

    fun convertTimeToLong(time: String): String {

        val formatter: DateFormat = SimpleDateFormat("hh:mm a")
        val date = (formatter.parse(time).time / 1000).toString()
        return date
    }

    fun convertTimeAndDAteToLong(time: String): String {

        val formatter: DateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm a")
        formatter.timeZone= TimeZone.getTimeZone("UTC")
        val date = formatter.parse(time) as Date

        return date.time.toString()
    }

    fun getDateAndTimeInEpoch(datesToConvert: String?): Long? {
        var date =""
        if(datesToConvert!!.contains(".")){
            date = datesToConvert!!.split(".").get(0)
        }else{
            date = datesToConvert!!
        }
        val spf = SimpleDateFormat("MM/dd/yyyy hh:mm a")
        spf.timeZone=TimeZone.getTimeZone("Etc/UTC")
        val newDate = spf.parse(date)
        Log.e("DATA", "DATe== " + newDate!!.time / 1000)

        return newDate!!.time/1000
    }

    fun getDateInEpoch(datesToConvert: String?): Long? {

        val spf = SimpleDateFormat("MM/dd/yyyy")
        spf.timeZone=TimeZone.getTimeZone("UTC")
        val newDate = spf.parse(datesToConvert)
        Log.e("DATA", "DATe== " + newDate!!.time / 1000)

        return newDate!!.time
    }

    fun convertDateToTimeStamp(date: String): String {
        val formatter: DateFormat = SimpleDateFormat("EE, MMM dd yyyy")
        val date = (formatter.parse(date).time / 1000).toString()
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

    fun getTimeAgo(time: Long): String? {
        var time = time
        if (time < 1000000000000L) {
            time *= 1000
        }
        val now = System.currentTimeMillis()
        if (time > now || time <= 0) {
            return null
        }
        val diff = now - time
        return if (diff < MINUTE_MILLIS) {
            "just now"
        } else if (diff < 2 * MINUTE_MILLIS) {
            "a minute ago"
        } else if (diff < 50 * MINUTE_MILLIS) {
            (diff / MINUTE_MILLIS).toString() + " minutes ago"
        } else if (diff < 90 * MINUTE_MILLIS) {
            "an hour ago"
        } else if (diff < 24 * HOUR_MILLIS) {
            (diff / HOUR_MILLIS).toString() + " hours ago"
        } else if (diff < 48 * HOUR_MILLIS) {
            "yesterday"
        } else {
            (diff / DAY_MILLIS).toString() + " days ago"
        }
    }

    fun getDayDate(timestamp: Long): String {
        return try {
            val sdf = SimpleDateFormat("EE,MMM dd yyyy")
            val netDate = Date(timestamp * 1000L)
            sdf.format(netDate)
        } catch (ex: java.lang.Exception) {
            "xx"
        }
    }

    fun getTimeStampTODate(timestamp: Long): String {
        return try {
            val sdf = SimpleDateFormat("EE,MMM dd yyyy")
            val netDate = Date(timestamp)
            sdf.format(netDate)
        } catch (ex: java.lang.Exception) {
            "xx"
        }
    }

    fun getTimeStampTOTime(timestamp: Long): String {
        return try {
            val sdf = SimpleDateFormat("hh:mm aa")
            val netDate = Date(timestamp * 1000L)
            sdf.format(netDate)
        } catch (ex: java.lang.Exception) {
            "xx"
        }
    }



    fun convertDateToTestTimeStamp(strDate: String):String {
        val sdf = SimpleDateFormat("MM/dd/yyyy")
        sdf.timeZone=TimeZone.getTimeZone("UTC")
        val dateInString = strDate
        val date = sdf.parse(dateInString)
        System.out.println(date)
        return (date.time/1000).toString()
    }
    fun convertDateTimeToTestTimeStamp(strDate: String):String {
        val sdf = SimpleDateFormat("MM/dd/yyyy hh:mm aa")
        sdf.timeZone=TimeZone.getTimeZone("UTC")
        val dateInString = strDate
        val date = sdf.parse(dateInString)
        System.out.println(date)
        return (date.time/1000).toString()
    }

    fun getDateTest(timeStamp: Long): String? {
        return try {
            val sdf = SimpleDateFormat("MM/dd/yyyy")
            val netDate = Date(timeStamp * 1000L)
            sdf.format(netDate)
        } catch (ex: java.lang.Exception) {
            "xx"
        }
    }
    fun getDateTime(timestamp:Long):String{
        return try {
            val sdf = SimpleDateFormat("MMM dd,yyyy hh:mm:a")
            val netDate = Date(timestamp * 1000L)
            sdf.format(netDate)
        } catch (ex: java.lang.Exception) {
            "xx"
        }
    }


    fun getTimeTest(timeStamp: Long): String? {
        return try {
            val sdf = SimpleDateFormat("hh:mm a")
            sdf.timeZone= TimeZone.getTimeZone("GMT")
            val netDate = Date(timeStamp * 1000L)
            sdf.format(netDate)
        } catch (ex: java.lang.Exception) {
            "xx"
        }
    }

}