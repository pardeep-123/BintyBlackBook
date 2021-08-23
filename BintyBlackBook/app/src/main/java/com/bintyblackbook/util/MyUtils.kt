package com.bintyblackbook.util

import android.app.Activity
import android.os.Build
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import java.text.SimpleDateFormat
import java.util.*

object MyUtils {

    fun fullscreen(context: Activity){
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

     fun getDate(s: String): String? {
        return try {
            val sdf = SimpleDateFormat("MM/dd/yyyy")
            val netDate = Date(s.toLong() * 1000)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }

    fun getTime(s: String):String?{
        return try {
            val sdf = SimpleDateFormat("HH:mm:a")
            val netDate = Date(s.toLong() * 1000)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd")
        return format.format(date)
    }

    fun currentTimeToLong(): Long {
        return System.currentTimeMillis()
    }

    fun convertDateToLong(date: String): Long {
        val df = SimpleDateFormat("yyyy.MM.dd HH:mm")
        return df.parse(date).time
    }

    fun getCurrentDay(s:String) :String {
            val sdf = SimpleDateFormat("EE")
            val netDate = Date(s.toLong() * 1000)
            val dayOfTheWeek = sdf.format(netDate)
            return dayOfTheWeek
//        val sdf = SimpleDateFormat("EEEE")
//        val d = Date()
//        val dayOfTheWeek = sdf.format(d)
    }

    fun getCurrentDate(s:String) :String {
        return try {
            val sdf = SimpleDateFormat("MMM dd")
            val netDate = Date(s.toLong() * 1000)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
//        val sdf = SimpleDateFormat("EEEE")
//        val d = Date()
//        val dayOfTheWeek = sdf.format(d)
    }


}
