package com.bintyblackbook.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.bintyblackbook.R
import com.bintyblackbook.ui.activities.home.loop.MyLoopsActivity
import com.bintyblackbook.ui.activities.home.message.ChatActivity
import com.bintyblackbook.ui.activities.home.message.IncomingCallActivity
import com.bintyblackbook.ui.activities.home.message.VideoCallBroadCastReceiver
import com.bintyblackbook.ui.activities.home.notification.BookingRequestActivity
import com.bintyblackbook.ui.activities.home.notification.LoopRequestActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject

class NotificationHandlingService : FirebaseMessagingService() {
    private val TAG = "FireBasePush"
    private var i = 0
    var title = ""
    var message: String? = ""
    var type: String? = ""
    var CHANNEL_ID = "BintyBlackBook"

    var id = ""
    var productId = ""
    var name = ""
    var image = ""
    var senderId = ""
    var orderId: String? = ""
    var recieverName = ""
    var recieverImage = ""
    var bookingId: String? = ""
    var bookId = ""
    var status: Int? = null
    var receiverId: Int? = null
    var senderName = ""
    lateinit var soundUri: Uri
    var objectBody: JSONObject? = null
    var channel_name = ""


    override fun onNewToken(refreshedToken: String) {
        super.onNewToken(refreshedToken)
        Log.e(TAG, "Refreshed token: $refreshedToken")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.e(TAG, "Notification: ${remoteMessage.data}")
        Log.e(TAG, "Notification---: ${remoteMessage.data["body"]}")

        type = remoteMessage.data["type"]
        message = remoteMessage.data["message"]
        title = remoteMessage.data["title"].toString()
        val jsonObj = JSONObject(remoteMessage.data["body"].toString())
        val pushType = jsonObj.getInt("type")

        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val componentName = am.getRunningTasks(1)[0].topActivity
        val className = componentName?.className
        try {
            title = getString(R.string.app_name)//Setting APP Name as Title
            objectBody = JSONObject(remoteMessage.data["body"].toString())
            recieverName = objectBody!!.getString("senderName")
            recieverImage = objectBody!!.getString("senderImage")
            bookId = objectBody!!.getString("bookingId")
            receiverId = objectBody!!.getInt("userId")
            channel_name = objectBody!!.getString("channelName")

            message = objectBody?.getString("message")!!
            bookingId = objectBody?.getString("bookingId")
            orderId = remoteMessage.data["orderId"]
            status = Integer.valueOf(remoteMessage.data["status"])


        } catch (ex: Exception) {
            ex.message?.let { Log.e("FirebaseRam", it) }
        }
        var intent = Intent()

        if (pushType == 1) {
            intent = Intent(this, LoopRequestActivity::class.java)
            intent.putExtra("message", message)
            intent.putExtra("user_id", receiverId.toString())
            makePush(intent)
        } else if (pushType == 2) {
            intent = Intent(this, MyLoopsActivity::class.java)
            makePush(intent)
        } else if (pushType == 3) {
            intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("type", "0")
            intent.putExtra("sender_id", receiverId.toString())
            intent.putExtra("name", recieverName)
            intent.putExtra("isGroup", "0")
            makePush(intent)
        } else if (pushType == 4) {
            intent = Intent(this, BookingRequestActivity::class.java)
            intent.putExtra("message", message)
            intent.putExtra("user_id", receiverId.toString())
            makePush(intent)
        } else if (pushType == 5) {
            intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("type", "1")
            intent.putExtra("sender_id", receiverId.toString())
            intent.putExtra("name", recieverName)
            intent.putExtra("isGroup", "0")
            makePush(intent)
        } else if (pushType == 7) {
            val jsonObj = JSONObject(remoteMessage.data["body"].toString())
//            intent = Intent(this, VideoCallActivity::class.java)
//            intent.putExtra("channelName",channel_name)
//            intent.putExtra("otheruserId",receiverId.toString())
//            makePush(intent)
            createCallNotification(jsonObj)
        }

    }

    fun createCallNotification(map: JSONObject) {
        //if (map["isVideo"] == "true") {

        val intentAction = Intent(this, IncomingCallActivity::class.java)
        //This is optional if you have more than one buttons and want to differentiate between two
        intentAction.putExtra("data", map.toString())
        intentAction.putExtra("recieverID", receiverId);
        intentAction.putExtra("channelName", channel_name);
        intentAction.putExtra("recieverName", recieverName)
        intentAction.putExtra("senderImage", recieverImage)

        val acceptCallIntent = Intent(this, VideoCallBroadCastReceiver::class.java)
        acceptCallIntent.putExtra("type", "call")
        acceptCallIntent.putExtra("recieverID", receiverId)
        acceptCallIntent.putExtra("channelName", channel_name)

        acceptCallIntent.putExtra("recieverName", recieverName)
        acceptCallIntent.putExtra("senderImage", recieverImage)
        val rejectCallIntent = Intent(this, VideoCallBroadCastReceiver::class.java)
        rejectCallIntent.putExtra("type", "reject")
        rejectCallIntent.putExtra("recieverName", recieverName)
        rejectCallIntent.putExtra("senderImage", recieverImage)
        rejectCallIntent.putExtra("recieverID", receiverId);
        rejectCallIntent.putExtra("channelName", channel_name);
        val contentIntent = PendingIntent.getActivity(
            this,
            receiverId!!,
            intentAction,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val acceptCall =
            PendingIntent.getBroadcast(this, 1, acceptCallIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val rejectCall =
            PendingIntent.getBroadcast(this, 2, rejectCallIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(
            this,
            channelId
        ) ///.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
            .setSmallIcon(R.mipmap.app_icon)
            .setContentTitle("Incoming call...")
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setOngoing(true)
            .addAction(0, "Accept", acceptCall)
            .addAction(0, "Reject", rejectCall)
            .setContentIntent(contentIntent)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (notificationManager != null) {
            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    getString(R.string.default_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationManager.createNotificationChannel(channel)
            }
            notificationManager.notify(1, notificationBuilder.build())
        }
        /* } else if (map["Msg"] == "Rejected") {
             val rejectCallIntent = Intent(this, VideoCallBroadCastReceiver::class.java)
             rejectCallIntent.putExtra("notificationId",1)
             startActivity(rejectCallIntent)
         }*/
        // }
        //This is the intent of PendingIntent
    }

    private fun makePush(intent: Intent?) {
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, i, intent, PendingIntent.FLAG_ONE_SHOT)
        val channelId = CHANNEL_ID
        soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(notificationIcon)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.app_icon))
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setColor(ContextCompat.getColor(this, R.color.themeColor))
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)
            .setDefaults(Notification.DEFAULT_ALL)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "B3", NotificationManager.IMPORTANCE_HIGH)
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.setShowBadge(true)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

    private val notificationIcon: Int
        get() {
            val useWhiteIcon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
            return if (useWhiteIcon) R.mipmap.app_icon else R.mipmap.ic_launcher
        }

}