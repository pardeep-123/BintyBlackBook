package com.bintyblackbook.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.models.TimelineModel
import com.bintyblackbook.ui.activities.home.timeline.CommentsActivity
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_timeline.view.*

class TimelineAdapter(var context: Context, var arrayList: ArrayList<TimelineModel>) :
    RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder>() {

    var myPopupWindow: PopupWindow? = null
    var onItemClick: ((timelineModel: TimelineModel, clickOn: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_timeline, parent, false)
        return TimelineViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        holder.bind(position)
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

        fun bind(pos: Int) {
            val timelineModel = arrayList[pos]

            setPopUpWindow(timelineModel)

            civProfile.setImageResource(timelineModel.profileImage!!)
            tvName.text = timelineModel.name
            tvTime.text = timelineModel.time
            tvLikes.text = timelineModel.likes
            tvComments.text = timelineModel.comments


            if (timelineModel.myPost) {
                rlDots.visibility = View.VISIBLE
            } else {
                rlDots.visibility = View.GONE
            }

            if (timelineModel.postImage != null) {
                ivPost.setImageResource(timelineModel.postImage!!)
                ivPost.visibility = View.VISIBLE
            } else {
                ivPost.visibility = View.GONE
            }

            if (timelineModel.heartFilled) {
                ivHeart.setImageResource(R.drawable.like21)
            } else {
                ivHeart.setImageResource(R.drawable.like)
            }

            rlHeart.setOnClickListener {
                timelineModel.heartFilled = !timelineModel.heartFilled
                notifyDataSetChanged()
            }

            rlComment.setOnClickListener {
                context.startActivity(Intent(context, CommentsActivity::class.java))
            }

            rlDots.setOnClickListener {
                myPopupWindow?.showAsDropDown(it, -165, -20)
            }
        }

        private fun setPopUpWindow(timelineModel: TimelineModel) {
            val inflater =
                context.applicationContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.popup, null)

            val tvEdit = view.findViewById<TextView>(R.id.tvEdit)
            val tvDelete = view.findViewById<TextView>(R.id.tvDelete)

            tvEdit.setOnClickListener {
                myPopupWindow?.dismiss()
                onItemClick?.invoke(timelineModel, "editClick")
            }

            tvDelete.setOnClickListener {
                myPopupWindow?.dismiss()
                onItemClick?.invoke(timelineModel, "deleteClick")
            }

            myPopupWindow = PopupWindow(
                view,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                true
            )
        }
    }
}