package com.bintyblackbook.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Icon
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bintyblackbook.R
import com.bintyblackbook.ui.activities.home.message.ChatActivity
import com.bintyblackbook.ui.activities.home.message.IncomingCallActivity
import com.bintyblackbook.ui.activities.home.message.VideoCallBroadCastReceiver
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.GsonBuilder
import org.json.JSONObject
import java.util.*


class FbMessagingService : FirebaseMessagingService() {

    private var notificationChannel: NotificationChannel? = null
    private var mNotificationManager: NotificationManager? = null
    private var CHANNEL_ID: String = ""
    private val gson = GsonBuilder().serializeNulls().create()
    var mSenderName = ""
    var mSenderImage = ""
    var mSenderMessage = ""
    lateinit var mNotificationBuilder: Notification.Builder
    private var resultIntent: PendingIntent? = null

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.data != null) {
            Log.e(TAG, remoteMessage.data.toString())
            if (remoteMessage.data["isVideo"] != null) {
                createCallNotification(remoteMessage.data)
            } else {
                createNotification(remoteMessage.data)
            }
        }
       /* if (remoteMessage.notification!!.body != null) {
            Log.e(TAG, remoteMessage.notification!!.body!!)
        }*/
    }

    fun createCallNotification(map: Map<String?, String?>) {
        if (map["isVideo"] == "true") {
            if (map["Msg"] == "Incoming") {
                val intentAction = Intent(this, IncomingCallActivity::class.java)
                //This is optional if you have more than one buttons and want to differentiate between two
                intentAction.putExtra("data", map.toString())
                intentAction.putExtra("SenderName",map.get("SenderName"));
                intentAction.putExtra("recieverID", map.get("recieverID")!!.toInt());
                intentAction.putExtra("senderID", map.get("senderID")!!.toInt());
                /*intentAction.putExtra("ID", map.get("ID"));
                intentAction.putExtra("Msg", map.get("Msg"));
                intentAction.putExtra("TimeStamp", map.get("TimeStamp"));
                intentAction.putExtra("isVideo", map.get("isVideo"));*/
                val acceptCallIntent = Intent(this, VideoCallBroadCastReceiver::class.java)
                acceptCallIntent.putExtra("type", "call")
                acceptCallIntent.putExtra("id", map["senderID"]!!.toInt())
                acceptCallIntent.putExtra("notificationId", map["ID"]!!.toInt())
                acceptCallIntent.putExtra("recieverID", map.get("recieverID")!!.toInt());
                acceptCallIntent.putExtra("senderID", map.get("senderID")!!.toInt());
                val rejectCallIntent = Intent(this, VideoCallBroadCastReceiver::class.java)
                rejectCallIntent.putExtra("type", "reject")
                rejectCallIntent.putExtra("notificationId", map["ID"]!!.toInt())
                rejectCallIntent.putExtra("id", map["senderID"]!!.toInt())
                rejectCallIntent.putExtra("recieverID", map.get("recieverID")!!.toInt());
                rejectCallIntent.putExtra("senderID", map.get("senderID")!!.toInt());
                val contentIntent = PendingIntent.getActivity(this, map["ID"]!!.toInt(), intentAction, PendingIntent.FLAG_UPDATE_CURRENT)
                val acceptCall = PendingIntent.getBroadcast(this, 1, acceptCallIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                val rejectCall = PendingIntent.getBroadcast(this, 2, rejectCallIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                val channelId = getString(R.string.default_notification_channel_id)
                val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val notificationBuilder = NotificationCompat.Builder(this, channelId) ///.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .setSmallIcon(R.mipmap.app_icon)
                        .setContentTitle("Incoming call...")
                        .setContentText(map["SenderName"])
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setOngoing(true)
                        .addAction(0, "Accept", acceptCall)
                        .addAction(0, "Reject", rejectCall)
                        .setContentIntent(contentIntent)
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (notificationManager != null) {
                    // Since android Oreo notification channel is needed.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val channel = NotificationChannel(
                                channelId,
                                getString(R.string.default_notification_channel_name),
                                NotificationManager.IMPORTANCE_HIGH)
                        notificationManager.createNotificationChannel(channel)
                    }
                    notificationManager.notify(map["ID"]!!.toInt(), notificationBuilder.build())
                }
            } else if (map["Msg"] == "Rejected") {
                val rejectCallIntent = Intent(this, VideoCallBroadCastReceiver::class.java)
                rejectCallIntent.putExtra("notificationId", map["ID"]!!.toInt())
                startActivity(rejectCallIntent)
            }
        }
        //This is the intent of PendingIntent
    }
    private fun createNotification(jsonString: Map<String, String>) {
        var messageBody = ""
        var type = 1

        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject(jsonString)
            messageBody = jsonObject.getString("message")
            type = jsonObject.getInt("notification_type")

        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (type == 1){
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        CHANNEL_ID = applicationContext.packageName
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importanceLow = NotificationManager.IMPORTANCE_HIGH
            notificationChannel = NotificationChannel(CHANNEL_ID, "Channel One", importanceLow)
            notificationChannel!!.enableLights(true)
            notificationChannel!!.setLightColor(Color.RED)
            notificationChannel!!.setShowBadge(true)
            notificationChannel!!.setDescription(messageBody)
            notificationChannel!!.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC)
        }

        val data = JSONObject(jsonObject!!.getString("msgData"))
        val userId = data.optString("user_id")
        val userName = data.optString("userName")
        val senderDeviceToken = data.optString("senderDeviceToken")

        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("businessId",userId)
        intent.putExtra("businessName",userName)
        intent.putExtra("businessDeviceToken",senderDeviceToken)
        intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        val now = Date()
        val uniqueId =
                now.time//use date to generate an unique id to differentiate the notifications.
        resultIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mNotificationBuilder = Notification.Builder(this)
                    .setSmallIcon(R.mipmap.app_icon)
                    .setLargeIcon(Icon.createWithResource(this, R.mipmap.app_icon))
                    .setContentTitle(getString(R.string.app_name))
                    .setOngoing(false)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(sound)
                    .setWhen(System.currentTimeMillis())
                    .setShowWhen(true)
                    .setContentIntent(resultIntent)

        }else{
            mNotificationBuilder = Notification.Builder(this)
                    .setSmallIcon(R.mipmap.app_icon)
                    .setContentTitle(getString(R.string.app_name))
                    .setOngoing(false)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(sound)
                    .setWhen(System.currentTimeMillis())
                    .setShowWhen(true)
                    .setContentIntent(resultIntent)
        }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationBuilder.setChannelId(CHANNEL_ID)
                mNotificationManager!!.createNotificationChannel(notificationChannel!!)
            }
            mNotificationManager!!.notify(uniqueId.toInt(), mNotificationBuilder.build())
    }else
        {
            val data = JSONObject(jsonObject!!.getString("msgData"))
            createVideoNotification(data)
        }
    }

    fun createVideoNotification(map: JSONObject) {
            /*if (map["Msg"] == "Incoming") {*/
                val intentAction = Intent(this, IncomingCallActivity::class.java)
                //This is optional if you have more than one buttons and want to differentiate between two
                intentAction.putExtra("data", map.toString())
                intentAction.putExtra("SenderName",map.optString("senderName"));
                intentAction.putExtra("recieverID", map.optString("receiverId")!!.toInt());
                intentAction.putExtra("senderID", map.optString("senderId")!!.toInt());
                intentAction.putExtra("senderImage", map.optString("senderImage"))
                intentAction.putExtra("channelName", map.optString("channelName"))
                intentAction.putExtra("videoToken", map.optString("videoToken"))
               intentAction.putExtra("id", map.optString("useruuid")!!)

                val acceptCallIntent = Intent(this, VideoCallBroadCastReceiver::class.java)
                acceptCallIntent.putExtra("type", "call")
                acceptCallIntent.putExtra("id", map.optString("useruuid")!!)
//                acceptCallIntent.putExtra("notificationId", map.optString("useruuid")!!.toString())
                acceptCallIntent.putExtra("notificationId", map.optString("senderId")!!.toInt())
                acceptCallIntent.putExtra("recieverID", map.optString("receiverId")!!.toInt());
                acceptCallIntent.putExtra("senderID", map.optString("senderId")!!.toInt());
                acceptCallIntent.putExtra("channelName", map.optString("channelName"))
                acceptCallIntent.putExtra("videoToken", map.optString("videoToken"))
                acceptCallIntent.putExtra("senderImage", map.optString("senderImage"))
                acceptCallIntent.putExtra("SenderName",map.optString("senderName"));
                /*val rejectCallIntent = Intent(this, VideoCallBroadCastReceiver::class.java)
                rejectCallIntent.putExtra("type", "reject")
                rejectCallIntent.putExtra("notificationId", map.optString("useruuid").toInt())
                rejectCallIntent.putExtra("id", map.optString("useruuid")!!)
                rejectCallIntent.putExtra("recieverID", map.optString("receiverId")!!.toInt());
                rejectCallIntent.putExtra("senderID", map.optString("senderId").toInt());
                rejectCallIntent.putExtra("channelName", map.optString("channelName"))
                val contentIntent = PendingIntent.getActivity(this, map.optString("useruuid").toInt(), intentAction, PendingIntent.FLAG_UPDATE_CURRENT)*/
                val acceptCall = PendingIntent.getBroadcast(this, 1, acceptCallIntent, PendingIntent.FLAG_UPDATE_CURRENT)
              acceptCall.send()
      /*  val rejectCall = PendingIntent.getBroadcast(this, 2, rejectCallIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                val channelId = getString(R.string.default_notification_channel_id)
                val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val notificationBuilder = NotificationCompat.Builder(this, channelId) ///.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .setSmallIcon(R.mipmap.ic_launcher_foreground)
                        .setContentTitle("Incoming call...")
                        .setContentText(map.optString("senderName"))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setOngoing(true)
                        .addAction(0, "Accept", acceptCall)
                        .addAction(0, "Reject", rejectCall)
                        .setContentIntent(contentIntent)
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (notificationManager != null) {
                    // Since android Oreo notification channel is needed.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val channel = NotificationChannel(
                                channelId,
                                getString(R.string.default_notification_channel_name),
                                NotificationManager.IMPORTANCE_DEFAULT)
                        notificationManager.createNotificationChannel(channel)
                    }
                    notificationManager.notify(map.optString("useruuid").toInt(), notificationBuilder.build())
                }*/
            /*} else if (map["Msg"] == "Rejected") {
                val rejectCallIntent = Intent(this, VideoCallBroadCastReceiver::class.java)
                rejectCallIntent.putExtra("notificationId", map.optString("useruuid").toInt())
                startActivity(rejectCallIntent)
            }*/
        //This is the intent of PendingIntent
    }

    /*
    private static int count = 0;
    private Context mContext = MyApplication.getMyApplicationInstance().getContext();
    Intent resultIntent, messageIntent, msgIntent;
    Bundle bundle, messageBundle;




    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage == null || remoteMessage.getData() == null) {
            Log.e("RETURN NOTIFICATION", "RETURN NOTIFICATION");
            return;
        }
        Log.e("NOTIFICATION DATA", remoteMessage.getData().toString());
        Log.e(TAG, "onMessageReceived, getFrom: " + remoteMessage.getFrom());
        Log.e(TAG, "onMessageReceived, getCollapseKey: " + remoteMessage.getCollapseKey());
        Log.e(TAG, "onMessageReceived, getMessageId: " + remoteMessage.getMessageId());
        Log.e(TAG, "onMessageReceived, getMessageType: " + remoteMessage.getMessageType());
        Log.e(TAG, "onMessageReceived, getTo: " + remoteMessage.getTo());
        Log.e(TAG, "onMessageReceived, getOriginalPriority: " + remoteMessage.getOriginalPriority());
        Log.e(TAG, "onMessageReceived, getPriority: " + remoteMessage.getPriority());
        Log.e(TAG, "onMessageReceived, getSentTime: " + remoteMessage.getSentTime());
        Log.e(TAG, "onMessageReceived, getTtl: " + remoteMessage.getTtl());
        Log.e(TAG, "onMessageReceived, getData: " + remoteMessage.getData());
        Log.e(TAG, "onMessageReceived, getDataStr: " + remoteMessage.getData().toString());
// Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        //Calling method to generate notification


       */

    /* sendNotification(remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody(), remoteMessage.getData());
*/
    /*
        sendNotification(remoteMessage.getData());

    }

    private void sendNotification(Map<String, String> row) {
        PendingIntent contentIntent = null;
        */
    /*RemoteInput remoteInput = new RemoteInput.Builder("key_text_reply")
                .setLabel("Reply").build() ;
        */
    /*
        Gson gson = MyApplication.getMyApplicationInstance().getGsonBuilder();
        NotificationDataModel data = gson.fromJson(gson.toJson(row), new TypeToken<NotificationDataModel>() {
        }.getType());
        Log.e("DATAAAA", data.toString());
        Log.e("DATA getType", data.getType());
        Log.e(" getTitle", data.getTitle());
        Log.e("getMessage", data.getMessage());

        Intent intent = createIntent(data);
        if (intent != null) {
            Random random = new Random();
            contentIntent = PendingIntent.getActivity(mContext, random.nextInt(100), intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                ///.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.drawable.logo_orange)
                .setContentTitle(data.getTitle())
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(data.getMessage()))
                .setContentText(data.getMessage())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(contentIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        channelId,
                        getString(R.string.default_notification_channel_name),
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(count, notificationBuilder.build());
        }
        count++;
    }

    private Intent createIntent(NotificationDataModel data) {
        bundle = new Bundle();
        bundle.putString(AppConstants.BK_NOTIFICATION_TYPE, data.getType());
        if (data.getType().equalsIgnoreCase("review")) {
            sendLocalBroadcast("review");
            callMainActivity();
        }
        else if(data.getType().equalsIgnoreCase("in kitchen")||data.getType().equalsIgnoreCase("on way")||
                data.getType().equalsIgnoreCase("ready")||data.getType().equalsIgnoreCase("accepted")
        ||data.getType().equalsIgnoreCase("delivered")){
            sendLocalBroadcast(data.getType());
            callMainActivity();
        }

        return resultIntent;
    }
    private void sendLocalBroadcast(String type) {
        Log.d("sender", "Broadcasting message");
        if (type.equalsIgnoreCase("review")) {
            Intent intent = new Intent("review-broadcast");
            // You can also include some extra data.
          //  intent.putExtra("message", "This is my message!");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
        else if(type.equalsIgnoreCase("in kitchen")||type.equalsIgnoreCase("on way")||
                type.equalsIgnoreCase("ready")||type.equalsIgnoreCase("accepted")){
            Intent intent = new Intent("ongoing-broadcast");
            // You can also include some extra data.
            //  intent.putExtra("message", "This is my message!");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
        else if(type.equalsIgnoreCase("delivered")){
            Intent intent = new Intent("delivered-broadcast");
            // You can also include some extra data.
            //  intent.putExtra("message", "This is my message!");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
        */
    /*else if (type.equalsIgnoreCase("accepted")) {
            Intent intent = new Intent("accepted-broadcast");
            // You can also include some extra data.
            intent.putExtra("message", "This is my message!");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }*/
    /*
    }

    private void callMainActivity() {
        resultIntent = new Intent(mContext, HomeScreenActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        resultIntent.putExtras(bundle);
    }*/
    companion object {
        private val TAG = FbMessagingService::class.java.simpleName
    }
}