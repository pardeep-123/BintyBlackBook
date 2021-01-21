package com.bintyblackbook.ui.activities.authentication

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bintyblackbook.R
import com.bintyblackbook.ui.activities.home.HomeActivity
import com.bintyblackbook.util.MySharedPreferences
import com.bintyblackbook.util.MyUtils
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_settings.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    val context: Context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        MyUtils.fullscreen(this@LoginActivity)
        setContentView(R.layout.activity_login)

        clickHandles()
        tv_createAcct.setPaintFlags(tv_createAcct.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

    }

    private fun clickHandles() {
        forgotPasswordText.setOnClickListener(this)
        ll_signup.setOnClickListener(this)
        signInBtn.setOnClickListener(this)

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.forgotPasswordText -> {
                val intent = Intent(context, ForgotPasswordActivity::class.java)
                startActivity(intent)
            }
            R.id.signInBtn -> {
                if (email_text != null && email_text.text.isNotEmpty()) {
                    MySharedPreferences.storeUserType(this, "Business")
                    val intent = Intent(context, HomeActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                } else {
                    MySharedPreferences.storeUserType(this, "User")
                    val intent = Intent(context, HomeActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
            }
            R.id.ll_signup -> {
                val intent = Intent(context, SignupActivity::class.java)
                startActivity(intent)
            }

        }
    }
}