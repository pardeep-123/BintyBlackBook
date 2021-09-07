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
import com.bintyblackbook.model.LoopRequestData
import com.bintyblackbook.ui.activities.home.UserDetailActivity
import com.bintyblackbook.ui.activities.home.bookings.MyBookingsActivity
import com.bintyblackbook.util.MyUtils
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_booking_request.view.*

class BookingRequestAdapter(var context: Context): RecyclerView.Adapter<BookingRequestAdapter.BookingRequestViewHolder>() {

    var loopList= ArrayList<LoopRequestData>()
    lateinit var loopRequestInterface: LoopRequestInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingRequestViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_booking_request,parent,false)
        return BookingRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingRequestViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return loopList.size
    }

    inner class BookingRequestViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val tvMessage: TextView = itemView.tvMessage
        val btnAccept: Button = itemView.btnAccept
        val btnCancel: Button = itemView.btnCancel

        fun bind(pos: Int) {
            val data= loopList[pos]
            tvMessage.text= data.userName+" "+"has send you loop request"
            Glide.with(context).load(data.userImage).into(itemView.civ_profile)
            itemView.tvTime.text= MyUtils.getTimeAgo(data.created.toLong())
            btnAccept.setOnClickListener {
                loopRequestInterface.onItemClick("2",data,pos)

            }

            btnCancel.setOnClickListener {
                loopRequestInterface.onItemClick("0",data,pos)

            }
        }
    }
    interface LoopRequestInterface{
        fun onItemClick(status: String, data: LoopRequestData,position: Int)
    }
}