package com.bintyblackbook.ui.activities.welcome

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bintyblackbook.R
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.Data
import com.bintyblackbook.ui.activities.home.HomeActivity
import com.bintyblackbook.util.getUser
import com.bintyblackbook.util.getUserList
import com.bintyblackbook.util.saveUsers
import info.jeovani.viewpagerindicator.ViewPagerIndicator
import kotlinx.android.synthetic.main.fragment_welcome.*

class WelcomeTutorial : BaseActivity(), ViewPager.OnPageChangeListener {

    private var viewPager: ViewPager? = null
    private var viewPagerIndicator: ViewPagerIndicator? = null
    private var pos: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_welcome)
     /*   window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )*/
        //ViewPager
        initViewPager()

        //ClickListener
        tvSkip.setOnClickListener {
            val list= getUserList(this)
            for(i in 0 until list?.size!!){
                  if(list[i].userType==1){
                      list.add(getUser(this)!!)
                  }
            }
          //add navigation here
          //  list.add(getUser(this)!!)
            list[0].isCurrentUser=true
            saveUsers(this,list)
            val intent= Intent(this,HomeActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }


    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {

        if (position == 4) {
            tvSkip.setText("Get Started")
            tvSkip.setTextColor(resources.getColor(R.color.whiteColor))
            tv_heading_txt.text = "We are *cosmopolitans. Welcome to our not so small Black book.\n\n*citizens of the world"
        } else if (position == 3) {

            tvSkip.setTextColor(resources.getColor(R.color.whiteColor))
            tvSkip.setText("Skip")
            tv_heading_txt.text = "Our “swap” system is designed especially for businesses and business owners. Maybe you can partner and swap a photograph session for a training session. Perhaps you are just open to collaborations. Let us know on your profile!"
        }else if (position == 2) {
            tvSkip.setTextColor(resources.getColor(R.color.whiteColor))
            tvSkip.setText("Skip")
            tv_heading_txt.text = "Don’t be shy! Use your personalized referral code to send to your friends so they too can join in on the fun."
        }
        else if (position == 1) {

            tvSkip.setTextColor(resources.getColor(R.color.whiteColor))
            tvSkip.setText("Skip")
            tv_heading_txt.text = "Use our “Loop” feature to loop and connect with friends and businesses."
        } else if (position == 0) {

            tvSkip.setTextColor(resources.getColor(R.color.whiteColor))
            tvSkip.setText("Skip")
            tv_heading_txt.text = "Welcome to B3, short for Binty’s Black Book\n\nA place where you can connect with old and new friends, book services, find businesses, promote events, post opportunities and much more!"
        }
        pos = position
    }

    fun initViewPager() {
        viewPagerIndicator = findViewById(R.id.mainViewPagerIndicator)
        viewPager = findViewById(R.id.welcomeViewPager)
        viewPager?.adapter = ImagesPagerAdapter(supportFragmentManager)
        viewPager?.addOnPageChangeListener(this)
    }

    //PagerAdapter
    class ImagesPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            when (position) {
                0 -> {
                    val fragment = WelcomeScreen_1()
                    return fragment
                }1 -> {
                    val fragment = WelcomeScreen_2()
                    return fragment
                }2 -> {
                    val fragment = WelcomeScreen_3()
                    return fragment
                } 3 ->{
                    val fragment= WelcomeScreen_4()
                    return fragment
                } else ->{
                    val fragment= WelcomeScreen_5()
                    return fragment
                }
            }
        }
        override fun getCount(): Int {
            return 5
        }

    }

}
