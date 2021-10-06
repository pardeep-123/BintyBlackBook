package com.bintyblackbook.ui.activities.home.message

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.adapters.EditMessagesAdapter
import com.bintyblackbook.model.AllData
import com.bintyblackbook.model.MessageData
import com.bintyblackbook.util.MyUtils
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_edit_message.view.*
import kotlinx.android.synthetic.main.item_edit_message.view.tv_name
import kotlinx.android.synthetic.main.row_messages.view.*

class VideoCallListAdapter(val context: Context) : RecyclerView.Adapter<VideoCallListAdapter.VideoCallViewHolder>() {

    var arrayList = ArrayList<MessageData>()
    lateinit var videoCallListInterface: VideoCallListInterface


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoCallViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.row_messages, parent, false)
        return VideoCallViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: VideoCallViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class VideoCallViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(pos: Int) {
            val data = arrayList[pos]

            if(data.isGroup==0){
                Glide.with(context).load(data.userImage).into(itemView.civ_profile)
                itemView.tv_name.text = data.userName
                itemView.tv_msg.text= data.lastMessage
                itemView.tvTime.text= MyUtils.getTimeAgo(data.created_at.toLong())
            }

            itemView.setOnClickListener {
                videoCallListInterface.onItemClick(arrayList[pos],pos)
            }
        }
    }

    interface VideoCallListInterface{
        fun onItemClick(data:MessageData,position: Int)
    }
}