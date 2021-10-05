package com.bintyblackbook.service

import android.app.*
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.bintyblackbook.R
import com.bintyblackbook.ui.activities.home.notification.BookingRequestActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject

class FbMessagingService : FirebaseMessagingService() {
    var objectBody: JSONObject? = null
    var message: String? = ""
    var orderId: String? = ""
    var recieverName = ""
    var recieverImage = ""
    var bookingId: String? = ""
    var bookId = ""
    var type: Int? = null
    var status: Int? = null
    var receiverId: Int? = null

    //    for oreo
    var CHANNEL_ID = "" // The id of the channel.
    var CHANNEL_ONE_NAME = "Channel One"
    var notificationManager: NotificationManager? = null
    var notificationChannel: NotificationChannel? = null
    var notification: Notification? = null

    override fun onNewToken(refreshedToken: String) {
        super.onNewToken(refreshedToken)
        Log.d(TAG, "Refreshed token: $refreshedToken")
        sendRegistrationToServer(refreshedToken)
    }

    private fun sendRegistrationToServer(token: String) {
        Log.e("device_token", token)
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.e("firebase","${remoteMessage.data}")
        manager
        CHANNEL_ID = applicationContext.packageName
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_ONE_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel?.enableLights(true)
            notificationChannel?.lightColor = Color.RED
            notificationChannel?.setShowBadge(true)
            notificationChannel?.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }
        try {

            objectBody = JSONObject(remoteMessage.data["body"].toString())
            recieverName = objectBody!!.getString("senderName")
            recieverImage = objectBody!!.getString("senderImage")
            bookId = objectBody!!.getString("bookingId")
            receiverId = objectBody!!.getInt("userId")

            message = objectBody?.getString("message")
            bookingId = objectBody?.getString("bookingId")
            orderId = remoteMessage.data["orderId"]
            type = objectBody?.getInt("type")
            status = Integer.valueOf(remoteMessage.data["status"])
            Log.e("pushData", remoteMessage.data.toString())
            sendMessagePush(message, bookingId)
        } catch (e: Exception) {
            type = 5429
            status = 5429
            e.printStackTrace()
            Log.e("exception", e.toString())
        }
    }

    private fun sendMessagePush(message: String?, booking_id: String?) {
        Log.e("=====",type.toString())
        var intent: Intent? = null
        if(type==4){
            intent = Intent(baseContext, BookingRequestActivity::class.java)
            intent.putExtra("message",message)
            intent.putExtra("user_id",receiverId.toString())

        }

        intent?.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        val pendingIntent = PendingIntent.getActivity(this, i, intent, PendingIntent.FLAG_ONE_SHOT)
        val icon1 = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = Notification.Builder(
            applicationContext
        )
            .setSmallIcon(notificationIcon).setLargeIcon(icon1)
            .setStyle(Notification.BigTextStyle().bigText(message))
            .setColor(ContextCompat.getColor(applicationContext, R.color.colorAccent))
            .setContentTitle("New message received")
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setChannelId(CHANNEL_ID)
            notificationManager!!.createNotificationChannel(notificationChannel!!)
        }
        notification = notificationBuilder.build()
        notificationManager!!.notify(i++, notification)

        val i = Intent("msg") //action: "msg"
        i.setPackage(packageName)
        i.putExtra("message", "uu")
        i.putExtra("type", type.toString())
        applicationContext.sendBroadcast(i)
    }


    private val manager: NotificationManager?
        private get() {
            if (notificationManager == null) {
                notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            }
            return notificationManager
        }




    private val notificationIcon: Int
        private get() {
            val useWhiteIcon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
            return if (useWhiteIcon) R.mipmap.ic_launcher else R.mipmap.ic_launcher
        }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
        private var i = 0
    }
}