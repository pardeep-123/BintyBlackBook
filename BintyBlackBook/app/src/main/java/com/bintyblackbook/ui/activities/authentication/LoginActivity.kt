package com.bintyblackbook.ui.activities.authentication

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
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
        checkStatus()
        loginViewModel= LoginViewModel(this)
        clickHandles()
        tv_createAcct.setPaintFlags(tv_createAcct.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

    }

    private fun checkStatus() {
        if(getStatus(context)=="1"){
            email_text.setText(getEmail(context))
            password_text.setText(getPassword(context))
            cbRemember.isChecked=true
        }
        else{
            email_text.setText("")
            password_text.setText("")
            !cbRemember.isChecked
        }
    }

    private fun setObservables() {
       loginViewModel.loginObservable.observe(this, Observer<LoginSignUpModel> { t ->
               if(t?.code==200){
                   val response=t.data
                   saveUser(this,response!!)

                   if(response.userType==0){
                       MySharedPreferences.storeUserType(this,"User")
                   }
                   else{
                       MySharedPreferences.storeUserType(this,"Business")
                   }


                   if(response.userType==0){
                       val intent=Intent(this,HomeActivity::class.java)
                       startActivity(intent)
                   }
                   else if(response.userType==1 && response.experience.isNullOrEmpty()){
                       val intent =Intent(this,InfoActivity::class.java)
                       startActivity(intent)
                   }

                   else{
                       val intent =Intent(this,HomeActivity::class.java)
                       startActivity(intent)
                   }
               }
           else{
                   Toast.makeText(this,t.msg.toString(),Toast.LENGTH_LONG).show()
               }
           })
    }

    private fun clickHandles() {
        forgotPasswordText.setOnClickListener(this)
        ll_signup.setOnClickListener(this)
        signInBtn.setOnClickListener(this)
        cbRemember.setOnCheckedChangeListener { p0, isChecked ->
            if(isChecked){
                saveStatus(this,"1")
                saveEmail(this,email_text.text.toString())
                savePassword(this,password_text.text.toString())
            }
            else{
                saveStatus(this,"0")
                saveEmail(this,email_text.text.toString())
                savePassword(this,password_text.text.toString())
            }

        }

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
                    && Validations.isEmpty(this,password_text,getString(R.string.err_password))
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