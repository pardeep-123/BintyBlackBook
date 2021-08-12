package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.UserMedia
import com.bintyblackbook.models.PhotosModel
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.android.synthetic.main.item_image.view.*


class HorizontalImagesAdapter(var context: Context) :
    RecyclerView.Adapter<HorizontalImagesAdapter.HorizontalImagesViewHolder>() {
    var arrayList= ArrayList<UserMedia>()
    var onItemClick:((image:String)->Unit)?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalImagesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false)
        return HorizontalImagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: HorizontalImagesViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class HorizontalImagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val roundedImageView: RoundedImageView = itemView.roundedImageView

        fun bind(pos: Int) {
            val position = arrayList[pos]
            Glide.with(context).load(position.image).into(roundedImageView)
           // roundedImageView.setImageResource(position)

            itemView.setOnClickListener {
                onItemClick?.invoke(position.image)
            }
        }
    }
}