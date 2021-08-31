package com.bintyblackbook.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.PostData
import com.bintyblackbook.util.getUser
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_timeline.view.*

class TimelineAdapter(var context: Context) : RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder>() {

    var arrayList= ArrayList<PostData>()
    var myPopupWindow: PopupWindow? = null
    var onItemClick: ((timelineModel: PostData, clickOn: String,position:Int) -> Unit)? = null
    var onCommentClick: ((timelineModel: PostData) -> Unit)? = null
    var p=0

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

            Glide.with(context).load(timelineModel.userImage).into(civProfile)

            tvName.text = timelineModel.userName
            //tvTime.text = timelineModel.time
            tvLikes.text = timelineModel.totalLikes.toString()
            tvMessage.text=timelineModel.description
            tvComments.text = timelineModel.postComments.size.toString()

            if (timelineModel.image != null) {

                Glide.with(context).load(timelineModel.image).into(ivPost)
                ivPost.visibility = View.VISIBLE
            } else {
                ivPost.visibility = View.GONE
            }

            if (timelineModel.isLike==1) {
                ivHeart.setImageResource(R.drawable.heart_new)
            } else {
                ivHeart.setImageResource(R.drawable.like)
            }

          /*  rlHeart.setOnClickListener {
                timelineModel.isLike = !timelineModel.heartFilled
                notifyDataSetChanged()
            }*/

            civProfile.setOnClickListener {
                onItemClick?.invoke(timelineModel, "imageClick",pos)
            }

            tvName.setOnClickListener {
                onItemClick?.invoke(timelineModel, "nameClick",pos)
            }

            rlComment.setOnClickListener {
              onCommentClick?.invoke(timelineModel)
            }

            rlDots.setOnClickListener {
                p = pos
                myPopupWindow?.showAsDropDown(it, -165, -20)
                Log.i("=====", getUser(context)?.id.toString() +""+ arrayList[p].userId.toString())

                if(getUser(context)?.id==arrayList[p].userId){
                    setPopUpWindow(timelineModel,"myProfile",p)
                }
                else{
                    setPopUpWindow(timelineModel,"other",p)
                }
            }
        }

        private fun setPopUpWindow(timelineModel: PostData,type:String,position: Int) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.popup, null)
            val tvEdit = view.findViewById<TextView>(R.id.tvEdit)
            val tvDelete = view.findViewById<TextView>(R.id.tvDelete)
            val tvReport = view.findViewById<TextView>(R.id.tvReport)

            if(type == "myProfile"){
                tvEdit.visibility=View.VISIBLE
                tvDelete.visibility=View.VISIBLE
                tvReport.visibility=View.GONE
            }
            else{
                tvEdit.visibility=View.GONE
                tvDelete.visibility=View.GONE
                tvReport.visibility=View.VISIBLE
            }

            tvEdit.setOnClickListener {
                myPopupWindow?.dismiss()
                onItemClick?.invoke(timelineModel, "editClick",position)
            }

            tvDelete.setOnClickListener {
                myPopupWindow?.dismiss()
                onItemClick?.invoke(timelineModel, "deleteClick",position)
            }

            myPopupWindow = PopupWindow(view,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                true
            )
        }
    }
}