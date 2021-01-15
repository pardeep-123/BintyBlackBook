package com.bintyblackbook.ui.activities.home.settings

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bintyblackbook.R
import com.bintyblackbook.ui.activities.authentication.ChangePasswordActivity
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.toolbar.*

class SettingsActivity : AppCompatActivity(), View.OnClickListener {
    val context: Context =this
    var clicked:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        headingText.setText("SETTINGS")
        clickHandles()
    }

    fun clickHandles(){
        iv_back.setOnClickListener(this)
        rlChangePassword.setOnClickListener(this)
        ivNotificationToggle.setOnClickListener(this)
        rlPrivacy.setOnClickListener(this)
        rlTerms.setOnClickListener(this)
        rlBlocked.setOnClickListener(this)
        rlMyWallet.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_back -> {
                finish()
            }
            R.id.rlChangePassword -> {
                val intent = Intent(context, ChangePasswordActivity::class.java)
                startActivity(intent)
            }
            R.id.rlPrivacy -> {
                val intent = Intent(context, PrivacyPolicyActivity::class.java)
                intent.putExtra("from", "privacy")
                startActivity(intent)
            }
            R.id.rlTerms -> {
                val intent = Intent(context, PrivacyPolicyActivity::class.java)
                intent.putExtra("from", "terms")
                startActivity(intent)
            }
            R.id.rlBlocked -> {
                val intent = Intent(context, BlockedContactsActivity::class.java)
                startActivity(intent)
            }
            R.id.rlMyWallet -> {
                val intent = Intent(context, MyWalletActivity::class.java)
                startActivity(intent)
            }
            R.id.ivNotificationToggle -> {
                if (clicked==false) {
                    ivNotificationToggle.setImageResource(R.drawable.on)
                    clicked=true
                } else {
                    ivNotificationToggle.setImageResource(R.drawable.off)
                    clicked=false
                }
            }
        }
    }
}