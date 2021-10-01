package com.bintyblackbook.util

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bintyblackbook.R
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

fun ImageView.loadImage(path: Any) {
    val circularProgressDrawable = CircularProgressDrawable(this.context)
    circularProgressDrawable.strokeWidth = 8f
    circularProgressDrawable.centerRadius = 40f
    circularProgressDrawable.setColorSchemeColors(
        ContextCompat.getColor(
            this.context,
            R.color.whiteColor
        )
    )
    circularProgressDrawable.start()
    Glide.with(this)
        .load(path)
        .placeholder(circularProgressDrawable)
       // .error(R.drawable.placeholder)
        .into(this)

}




fun CircleImageView.loadImg(path: Any) {
    val circularProgressDrawable = CircularProgressDrawable(this.context)
    circularProgressDrawable.strokeWidth = 8f
    circularProgressDrawable.centerRadius = 15f
    circularProgressDrawable.setColorSchemeColors(
        ContextCompat.getColor(
            this.context,
            R.color.whiteColor
        )
    )
    circularProgressDrawable.start()
    Glide.with(this)
        .load(path)
        .placeholder(circularProgressDrawable)
        // .error(R.drawable.placeholder)
        .into(this)

}