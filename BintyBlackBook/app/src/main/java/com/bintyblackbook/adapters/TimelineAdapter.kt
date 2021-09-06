package com.bintyblackbook.adapters

import android.content.Context
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.PostData
import com.bintyblackbook.util.MyUtils
import com.bintyblackbook.util.getUser
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_events_in_profile.view.*
import kotlinx.android.synthetic.main.item_timeline.view.*
import kotlinx.android.synthetic.main.item_timeline.view.rlDots
import kotlinx.android.synthetic.main.item_timeline.view.tvName
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TimelineAdapter(var context: Context) : RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder>() {

    var arrayList= ArrayList<PostData>()
    lateinit var timeLineInterface:TimeLineInterface
//    var myPopupWindow: PopupWindow? = null
//    var onItemClick: ((timelineModel: PostData, clickOn: String,position:Int) -> Unit)? = null
//    var onCommentClick: ((timelineModel: PostData) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_timeline, parent, false)
        return TimelineViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        holder.bind(arrayList[position], position)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class TimelineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var civProfile: CircleImageView = itemView.civ_profile
        var tvName: TextView = itemView.tvName
        var tvTime: TextView = itemView.tvTime
        var tvMessage: TextView = itemView.tvMessage
        var tvLikes: TextView = itemView.tvLikes
        var tvComments: TextView = itemView.tvComments
        var ivHeart: ImageView = itemView.ivHeart
        var ivPost: ImageView = itemView.ivPost
        var rlHeart: RelativeLayout = itemView.rlHeart
        var rlComment: RelativeLayout = itemView.rlComments
        var rlDots: RelativeLayout = itemView.rlDots

        fun bind(timelineModel: PostData, pos: Int) {

            Glide.with(context).load(timelineModel.userImage).into(civProfile)

            tvName.text = timelineModel.userName
            //tvTime.text = timelineModel.time
            tvLikes.text = timelineModel.totalLikes.toString()
            tvMessage.text = timelineModel.description
            tvComments.text = timelineModel.postComments.size.toString()
            tvTime.text= MyUtils.getTimeAgo(timelineModel.created.toLong())

            if (timelineModel.image != null) {

                Glide.with(context).load(timelineModel.image).into(ivPost)
                ivPost.visibility = View.VISIBLE
            } else {
                ivPost.visibility = View.GONE
            }

            if (timelineModel.isLike == 1) {
                ivHeart.setImageResource(R.drawable.heart_new)
            } else {
                ivHeart.setImageResource(R.drawable.like)
            }

            /*  rlHeart.setOnClickListener {
                timelineModel.isLike = !timelineModel.heartFilled
                notifyDataSetChanged()
            }*/

            civProfile.setOnClickListener {
                timeLineInterface.onProfileClick(arrayList[pos], pos)
            }

            rlComment.setOnClickListener {
               timeLineInterface.onCommentSelect(arrayList[pos], pos)
            }
           /* if (getUser(context)?.id == arrayList[pos].userId) {
                setPopUpWindow(timelineModel, "myProfile", pos)
            } else {
                setPopUpWindow(timelineModel, "other", pos)
            }*/
            rlDots.setOnClickListener {

                val popup = PopupMenu(context, rlDots)

                if (getUser(context)?.id == arrayList[pos].userId) {
                    popup.inflate(R.menu.profile_menu)
                } else {
                    popup.inflate(R.menu.timeline_menu)
                }

                popup.setOnMenuItemClickListener { item ->

                    when (item.itemId) {
                        R.id.edit -> {
                            timeLineInterface.onEditItem(arrayList[pos], pos)
                        }

                        R.id.delete -> {
                            timeLineInterface.onDeleteItem(arrayList[pos], pos)
                        }

                        R.id.report -> {
                            //show report dialog
                            timeLineInterface.onReportPost(arrayList[pos], pos)
                        }
                    }
                    true
                }

                popup.show()
            }
        }
    }

    interface TimeLineInterface{
        fun onProfileClick(data: PostData, position: Int)
        fun onEditItem(data: PostData, position: Int)
        fun onDeleteItem(data: PostData, position: Int)
        fun onCommentSelect(data: PostData, position: Int)
        fun onReportPost(data: PostData, position: Int)
    }
}