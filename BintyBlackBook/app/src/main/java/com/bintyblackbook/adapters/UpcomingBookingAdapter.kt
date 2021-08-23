package com.bintyblackbook.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.UpcomingBookings
import com.bintyblackbook.models.UpcomingBookingModel
import com.bintyblackbook.ui.activities.home.UserDetailActivity
import com.bintyblackbook.ui.activities.home.bookings.MyBookingsActivity
import com.bintyblackbook.ui.dialogues.RequestDialogFragment
import com.bintyblackbook.util.MyUtils
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_upcoming_booking.view.*

class UpcomingBookingAdapter(var context: Context) :
    RecyclerView.Adapter<UpcomingBookingAdapter.UpcomingBookingViewHolder>() {

    var arrayList = ArrayList<UpcomingBookings>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingBookingViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_upcoming_booking, parent, false)
        return UpcomingBookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: UpcomingBookingViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class UpcomingBookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val civProfile: CircleImageView = itemView.civ_profile
        val btnCancel: Button = itemView.btnCancel
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

            btnCancel.setOnClickListener {
                val requestDialog = RequestDialogFragment()
                requestDialog.show((context as MyBookingsActivity).supportFragmentManager,"requestDialog")
            }

            itemView.setOnClickListener {
                context.startActivity(Intent(context, UserDetailActivity::class.java))
            }
        }
    }
}