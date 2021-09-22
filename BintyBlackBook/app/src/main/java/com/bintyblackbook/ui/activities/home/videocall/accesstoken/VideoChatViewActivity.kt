package com.bintyblackbook.ui.activities.home.videocall.accesstoken

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.SurfaceView
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.R
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.socket.SocketManager
import com.bintyblackbook.util.Validations
import com.bintyblackbook.util.getUser
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import io.agora.rtc.video.VideoEncoderConfiguration
import org.json.JSONObject

class VideoChatViewActivity : BaseActivity(), SocketManager.Observer {


    var socketManager:SocketManager?=null

    private var mRtcEngine: RtcEngine? = null
    private val mCallEnd = true
    private var mMuted = false
    private var mLocalContainer: FrameLayout? = null
    private var mRemoteContainer: RelativeLayout? = null
    private var mLocalView: SurfaceView? = null
    private var mRemoteView: SurfaceView? = null
    private var mCallBtn: ImageView? = null
    private var mMuteBtn: ImageView? = null
    private var mSwitchCameraBtn: ImageView? = null
    var appCertificate = "c9b2dbcbe5bb473697aa893b10433819"
    var mChannelName = "Binty"
    var userAccount = "0"
    var expirationTimeInSeconds = 3600
   // var isVideoCallPicked = false
    private var mCounter :CountDownTimer? = null
    private var isReciever = false
    private var mPlayer: MediaPlayer? = null
    var builder: AlertDialog.Builder? = null
    var videotoken:String?=null
    /**
     * Event handler registered into RTC engine for RTC callbacks.
     * Note that UI operations needs to be in UI thread because RTC
     * engine deals with the events in a separate thread.
     */
    private val mRtcEventHandler: IRtcEngineEventHandler = object : IRtcEngineEventHandler() {
        /**
         * Occurs when the local user joins a specified channel.
         * The channel name assignment is based on channelName specified in the joinChannel method.
         * If the uid is not specified when joinChannel is called, the server automatically assigns a uid.
         *
         * @param channel Channel name.
         * @param uid User ID.
         * @param elapsed Time elapsed (ms) from the user calling joinChannel until this callback is triggered.
         */
        override fun onJoinChannelSuccess(channel: String, uid: Int, elapsed: Int) {
            runOnUiThread {
                //                    mLogView.logI("Join channel success, uid: " + (uid & 0xFFFFFFFFL));
            }
        }

        /**
         * Occurs when the first remote video frame is received and decoded.
         * This callback is triggered in either of the following scenarios:
         *
         * The remote user joins the channel and sends the video stream.
         * The remote user stops sending the video stream and re-sends it after 15 seconds. Possible reasons include:
         * The remote user leaves channel.
         * The remote user drops offline.
         * The remote user calls the muteLocalVideoStream method.
         * The remote user calls the disableVideo method.
         *
         * @param uid User ID of the remote user sending the video streams.
         * @param width Width (pixels) of the video stream.
         * @param height Height (pixels) of the video stream.
         * @param elapsed Time elapsed (ms) from the local user calling the joinChannel method until this callback is triggered.
         */
        override fun onFirstRemoteVideoDecoded(uid: Int, width: Int, height: Int, elapsed: Int) {
            runOnUiThread { //                    mLogView.logI("First remote video decoded, uid: " + (uid & 0xFFFFFFFFL));
                Log.e("callAcceted",uid.toString())
              //  isVideoCallPicked = true
                if (mCounter != null)
                    mCounter!!.cancel()
                stopRinging()
                setupRemoteVideo(uid)
            }
        }

        override fun onConnectionStateChanged(state: Int, reason: Int) {
            super.onConnectionStateChanged(state, reason)
            Log.e("Tag",reason.toString()+" "+state)
        }
        /**
         * Occurs when a remote user (Communication)/host (Live Broadcast) leaves the channel.
         *
         * There are two reasons for users to become offline:
         *
         * Leave the channel: When the user/host leaves the channel, the user/host sends a
         * goodbye message. When this message is received, the SDK determines that the
         * user/host leaves the channel.
         *
         * Drop offline: When no data packet of the user or host is received for a certain
         * period of time (20 seconds for the communication profile, and more for the live
         * broadcast profile), the SDK assumes that the user/host drops offline. A poor
         * network connection may lead to false detections, so we recommend using the
         * Agora RTM SDK for reliable offline detection.
         *
         * @param uid ID of the user or host who leaves the channel or goes offline.
         * @param reason Reason why the user goes offline:
         *
         * USER_OFFLINE_QUIT(0): The user left the current channel.
         * USER_OFFLINE_DROPPED(1): The SDK timed out and the user dropped offline because no data packet was received within a certain period of time. If a user quits the call and the message is not passed to the SDK (due to an unreliable channel), the SDK assumes the user dropped offline.
         * USER_OFFLINE_BECOME_AUDIENCE(2): (Live broadcast only.) The client role switched from the host to the audience.
         */


        override fun onUserOffline(uid: Int, reason: Int) {
            runOnUiThread { //                    mLogView.logI("User offline, uid: " + (uid & 0xFFFFFFFFL));
                onRemoteUserLeft()
            }
        }
    }

