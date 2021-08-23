package com.bintyblackbook.ui.activities.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bintyblackbook.R
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.ui.activities.authentication.LoginActivity
import com.bintyblackbook.ui.activities.home.bookings.MyBookingsActivity
import com.bintyblackbook.ui.activities.home.eventCalender.EventCalenderActivity
import com.bintyblackbook.ui.activities.home.loop.MyLoopsActivity
import com.bintyblackbook.ui.activities.home.message.MessagesActivity
import com.bintyblackbook.ui.activities.home.notification.NotificationActivity
import com.bintyblackbook.ui.activities.home.profileBusiness.MyProfileBusinessActivity
import com.bintyblackbook.ui.activities.home.profileUser.MyProfileActivity
import com.bintyblackbook.ui.activities.home.promote.PromoteBusinessActivity
import com.bintyblackbook.ui.activities.home.settings.SettingsActivity
import com.bintyblackbook.ui.activities.home.timeline.TimelineActivity
import com.bintyblackbook.ui.dialogues.LogoutDialogFragment
import com.bintyblackbook.ui.fragments.HomeFragment
import com.bintyblackbook.util.*
import com.bintyblackbook.viewmodel.SettingsViewModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.drawerhome.*

class HomeActivity : BaseActivity(), View.OnClickListener {

    private lateinit var drawerLayout: DrawerLayout
    var searchClick = false
    var userType:String? = null
    lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawerhome)
        settingsViewModel= SettingsViewModel(this)
        drawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        userType = MySharedPreferences.getUserType(this)

        if(userType.equals("Business")){
            ll_promote.visibility = View.VISIBLE
        }

        clickListenerHandling()
        switchFragment(HomeFragment())

        ivSearch.setOnClickListener {
            if (searchClick){
                edtSearch.visibility = View.GONE
                searchClick = false
                MyUtils.hideSoftKeyboard(this)
            }else{
                edtSearch.visibility = View.VISIBLE
                searchClick = true
            }
        }
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
                startActivity(Intent(context, NotificationActivity::class.java))
            }
            R.id.ll_Profile -> {
               if (userType.equals("User")){
                   val intent = Intent(context, MyProfileActivity::class.java)
                   startActivity(intent)
               }else if(userType.equals("Business")){
                   val intent = Intent(context, MyProfileBusinessActivity::class.java)
                   startActivity(intent)
               }

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
                val logoutDialog = LogoutDialogFragment(this)
                logoutDialog.show(supportFragmentManager,"logoutDialog")
            }
        }
    }

    fun drawerClose() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerClose()
        } else {
            super.onBackPressed()
        }
    }

    fun logoutUser(){
        settingsViewModel.logout(getSecurityKey(this)!!, getUser(this)?.authKey!!, getUser(this)?.id.toString())
        settingsViewModel.baseLiveData.observe(this, Observer {

            if(getStatus(context)?.equals("1")!!){
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                this.finishAffinity()
                clearAllData(context)
            }

            else{
                clearAllData(context)
                clearEmail(context)
                clearPassword(context)
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                this.finishAffinity()
            }

        })

    }
}