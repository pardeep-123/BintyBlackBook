package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.Suggested
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_view_sub_categories.view.*
import kotlinx.android.synthetic.main.item_view_suggestions.view.*

class SuggestionsAdapter (var context: Context,val list: ArrayList<Suggested>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SuggestionsAdapter.MyViewHolder(inflater.inflate(R.layout.item_view_suggestions, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MyViewHolder).bindItems(list[position], position)
    }

    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        fun bindItems(data: Suggested, position: Int, ) {

            itemView.apply {
                tvUserName.text = data.userName
                Glide.with(context).load(data.userImage).into(img_user)

            }

        }
    }

}