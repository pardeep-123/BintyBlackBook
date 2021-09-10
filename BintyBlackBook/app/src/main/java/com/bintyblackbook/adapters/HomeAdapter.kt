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
import com.bintyblackbook.model.HomeData
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager
import kotlinx.android.synthetic.main.item_home.view.*
import java.util.*
import kotlin.collections.ArrayList

class HomeAdapter(val context: Context, var list: ArrayList<HomeData>) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>(), Filterable {
    var arrayList = ArrayList<HomeData>()
    var onItemClick: ((homeModel: HomeData) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_home, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHeading: TextView = itemView.tvHeading
        val horizontal_cycle: HorizontalInfiniteCycleViewPager = itemView.horizontal_cycle

        fun bind(position: Int) {
            val model = arrayList[position]
            tvHeading.text = model.name

            val infinityPagerAdapter = InfinityPagerAdapter(context, model.categoryName)
            horizontal_cycle.adapter = infinityPagerAdapter

            itemView.setOnClickListener {
                onItemClick?.invoke(arrayList[position])
            }

            infinityPagerAdapter.onInfinityPagerItemClick = { infinityPagerItemPosition: Int ->
                onItemClick?.invoke(arrayList[position])
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
                    val resultList = ArrayList<HomeData>()
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
                arrayList = results?.values as ArrayList<HomeData>
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