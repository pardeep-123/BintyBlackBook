package com.bintyblackbook.ui.activities.home.timeline

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.CommentsAdapter
import kotlinx.android.synthetic.main.activity_comments.*

class CommentsActivity : AppCompatActivity() {

    var commentsAdapter:CommentsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        rlBack.setOnClickListener {
            finish()
        }

        rvComments.layoutManager = LinearLayoutManager(this)
        commentsAdapter = CommentsAdapter(this)
        rvComments.adapter = commentsAdapter
    }
}