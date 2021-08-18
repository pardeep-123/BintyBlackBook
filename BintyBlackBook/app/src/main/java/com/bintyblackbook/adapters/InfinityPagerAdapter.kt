package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import com.bintyblackbook.R
import com.bintyblackbook.model.CategoryName
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_infinity_pager.view.*

class InfinityPagerAdapter(var context: Context, var arrayList: ArrayList<CategoryName>) : PagerAdapter() {

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
        val name= view.tvName
        val location = view.tvLocation

        name.text=arrayList[position].firstName

        if(arrayList[position].userLocation.isEmpty()){
            location.visibility=View.GONE
        }
        else{
            location.visibility=View.VISIBLE
            location.text=arrayList[position].userLocation
        }

        Glide.with(context).load(arrayList[position].userImage).into(roundedImageView)
      //  roundedImageView.setImageResource(arrayList[position].userImage)
        container.addView(view)

        view.setOnClickListener {
            onInfinityPagerItemClick?.invoke(position)
        }

        return view
    }
}