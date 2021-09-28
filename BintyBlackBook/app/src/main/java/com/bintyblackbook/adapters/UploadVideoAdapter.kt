package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.NotificationListData
import com.bintyblackbook.model.UploadVideoModel
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.android.synthetic.main.item_notification.view.*

class UploadVideoAdapter(var context: Context): RecyclerView.Adapter<UploadVideoAdapter.UploadVideoViewHolder>() {
    var arrayList= ArrayList<UploadVideoModel>()
    lateinit var uploadVideoInterface: UploadVideoInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadVideoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_view_upload_video,parent,false)
        return UploadVideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: UploadVideoViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class UploadVideoViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var imageView:RoundedImageView=itemView.findViewById(R.id.riv_video)
        var img_upload:ImageView= itemView.findViewById(R.id.ivUpload)
        var img_delete:ImageView= itemView.findViewById(R.id.ivDeleteVideo)
        var addNewVideo:ImageView= itemView.findViewById(R.id.addNewVideo)

        fun bind(pos:Int){
            val data = arrayList[pos]

            if(arrayList.size-1 ==pos){
                addNewVideo.visibility=View.VISIBLE
            }else{
                addNewVideo.visibility=View.GONE
            }

            if(data.type=="undefined"){
                imageView.setImageBitmap(null)
                imageView.visibility=View.VISIBLE
                img_upload.visibility=View.VISIBLE
                img_delete.visibility=View.GONE

            }
            else {
                img_upload.visibility=View.GONE
                img_delete.visibility=View.VISIBLE
                Glide.with(context).load(data.video_url).into(imageView)
            }

            img_upload.setOnClickListener {
                uploadVideoInterface.onVideoUpload(arrayList[pos],pos)
            }
            img_delete.setOnClickListener {
                uploadVideoInterface.deleteVideo(arrayList[pos],pos)
            }

            addNewVideo.setOnClickListener {
                uploadVideoInterface.addVideo(arrayList[pos],pos)
            }
        }
    }

    interface UploadVideoInterface{
        fun onVideoUpload(data:UploadVideoModel,position: Int)
        fun deleteVideo(data:UploadVideoModel,position: Int)
        fun addVideo(data:UploadVideoModel,position: Int)
    }
}