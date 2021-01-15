package com.bintyblackbook.ui.activities.home.message

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bintyblackbook.R
import com.bintyblackbook.adapters.MessagesAdapter
import com.bintyblackbook.models.EditMessageModel
import kotlinx.android.synthetic.main.activity_messages.*

class MessagesActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var arrayList: ArrayList<EditMessageModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        rlBack.setOnClickListener(this)
        rlEdit.setOnClickListener(this)

        arrayList = ArrayList()
        arrayList.add(EditMessageModel(R.drawable.bamie, "John","10 min ago", false))
        arrayList.add(EditMessageModel(R.drawable.soph, "Jian","10 min ago", false))
        arrayList.add(EditMessageModel(R.drawable.robert, "Malli","10 min ago", false))

        rvMessages.adapter = MessagesAdapter(this,arrayList)
    }
    override fun onClick(view: View) {
        when (view.id) {
            R.id.rlBack -> {
                finish()
            }
            R.id.rlEdit -> {
                startActivity(Intent(this,EditMessageActivity::class.java))
            }
        }
    }
}