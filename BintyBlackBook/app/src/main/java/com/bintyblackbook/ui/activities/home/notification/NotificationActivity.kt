package com.bintyblackbook.ui.activities.home.notification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bintyblackbook.R
import com.bintyblackbook.adapters.NotificationAdapter
import com.bintyblackbook.models.NotificationModel
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationActivity : AppCompatActivity() {

    var notificationAdapter:NotificationAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        rlBack.setOnClickListener {
            finish()
        }

        val arrayList = ArrayList<NotificationModel>()
        arrayList.add(NotificationModel("Hi John, You have a new message","newMessage"))
        arrayList.add(NotificationModel("Alisha has send loop Request","loopRequest"))
        arrayList.add(NotificationModel("Joy has send Booking Request","bookingRequest"))

        notificationAdapter = NotificationAdapter(this,arrayList)
        rvNotification.adapter = notificationAdapter
        adapterItemClick()
    }

    private fun adapterItemClick(){
        notificationAdapter?.onItemClick = {notificationModel: NotificationModel ->
            if (notificationModel.notificationType == "loopRequest"){
                startActivity(Intent(this,LoopRequestActivity::class.java))
            }else  if (notificationModel.notificationType == "bookingRequest"){
                startActivity(Intent(this,BookingRequestActivity::class.java))
            }
        }
    }
}