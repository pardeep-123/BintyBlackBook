package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R

class NotificationAdapter(var context: Context):
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_notification,parent,false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 10
    }

    inner class NotificationViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

    }
}