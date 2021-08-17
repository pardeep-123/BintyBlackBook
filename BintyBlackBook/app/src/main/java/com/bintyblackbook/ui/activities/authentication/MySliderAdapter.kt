package com.bintyblackbook.ui.activities.authentication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bintyblackbook.R


class MySliderAdapter(context: Context, private val images: List<Int>,private val textTitle: List<String>
          ) : PagerAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val myImageLayout: View = inflater.inflate(R.layout.adapter_banner, view, false)
        val myImage = myImageLayout.findViewById<ImageView>(R.id.ivBanner)
        val myImageText = myImageLayout.findViewById<TextView>(R.id.textWalkthrough)
        myImage.setImageResource(images[position])
        myImageText.text = textTitle[position]

        // Log.e(TAG, "instantiateItem: " + RetrofitService.AD_IMAGE_URL + images.get(position).getImage());

        view.addView(myImageLayout, 0)
        return myImageLayout
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view == o
    }

}