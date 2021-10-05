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
import com.bintyblackbook.ui.activities.home.OtherUserProfileActivity
import com.bintyblackbook.ui.activities.home.UserDetailActivity
import com.bintyblackbook.ui.activities.home.bookings.MyBookingsActivity
import com.bintyblackbook.ui.dialogues.RequestDialogFragment
import com.bintyblackbook.util.MyUtils
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_upcoming_booking.view.*

class UpcomingBookingAdapter(var context: Context) :
    RecyclerView.Adapter<UpcomingBookingAdapter.UpcomingBookingViewHolder>() {

    lateinit var bookingInterface: BookingInterface

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
            tvDate.text= "Date: "+MyUtils.getDate(upcomingBookingModel.availabilityDate.toLong())
            tvTime.text= "Time: "+MyUtils.getTime(upcomingBookingModel.availabilityDate.toLong())

            //set booking status here
            if(upcomingBookingModel.status==0){
                tvStatus.text="Status: PENDING"
            }else if(upcomingBookingModel.status==1){
                tvStatus.text="Status: ACCEPTED"
            }else if(upcomingBookingModel.status==2){
                tvStatus.text="Status: DECLINED"
            }else if(upcomingBookingModel.status==3){
                tvStatus.text="Status: IN PROCESS"
            }else{
                tvStatus.text="Status: COMPLETED"
            }


            btnCancel.setOnClickListener {
                bookingInterface.onCancelBooking(pos,arrayList[pos])
            }

            itemView.setOnClickListener {
                val intent= Intent(context,OtherUserProfileActivity::class.java)
                intent.putExtra("user_id",upcomingBookingModel.otherUserId.toString())
                intent.putExtra("user_type","1")
                context.startActivity(intent)
            }
        }
    }

    interface BookingInterface{
        fun onCancelBooking(position: Int,data:UpcomingBookings)
    }
}