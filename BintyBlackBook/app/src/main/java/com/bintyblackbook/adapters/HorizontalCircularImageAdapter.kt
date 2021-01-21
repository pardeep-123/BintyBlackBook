package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.models.EditMessageModel
import com.makeramen.roundedimageview.RoundedImageView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_horizontal_circular_image.view.*


class HorizontalCircularImageAdapter(var context: Context, var arrayList: ArrayList<EditMessageModel>) :
    RecyclerView.Adapter<HorizontalCircularImageAdapter.HorizontalImagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalImagesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_horizontal_circular_image, parent, false)
        return HorizontalImagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: HorizontalImagesViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class HorizontalImagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val civProfile: CircleImageView = itemView.civ_profile
        private val rlCross: RelativeLayout = itemView.rlCross
        private val tvName:TextView = itemView.tvName

        fun bind(pos: Int) {
            val editMessageModel = arrayList[pos]
            civProfile.setImageResource(editMessageModel.image!!)
            tvName.text = editMessageModel.name

        }
    }
}