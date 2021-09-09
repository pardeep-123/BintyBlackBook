package com.bintyblackbook.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.MessageData
import com.bintyblackbook.models.EditMessageModel
import com.bintyblackbook.ui.activities.home.message.ChatActivity
import com.bintyblackbook.util.MyUtils
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.row_messages.view.*

class MessagesAdapter(val context: Context, var arrayList: ArrayList<MessageData>) : RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.row_messages, parent, false)
        return MessagesViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class MessagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val civ_profile:CircleImageView = itemView.civ_profile
        val tv_name:TextView = itemView.tv_name
        val tv_msg:TextView = itemView.tv_msg
        val tvTime:TextView = itemView.tvTime

        fun bind(pos:Int){
            val messageModel = arrayList[pos]

            Glide.with(context).load(messageModel.userImage).into(civ_profile)

            tv_name.text = messageModel.userName

            tvTime.text= MyUtils.getTimeAgo(messageModel.updated.toLong())
            tv_msg.text= messageModel.lastMessage

            itemView.setOnClickListener {

                val intent=Intent(context, ChatActivity::class.java)
                intent.putExtra("message_type",arrayList[pos].messageType.toString())
                intent.putExtra("receiver_id",arrayList[pos].receiverId.toString())
                context.startActivity(intent)
            }
        }
    }
}