package com.bintyblackbook.ui.activities.authentication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bintyblackbook.R
import com.bintyblackbook.ui.activities.home.HomeActivity
import com.bintyblackbook.ui.activities.home.settings.PrivacyPolicyActivity
import com.bintyblackbook.util.*
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup.civ_profile
import kotlinx.android.synthetic.main.user_signup_layout.*

class SignupActivity : ImagePickerUtility(), View.OnClickListener {
    val context: Context =this
    var user_type=""

    override fun selectedImage(imagePath: String?) {

    }

    override fun selectedVideoUri(videoUri: Uri?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_signup)

        MySharedPreferences.storeUserType(this,"User")

        clickHandles()
        /*aboutTypingTimeScroll()*/
        tvTermsConditions.setPaintFlags(tvTermsConditions.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        tv_loginAcct.setPaintFlags(tv_loginAcct.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
    }

    fun clickHandles(){
        userBtn.setOnClickListener(this)
        iv_back.setOnClickListener(this)
        civ_profile.setOnClickListener(this)
        businessBtn.setOnClickListener(this)
        tvTermsConditions.setOnClickListener(this)
        tv_loginAcct.setOnClickListener(this)
        signUpBtn.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.iv_back->{
               finish()
            }
            R.id.civ_profile->{
                getImage(this,0,false)
            }
            R.id.tvTermsConditions -> {
                val intent = Intent(context, PrivacyPolicyActivity::class.java)
                intent.putExtra("from", "terms")
                startActivity(intent)
            }
            R.id.userBtn->{
                user_type="User"
                userBtnClick()
            }
            R.id.businessBtn->{
                user_type="Business"
                businessBtnClick()
            }
            R.id.signUpBtn -> {

                if(user_type.equals("User")){
                    if(InternetCheck.isConnectedToInternet(context)
                        && Validations.isEmpty(context,uname_text,getString(R.string.err_user_name))
                        && Validations.isValidPhoneNumber(context,phone_text,getString(R.string.err_valid_phone))
                        && Validations.validateEmailAddress(context,email_text)
                        && Validations.isValidPassword(context,password_text)
                        && Validations.confirmPassword(context,password_text,confpassword_text,"Password does not match")
                        && Validations.isEmpty(context,about_text,"About not be empty")
                    ){
                        if(cbAccept.isChecked==false){
                            Toast.makeText(context,"Please accept terms & conditions",Toast.LENGTH_LONG).show()
                            return
                        }
                        //call api here
                    }

                    val intent = Intent(context, HomeActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
                else{

                }


               /* if (MySharedPreferences.getUserType(this).equals("User")){
                    val intent = Intent(context, HomeActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }else if(MySharedPreferences.getUserType(this).equals("Business")){
                    val intent = Intent(context, InfoActivity::class.java)
                    startActivity(intent)
                }*/
            }
            R.id.tv_loginAcct ->{
                finish()
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

    @SuppressLint("ClickableViewAccessibility")
    private fun aboutTypingTimeScroll() {
        about_text.setOnTouchListener { view, motionEvent ->
            if (view.id == R.id.about_text) {
                view.parent.requestDisallowInterceptTouchEvent(true)
                when (motionEvent.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> view.parent.requestDisallowInterceptTouchEvent(false)
                }
            }

            false
        }
    }
}