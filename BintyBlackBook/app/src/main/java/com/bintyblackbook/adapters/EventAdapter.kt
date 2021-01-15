package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.models.EventsModel
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.android.synthetic.main.item_events.view.*


class EventAdapter(var context: Context, var arrayList: ArrayList<EventsModel>) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    var onItemClick:((eventsModel:EventsModel)->Unit)?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_events, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val roundedImageView: RoundedImageView = itemView.roundedImageView
        private val tvName: TextView = itemView.tvName
        private val tvLocation: TextView = itemView.tvLocation
        private val ivHeart: ImageView = itemView.ivHeart

        fun bind(pos: Int) {
            val eventsModel = arrayList[pos]
            roundedImageView.setImageResource(eventsModel.image!!)
            tvName.text = eventsModel.name
            tvLocation.text = eventsModel.address

            if (eventsModel.ivFav == true){
                ivHeart.setImageResource(R.drawable.fill_heart)
            }else{
                ivHeart.setImageResource(R.drawable.unfill_heart)
            }

            ivHeart.setOnClickListener {
                eventsModel.ivFav = !eventsModel.ivFav!!
                notifyDataSetChanged()
            }

            itemView.setOnClickListener {
                onItemClick?.invoke(eventsModel)
            }

        }
    }
}