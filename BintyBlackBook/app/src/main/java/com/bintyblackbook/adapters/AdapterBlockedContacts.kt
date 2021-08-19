package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.BlockedData
import com.bintyblackbook.ui.activities.home.settings.BlockedContactsActivity
import com.bintyblackbook.ui.dialogues.BlockUserDialogFragment
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.row_blocked_contacts.view.*

class AdapterBlockedContacts(val context: Context) : RecyclerView.Adapter<AdapterBlockedContacts.MyViewHolder>() {

    var arrayList= ArrayList<BlockedData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_blocked_contacts, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvUnblock.setOnClickListener {
            val dialog = BlockUserDialogFragment("unBlockUser")
            dialog.show((context as BlockedContactsActivity).supportFragmentManager,"unBlock")
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var civ_profile:CircleImageView = itemView.civ_profile
        var tv_name:TextView = itemView.tv_name
        var tvUnblock:TextView = itemView.tvUnblock
    }
}