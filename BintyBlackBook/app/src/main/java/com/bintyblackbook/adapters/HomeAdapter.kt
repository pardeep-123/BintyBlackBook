package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.HomeData
import com.bintyblackbook.models.HomeModel
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager
import kotlinx.android.synthetic.main.item_home.view.*

class HomeAdapter(val context: Context) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
     var arrayList= ArrayList<HomeData>()
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

        fun bind(pos: Int) {
            val model = arrayList[pos]
            tvHeading.text = model.name

            val infinityPagerAdapter = InfinityPagerAdapter(context, model.categoryName)
            horizontal_cycle.adapter = infinityPagerAdapter

            itemView.setOnClickListener {
                onItemClick?.invoke(model)
            }

            infinityPagerAdapter.onInfinityPagerItemClick = {infinityPagerItemPosition: Int ->
                onItemClick?.invoke(model)
            }
        }
    }
}