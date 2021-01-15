package com.bintyblackbook.ui.activities.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bintyblackbook.R
import com.bintyblackbook.ui.activities.authentication.LoginActivity
import com.bintyblackbook.ui.activities.home.bookings.MyBookingsActivity
import com.bintyblackbook.ui.activities.home.eventCalender.EventCalenderActivity
import com.bintyblackbook.ui.activities.home.loop.MyLoopsActivity
import com.bintyblackbook.ui.activities.home.message.MessagesActivity
import com.bintyblackbook.ui.activities.home.profile.MyProfileActivity
import com.bintyblackbook.ui.activities.home.promote.PromoteBusinessActivity
import com.bintyblackbook.ui.activities.home.settings.SettingsActivity
import com.bintyblackbook.ui.activities.home.timeline.TimelineActivity
import com.bintyblackbook.ui.fragments.HomeFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.drawerhome.*

class HomeActivity : AppCompatActivity(), View.OnClickListener {

    val context: Context = this
    private lateinit var drawerLayout: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawerhome)

        drawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        clickListenerHandling()
        switchFragment(HomeFragment())
    }

    private fun clickListenerHandling() {
        rlMenu.setOnClickListener(this)
        rlBell.setOnClickListener(this)
        ll_Home.setOnClickListener(this)
        ll_Profile.setOnClickListener(this)
        ll_mybooking.setOnClickListener(this)
        ll_messages.setOnClickListener(this)
        ll_myloops.setOnClickListener(this)
        ll_promote.setOnClickListener(this)
        ll_timeline.setOnClickListener(this)
        ll_refer.setOnClickListener(this)
        ll_eventCalendar.setOnClickListener(this)
        ll_settings.setOnClickListener(this)
        ll_logout.setOnClickListener(this)
    }

    protected fun switchFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.rl_main, fragment)
        fragmentTransaction.commit()
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.ll_Home -> {
                drawerClose()
            }
            R.id.rlMenu -> {
                drawerLayout.openDrawer(GravityCompat.START)
            }
            R.id.rlBell -> {
                startActivity(Intent(context,NotificationActivity::class.java))
            }
            R.id.ll_Profile -> {
                val intent = Intent(context, MyProfileActivity::class.java)
                startActivity(intent)

            }
            R.id.ll_mybooking -> {
                val intent = Intent(context, MyBookingsActivity::class.java)
                startActivity(intent)

            }
            R.id.ll_messages -> {
                val intent = Intent(context, MessagesActivity::class.java)
                startActivity(intent)

            }
            R.id.ll_myloops -> {
                val intent = Intent(context, MyLoopsActivity::class.java)
                startActivity(intent)
            }
            R.id.ll_promote -> {
                val intent = Intent(context, PromoteBusinessActivity::class.java)
                startActivity(intent)
            }
            R.id.ll_timeline -> {
                val intent = Intent(context, TimelineActivity::class.java)
                startActivity(intent)
            }
            R.id.ll_refer -> {
                val intent = Intent(context, ReferActivity::class.java)
                startActivity(intent)
            }
            R.id.ll_eventCalendar -> {
                val intent = Intent(context, EventCalenderActivity::class.java)
                startActivity(intent)
            }
            R.id.ll_settings -> {
                val intent = Intent(context, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.ll_logout -> {
                openLogoutDialog()
            }
        }
    }

    private fun openLogoutDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to logout?")
        builder.setCancelable(false)
        builder.setNegativeButton("Cancel") { dialogInterface, i ->
            dialogInterface.dismiss()
        }
        builder.setPositiveButton(
            "Logout"
        ) { dialog, id ->
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }.create().show()
    }

    private fun drawerClose() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerClose()
        } else {
            super.onBackPressed()
        }

    }
}