    private fun setupRemoteVideo(uid: Int) {
        // Only one remote video view is available for this
        // tutorial. Here we check if there exists a surface
        // view tagged as this uid.
        val count = mRemoteContainer!!.childCount
        var view: View? = null
        for (i in 0 until count) {
            val v = mRemoteContainer!!.getChildAt(i)
            if (v.tag is Int && v.tag as Int == uid) {
                view = v
            }
        }
        if (view != null) {
            return
        }

        /*
          Creates the video renderer view.
          CreateRendererView returns the SurfaceView type. The operation and layout of the view
          are managed by the app, and the Agora SDK renders the view provided by the app.
          The video display view must be created using this method instead of directly
          calling SurfaceView.
         */mRemoteView = RtcEngine.CreateRendererView(baseContext)
        mRemoteContainer!!.addView(mRemoteView)
        // Initializes the video view of a remote user.
        mRtcEngine!!.setupRemoteVideo(VideoCanvas(mRemoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid))
        mRemoteView!!.setTag(uid)
    }

    private fun onRemoteUserLeft() {
        removeRemoteVideo()
    }

    private fun removeRemoteVideo() {
        if (mRemoteView != null) {
            mRemoteContainer!!.removeView(mRemoteView)
            showToast(resources.getString(R.string.callended))
        }
        // Destroys remote view
        mRemoteView = null
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_view_call)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        socketManager= BintyBookApplication.getSocketManager()
        initializeSocket()
//        setContentView(R.layout.activity_video_chat_view)
        isReciever = intent.getBooleanExtra("isReciever",false)
        initUI()
        builder = AlertDialog.Builder(this)
        // Ask for permissions at runtime.
        // This is just an example set of permissions. Other permissions
        // may be needed, and please refer to our online documents.
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)) {
            initEngineAndJoinChannel()
        }
    }
    fun initializeSocket() {
        socketManager = BintyBookApplication.getSocketManager()
        if (socketManager == null) {
            socketManager?.initializeSocket()
        }
    }


    private fun initUI() {
        mLocalContainer = findViewById(R.id.local_video_view_container)
        mRemoteContainer = findViewById(R.id.remote_video_view_container)
        mCallBtn = findViewById(R.id.btn_call)
        mMuteBtn = findViewById(R.id.btn_mute)
        mSwitchCameraBtn = findViewById(R.id.btn_switch_camera)
        mChannelName = intent.getStringExtra("channelName")!!
        videotoken = intent.getStringExtra("videoToken")!!
//        mLogView = findViewById(R.id.log_recycler_view);

        // Sample logs are optional.
        showSampleLogs()
    }

    private fun showSampleLogs() {
//        mLogView.logI("Welcome to Agora 1v1 video call");
//        mLogView.logW("You will see custom logs here");
//        mLogView.logE("You can also use this to show errors");
    }

    private fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQ_ID) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED || grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                showLongToast("Need permissions " + Manifest.permission.RECORD_AUDIO +
                        "/" + Manifest.permission.CAMERA + "/" + Manifest.permission.WRITE_EXTERNAL_STORAGE)
                finish()
                return
            }

            // Here we continue only if all permissions are granted.
            // The permissions can also be granted in the system settings manually.
            initEngineAndJoinChannel()
        }
    }

    private fun showLongToast(msg: String) {
        runOnUiThread {

            Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
        }
    }

    private fun initEngineAndJoinChannel() {
        // This is our usual steps for joining
        // a channel and starting a call.
        initializeEngine()
        setupVideoConfig()
        setupLocalVideo()
        joinChannel()
    }

    private fun initializeEngine() {
        mRtcEngine = try {
            RtcEngine.create(baseContext, getString(R.string.agora_app_id), mRtcEventHandler)
        } catch (e: Exception) {
            Log.e(TAG, Log.getStackTraceString(e))
            throw RuntimeException("""
    NEED TO check rtc sdk init fatal error
    ${Log.getStackTraceString(e)}
    """.trimIndent())
        }
    }

    private fun setupVideoConfig() {
        // In simple use cases, we only need to enable video capturing
        // and rendering once at the initialization step.
        // Note: audio recording and playing is enabled by default.
        mRtcEngine!!.enableVideo()

        // Please go to this page for detailed explanation
        // https://docs.agora.io/en/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#af5f4de754e2c1f493096641c5c5c1d8f
        mRtcEngine!!.setVideoEncoderConfiguration(VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT))
    }

    private fun setupLocalVideo() {
        // This is used to set a local preview.
        // The steps setting local and remote view are very similar.
        // But note that if the local user do not have a uid or do
        // not care what the uid is, he can set his uid as ZERO.
        // Our server will assign one and return the uid via the event
        // handler callback function (onJoinChannelSuccess) after
        // joining the channel successfully.
        mLocalView = RtcEngine.CreateRendererView(baseContext)
        mLocalView?.setZOrderMediaOverlay(true)
        mLocalContainer?.addView(mLocalView)
        // Initializes the local video view.
        // RENDER_MODE_HIDDEN: Uniformly scale the video until it fills the visible boundaries. One dimension of the video may have clipped contents.
//        mRtcEngine!!.setupLocalVideo(VideoCanvas(mLocalView, VideoCanvas.RENDER_MODE_HIDDEN, 0))


       // val userData = MyApplication.instance!!.getString(Constants.UserData)
//        if(!mValidationClass.checkStringNull(userData))
//        {
            //val mModel = stringToModel(userData, AuthenticationResponse.Body::class.java) as AuthenticationResponse.Body
            mRtcEngine!!.setupLocalVideo(VideoCanvas(mLocalView, VideoCanvas.RENDER_MODE_HIDDEN,
                getUser(context)?.id!!))
        //}
    }


    private fun joinChannel() {
        // 1. Users can only see each other after they join the
        // same channel successfully using the same app id.
        // 2. One token is only valid for the channel name that
        // you use to generate this token.
        val timestamp = (System.currentTimeMillis() / 1000 + expirationTimeInSeconds).toInt()
        var token: String? = RtcTokenBuilder().buildTokenWithUid(resources.getString(R.string.agora_app_id), appCertificate,
                mChannelName,userAccount.toInt(), RtcTokenBuilder.Role.Role_Publisher, timestamp)
        println(token)

//        var token: String? = getString(R.string.agora_access_token)
        if (token?.isEmpty()!!) {
            token = null
        }
        Log.e("channelName",mChannelName)
        /*var token: String? = "0060de73433fd6d4c3cab6b53b863c5630cIADRVybGIvNv7sXTJV/k6ASpF3FGKcfHmsv1RK2uPOW9fOIVUyEAAAAAEABVr+wwKRu9XwEAAQApG71f"
        if (TextUtils.isEmpty(token) || TextUtils.equals(token, "#YOUR ACCESS TOKEN#")) {
            token = null // default, no token
        }*/
        if (!isReciever) {
            startRinging()
            timeCounter()
        }
        mRtcEngine?.joinChannel(videotoken, mChannelName, "Extra Optional Data", 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mCounter != null)
            mCounter?.cancel()
        stopRinging()
        BintyBookApplication.getSocketManager()?.unRegister(this)
        leaveChannel()
        /*if (!mCallEnd) {
            leaveChannel()
        }*/
        /*
          Destroys the RtcEngine instance and releases all resources used by the Agora SDK.

          This method is useful for apps that occasionally make voice or video calls,
          to free up resources for other operations when not making calls.
         */RtcEngine.destroy()
    }

    private fun leaveChannel() {
        mRtcEngine!!.leaveChannel()
    }

    fun onLocalAudioMuteClicked(view: View?) {
        mMuted = !mMuted
        // Stops/Resumes sending the local audio stream.
        mRtcEngine?.muteLocalAudioStream(mMuted)
        val res = if (mMuted) R.drawable.btn_mutes else R.drawable.btn_unmute
        mMuteBtn?.setImageResource(res)
    }

    fun onSwitchCameraClicked(view: View?) {
        // Switches between front and rear cameras.
        mRtcEngine?.switchCamera()
    }

    fun onEndCallClicked(view: View?) {
     //   if (!isVideoCallPicked) {
            if (Validations.isNetworkConnected()) {
                val jsonObject = JSONObject()
                jsonObject.put("senderId", intent.getStringExtra("userId"))
                jsonObject.put("receiverId", intent.getStringExtra("otheruserId"))
                jsonObject.put("status", 2)
                socketManager?.getVideoCallStatus(jsonObject)
                finish()
            } else
                showAlertWithOk(resources.getString(R.string.internet_connection))
       // }else
          //  finish()
    }
    private fun startCall() {
        setupLocalVideo()
        joinChannel()
    }

    private fun endCall() {
        removeLocalVideo()
        removeRemoteVideo()
        leaveChannel()
    }

    private fun removeLocalVideo() {
        if (mLocalView != null) {
            mLocalContainer!!.removeView(mLocalView)
        }
        mLocalView = null
    }

    private fun showButtons(show: Boolean) {
        val visibility = if (show) View.VISIBLE else View.GONE
        mMuteBtn?.visibility = visibility
        mSwitchCameraBtn?.visibility = visibility
    }

    private fun timeCounter()
    {
        mCounter = object : CountDownTimer(45000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                Log.e("Tag","seconds remaining: " + millisUntilFinished / 1000)

            }
            override fun onFinish() {
                stopRinging()
                finish()
                showToast(resources.getString(R.string.no_answer))
            }
        }.start()
    }




