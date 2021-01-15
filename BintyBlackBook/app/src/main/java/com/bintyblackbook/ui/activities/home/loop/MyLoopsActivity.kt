package com.bintyblackbook.ui.activities.home.loop

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bintyblackbook.R
import com.bintyblackbook.adapters.AdapterMyLoops
import kotlinx.android.synthetic.main.activity_my_loops.*
import kotlinx.android.synthetic.main.toolbar.*

class MyLoopsActivity : AppCompatActivity(), View.OnClickListener {
    val context: Context =this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_loops)
        headingText.setText("MY LOOPS")
        iv_back.setOnClickListener(this)
        rvMyLoops.adapter = AdapterMyLoops(context)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_back -> {
                finish()
            }
        }
    }
}