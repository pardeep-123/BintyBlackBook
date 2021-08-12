package com.bintyblackbook.ui.activities.authentication

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.Observer
import com.bintyblackbook.R
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.LoginSignUpModel
import com.bintyblackbook.ui.activities.home.HomeActivity
import com.bintyblackbook.util.*
import com.bintyblackbook.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity(), View.OnClickListener {

    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        setContentView(R.layout.activity_login)
        MyUtils.fullscreen(this@LoginActivity)
        loginViewModel= LoginViewModel(this)
        clickHandles()
        tv_createAcct.setPaintFlags(tv_createAcct.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

    }

    private fun setObservables() {
       loginViewModel.loginObservable.observe(this, Observer<LoginSignUpModel> { t ->
               if(t?.code==200){
                   val response=t.data

                   if(response?.userType==0){
                       MySharedPreferences.storeUserType(this,"User")
                   }
                   else{
                       MySharedPreferences.storeUserType(this,"Business")
                   }
                   saveUser(this,response!!)

                   Log.i("response",t.msg.toString())
                   val intent=Intent(this,HomeActivity::class.java)
                   startActivity(intent)
               }
           })
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

                if (InternetCheck.isConnectedToInternet(this)
                    && Validations.validateEmailAddress(this, email_text)
                    && Validations.isValidPassword(this,password_text)
                ) {
                    //call api or pass any intent here
                    loginViewModel.loginUser(getSecurityKey(this)!!,email_text.text.toString(),password_text.text.toString(),"","","","1","12345")

                    setObservables()
                }


            }
            R.id.ll_signup -> {
                val intent = Intent(context, SignupActivity::class.java)
                startActivity(intent)
            }

        }
    }
}