package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.AllUsersData
import com.bintyblackbook.model.LoopRequestData
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_loop_request.view.btnAccept
import kotlinx.android.synthetic.main.item_loop_request.view.btnCancel
import kotlinx.android.synthetic.main.item_view_loop_requets.view.*

class SearchRequestAdapter(val context: Context): RecyclerView.Adapter<SearchRequestAdapter.LoopRequestViewHolder>() {

    var loopList= ArrayList<AllUsersData>()
    lateinit var loopRequestInterface:LoopRequestInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoopRequestViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_view_loop_requets, parent, false)
        return LoopRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: LoopRequestViewHolder, position: Int) {
        holder.bind(loopList[position],position)
    }

    override fun getItemCount(): Int {
        return loopList.size
    }

    inner class LoopRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data:AllUsersData, pos: Int) {

            // 0: Not a Loop, 1: Loop Request Sent , 2: Loop, 3: Received a loop request

            if(data.isLoop==1) {
                itemView.layout_request.visibility=View.VISIBLE
                itemView.tvName.text = data.firstName
                Glide.with(context).load(data.image).into(itemView.ivProfile)
            }
            else{
                itemView.layout_request.visibility= View.GONE
            }


            itemView.btnAccept.setOnClickListener {
                loopRequestInterface.acceptRequest("2",data)
            }

            itemView.btnCancel.setOnClickListener {
                loopRequestInterface.cancelRequest("0",data)

            }
        }
    }

    interface LoopRequestInterface{
        fun acceptRequest(status: String, data: AllUsersData)
        fun cancelRequest(status:String, data:AllUsersData)
    }
}