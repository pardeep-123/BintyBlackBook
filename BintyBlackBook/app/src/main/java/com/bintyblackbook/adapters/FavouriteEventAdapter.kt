package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.EventData
import com.bintyblackbook.model.FavEventData
import com.bintyblackbook.ui.activities.home.eventCalender.EventCalenderActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.android.synthetic.main.item_events.view.*
import java.util.*
import kotlin.collections.ArrayList

class FavouriteEventAdapter(var context: Context, var list: ArrayList<FavEventData>) :
    RecyclerView.Adapter<FavouriteEventAdapter.EventViewHolder>(), Filterable {

    var clicked = true
    var arrayList = ArrayList<FavEventData>()
    lateinit var favEventInterface: FavEventInteface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_events, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val roundedImageView: RoundedImageView = itemView.roundedImageView
        private val tvName: TextView = itemView.tvName
        private val tvLocation: TextView = itemView.tvLocation
        private val ivHeart: ImageView = itemView.ivHeart

        fun bind(pos: Int) {
            val eventsModel = arrayList[pos]

            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.progress_animation)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)

            Glide.with(context).load(eventsModel.image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(options)
                .into(roundedImageView)
            tvName.text = eventsModel.name
            tvLocation.text = eventsModel.location

            ivHeart.setOnClickListener {
                ivHeart.setImageResource(R.drawable.fill_heart)
                favEventInterface.onUnFavEvent("0", arrayList[pos], pos)
            }
            itemView.setOnClickListener {
                favEventInterface.onFavItemClick(arrayList[pos])
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    arrayList = list
                } else {
                    val resultList = ArrayList<FavEventData>()
                    for (row in list) {
                        if (row.name.toUpperCase(Locale.US).contains(
                                constraint.toString().toUpperCase(
                                    Locale.US
                                )
                            )

                            || row.name.toLowerCase(Locale.US).contains(
                                constraint.toString().toLowerCase(
                                    Locale.US
                                )
                            )
                            || row.location.toUpperCase(Locale.US).contains(
                                constraint.toString().toUpperCase(
                                    Locale.US
                                )
                            )
                            || row.location.toLowerCase(Locale.US).contains(
                                constraint.toString().toLowerCase(
                                    Locale.US
                                )
                            )
                        ) {
                            resultList.add(row)
                        }
                    }
                    arrayList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = arrayList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                arrayList = results?.values as ArrayList<FavEventData>
                if(arrayList.size>0){
                    EventCalenderActivity.tvNotfound.visibility= View.GONE
                }else{
                    EventCalenderActivity.tvNotfound.visibility= View.VISIBLE
                }
                notifyDataSetChanged()
            }

        }
    }

    interface FavEventInteface {
        fun onUnFavEvent(status: String, data: FavEventData, position: Int)

        fun onFavItemClick(data:FavEventData)
    }
}