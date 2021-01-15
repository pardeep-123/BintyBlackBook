package com.bintyblackbook.ui.activities.authentication

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.bintyblackbook.R
import com.bintyblackbook.ui.activities.home.HomeActivity
import com.bintyblackbook.ui.activities.home.settings.PrivacyPolicyActivity
import com.bintyblackbook.util.MySharedPreferences
import com.bintyblackbook.util.MyUtils
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity(), View.OnClickListener {
    val context: Context =this
    var clicked:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        MyUtils.fullscreen(this@SignupActivity)
        setContentView(R.layout.activity_signup)

        MySharedPreferences.storeUserType(this,"User")

        clickHandles()
        tvTermsConditions.setPaintFlags(tvTermsConditions.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        tv_loginAcct.setPaintFlags(tv_loginAcct.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
    }

    fun clickHandles(){
        userBtn.setOnClickListener(this)
        iv_back.setOnClickListener(this)
        businessBtn.setOnClickListener(this)
        tvTermsConditions.setOnClickListener(this)
        signUpBtn.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.iv_back->{
               finish()
            }
            R.id.tvTermsConditions -> {
                val intent = Intent(context, PrivacyPolicyActivity::class.java)
                intent.putExtra("from", "terms")
                startActivity(intent)
            }
            R.id.userBtn->{
               userBtnClick()
            }
            R.id.businessBtn->{
               businessBtnClick()
            }
            R.id.signUpBtn -> {
                if (MySharedPreferences.getUserType(this).equals("User")){
                    val intent = Intent(context, HomeActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }else if(MySharedPreferences.getUserType(this).equals("Business")){
                    val intent = Intent(context, InfoActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun userBtnClick(){
        MySharedPreferences.storeUserType(this,"User")

        userBtn.setTextColor(ContextCompat.getColor(context, R.color.blackColor))
        businessBtn.setTextColor(ContextCompat.getColor(context, R.color.whiteColor))
        userBtn.background = ContextCompat.getDrawable(context,R.drawable.pink_background)
        businessBtn.background = ContextCompat.getDrawable(context,R.drawable.pink_transbackground)
        lin_user.visibility = View.VISIBLE
        lin_business.visibility = View.GONE
    }

    private fun businessBtnClick(){
        MySharedPreferences.storeUserType(this, "Business")

        userBtn.setTextColor(ContextCompat.getColor(context, R.color.whiteColor))
        businessBtn.setTextColor(ContextCompat.getColor(context, R.color.blackColor))
        userBtn.background = ContextCompat.getDrawable(context,R.drawable.pink_transbackground)
        businessBtn.background = ContextCompat.getDrawable(context,R.drawable.pink_background)
        lin_user.visibility = View.GONE
        lin_business.visibility = View.VISIBLE
    }
}