package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.Suggested
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_view_suggestions.view.*

class SuggestionsAdapter (var context: Context,val list: ArrayList<Suggested>): RecyclerView.Adapter<SuggestionsAdapter.MyViewHolder>(){

    var onSendLoopRequest:((data:Suggested,pos:Int)->Unit)?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_view_suggestions, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SuggestionsAdapter.MyViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val civProfile: CircleImageView = itemView.img_user
        val tvName: TextView = itemView.tvUserName
        val btnLoopReq: Button = itemView.btnLoopReq

        fun bind(pos: Int) {
            tvName.text=list[pos].userName
            Glide.with(context).load(list[pos].userImage).into(civProfile)

            btnLoopReq.setOnClickListener {
                onSendLoopRequest?.invoke(list[pos],pos)

            }
        }
    }

}