package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.CategoryData
import com.bintyblackbook.model.CategoryName
import com.bintyblackbook.models.PhotosModel
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.android.synthetic.main.item_photos.view.*
import java.util.*
import kotlin.collections.ArrayList


class PhotoAdapter(var list: ArrayList<CategoryName>, var context: Context) : RecyclerView.Adapter<PhotoAdapter.HorizontalPhotoViewHolder>(),Filterable {

    var arrayList= ArrayList<CategoryName>()
    var onItemClick:((photosModel:CategoryName)->Unit)?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalPhotoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_photos, parent, false)
        return HorizontalPhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: HorizontalPhotoViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class HorizontalPhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val roundedImageView: RoundedImageView = itemView.roundedImageView
        private val tvName: TextView = itemView.tvName
        private val tvLocation: TextView = itemView.tvLocation

        fun bind(pos: Int) {
            val photosModel = arrayList[pos]
            Glide.with(context).load(photosModel.userImage).into(roundedImageView)
           // roundedImageView.setImageResource(photosModel.image!!)
            tvName.text = photosModel.firstName

            if(photosModel.userLocation.isNullOrEmpty()){
                tvLocation.visibility=View.GONE
            }else{
                tvLocation.visibility=View.VISIBLE
                tvLocation.text = photosModel.userLocation
            }

            itemView.setOnClickListener {
                onItemClick?.invoke(photosModel)
            }

        }
    }

    override fun getFilter(): Filter {

        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charSearch=constraint.toString()
                if(charSearch.isEmpty()){
                    arrayList=list
                } else{
                    val resultList=ArrayList<CategoryName>()
                    for(row in list){
                        if (row.firstName.toUpperCase(Locale.US).contains(constraint.toString().toUpperCase(Locale.US))
                            || row.categoryName.toLowerCase(Locale.US).contains(
                                constraint.toString().toLowerCase(
                                    Locale.US
                                )
                            )
                            || row.userLocation.toLowerCase(Locale.US).contains(
                                constraint.toString().toLowerCase(
                                    Locale.US
                                )
                            )
                            || row.userLocation.toUpperCase(Locale.US).contains(
                                constraint.toString().toUpperCase(
                                    Locale.US
                                )
                            )
                        ) {
                            resultList.add(row)
                        }
                    }
                    arrayList=resultList
                }
                val filterResults=FilterResults()
                filterResults.values=arrayList
                return filterResults

            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                arrayList=results?.values as ArrayList<CategoryName>
                notifyDataSetChanged()
                /*if(list.size>0){
                    HomeFragment.tv_Notfound.visibility= View.GONE
                }else{
                    HomeFragment.tv_Notfound.visibility= View.VISIBLE
                }*/

                }
              }
            }


        }