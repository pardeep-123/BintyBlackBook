package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.models.EditMessageModel
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_edit_message.view.*

class EditMessagesAdapter(val context: Context, var arrayList: ArrayList<EditMessageModel>) :
    RecyclerView.Adapter<EditMessagesAdapter.EditMessagesViewHolder>() {

    var onItemClick:((editMessageModel:EditMessageModel)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditMessagesViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_edit_message, parent, false)
        return EditMessagesViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: EditMessagesViewHolder, position: Int) {
        holder.bind(position)
    }

   inner class EditMessagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProfile: CircleImageView = itemView.civProfile
        val tvName: TextView = itemView.tv_name
        val cb: CheckBox = itemView.cb

        fun bind(pos: Int) {
            val editMessageModel = arrayList[pos]

            ivProfile.setImageResource(editMessageModel.image!!)
            tvName.text = editMessageModel.name

            cb.setOnCheckedChangeListener { compoundButton, boolean ->
                if (boolean){
                    editMessageModel.selected = boolean
                    onItemClick?.invoke(editMessageModel)
                }else{
                    editMessageModel.selected = boolean
                    onItemClick?.invoke(editMessageModel)
                }

                notifyDataSetChanged()
            }
        }
    }
}