package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.AllUsersData
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.row_myloops.view.*

class SearchLoopsAdapter(val context: Context) : RecyclerView.Adapter<SearchLoopsAdapter.LoopRequestViewHolder>() {

    var loopList= ArrayList<AllUsersData>()
    lateinit var searchLoopInterface:SearchLoopInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoopRequestViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_myloops, parent, false)
        return LoopRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: LoopRequestViewHolder, position: Int) {
        holder.bind(loopList[position],position)
    }

    override fun getItemCount(): Int {
        return loopList.size
    }

    inner class LoopRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: AllUsersData, pos: Int) {
           // 0: Not a Loop, 1: Loop Request Sent , 2: Loop, 3: Received a loop request

            if (data.isLoop == 0) {
                itemView.ll_loops.visibility = View.VISIBLE
                itemView.ll_request.visibility = View.GONE
                itemView.tv_name.text = data.firstName
                Glide.with(context).load(data.image).into(itemView.civ_profile)
                itemView.btnUnLoop.text="Loop"

            }else if (data.isLoop == 1) {
                itemView.ll_loops.visibility = View.VISIBLE
                itemView.ll_request.visibility = View.GONE
                itemView.tv_name.text = data.firstName
                Glide.with(context).load(data.image).into(itemView.civ_profile)
                itemView.btnUnLoop.text="Cancel Loop Request"

            }
            else if (data.isLoop == 2) {
                itemView.ll_loops.visibility = View.VISIBLE
                itemView.ll_request.visibility = View.GONE
                itemView.tv_name.text = data.firstName
                Glide.with(context).load(data.image).into(itemView.civ_profile)
                itemView.btnUnLoop.text="UnLoop"

            } else if(data.isLoop==3) {

                itemView.ll_loops.visibility = View.GONE
                itemView.ll_request.visibility = View.VISIBLE
                itemView.tv_request.text = data.firstName + " " + "has sent you loop request"
                Glide.with(context).load(data.image).into(itemView.civ_profile)
            }

            itemView.btnAcceptReq.setOnClickListener {
                searchLoopInterface.acceptRejectRequest(data,pos,"2")
            }
            itemView.btnCancelReq.setOnClickListener {
                searchLoopInterface.acceptRejectRequest(data,pos,"0")
            }

            itemView.btnUnLoop.setOnClickListener {
                if (data.isLoop==0) {
                   // itemView.btnUnLoop.text="Un loop"
                    searchLoopInterface.sendLoopRequest(data,pos)
                }
                else if(data.isLoop==1){
                    searchLoopInterface.acceptRejectRequest(data,pos,"0")
                }
                else {
                   // itemView.btnUnLoop.text="Loop"
                    searchLoopInterface.onUnLoopRequest(data,pos)
                }

                notifyItemChanged(pos)
            }
        }
    }

    interface SearchLoopInterface{
        fun sendLoopRequest(data: AllUsersData,position: Int)
        fun onUnLoopRequest(data: AllUsersData,position: Int)
        fun acceptRejectRequest(data:AllUsersData,position: Int,status:String)
    }
}