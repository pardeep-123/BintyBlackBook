package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import com.bintyblackbook.R
import kotlinx.android.synthetic.main.item_infinity_pager.view.*

class InfinityPagerAdapter(var context: Context, var arrayList: ArrayList<Int>) : PagerAdapter() {

    var onInfinityPagerItemClick:((infinityPagerItemPosition:Int)->Unit)? = null

    override fun getCount(): Int {
        return arrayList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view.equals(`object`)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.item_infinity_pager, container, false)
        val roundedImageView = view.roundedImageView
        roundedImageView.setImageResource(arrayList[position])
        container.addView(view)

        view.setOnClickListener {
            onInfinityPagerItemClick?.invoke(position)
        }

        return view
    }
}