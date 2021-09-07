package com.bintyblackbook.ui.activities.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bintyblackbook.R
import info.jeovani.viewpagerindicator.ViewPagerIndicator
import kotlinx.android.synthetic.main.fragment_welcome.*

class WelcomeTutorial : Fragment(), ViewPager.OnPageChangeListener {

    private var viewPager: ViewPager? = null
    private var viewPagerIndicator: ViewPagerIndicator? = null
    private var pos: Int = 0;
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        //ViewPager
        initViewPager()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //ViewPager
        //initViewPager()

        //ClickListener
        /*tvSkip.setOnClickListener {
            this.findNavController().navigate(R.id.action_welcomeTutorial_to_loginSignUpFragment)
            SharedPreference.setString(requireContext(), CacheConstants.SCREEN_TYPE,"-1")
        }*/
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        if (position == 2) {
            tvSkip.setText("Get Started")
            tvSkip.setBackgroundResource(R.drawable.background_rounded_corners)
            tvSkip.setTextColor(resources.getColor(R.color.whiteColor))
            tv_heading.text = "Upgrade Your Package"
            tv_heading_txt.text = "With the Select Membership, you can help you get more profile views, access to some cool new features such as compatibility, more likes, chat, add free experience and a lot more!"
        } else if (position == 1) {
            tvSkip.setBackgroundResource(R.drawable.bg_chat_left_second)
            tvSkip.setTextColor(resources.getColor(R.color.colorPrimary))
            tvSkip.setText("Skip")
            tv_heading.text = "Setup Date"
            tv_heading_txt.text = "Find yourself someone who's always willing to ask for the perfect date with you."
        } else if (position == 0) {
            tvSkip.setBackgroundResource(R.drawable.bg_chat_left_second)
            tvSkip.setTextColor(resources.getColor(R.color.colorPrimary))
            tvSkip.setText("Skip")
            tv_heading.text = "Discover New People"
            tv_heading_txt.text = "Professional Entwined will make you most happy to connect with others. You will love meeting with a new people, being social, and engaging in empowering discussions."
        }
        pos = position
    }

    fun initViewPager() {
        viewPagerIndicator = activity?.findViewById(R.id.mainViewPagerIndicator)
        viewPager = activity?.findViewById(R.id.welcomeViewPager)
        viewPager!!.adapter = ImagesPagerAdapter(childFragmentManager)
        viewPager!!.addOnPageChangeListener(this)
    }

    //PagerAdapter
    class ImagesPagerAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            when (position) {
                0 -> {
                    val fragment = WelcomeScreen_1()
                    return fragment
                }
                1 -> {
                    val fragment = WelcomeScreen_2()
                    return fragment
                }
                else -> {
                    val fragment = WelcomeScreen_3()
                    return fragment
                }
            }
        }
        override fun getCount(): Int {
            return 3
        }

    }

}
