package com.bintyblackbook.ui.activities.authentication

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.bintyblackbook.R

import java.util.*
import kotlin.collections.ArrayList

class WalkthroughActivity : AppCompatActivity(), ViewPager.OnPageChangeListener, View.OnClickListener {

    private var viewPager: ViewPager? = null
   // private var viewPagerIndicator: ViewPagerIndicator? = null
    private var currentPage = 0
    private val NUM_PAGES = 5
    // private var categoryList: ArrayList<String> = ArrayList()
    private val selectedArrayList = ArrayList<Int>()
    private val unSelectedArrayList = ArrayList<Int>()

    private var handler: Handler? = null
    private var swipeTimer: Timer? = null

    private var imagesArray = ArrayList<Int>()
    private var textArray = ArrayList<String>()

    private val images = arrayOf(
        R.drawable.slider1,
        R.drawable.slider1,
        R.drawable.slider1)
    //
//    private val textTitle = arrayOf(getString(R.string.walkOne),getString(R.string.walkTwo),
//                               getString(R.string.walkOne))
    private val textTitle = arrayOf("Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
        "It is a long established fact that a reader will be distracted by the readable content",
        "okay")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walkthrough)
        //viewPagerIndicator = findViewById(R.id.bannerPagerIndicator)
        viewPager = findViewById(R.id.bannerViewPager)
        initViewPager()

        setClicks()
    }

    private fun setClicks() {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        currentPage = position
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onClick(p0: View?) {
        when(p0?.id) {

        }
    }

    private fun initViewPager() {

        for (i in images.indices) imagesArray.add(images[i])
        for (i in textTitle.indices) textArray.add(textTitle[i])

        selectedArrayList.add(Color.parseColor("#E53935"))
        unSelectedArrayList.add(Color.parseColor("#E1E1E1"))

//        viewPagerIndicator!!.itemsCount = 3
//        viewPagerIndicator!!.itemType = PagerItemType.OVAL
//        viewPagerIndicator!!.itemSelectedColors = selectedArrayList
//        viewPagerIndicator!!.itemsUnselectedColors = unSelectedArrayList
//        viewPagerIndicator!!.itemElevation = 0
//        viewPagerIndicator!!.itemWidth = 7
//        viewPagerIndicator!!.itemHeight = 7
//        viewPagerIndicator!!.itemMargin = 3
//        viewPagerIndicator!!.setBackgroundColor(Color.TRANSPARENT)

        viewPager?.adapter = MySliderAdapter(this, imagesArray , textArray)

        // Auto start of viewpager
        handler = Handler()
        val update = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }

            viewPager?.setCurrentItem(
                currentPage++,
                true
            )
        }

        swipeTimer = Timer()
        swipeTimer?.schedule(object : TimerTask() {
            override fun run() {
                handler?.post(update)
            }
        }, 3000, 3000)

        viewPager!!.addOnPageChangeListener(this)
    }
}