package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.Data
import com.bintyblackbook.model.LoopRequestData
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_view_accounts.view.*

class AccountsAdapter(var context: Context) : RecyclerView.Adapter<AccountsAdapter.AccountsViewHolder>() {

    var list= ArrayList<Data>()
    lateinit var accountInterface: AccountInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_view_accounts, parent, false)
        return AccountsViewHolder(view)
    }

    override fun onBindViewHolder(holder: AccountsViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class AccountsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(pos: Int) {
            val data= list[pos]
            Glide.with(context).load(data.image).into(itemView.imgUser)
            itemView.tvUserName.text=data.firstName

            itemView.setOnClickListener {
                accountInterface.onItemClick(list[pos])
            }
        }
    }

    interface AccountInterface{
        fun onItemClick(data:Data)
    }
}