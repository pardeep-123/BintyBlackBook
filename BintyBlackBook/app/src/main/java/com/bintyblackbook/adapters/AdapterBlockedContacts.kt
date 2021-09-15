package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.BlockedData
import com.bintyblackbook.model.CommentData
import com.bintyblackbook.ui.activities.home.settings.BlockedContactsActivity
import com.bintyblackbook.ui.dialogues.BlockUserDialogFragment
import com.bintyblackbook.util.MyUtils
import com.bintyblackbook.util.getUser
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_comments.view.*
import kotlinx.android.synthetic.main.row_blocked_contacts.view.*
import kotlinx.android.synthetic.main.row_blocked_contacts.view.civ_profile

class AdapterBlockedContacts(val context: Context): RecyclerView.Adapter<AdapterBlockedContacts.MyViewHolder>() {

    var arrayList= ArrayList<BlockedData>()

    lateinit var blockInterface: BlockAdapterInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_blocked_contacts, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image: CircleImageView= itemView.civ_profile
        val userName: TextView= itemView.tv_name
        val tvUnblock:TextView= itemView.tvUnblock

        fun bind(pos: Int) {
            val data=arrayList[pos]
            Glide.with(context).load(data.otherUserImage).into(image)
            userName.text= data.otherUserName
            tvUnblock.setOnClickListener {
                blockInterface.onUnblockUser(arrayList[pos],pos,"0")
            }

        }
    }

    interface BlockAdapterInterface{
        fun onUnblockUser(data: BlockedData, position: Int,status:String)
    }
}