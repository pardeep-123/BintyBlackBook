package com.bintyblackbook.ui.activities.authentication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.bintyblackbook.R
import com.bintyblackbook.util.MyUtils
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity(), View.OnClickListener {
    val context: Context =this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        MyUtils.fullscreen(this@ForgotPasswordActivity)
        setContentView(R.layout.activity_forgot_password)
        handleClickListeners()
    }

    fun handleClickListeners(){
        sendBtn.setOnClickListener(this)
        iv_back.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.sendBtn->{
               finish()
            }
            R.id.iv_back->{
               finish()
            }
        }
    }
}