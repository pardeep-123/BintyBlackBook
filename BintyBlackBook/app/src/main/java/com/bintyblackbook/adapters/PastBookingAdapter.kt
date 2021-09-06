package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.PastBooking
import com.bintyblackbook.util.MyUtils
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_past_booking.view.*

class PastBookingAdapter(var context: Context) : RecyclerView.Adapter<PastBookingAdapter.PastBookingViewHolder>() {

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
            tvDate.text = MyUtils.getDate(upcomingBookingModel.availabilityDate.toLong())
            tvTime.text = MyUtils.getTime(upcomingBookingModel.availabilityDate.toLong())

            //handle booking status here
            if(upcomingBookingModel.status==0){
                tvStatus.text="PENDING"
            } else if(upcomingBookingModel.status==1){
                tvStatus.text="ACCEPTED"
            }else if(upcomingBookingModel.status==2){
                tvStatus.text="DECLINED"
            }else if(upcomingBookingModel.status==3){
                tvStatus.text="IN PROCESS"
            }else{
                tvStatus.text="COMPLETED"
            }

            itemView.setOnClickListener {
               // context.startActivity(Intent(context, UserDetailActivity::class.java))
            }

        }
    }
}