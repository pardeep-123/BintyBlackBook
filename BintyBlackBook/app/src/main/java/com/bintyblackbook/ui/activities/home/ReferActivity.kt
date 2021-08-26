package com.bintyblackbook.ui.activities.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bintyblackbook.BuildConfig
import com.bintyblackbook.R
import com.bintyblackbook.util.getUser
import kotlinx.android.synthetic.main.activity_refer.*

class ReferActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refer)

        tvTitle.text= getString(R.string.you_can_earn_more_rewards) + getUser(this)?.promoCode +")"
        rlBack.setOnClickListener {
            finish()
        }

        btnInvite.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Hey check out Binty's Black Book at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
            sendIntent.type = "text/plain"
            startActivity(sendIntent)
        }
    }
}