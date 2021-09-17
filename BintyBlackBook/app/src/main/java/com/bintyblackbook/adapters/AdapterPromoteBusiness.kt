package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.PromotionData
import kotlinx.android.synthetic.main.row_promote_business.view.*

class AdapterPromoteBusiness(val context: Context,val screen_type:String) : RecyclerView.Adapter<AdapterPromoteBusiness.MyViewHolder>() {

    var arrayList= ArrayList<PromotionData>()
    lateinit var promoteAdapterInterface: PromoteAdapterInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.row_promote_business, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position)
    }
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int){
            val data= arrayList[position]
            itemView.tvType.text= data.subscription_type
            itemView.tvPrice.text= data.price
            itemView.setOnClickListener {
                promoteAdapterInterface.onItemClick(data,position,screen_type)
            }
        }
    }

    interface PromoteAdapterInterface{
        fun onItemClick(data: PromotionData, position: Int, screen_type: String)
    }
}