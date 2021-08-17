package com.bintyblackbook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.model.CommentData

class CommentsAdapter(var context: Context) :
    RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {

    var arrayList= ArrayList<CommentData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_comments, parent, false)
        return CommentsViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class CommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}