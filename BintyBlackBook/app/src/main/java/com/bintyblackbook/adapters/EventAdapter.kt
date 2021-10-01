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
import com.bintyblackbook.model.HomeData
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.android.synthetic.main.item_events.view.*
import java.util.*
import kotlin.collections.ArrayList


class EventAdapter(var context: Context, var list: ArrayList<EventData>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>(), Filterable {
    var arrayList = ArrayList<EventData>()
    var clicked=true

    lateinit var eventAdapterInterface:EventAdapterInterface
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

            if(!eventsModel.image.isNullOrEmpty()){
                Glide.with(context).load(eventsModel.image).into(roundedImageView)
            }

            tvName.text = eventsModel.name
            tvLocation.text = eventsModel.location

            if (eventsModel.isFavourite == 1) {
                clicked = false
                ivHeart.setImageResource(R.drawable.fill_heart)
            } else {
                clicked = true
                ivHeart.setImageResource(R.drawable.unfill_heart)
            }
            ivHeart.setOnClickListener {
                if (clicked) {
                    clicked = false
                    ivHeart.setImageResource(R.drawable.fill_heart)
                    eventAdapterInterface.onSelectFav(arrayList[pos],"1")

                } else {
                    clicked = true
                    ivHeart.setImageResource(R.drawable.unfill_heart)
                    eventAdapterInterface.onSelectFav(arrayList[pos],"0")
                }
            }

            itemView.setOnClickListener {
                eventAdapterInterface.onItemClick(arrayList[pos])
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
                    val resultList = ArrayList<EventData>()
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
                arrayList = results?.values as ArrayList<EventData>
                notifyDataSetChanged()
            }

        }
    }

    interface EventAdapterInterface{
        fun onSelectFav(data:EventData,status:String)
        fun onItemClick(data: EventData)
    }

}