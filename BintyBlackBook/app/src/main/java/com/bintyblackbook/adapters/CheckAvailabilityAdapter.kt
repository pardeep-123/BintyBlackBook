package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.models.AvailabilityModel
import kotlinx.android.synthetic.main.item_time.view.*

class CheckAvailabilityAdapter(var context: Context, var arrayList: ArrayList<AvailabilityModel>) :
    RecyclerView.Adapter<CheckAvailabilityAdapter.AvailabilityViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CheckAvailabilityAdapter.AvailabilityViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_time, parent, false)
        return AvailabilityViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CheckAvailabilityAdapter.AvailabilityViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class AvailabilityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_time: TextView = itemView.tv_time

        fun bind(pos: Int) {
            val availabilityModel = arrayList[pos]

            tv_time.text = availabilityModel.time


            itemView.setOnClickListener {

            }
        }
    }
}