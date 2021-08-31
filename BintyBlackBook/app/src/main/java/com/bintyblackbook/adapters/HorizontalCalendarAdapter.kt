package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.AvailabilityData
import com.bintyblackbook.util.MyUtils
import kotlinx.android.synthetic.main.calendar_item.view.*

class HorizontalCalendarAdapter(var context: Context) : RecyclerView.Adapter<HorizontalCalendarAdapter.HorizontalCalendarViewHolder>() {
    var arrayList=  ArrayList<AvailabilityData>()
    lateinit var calenderInterface:CalenderInterface
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HorizontalCalendarViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.calendar_item, parent, false)
        return HorizontalCalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: HorizontalCalendarViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class HorizontalCalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvWeek: TextView = itemView.tv_day_calendar_item
        val tvMonthAndDate: TextView = itemView.tv_date_calendar_item

        fun bind(pos: Int) {

            val horizontalCalendarModel = arrayList[pos]

            if(pos==0){
                horizontalCalendarModel.isSelected=true
            }

            tvWeek.text = MyUtils.getCurrentDay(horizontalCalendarModel.date!!)
            tvMonthAndDate.text = MyUtils.getCurrentDate(horizontalCalendarModel.date)

            if (horizontalCalendarModel.isSelected==true) {
                itemView.background = ContextCompat.getDrawable(context, R.drawable.bg_border_green)
                calenderInterface.onItemClick(horizontalCalendarModel,pos)
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.fadePink))

                itemView.setOnClickListener {
                    arrayList.forEachIndexed { index, horizontalCalendarModel ->
                        horizontalCalendarModel.isSelected = index == pos
                        notifyDataSetChanged()
                    }
                    calenderInterface.onItemClick(horizontalCalendarModel,pos)
                }
            }
        }
    }

    interface CalenderInterface {
        fun onItemClick(data: AvailabilityData,position: Int)
    }
}