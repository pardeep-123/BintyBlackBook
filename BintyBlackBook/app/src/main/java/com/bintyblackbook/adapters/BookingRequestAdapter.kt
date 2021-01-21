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
import com.bintyblackbook.ui.activities.home.UserDetailActivity
import com.bintyblackbook.ui.activities.home.bookings.MyBookingsActivity
import kotlinx.android.synthetic.main.item_booking_request.view.*

class BookingRequestAdapter(var context: Context):
    RecyclerView.Adapter<BookingRequestAdapter.BookingRequestViewHolder>() {

    var onItemBtnClick:((clickOn:String)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingRequestViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_booking_request,parent,false)
        return BookingRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingRequestViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return 1
    }

    inner class BookingRequestViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val tvMessage: TextView = itemView.tvMessage
        val btnAccept: Button = itemView.btnAccept
        val btnCancel: Button = itemView.btnCancel

        fun bind(pos: Int) {
            btnAccept.setOnClickListener {
                onItemBtnClick?.invoke("accept")
            }

            btnCancel.setOnClickListener {
                onItemBtnClick?.invoke("cancel")
            }
        }
    }
}