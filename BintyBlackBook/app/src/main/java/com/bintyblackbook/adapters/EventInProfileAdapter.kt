package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.EventData
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.android.synthetic.main.item_events_in_profile.view.*


class EventInProfileAdapter(var context: Context) : RecyclerView.Adapter<EventInProfileAdapter.EventViewHolder>() {

    var arrayList= ArrayList<EventData>()
    lateinit var eventProfileInterface: EventProfileInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.item_events_in_profile,
            parent,
            false
        )
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(position)

        holder.itemView.rlDots.setOnClickListener {

            val popup = PopupMenu(context,  holder.itemView.rlDots)

            popup.inflate(R.menu.profile_menu)

            popup.setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.edit -> {
                        eventProfileInterface.onEditClick(arrayList[position], position)
                    }

                    R.id.delete -> {
                      eventProfileInterface.onDeleteClick(arrayList[position], position)
                    }
                }
                true
            }

            popup.show()

        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

  /*  override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }*/

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val roundedImageView: RoundedImageView = itemView.roundedImageView
        private val rlDots:RelativeLayout = itemView.rlDots
        private val tvName: TextView = itemView.tvName
        private val tvLocation: TextView = itemView.tvLocation
        private val imgDFavourite:ImageView= itemView.ivFavourite

        fun bind(pos: Int) {
            val eventsModel = arrayList[pos]
            Glide.with(context).load(eventsModel.image).into(roundedImageView)
            tvName.text = eventsModel.name
            tvLocation.text = eventsModel.location

            if(eventsModel.isFavourite==1){
                imgDFavourite.setImageResource(R.drawable.fill_heart)
            }
            else{
                imgDFavourite.setImageResource(R.drawable.unfill_heart)
            }
        }

    }

    interface EventProfileInterface{
        fun onEditClick(data: EventData, position: Int)
        fun onDeleteClick(data: EventData, position: Int)
    }
}