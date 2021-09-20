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
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_edit_message.view.*
import kotlinx.android.synthetic.main.item_edit_message.view.tv_name
import kotlinx.android.synthetic.main.row_messages.view.*

class VideoCallListAdapter(val context: Context) : RecyclerView.Adapter<VideoCallListAdapter.VideoCallViewHolder>() {

    var arrayList = ArrayList<AllData>()
    var onItemClick:((editMessageModel:AllData)->Unit)? = null

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
        val ivProfile: CircleImageView = itemView.civ_profile
        val tvName: TextView = itemView.tv_name

        fun bind(pos: Int) {
            itemView.tv_msg.visibility=View.GONE
            itemView.llTime.visibility=View.GONE
            val editMessageModel = arrayList[pos]

            if(editMessageModel.status!=1){
                Glide.with(context).load(editMessageModel.userImage).into(ivProfile)
                tvName.text = editMessageModel.userName
            }
        }
    }
}