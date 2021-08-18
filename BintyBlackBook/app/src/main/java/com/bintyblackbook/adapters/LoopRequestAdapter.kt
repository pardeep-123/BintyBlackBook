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
import kotlinx.android.synthetic.main.item_loop_request.view.*

class LoopRequestAdapter(var context: Context) :
    RecyclerView.Adapter<LoopRequestAdapter.LoopRequestViewHolder>() {

    var onItemBtnClick:((status:String)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoopRequestViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_loop_request, parent, false)
        return LoopRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: LoopRequestViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return 1
    }

    inner class LoopRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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