package com.bintyblackbook.ui.activities.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bintyblackbook.R
import com.bintyblackbook.util.AppConstant
import kotlinx.android.synthetic.main.activity_event_detail.*
import kotlinx.android.synthetic.main.toolbar.*

class EventDetailActivity : AppCompatActivity() {

    var heartSelected = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        val heading = intent.getStringExtra(AppConstant.HEADING)

        iv_back.setOnClickListener {
            finish()
        }

        if (heading != null) {
            headingText.text = heading
        }

        rlHeart.setOnClickListener {
            if (heartSelected) {
                heartSelected = false
                ivHeart.setImageResource(R.drawable.like)
            } else {
                heartSelected = true
                ivHeart.setImageResource(R.drawable.heart_new)
            }
        }
    }
}