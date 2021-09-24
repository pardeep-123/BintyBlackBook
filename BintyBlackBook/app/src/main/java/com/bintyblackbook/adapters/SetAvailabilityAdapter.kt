package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.SlotsModel
import com.bintyblackbook.timeslots.TimeSlotsInterface
import com.bintyblackbook.util.MyUtils
import kotlinx.android.synthetic.main.item_time.view.*

class SetAvailabilityAdapter(
    var context: Context,
    var arrayList: ArrayList<SlotsModel>,
    var selectedDate: String,
    var mInterface: TimeSlotsInterface
) :
    RecyclerView.Adapter<SetAvailabilityAdapter.AvailabilityViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SetAvailabilityAdapter.AvailabilityViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_time, parent, false)
        return AvailabilityViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: SetAvailabilityAdapter.AvailabilityViewHolder,
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
            val date1 = MyUtils.getDateTest(availabilityModel.date!!.toLong()/1000)
            val date2 = MyUtils.getDateTest(selectedDate.toLong()/1000)
            if (date1.equals(date2)) {
                if (availabilityModel.selected) {
                    itemView.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_border_white)
                } else {
                    itemView.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.themeColor2
                        )
                    )
                }
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.themeColor2))
            }


            itemView.setOnClickListener {
                availabilityModel.selected = !availabilityModel.selected
                notifyDataSetChanged()
                mInterface.onClick()
            }

        }
    }
}