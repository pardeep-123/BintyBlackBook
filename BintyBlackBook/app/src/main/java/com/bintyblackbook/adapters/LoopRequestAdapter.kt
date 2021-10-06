package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.LoopRequestData
import com.bintyblackbook.util.MyUtils
import kotlinx.android.synthetic.main.item_loop_request.view.*

class LoopRequestAdapter(var context: Context) :
    RecyclerView.Adapter<LoopRequestAdapter.LoopRequestViewHolder>() {

    var loopList= ArrayList<LoopRequestData>()
   lateinit var loopRequestInterface:LoopRequestInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoopRequestViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_loop_request, parent, false)
        return LoopRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: LoopRequestViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return loopList.size
    }

    inner class LoopRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMessage: TextView = itemView.tvMessage
        val btnAccept: Button = itemView.btnAccept
        val btnCancel: Button = itemView.btnCancel

        fun bind(pos: Int) {
            val data= loopList[pos]
            tvMessage.text= data.userName+" has send you loop request"
            itemView.tvTime.text=MyUtils.getTimeAgo(data.created.toLong())
            btnAccept.setOnClickListener {
                loopRequestInterface.onItemClick("2",data,pos)
              //  onItemBtnClick?.invoke("2",data)
            }

            btnCancel.setOnClickListener {
                loopRequestInterface.onItemClick("0",data,pos)
               // onItemBtnClick?.invoke("0",data)
            }
        }
    }

    interface LoopRequestInterface{
        fun onItemClick(status: String, data: LoopRequestData,position: Int)
    }
}