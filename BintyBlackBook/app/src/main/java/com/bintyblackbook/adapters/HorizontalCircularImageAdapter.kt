package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.AllData
import com.bintyblackbook.models.EditMessageModel
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_horizontal_circular_image.view.*


class HorizontalCircularImageAdapter(var context: Context) :
    RecyclerView.Adapter<HorizontalCircularImageAdapter.HorizontalImagesViewHolder>() {

    var arrayList= ArrayList<AllData>()
    lateinit var horizontalAdapterInterface: HorizontalAdapterInterface
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
            Glide.with(context).load(editMessageModel.userImage).into(civProfile)
            tvName.text = editMessageModel.userName

            rlCross.setOnClickListener {
                horizontalAdapterInterface.onDelete(arrayList[pos],pos)
            }
        }
    }

    interface HorizontalAdapterInterface{
        fun onDelete(data:AllData,position: Int)
    }
}
