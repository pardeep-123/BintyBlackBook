package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.CommentData
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_comments.view.*

class CommentsAdapter(var context: Context) : RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {

    var arrayList= ArrayList<CommentData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_comments, parent, false)
        return CommentsViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class CommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image: CircleImageView= itemView.civ_profile
        val userName: TextView= itemView.tvName
        val comment:TextView= itemView.tvComment
        val time:TextView= itemView.tvTime

        fun bind(pos: Int) {
            val data = arrayList[pos]
            Glide.with(context).load(data.userImage).into(image)
            userName.text= data.userName
            comment.text= data.comment
        }
    }
}