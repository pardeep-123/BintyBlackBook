package com.bintyblackbook.ui.activities.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bintyblackbook.R
import com.bintyblackbook.adapters.NotificationAdapter
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        rlBack.setOnClickListener {
            finish()
        }

        rvNotification.adapter = NotificationAdapter(this)
    }
}