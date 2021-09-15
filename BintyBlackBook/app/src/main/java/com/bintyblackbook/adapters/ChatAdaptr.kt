package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.Datum
import com.bintyblackbook.util.MyUtils
import com.bintyblackbook.util.getUser
import com.bumptech.glide.Glide

import de.hdodenhof.circleimageview.CircleImageView

class ChatAdaptr(val context: Context, internal val arrayList: ArrayList<Datum>, val receiverId:String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TYPE_USER = 0
    val TYPE_FRIEND = 2
    var pos:Int=0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == TYPE_USER) {
            return (UserViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_chat_right,
                    parent,
                    false
                )
            ))
        } else if (viewType == TYPE_FRIEND) {
            return (FriendViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_chat_left,
                    parent,
                    false
                )
            ))
        }

        return (FriendViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_chat_left,
                parent,
                false
            )
        ))

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder.itemViewType == TYPE_FRIEND) {
            val friendViewHolder = holder as FriendViewHolder
            friendViewHolder.txt_friendView.setText(arrayList[position].message)
            friendViewHolder.txt_time.setText(MyUtils.getDateTime(arrayList[position].created?.toLong()!!))
           Glide.with(context).load(arrayList[position].recieverImage).into(friendViewHolder.profileimage)

        } else if (holder.itemViewType == TYPE_USER) {
            val userViewHolder = holder as UserViewHolder
            userViewHolder.txt_userView.setText(arrayList[position].message)
            Glide.with(context).load(arrayList[position].senderImage).into(userViewHolder.profileimage)
            userViewHolder.txt_time.setText(MyUtils.getDateTime(arrayList[position].created?.toLong()!!))
        }

    }

    override fun getItemViewType(position: Int): Int {
        if (receiverId.equals(arrayList[position].senderId.toString()))
        {
            return TYPE_USER
        }
        else
        {
            return TYPE_FRIEND
        }
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txt_userView: TextView = itemView.findViewById(R.id.tvMessageUser)
        val txt_time: TextView = itemView.findViewById(R.id.tvTimeUser)
        val profileimage: CircleImageView = itemView.findViewById(R.id.profile_image_user)
    }

    inner class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txt_friendView: TextView = itemView.findViewById(R.id.tvMessage)
        val txt_time: TextView = itemView.findViewById(R.id.tvTime)
        val profileimage: CircleImageView = itemView.findViewById(R.id.profile_image)

    }
}
