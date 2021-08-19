package com.bintyblackbook.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.EventData
import com.bintyblackbook.models.EventsModel
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.android.synthetic.main.item_events_in_profile.view.*


class EventInProfileAdapter(var context: Context) :
    RecyclerView.Adapter<EventInProfileAdapter.EventViewHolder>() {

    var arrayList= ArrayList<EventData>()
    var myPopupWindow: PopupWindow? = null
    var onItemClick:((eventsModel:EventData,clickOn:String)->Unit)?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_events_in_profile, parent, false)
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
        private val rlDots:RelativeLayout = itemView.rlDots
        private val tvName: TextView = itemView.tvName
        private val tvLocation: TextView = itemView.tvLocation

        fun bind(pos: Int) {
            val eventsModel = arrayList[pos]
            Glide.with(context).load(eventsModel.image).into(roundedImageView)
            tvName.text = eventsModel.name
            tvLocation.text = eventsModel.location

            setPopUpWindow(eventsModel)

            rlDots.setOnClickListener {
                myPopupWindow?.showAsDropDown(it,-165,-20)
            }

        }

        private fun setPopUpWindow(eventsModel:EventData) {
            val inflater =
                context.applicationContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val  view = inflater.inflate(R.layout.popup, null)

            val tvEdit = view.findViewById<TextView>(R.id.tvEdit)
            val tvDelete = view.findViewById<TextView>(R.id.tvDelete)

            tvEdit.setOnClickListener {
                myPopupWindow?.dismiss()
                onItemClick?.invoke(eventsModel,"editClick")
            }

            tvDelete.setOnClickListener {
                myPopupWindow?.dismiss()
                onItemClick?.invoke(eventsModel,"deleteClick")
            }

            myPopupWindow =  PopupWindow (view, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        }
    }
}