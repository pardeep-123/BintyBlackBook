package com.bintyblackbook.ui.activities.authentication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.bintyblackbook.R
import com.bintyblackbook.ui.activities.home.HomeActivity
import com.bintyblackbook.util.MyUtils
import com.bintyblackbook.util.getUser

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT = 1500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.activity_splash)
        MyUtils.fullscreen(this)
        Log.i("TAG","splash")
        gotoNext()
    }

    private fun gotoNext(){
        Handler(Looper.getMainLooper()).postDelayed({

            if(!getUser(this)?.authKey.isNullOrEmpty()){
                val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            else {
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, SPLASH_TIME_OUT.toLong())
    }
}