//    "senderId":229 himanshu

    override fun onError(event: String?, vararg args: Any?) {
    }

    override fun onResponse(event: String?, vararg args: Any?) {
        when (event) {
            SocketManager.CALL_TO_USER -> {
                Log.e("fgfdgg", "mychar")
                val mObject = args[0] as JSONObject
                if (mObject != null) {
                    val status = mObject.getInt("call_connect_status")
                    if (status==0) {
                        showToast(resources.getString(R.string.callreject))
                        finish()
                    }
                }
            }
        }
    }

    companion object {
        private val TAG = VideoChatViewActivity::class.java.simpleName
        private const val PERMISSION_REQ_ID = 22

        // Permission WRITE_EXTERNAL_STORAGE is not mandatory
        // for Agora RTC SDK, just in case if you wanna save
        // logs to external sdcard.
        private val REQUESTED_PERMISSIONS = arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
    private fun startRinging() {

        mPlayer = playCalleeRing()

    }

    private fun playCalleeRing(): MediaPlayer {
        return startRinging(R.raw.basic_tones)
    }

    private fun startRinging(resource: Int): MediaPlayer {
        val player = MediaPlayer.create(this, resource)
        player.isLooping = true
        player.start()
        return player
    }

    private fun stopRinging() {
        if (mPlayer != null && mPlayer!!.isPlaying()) {
            mPlayer!!.stop()
            mPlayer!!.release()
            mPlayer = null
        }
    }


    override fun onBackPressed() {
        callDialog()
    }

    private fun callDialog()
    {
        builder!!.setMessage("Are you sure you want to end call?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    //if (!isVideoCallPicked) {
                        if (Validations.isNetworkConnected()) {
                            val jsonObject = JSONObject()
                            jsonObject.put("senderId", intent.getStringExtra("userId"))
                            jsonObject.put("receiverId", intent.getStringExtra("otheruserId"))
                            jsonObject.put("status", 2)
                            socketManager?.getVideoCallStatus(jsonObject)
                            finish()
                        } else
                            showAlertWithOk(resources.getString(R.string.internet_connection))
                   // }else
                   //     finish()
                }
                .setNegativeButton("No") { dialog, id -> //  Action for 'NO' Button
                    dialog.cancel()
                }
        //Creating dialog box
        val alert = builder!!.create()
        alert.show()
    }

    override fun onResume() {
        super.onResume()
        socketManager?.onRegister(this)
    }

}