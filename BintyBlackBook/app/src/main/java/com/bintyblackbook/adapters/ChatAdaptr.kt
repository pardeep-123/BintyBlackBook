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
import com.bumptech.glide.Glide

import de.hdodenhof.circleimageview.CircleImageView

class ChatAdaptr(val context: Context, internal val arrayList: ArrayList<Datum>, val senderId:String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TYPE_USER = 0
    val TYPE_FRIEND = 2
    val TYPE_USER_MEDIA = 4
    val TYPE_FRIEND_MEDIA = 6
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

      /*  else if (viewType == TYPE_USER_MEDIA) {
            return (UserMediaViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.user_chat_image_item,
                    parent,
                    false
                )
            ))
        }
        else if (viewType == TYPE_FRIEND_MEDIA) {
            return (FriendMediaViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.friend_chat_image_item,
                    parent,
                    false
                )
            ))
        }*/

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
            friendViewHolder.txt_time.setText(MyUtils.getTime(arrayList[position].created?.toLong()!!))
           Glide.with(context).load(arrayList[position].recieverImage).into(friendViewHolder.profileimage)

        } else if (holder.itemViewType == TYPE_USER) {
            val userViewHolder = holder as UserViewHolder
            userViewHolder.txt_userView.setText(arrayList[position].message)
            Glide.with(context).load(arrayList[position].senderImage).into(userViewHolder.profileimage)
            userViewHolder.txt_time.setText(MyUtils.getTime(arrayList[position].created?.toLong()!!))
        }

     /*   else if (holder.itemViewType == TYPE_USER_MEDIA) {
            val userViewHolder = holder as UserMediaViewHolder
            Glide.with(context).load(Url.IMAGES_BASE_URL+arrayList[position].senderImage).placeholder(R.drawable.user_placeholder).into(userViewHolder.img2)
            Glide.with(context).load(Url.IMAGES_BASE_URL+arrayList[position].message).placeholder(R.drawable.image_placeholder).into(userViewHolder.rightImageMessage)
            userViewHolder.tvTime.setText(NetworkUtil.convertTimeStampTime(arrayList[position].created.toLong()))

            userViewHolder.rightImageMessage.setOnClickListener {
                pos = position
                context.startActivity(Intent(context, ViewImageActivity::class.java).putExtra("imgLink", Url.IMAGES_BASE_URL+arrayList[pos].message))
            }
        }
        else if (holder.itemViewType == TYPE_FRIEND_MEDIA) {
            val userViewHolder = holder as FriendMediaViewHolder
            Glide.with(context).load(Url.IMAGES_BASE_URL+arrayList[position].senderImage).placeholder(R.drawable.user_placeholder).into(userViewHolder.img1)
            Glide.with(context).load(Url.IMAGES_BASE_URL+arrayList[position].message).placeholder(R.drawable.image_placeholder).into(userViewHolder.leftImageMessage)
            userViewHolder.tvTime.setText(NetworkUtil.convertTimeStampTime(arrayList[position].created.toLong()))

            userViewHolder.leftImageMessage.setOnClickListener {
                pos = position
                context.startActivity(Intent(context, ViewImageActivity::class.java).putExtra("imgLink", Url.IMAGES_BASE_URL+arrayList[pos].message))
            }
        }*/
    }

    override fun getItemViewType(position: Int): Int {
        if (senderId.equals(arrayList[position].senderId.toString()))
        {
            if(arrayList[position].messageType.equals("0")){
                return TYPE_USER
            }else{
                return TYPE_USER_MEDIA
            }
        }
        else
        {
            if(arrayList[position].messageType.equals("0")){
                return TYPE_FRIEND
            }else{
                return TYPE_FRIEND_MEDIA
            }
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

  /*  inner class FriendMediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val leftImageMessage: ImageView = itemView.findViewById(R.id.leftImageMessage)
        val img1: ImageView = itemView.findViewById(R.id.img1)

    }
    inner class UserMediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val rightImageMessage: ImageView = itemView.findViewById(R.id.rightImageMessage)
        val img2: ImageView = itemView.findViewById(R.id.img2)

    }*/
}
