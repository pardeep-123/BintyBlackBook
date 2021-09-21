package com.bintyblackbook.ui.activities.home.message

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.R
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.socket.SocketManager
import com.bintyblackbook.ui.activities.home.videocall.accesstoken.VideoChatViewActivity
import com.bintyblackbook.util.Validations
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_incoming_call.*
import org.json.JSONObject

class IncomingCallActivity : BaseActivity(), SocketManager.Observer {
    var mCallerId = 0
    var mrecieverID= 0
    var mSenderImage = ""
    var mChannelName = ""
    var videoToken = ""
    private var mAnimator: PortraitAnimator? = null
    private var mPlayer: MediaPlayer? = null
    private var mCounter :CountDownTimer? = null
    var socketManager:SocketManager?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_incoming_call)
        socketManager= BintyBookApplication.getSocketManager()
        initializeSocket()
        tv_name.text = intent.getStringExtra("SenderName")
        mCallerId = intent.getIntExtra("senderID",0)
        mrecieverID = intent.getIntExtra("recieverID",0)
        mSenderImage = intent.getStringExtra("senderImage")!!
        mChannelName = intent.getStringExtra("channelName")!!
        videoToken = intent.getStringExtra("videoToken")!!

        Glide.with(this).load(mSenderImage).error(getResources().getDrawable(R.drawable.place_holder)).into(ivImage)

        setOnClicks()

        mAnimator = PortraitAnimator(
                findViewById(R.id.anim_layer_1),
                findViewById(R.id.anim_layer_2),
                findViewById(R.id.anim_layer_3))
        timeCounter()
        startRinging()
    }

    private fun setOnClicks() {
        ivCallPick.setOnClickListener {
            if (Validations.isNetworkConnected()) {
                val jsonObject = JSONObject()
                jsonObject.put("senderId", mCallerId)
                jsonObject.put("receiverId", mrecieverID)
                jsonObject.put("status", 1)
                //   jsonObject.put("channelName", mChannelName)
                socketManager?.getVideoCallStatus(jsonObject)

                val acceptCallIntent = Intent(this, VideoChatViewActivity::class.java)
                acceptCallIntent.putExtra("id", intent.getStringExtra("id"))
                acceptCallIntent.putExtra("userId", mCallerId.toString())
                acceptCallIntent.putExtra("otheruserId", mrecieverID.toString())
                acceptCallIntent.putExtra("channelName", mChannelName)
                acceptCallIntent.putExtra("videoToken",videoToken)
                acceptCallIntent.putExtra("isReciever", true)
                acceptCallIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(acceptCallIntent)
                finish()
            }
            else
                showAlertWithOk(resources.getString(R.string.internet_connection))

        }
        ivRejectCall.setOnClickListener {
            stopRinging()
            if (Validations.isNetworkConnected()) {
                val jsonObject = JSONObject()
                jsonObject.put("senderId", mCallerId)
                jsonObject.put("receiverId", mrecieverID)
                jsonObject.put("status", 2)
                socketManager?.getVideoCallStatus(jsonObject)
                finish()
            }
            else
                showAlertWithOk(resources.getString(R.string.internet_connection))
        }
    }

    private fun initializeSocket() {
        socketManager = BintyBookApplication.getSocketManager()
        if (socketManager == null) {
            socketManager?.initializeSocket()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mCounter?.cancel()
        stopRinging()
       socketManager?.unRegister(this)
    }

    override fun onResume() {
        super.onResume()
        socketManager?.onRegister(this)
    }



    override fun onError(event: String?, vararg args: Any?) {

    }

    override fun onResponse(event: String?, vararg args: Any?) {
        when (event) {
            SocketManager.CALL_TO_USER -> {

                val mObject = args[0] as JSONObject
                if (mObject != null) {
                    val status = mObject.getInt("status")
                    if (status==2) {
                        Log.e("call end ", "mychar")
                        showToast(resources.getString(R.string.callreject))
                        finish()
                    }
                }
            }
        }
    }

    private fun startRinging() {
        /*if (isCallee()) {
            mPlayer = playCalleeRing()
        } else if (isCaller()) {*/
            mPlayer = playCallerRing()
        /*}*/
    }

    private fun playCallerRing(): MediaPlayer {
        return startRinging(R.raw.basic_tones)
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
        if (mPlayer != null && mPlayer?.isPlaying!!) {
            mPlayer?.stop()
            mPlayer?.release()
            mPlayer = null
        }
    }

    override fun onStart() {
        super.onStart()
        mAnimator?.start()
    }

    override fun onStop() {
        super.onStop()
        mAnimator?.stop()
        stopRinging()
    }

    private fun timeCounter()
    {
        mCounter = object : CountDownTimer(45000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                Log.e("Tag","seconds remaining: " + millisUntilFinished / 1000)
            }

            override fun onFinish() {
                showToast(resources.getString(R.string.no_answer))
                finish()
            }
        }.start()
    }

    private class PortraitAnimator internal constructor(private val mLayer1: View, private val mLayer2: View, private val mLayer3: View) {
        private val mAnim1: Animation
        private val mAnim2: Animation
        private val mAnim3: Animation
        private var mIsRunning = false
        private fun buildAnimation(startOffset: Int): AnimationSet {
            val set = AnimationSet(true)
            val alphaAnimation = AlphaAnimation(1.0f, 0.0f)
            alphaAnimation.duration = ANIM_DURATION.toLong()
            alphaAnimation.startOffset = startOffset.toLong()
            alphaAnimation.repeatCount = Animation.INFINITE
            alphaAnimation.repeatMode = Animation.RESTART
            alphaAnimation.fillAfter = true
            val scaleAnimation = ScaleAnimation(
                    1.0f, 1.3f, 1.0f, 1.3f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f)
            scaleAnimation.duration = ANIM_DURATION.toLong()
            scaleAnimation.startOffset = startOffset.toLong()
            scaleAnimation.repeatCount = Animation.INFINITE
            scaleAnimation.repeatMode = Animation.RESTART
            scaleAnimation.fillAfter = true
            set.addAnimation(alphaAnimation)
            set.addAnimation(scaleAnimation)
            return set
        }

        fun start() {
            if (!mIsRunning) {
                mIsRunning = true
                mLayer1.visibility = View.VISIBLE
                mLayer2.visibility = View.VISIBLE
                mLayer3.visibility = View.VISIBLE
                mLayer1.startAnimation(mAnim1)
                mLayer2.startAnimation(mAnim2)
                mLayer3.startAnimation(mAnim3)
            }
        }

        fun stop() {
            mLayer1.clearAnimation()
            mLayer2.clearAnimation()
            mLayer3.clearAnimation()
            mLayer1.visibility = View.GONE
            mLayer2.visibility = View.GONE
            mLayer3.visibility = View.GONE
        }

        companion object {
            const val ANIM_DURATION = 3000
        }

        init {
            mAnim1 = buildAnimation(0)
            mAnim2 = buildAnimation(1000)
            mAnim3 = buildAnimation(2000)
        }
    }

    override fun onBackPressed() {
      showToast("Please accept or decline call")
    }
}
