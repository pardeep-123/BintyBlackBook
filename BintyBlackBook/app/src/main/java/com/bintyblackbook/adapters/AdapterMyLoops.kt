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
import com.bintyblackbook.ui.activities.home.loop.MyLoopsActivity
import com.bintyblackbook.ui.activities.home.UserDetailActivity
import com.bintyblackbook.ui.dialogues.UnLoopDialogFragment
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.row_myloops.view.*

class AdapterMyLoops(val context: Context) : RecyclerView.Adapter<AdapterMyLoops.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.row_myloops, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val civProfile: CircleImageView = itemView.civ_profile
        val tvName: TextView = itemView.tv_name
        val btnUnLoop: Button = itemView.btnUnLoop

        fun bind(pos: Int) {
            itemView.setOnClickListener {
                context.startActivity(Intent(context, UserDetailActivity::class.java))
            }

            btnUnLoop.setOnClickListener {
                val fragmentDialog = UnLoopDialogFragment()
                fragmentDialog.show((context as MyLoopsActivity).supportFragmentManager,"LoopDialog")
            }
        }
    }
}