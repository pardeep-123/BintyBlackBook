package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.LoopRequestData
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_view_accounts.view.*

class AccountsAdapter(var context: Context) : RecyclerView.Adapter<AccountsAdapter.AccountsViewHolder>() {

    var loopList= ArrayList<LoopRequestData>()
    lateinit var loopRequestInterface:LoopRequestInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_view_accounts, parent, false)
        return AccountsViewHolder(view)
    }

    override fun onBindViewHolder(holder: AccountsViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return loopList.size
    }

    inner class AccountsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(pos: Int) {
            val data= loopList[pos]
            Glide.with(context).load(data.userImage).into(itemView.imgUser)
            itemView.tvUserName.text=""
        }
    }

    interface LoopRequestInterface{
        fun onItemClick(status: String, data: LoopRequestData)
    }
}