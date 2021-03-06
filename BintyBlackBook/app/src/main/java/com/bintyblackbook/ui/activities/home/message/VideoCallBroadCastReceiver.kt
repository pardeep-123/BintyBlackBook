package com.bintyblackbook.ui.activities.home.message

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.R
import com.bintyblackbook.util.Validations
import org.json.JSONObject


class VideoCallBroadCastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {


        if (intent!!.getStringExtra("type") == "call") {
            val acceptCallIntent = Intent(
                context,
                IncomingCallActivity::class.java
            )
            acceptCallIntent.putExtra("id", intent.getStringExtra("id"))
            acceptCallIntent.putExtra("senderID", intent.getIntExtra("senderID", 0))
            acceptCallIntent.putExtra("channelName", intent.getStringExtra("channelName"))
            acceptCallIntent.putExtra("senderImage", intent.getStringExtra("senderImage"))
            acceptCallIntent.putExtra("recieverName", intent.getStringExtra("recieverName"))
            acceptCallIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context!!.startActivity(acceptCallIntent)

            /*val videoCall = AgoraVideoCall(
                context,
                context!!.getString(R.string.agora_app_id),
                context!!.getString(R.string.agora_access_token),
                "demo"
            )
            val uiConfig = UIConfig()
            uiConfig.hideVideoMute()
            videoCall.setConfig(uiConfig)

            val intent2: Intent = videoCall.start()
            intent2.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            ContextCompat.startActivity(context, intent2, Bundle())*/
        }else
            if (intent.getStringExtra("type") == "reject") {
                if (Validations.isNetworkConnected()) {
                    val jsonObject = JSONObject()
                    jsonObject.put("senderId", intent.getIntExtra("senderID", 0).toString())
                    jsonObject.put("receiverId", intent.getIntExtra("recieverID", 0).toString())
                    jsonObject.put("status", 1)
                    BintyBookApplication.getSocketManager()?.getVideoCallStatus(jsonObject)
                   // MyApplication.getSocketManager().sendDataToServer(SocketManager.CALL_STATUS, jsonObject)
                }
                else
                    Toast.makeText(context, context!!.resources.getString(R.string.internet_connection), Toast.LENGTH_SHORT).show()
            }

        val notificationId = intent.getIntExtra("notificationId", 1)

        val manager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(notificationId)

        val closeIntent = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        context.sendBroadcast(closeIntent)
    }
}