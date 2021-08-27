package com.bintyblackbook.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.PastBooking
import com.bintyblackbook.models.UpcomingBookingModel
import com.bintyblackbook.ui.activities.home.EventDetailActivity
import com.bintyblackbook.ui.activities.home.UserDetailActivity
import com.bintyblackbook.util.MyUtils
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_past_booking.view.*

class PastBookingAdapter(var context: Context) :
    RecyclerView.Adapter<PastBookingAdapter.PastBookingViewHolder>() {

    var arrayList = ArrayList<PastBooking>()
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

            Glide.with(context).load(upcomingBookingModel.userImage).into(civProfile)
            tvName.text = upcomingBookingModel.userName
            tvDate.text = MyUtils.getDate(upcomingBookingModel.availabilityDate)
            tvTime.text = MyUtils.getTime(upcomingBookingModel.availabilityDate)
            //tvStatus.text = upcomingBookingModel.status

            itemView.setOnClickListener {
               // context.startActivity(Intent(context, UserDetailActivity::class.java))
            }

        }
    }
}