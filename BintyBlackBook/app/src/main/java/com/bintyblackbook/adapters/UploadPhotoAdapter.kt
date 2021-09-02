package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.UploadPhotoModel
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView


class UploadPhotoAdapter(val context: Context): RecyclerView.Adapter<UploadPhotoAdapter.UploadPhotoViewHolder>() {
    var arrayList= ArrayList<UploadPhotoModel>()
    lateinit var uploadPhotoInterface:UploadPhotoInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadPhotoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_view_upload_photo,parent,false)
        return UploadPhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: UploadPhotoViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class UploadPhotoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var imageView: RoundedImageView =itemView.findViewById(R.id.riv_Picture)
        var img_upload: ImageView = itemView.findViewById(R.id.img_upload)
        var img_delete: ImageView = itemView.findViewById(R.id.ivDeletePhoto)

        fun bind(position:Int){
            val data = arrayList[position]

            if(data.type=="undefined"){
                imageView.visibility= View.VISIBLE
                img_upload.visibility= View.VISIBLE
                img_delete.visibility= View.GONE

            }
            else {
                img_upload.visibility= View.GONE
                img_delete.visibility= View.VISIBLE
                Glide.with(context).load(data.photo_url).into(imageView)
            }

            img_upload.setOnClickListener {
                uploadPhotoInterface.onPhotoUpload(position)
            }

            img_delete.setOnClickListener {
                uploadPhotoInterface.onDeletePhoto(position)
            }
        }
    }

    interface UploadPhotoInterface{
        fun onPhotoUpload(position: Int)
        fun onDeletePhoto(position: Int)
    }
}