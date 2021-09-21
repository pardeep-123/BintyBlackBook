package com.bintyblackbook.ui.activities.authentication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Window
import com.bintyblackbook.R
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.ui.activities.home.HomeActivity
import com.bintyblackbook.util.MyUtils
import com.bintyblackbook.util.getUser
import com.bintyblackbook.util.saveToken
import com.google.android.gms.tasks.*
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : BaseActivity() {

    private val SPLASH_TIME_OUT = 1500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.activity_splash)
        MyUtils.fullscreen(this)

        getFirebaseToken()
        val video: Uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.splash)
        videoView.setMediaController(null)
        videoView.setVideoURI(video)

        videoView.start()
        gotoNext()
    }

    private fun getFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            try {
                FirebaseMessaging.getInstance().token.addOnSuccessListener { token: String ->
                    if (!TextUtils.isEmpty(token)) {
                       saveToken(this,token)
                        Log.d("AppController", "retrieve token successful : $token")
                    } else {
                        Log.w("AppController", "token should not be null...")
                    }
                }.addOnFailureListener { e: Exception? -> }.addOnCanceledListener {}
                    .addOnCompleteListener { task: Task<String> ->
                        Log.v(
                            "AppController",
                            "This is the token : " + task.result
                        )
                    }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        })
    }

    private fun gotoNext(){

        videoView.setOnCompletionListener {

            if(getUser(context)?.authKey.isNullOrEmpty()){
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
           else if(getUser(context)?.userType==1 && getUser(context)?.experience.isNullOrEmpty()){
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            else if (getUser(context)?.userType==1 && getUser(context)?.authKey.isNullOrEmpty()) {
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        /* Handler(Looper.getMainLooper()).postDelayed({

             if (!getUser(this)?.authKey.isNullOrEmpty()) {
                 val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                 startActivity(intent)
                 finish()
             } else {
                 val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                 startActivity(intent)
                 finish()
             }
         }, SPLASH_TIME_OUT.toLong())*/
    }
}