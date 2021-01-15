package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.models.PhotosModel
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.android.synthetic.main.item_photos.view.*


class PhotoAdapter(var context: Context, var arrayList: ArrayList<PhotosModel>) :
    RecyclerView.Adapter<PhotoAdapter.HorizontalPhotoViewHolder>() {

    var onItemClick:((photosModel:PhotosModel)->Unit)?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalPhotoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_photos, parent, false)
        return HorizontalPhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: HorizontalPhotoViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class HorizontalPhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val roundedImageView: RoundedImageView = itemView.roundedImageView
        private val tvName: TextView = itemView.tvName
        private val tvLocation: TextView = itemView.tvLocation

        fun bind(pos: Int) {
            val photosModel = arrayList[pos]
            roundedImageView.setImageResource(photosModel.image!!)
            tvName.text = photosModel.name
            tvLocation.text = photosModel.address

            itemView.setOnClickListener {
                onItemClick?.invoke(photosModel)
            }

        }
    }
}