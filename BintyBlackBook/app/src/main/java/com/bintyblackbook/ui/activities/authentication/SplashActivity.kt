package com.bintyblackbook.ui.activities.authentication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Window
import com.bintyblackbook.R
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.ui.activities.home.HomeActivity
import com.bintyblackbook.util.MyUtils
import com.bintyblackbook.util.getUser
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : BaseActivity() {

    private val SPLASH_TIME_OUT = 1500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.activity_splash)
        MyUtils.fullscreen(this)
        val video: Uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.splash)
        videoView.setVideoURI(video)
        videoView.start()
        gotoNext()
    }

    private fun gotoNext(){

        videoView.setOnCompletionListener {
            if (!getUser(context)?.authKey.isNullOrEmpty()) {
                val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
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