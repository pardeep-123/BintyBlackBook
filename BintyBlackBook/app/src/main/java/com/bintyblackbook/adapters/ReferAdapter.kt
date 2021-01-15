package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R

class ReferAdapter(var context:Context):RecyclerView.Adapter<ReferAdapter.ReferViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReferViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_refer,parent,false)
        return ReferViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReferViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 10
    }

    inner class ReferViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

    }
}