package com.bintyblackbook.ui.activities.authentication

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import com.bintyblackbook.R
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.util.*
import com.bintyblackbook.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : BaseActivity(), View.OnClickListener {

    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        MyUtils.fullscreen(this@ForgotPasswordActivity)
        setContentView(R.layout.activity_forgot_password)
        loginViewModel=LoginViewModel(this)
        handleClickListeners()
    }

    fun handleClickListeners(){
        sendBtn.setOnClickListener(this)
        iv_back.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.sendBtn->{

                if(InternetCheck.isConnectedToInternet(this)
                    && Validations.validateEmailAddress(this,email_text)){
                    loginViewModel.forgotPassword(getSecurityKey(this)!!,email_text.text.toString())
                    setObservables()

                }

            }
            R.id.iv_back->{
               finish()
                MyUtils.hideSoftKeyboard(this)
            }
        }
    }

    private fun setObservables() {
        loginViewModel.observable.observe(this, Observer {

            if(it?.code==200){
                //success
                showAlert(this,it.msg.toString(),getString(R.string.ok)) {
                    finish()
                }
                // showAlert(it.msg.toString())
                finish()
            }
            else{
                showAlert(this,it.msg.toString(),getString(R.string.ok)) {
                    finish()
                }
            }
        })
    }
}