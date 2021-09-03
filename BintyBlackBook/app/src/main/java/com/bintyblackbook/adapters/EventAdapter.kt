package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.EventData
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.android.synthetic.main.item_events.view.*


class EventAdapter(var context: Context) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
    var arrayList= ArrayList<EventData>()
    var isFavourite = false
    var onItemClick:((eventsModel:EventData)->Unit)?= null

    var onSelectFav:((eventsModel:EventData) ->Unit)?=null

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
            Glide.with(context).load(eventsModel.image).into(roundedImageView)
            tvName.text = eventsModel.name
            tvLocation.text = eventsModel.location

            /*if (isFavourite){
                ivHeart.setImageResource(R.drawable.unfill_heart)
                isFavourite = false

            }else{
                ivHeart.setImageResource(R.drawable.fill_heart)
                isFavourite = true
            }*/

            if(eventsModel.isFavourite==1){
                ivHeart.setImageResource(R.drawable.fill_heart)
            }else{
                ivHeart.setImageResource(R.drawable.unfill_heart)
            }
            ivHeart.setOnClickListener {
                eventsModel.isFavourite = 1
                notifyDataSetChanged()
                onSelectFav?.invoke(arrayList[pos])
            }

            itemView.setOnClickListener {
                onItemClick?.invoke(arrayList[pos])
            }

        }
    }

}