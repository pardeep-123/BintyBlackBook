package com.bintyblackbook.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.bintyblackbook.R
import com.bintyblackbook.ui.activities.home.HomeActivity
import com.bintyblackbook.ui.activities.home.loop.MyLoopsActivity
import com.bintyblackbook.ui.activities.home.message.ChatActivity
import com.bintyblackbook.ui.activities.home.notification.BookingRequestActivity
import com.bintyblackbook.ui.activities.home.notification.LoopRequestActivity
import com.bintyblackbook.util.saveToken
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject
import java.util.*


class MyFirebaseMessagingService : FirebaseMessagingService() {
    var TAG = "FirebaseService"
    var title = ""
    var message = ""
    var notification_code = 0
    var jobId = ""
    var oppId = ""
    var oppName = ""
    var orderId: String? = ""
    var recieverName = ""
    var recieverImage = ""
    var bookingId: String? = ""
    var bookId = ""
    var type: Int? = null
    var status: Int? = null
    var receiverId: Int? = null
    var objectBody: JSONObject? = null

    companion object {
        var isChatNotOpened = true
    }

    //for oreo
    private var CHANNEL_ID = "cnid"
    private var CHANNEL_ONE_NAME = "Notifications"

    var notificationManager: NotificationManager? = null
    lateinit var notificationChannel: NotificationChannel
    lateinit var notification: Notification

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        message = ""
        Log.d(TAG, "MyTestingLog: ${remoteMessage.data}")
        if (remoteMessage.data != null && remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            try {
                if (!remoteMessage.data["body"].equals("Test Notification"))
                    createNotification(remoteMessage)
            } catch (ex: Exception) {
                ex.message?.let { Log.e("NotificationFcm", it) }
            }
        }

        remoteMessage.notification?.let {
            Log.d(TAG, "Message data payload Notification: ${it.body}")
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
         saveToken(this,p0)  //save firebase token
    }

    private fun createNotification(remoteMessage: RemoteMessage) {
        if (notificationManager == null)
            notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val importance = NotificationManager.IMPORTANCE_HIGH
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_ONE_NAME,
                importance
            )
            notificationChannel.enableVibration(true)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.enableVibration(true)
            notificationChannel.setShowBadge(true)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        }

        try {
            title = getString(R.string.app_name)//Setting APP Name as Title
            objectBody = JSONObject(remoteMessage.data["body"].toString())
            recieverName = objectBody!!.getString("senderName")
            recieverImage = objectBody!!.getString("senderImage")
            bookId = objectBody!!.getString("bookingId")
            receiverId = objectBody!!.getInt("userId")

            message = objectBody?.getString("message")!!
            bookingId = objectBody?.getString("bookingId")
            orderId = remoteMessage.data["orderId"]
            type = objectBody?.getInt("type")
            status = Integer.valueOf(remoteMessage.data["status"])


        } catch (ex: Exception) {
            ex.message?.let { Log.e("FirebaseRam", it) }
        }
        var intent = Intent()

         if (type == 1) {
            intent = Intent(this, LoopRequestActivity::class.java)
            intent.putExtra("message", message)
            intent.putExtra("user_id", receiverId.toString())
        } else if (type == 2) {
            intent = Intent(this, MyLoopsActivity::class.java)
        } else if (type == 3) {
            intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("type", "0")
            intent.putExtra("sender_id", receiverId.toString())
            intent.putExtra("name", recieverName)
             intent.putExtra("isGroup","0")
        } else if (type == 4) {
             intent = Intent(this, BookingRequestActivity::class.java)
             intent.putExtra("message", message)
             intent.putExtra("user_id", receiverId.toString())
         }else if (type == 5) {
             intent = Intent(this, ChatActivity::class.java)
             intent.putExtra("type","1")
             intent.putExtra("sender_id", receiverId.toString())
             intent.putExtra("name", recieverName)
             intent.putExtra("isGroup","0")
         }


        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
            applicationContext, CHANNEL_ID
        )
            .setSmallIcon(R.mipmap.app_icon)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setColor(ContextCompat.getColor(applicationContext, R.color.themeColor))
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            notificationBuilder.setChannelId(CHANNEL_ID)
            notificationManager!!.createNotificationChannel(notificationChannel)
        }

        if (message.isNotBlank() && isChatNotOpened) {
            notificationBuilder.build().flags =
                notificationBuilder.build().flags or Notification.FLAG_AUTO_CANCEL
            notification = notificationBuilder.build()
            notificationManager!!.notify(
                ((Date().getTime() / 1000L % Int.MAX_VALUE).toInt()),
                notification
            )
        }
    }
}