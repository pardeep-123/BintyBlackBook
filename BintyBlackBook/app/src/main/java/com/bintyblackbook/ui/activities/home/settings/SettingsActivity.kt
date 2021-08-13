package com.bintyblackbook.ui.activities.home.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bintyblackbook.R
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.ui.activities.authentication.ChangePasswordActivity
import com.bintyblackbook.util.MySharedPreferences
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.SettingsViewModel
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.toolbar.*

class SettingsActivity : BaseActivity(), View.OnClickListener {

    lateinit var settingsViewModel: SettingsViewModel
    var clicked:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        headingText.setText("SETTINGS")
        settingsViewModel=SettingsViewModel(this)

        getNotificationStatus()

        if(MySharedPreferences.getUserType(this).equals("Business")){
            rlMyWallet.visibility = View.VISIBLE
        }
        else{
            rlMyWallet.visibility = View.GONE
        }

        clickHandles()

    }

    private fun getNotificationStatus() {
        settingsViewModel.getNotificationStatus(getSecurityKey(this)!!, getUser(this)?.authKey!!)
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