package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.models.ChatModel
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_chat_left.view.*

class ChatAdapter(var mContext: Context, var mChat: ArrayList<ChatModel>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    val MSG_TYPE_LEFT = 0
    val MSG_TYPE_RIGHT = 1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        if (viewType == MSG_TYPE_RIGHT) {
            val view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_right, parent, false)
            return ChatViewHolder(view)
        }else{
            val view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_left, parent, false)
            return ChatViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return mChat.size
    }

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvMessage: TextView = itemView.tvMessage
        var profile_image: CircleImageView = itemView.profile_image
        /*var tvTime: TextView = itemView.tvTime*/


        fun bind(pos: Int) {
            val chatModel = mChat[pos]
            tvMessage.text = chatModel.message
            chatModel.image?.let { profile_image.setImageResource(it) }

            if (chatModel.profileVisible){
                profile_image.visibility = View.VISIBLE
            }else{
                profile_image.visibility = View.INVISIBLE
                if (chatModel.rightMessage){
                    tvMessage.background = ContextCompat.getDrawable(mContext,R.drawable.bg_chat_right_second)
                }else{
                    tvMessage.background = ContextCompat.getDrawable(mContext,R.drawable.bg_chat_left_second)
                }
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        if (mChat[position].rightMessage) {
            return MSG_TYPE_RIGHT
        } else {
            return MSG_TYPE_LEFT
        }
    }
}