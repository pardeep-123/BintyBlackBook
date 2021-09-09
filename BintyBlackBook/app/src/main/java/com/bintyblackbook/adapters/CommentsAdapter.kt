package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.CommentData
import com.bintyblackbook.util.MyUtils
import com.bintyblackbook.util.getUser
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_comments.view.*

class CommentsAdapter(var context: Context) : RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {

    var arrayList= ArrayList<CommentData>()

    lateinit var commentInterface: CommentInterface

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
        val rlDots:RelativeLayout= itemView.rlDots

        fun bind(pos: Int) {
            val data = arrayList[pos]
            Glide.with(context).load(data.userImage).into(image)
            userName.text= data.userName
            comment.text= data.comment
            time.text=MyUtils.getTimeAgo(data.created.toLong())

            rlDots.setOnClickListener {
                val popup = PopupMenu(context, rlDots)
                if (getUser(context)?.id == arrayList[pos].userId) {
                    popup.inflate(R.menu.comments_menu)
                } else {
                    popup.inflate(R.menu.timeline_menu)
                }

                popup.setOnMenuItemClickListener { item ->

                    when (item.itemId) {
//                        R.id.edit -> {
//                           // timeLineInterface.onEditItem(arrayList[pos], pos)
//                        }

                        R.id.delete_comment -> {
                            commentInterface.onDeleteComment(arrayList[pos],pos)
                        }

                        R.id.report -> {
//                            //show report dialog
//                            timeLineInterface.onReportPost(arrayList[pos], pos)
                        }
                    }
                    true
                }

                popup.show()
            }
        }
    }

    interface CommentInterface{
        fun onDeleteComment(data:CommentData,position: Int)
    }
}