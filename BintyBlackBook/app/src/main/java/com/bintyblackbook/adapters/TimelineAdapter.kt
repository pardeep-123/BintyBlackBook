package com.bintyblackbook.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.PostData
import com.bintyblackbook.util.MyUtils
import com.bintyblackbook.util.getUser
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_timeline.view.*


class TimelineAdapter(var context: Context) : RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder>() {

    var arrayList= ArrayList<PostData>()
    lateinit var timeLineInterface:TimeLineInterface
    var clicked=true
    var totalLikes=0


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

            Glide.with(context).load(timelineModel.userImage).placeholder(R.drawable.progress_bg_image).into(
                civProfile
            )

            tvName.text = timelineModel.userName
            //tvTime.text = timelineModel.time
            tvLikes.text = timelineModel.totalLikes.toString()
            totalLikes=timelineModel.totalLikes
            tvMessage.text = timelineModel.description
            tvComments.text = timelineModel.postComments.size.toString()
            tvTime.text= MyUtils.getTimeAgo(timelineModel.created.toLong())

            if (!timelineModel.image.isNullOrEmpty()) {

                itemView.rlImage.visibility=View.VISIBLE

                ivPost.visibility = View.VISIBLE

                Glide.with(context).load(timelineModel.image)
                    .listener(object :RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: com.bumptech.glide.request.target.Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            itemView.progress.setVisibility(View.GONE)
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: com.bumptech.glide.request.target.Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            itemView.progress.setVisibility(View.GONE)
                            return false
                        }

                    }) .into(ivPost)



            } else {
                itemView.rlImage.visibility=View.GONE
                ivPost.visibility = View.GONE
            }

            if (timelineModel.isLike == 1) {
                clicked = false
                ivHeart.setImageResource(R.drawable.heart_new)
            } else {
                clicked = true
                ivHeart.setImageResource(R.drawable.like)
            }

            ivHeart.setOnClickListener {
                if (clicked) {
                    clicked = false
                    ivHeart.setImageResource(R.drawable.heart_new)
                    totalLikes += 1
                    tvLikes.text=totalLikes.toString()
                    timeLineInterface.onLikeUnlike(arrayList[pos], pos, "1")

                } else {
                    clicked = true
                    ivHeart.setImageResource(R.drawable.like)
                    totalLikes -= 1
                    tvLikes.text=totalLikes.toString()
                    timeLineInterface.onLikeUnlike(arrayList[pos], pos, "2")
                }
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
        fun onLikeUnlike(data: PostData, position: Int, status: String)
        fun onReportPost(data: PostData, position: Int)
    }
}