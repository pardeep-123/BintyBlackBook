package com.bintyblackbook.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.AllData
import com.bintyblackbook.model.Suggested
import com.bintyblackbook.ui.activities.home.loop.MyLoopsActivity
import com.bintyblackbook.ui.activities.home.UserDetailActivity
import com.bintyblackbook.ui.dialogues.UnLoopDialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.row_myloops.view.*

class AdapterMyLoops(val context: Context) : RecyclerView.Adapter<AdapterMyLoops.MyViewHolder>() {

    var list= ArrayList<AllData>()
    lateinit var loopsInterface:LoopsInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.row_myloops, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val civProfile: CircleImageView = itemView.civ_profile
        val tvName: TextView = itemView.tv_name
        val btnUnLoop: Button = itemView.btnUnLoop
        val btnAccept:Button= itemView.btnAcceptReq
        val btnCancel:Button= itemView.btnCancelReq

         // 0: Not a Loop, 1: Loop Request Sent , 2: Loop, 3: Received a loop request

        fun bind(pos: Int) {


            if(list[pos].status==1){
                itemView.ll_loops.visibility=View.GONE
                itemView.ll_request.visibility=View.VISIBLE
                itemView.tv_request.text= list[pos].userName +" "+"has sent you loop request"

                Glide.with(context).load(list[pos].userImage).into(civProfile)
            }
            else{
                itemView.ll_loops.visibility=View.VISIBLE
                itemView.ll_request.visibility=View.GONE
                tvName.text=list[pos].userName
                Glide.with(context).load(list[pos].userImage).into(civProfile)
            }

            itemView.setOnClickListener {
                loopsInterface.onItemClick(list[pos],pos)
               // context.startActivity(Intent(context, UserDetailActivity::class.java))
            }

            btnUnLoop.setOnClickListener {
                loopsInterface.unLoop(list[pos],pos)
//                val fragmentDialog = UnLoopDialogFragment()
//                fragmentDialog.show((context as MyLoopsActivity).supportFragmentManager,"LoopDialog")
            }

            btnAccept.setOnClickListener {
                loopsInterface.acceptRequest(list[pos],"2")
            }

            btnCancel.setOnClickListener {
                loopsInterface.cancelRequest(list[pos],"0")
            }
        }
    }

    interface LoopsInterface{
        fun onItemClick(data:AllData,position: Int)
        fun unLoop(data: AllData,position: Int)
        fun acceptRequest(data:AllData,status:String)
        fun cancelRequest(data:AllData,status: String)
    }
}