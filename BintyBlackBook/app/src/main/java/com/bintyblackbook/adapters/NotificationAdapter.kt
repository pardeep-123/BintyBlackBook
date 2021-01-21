package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.models.NotificationModel
import kotlinx.android.synthetic.main.item_notification.view.*

class NotificationAdapter(var context: Context, var arrayList:ArrayList<NotificationModel>):
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    var onItemClick:((notificationModel:NotificationModel)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_notification,parent,false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class NotificationViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        var tvMessage:TextView = itemView.tvMessage

        fun bind(pos:Int){
            val notificationModel = arrayList[pos]
            tvMessage.text = notificationModel.notificationMessage

            itemView.setOnClickListener {
                onItemClick?.invoke(notificationModel)
            }
        }
    }
}