package com.bintyblackbook.util

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.makeramen.roundedimageview.RoundedImageView
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object MyUtils {

    private const val SECOND_MILLIS = 1000
    private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private const val DAY_MILLIS = 24 * HOUR_MILLIS
    private const val WEEK_MILLIS = 7 * DAY_MILLIS
    private const val MONTH_MILLIS = 4 * WEEK_MILLIS.toLong()
    private const val YEAR_MILLIS = 12 * MONTH_MILLIS

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

    fun getTimeInterval(oldDate:Date, newDate:Date):Long{
        val dateIntevalInMilis  = newDate.time - oldDate.time
        return dateIntevalInMilis/1000
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



    fun getCustomdate():Date{
        val string = "Jan 01,2021"
        val formatter =SimpleDateFormat("MMM dd,yyyy")

        val date = formatter.parse(string)
        return date
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



    fun getCurrentDay(s: Long): String {
        val sdf = SimpleDateFormat("EE")
        val netDate = Date(s.toLong() * 1000)
        val dayOfTheWeek = sdf.format(netDate)
        return dayOfTheWeek
    }

    fun getCurrentDate(timeStamp: Long): String? {
        return try {
            val sdf = SimpleDateFormat("MMM dd")
            val netDate = Date(timeStamp/1000)
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
        var dateFormat = "hh:mm a"
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

    fun getDateFromUTCTimestamp(mTimestamp: Long, mDateFormate: String?): String? {
        var date: String? = null
        try {
            val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            cal.timeInMillis = mTimestamp * 1000L
            date = android.text.format.DateFormat.format(mDateFormate, cal.timeInMillis).toString()
            val formatter = SimpleDateFormat(mDateFormate)
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            val value = formatter.parse(date)
            val dateFormatter = SimpleDateFormat(mDateFormate)
            dateFormatter.timeZone = TimeZone.getDefault()
            date = dateFormatter.format(value)
            return date
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return date
    }

    fun getTimeAgo(timeMillis: Long): String? {
        var time = timeMillis
        if (time < 1000000000000L) {
            time *= 1000
        }

        val now = System.currentTimeMillis()

        val diff = now - time
        when {
            diff < 2 * SECOND_MILLIS -> {
                return "Just now"
            }
            diff < 60 * SECOND_MILLIS -> {
                return (diff / SECOND_MILLIS).toString() + " sec ago"
            }
            diff < 2 * MINUTE_MILLIS -> {
                return "1 min ago"
            }
            diff < 50 * MINUTE_MILLIS -> {
                return (diff / MINUTE_MILLIS).toString() + " min ago"
            }
            diff < 90 * MINUTE_MILLIS -> {
                return "an hour ago"
            }
            diff < 24 * HOUR_MILLIS -> {
                return (diff / HOUR_MILLIS).toString() + " hours ago"
            }
            diff < 48 * HOUR_MILLIS -> {
                return "1 day ago"
            }
            diff < 7 * DAY_MILLIS -> {
                return (diff / DAY_MILLIS).toString() + " days ago";
            }
            diff < 2 * WEEK_MILLIS.toLong() -> {
                return "1 wk ago"
            }
            diff < 4 * WEEK_MILLIS.toLong() -> {
                return (diff / WEEK_MILLIS.toLong()).toString() + " week ago"
            }
            diff < 2 * MONTH_MILLIS -> {
                return "1 mo ago"
            }
            diff < 12 * MONTH_MILLIS -> {
                return (diff / MONTH_MILLIS).toString() + " month ago"
            }
            diff < 2 * YEAR_MILLIS -> {
                return "a year ago"
            }
            else -> {
                return (diff / YEAR_MILLIS).toString() + " years ago"
            }
        }
    }

    fun getDayDate(timestamp: Long): String {
        return try {
            val sdf = SimpleDateFormat("EEE,MMM dd yyyy")
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
    fun getDateTime(timestamp: Long):String{
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

    fun compareDate(date1: Date, date2: Date): String {

        val cal1 = Calendar.getInstance()
        val cal2 = Calendar.getInstance()
        cal1.time = date1
        cal2.time = date2
        val sameDay = cal1[Calendar.DAY_OF_YEAR] === cal2[Calendar.DAY_OF_YEAR] &&
                cal1[Calendar.YEAR] === cal2[Calendar.YEAR]
        if(sameDay==true){
            Log.e("Date", "Date1 is equal to Date2")
            return "equal"
        }else{
            if (date1.after(date2)) {
                Log.e("Date", "Date1 is after Date2")
                return "after"
            } else if (date1.before(date2)) {
                Log.e("Date", "Date1 is before Date2")
                return "before"
            }
        }
        return ""
    }

    fun loadImageWithProgress(
        context: Context,
        url: String,
        imageView: RoundedImageView,
        progressBar: ProgressBar
    ) {
        Glide.with(context)
            .load(url)
            .listener(object : RequestListener<Drawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility = View.GONE
                    return false
                }

            }).into(imageView)
    }


}