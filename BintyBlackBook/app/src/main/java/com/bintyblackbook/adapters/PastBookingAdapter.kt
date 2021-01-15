package com.bintyblackbook.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.models.UpcomingBookingModel
import com.bintyblackbook.ui.activities.home.EventDetailActivity
import com.bintyblackbook.ui.activities.home.UserDetailActivity
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_past_booking.view.*

class PastBookingAdapter(var context: Context, var arrayList: ArrayList<UpcomingBookingModel>) :
    RecyclerView.Adapter<PastBookingAdapter.PastBookingViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PastBookingViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_past_booking, parent, false)
        return PastBookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: PastBookingViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class PastBookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val civProfile: CircleImageView = itemView.civ_profile
        val tvName: TextView = itemView.tvName
        val tvDate: TextView = itemView.tvDate
        val tvTime: TextView = itemView.tvTime
        val tvStatus: TextView = itemView.tvStatus

        fun bind(pos: Int) {
            val upcomingBookingModel = arrayList[pos]
            civProfile.setImageResource(upcomingBookingModel.image!!)
            tvName.text = upcomingBookingModel.name
            tvDate.text = upcomingBookingModel.date
            tvTime.text = upcomingBookingModel.time
            tvStatus.text = upcomingBookingModel.status

            itemView.setOnClickListener {
                context.startActivity(Intent(context, UserDetailActivity::class.java))
            }

        }
    }
}