package com.bintyblackbook.ui.activities.authentication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bintyblackbook.R
import com.bintyblackbook.ui.activities.home.HomeActivity
import com.bintyblackbook.ui.activities.home.settings.PrivacyPolicyActivity
import com.bintyblackbook.util.*
import com.bintyblackbook.viewmodel.SignUpViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.business_signup_layout.*
import kotlinx.android.synthetic.main.user_signup_layout.*
import kotlinx.android.synthetic.main.user_signup_layout.ccp
import kotlinx.android.synthetic.main.user_signup_layout.confpassword_text
import kotlinx.android.synthetic.main.user_signup_layout.email_text
import kotlinx.android.synthetic.main.user_signup_layout.password_text
import kotlinx.android.synthetic.main.user_signup_layout.phone_text
import kotlinx.android.synthetic.main.user_signup_layout.uname_text
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*


class SignupActivity : ImagePickerUtility(), View.OnClickListener {

    val context: Context =this
    var user_type="User"
    var mProgress: CustomProgressDialog? = null
    private var mSnackBar: Snackbar? = null

    var selectedImagePath:File?=null
    var selectedVideoUri:Uri?=null

    var black_owned=""

    lateinit var signUpViewModel: SignUpViewModel

    override fun selectedImage(imagePath: File?) {
        Log.i("TAG",imagePath?.path.toString())
        Glide.with(context).load(imagePath).into(civ_profile)
        selectedImagePath=imagePath
    }

    override fun selectedVideoUri(videoUri: Uri?) {
        selectedVideoUri=videoUri!!
    }

    fun showProgressDialog(){
        mProgress = CustomProgressDialog(this)
        mProgress!!.show()
    }

    fun dismissProgressDialog(){

        mProgress!!.dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_signup)

        MySharedPreferences.storeUserType(this,"User")
        mProgress = CustomProgressDialog(this)
        signUpViewModel= SignUpViewModel(this)
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
        ivBusinessInfo.setOnClickListener(this)
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

            R.id.ivBusinessInfo ->{
//                CoachMarkOverlay.Builder(context)
//                    .setOverlayTargetView(ivBusinessInfo)
//                    .setInfoViewBuilder(
//                        CoachMarkInfo.Builder(context)
//                            .setInfoText("TextString").setMargin(30, 30, 30, 30))
//                            .setSkipButtonBuilder(
//                                CoachMarkSkipButton.Builder(context)
//                                    .setButtonClickListener(object : CoachMarkSkipButton.ButtonClickListener {
//                                        override fun onSkipButtonClick(view: View) {
//                                            (window.decorView as ViewGroup).removeView(view)
//
//                                        }
//                                    })
//                                        .build()
//                                        .show(viewToAttachCoachMark))

            }
            R.id.signUpBtn -> {

                if(user_type == "User"){
                    checkValidationForUser()
                }
                else{
                    checkValidationsForBusiness()
//                    val intent = Intent(context, InfoActivity::class.java)
//                    startActivity(intent)
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

    private fun checkValidationsForBusiness() {

        if(InternetCheck.isConnectedToInternet(context)
            && Validations.isEmpty(context,uname_text_business,getString(R.string.err_business_name))
            && Validations.isValidPhoneNumber(context,phone_text_business,getString(R.string.err_valid_phone))
            && Validations.validateEmailAddress(context,email_text_business)
            && Validations.isValidPassword(context,password_text_business)
            && Validations.confirmPassword(context,password_text_business,confpassword_text_business,"Password does not match")
        ){
            if(!cbAccept.isChecked){
                Toast.makeText(context,"Please accept terms & conditions",Toast.LENGTH_LONG).show()
                return
            }
            val map: HashMap<String, RequestBody> = HashMap()

            if(!edtAddPromoCode.text.toString().isNullOrEmpty()){
                map.put("promo_code",createRequestBody(edtAddPromoCode.text.toString()))
            }

            rgBusiness.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener{
                override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                    when(checkedId){
                        R.id.rbYesBusiness ->{
                            black_owned="1"
                        }

                        R.id.rbNoBusiness ->{
                            black_owned="0"
                        }
                    }
                }

            })

            map.put("business_name",createRequestBody(uname_text.text.toString()))
            map.put("phone",createRequestBody(phone_text.text.toString()))
            map.put("email",createRequestBody(email_text.text.toString()))
            map.put("password",createRequestBody(confpassword_text.text.toString()))
            map.put("country_code",createRequestBody(ccp.selectedCountryCodeWithPlus.toString()))
            map.put("device_type",createRequestBody("1"))
            map.put("device_token",createRequestBody("12345"))
            map.put("pushKitToken",createRequestBody("1234"))
            map.put("uuid",createRequestBody("1234"))
            map.put("black_owned",createRequestBody(black_owned))

            var imagenPerfil: MultipartBody.Part? = null
            if (selectedImagePath != null) {

                // create RequestBody instance from file
                val requestFile: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), selectedImagePath!!)
                // MultipartBody.Part is used to send also the actual file name
                imagenPerfil = MultipartBody.Part.createFormData("profile_image", selectedImagePath?.name, requestFile)
            }
            //call api here
            signUpViewModel.businessSignUp(getSecurityKey(context)!!,map,imagenPerfil)
            setObservables()

        }

    }

    private fun checkValidationForUser() {

        if(InternetCheck.isConnectedToInternet(context)
            && Validations.isEmpty(context,uname_text,getString(R.string.err_user_name))
            && Validations.isValidPhoneNumber(context,phone_text,getString(R.string.err_valid_phone))
            && Validations.validateEmailAddress(context,email_text)
            && Validations.isValidPassword(context,password_text)
            && Validations.confirmPassword(context,password_text,confpassword_text,"Password does not match")
            && Validations.isEmpty(context,about_text,"About not be empty")
        ){
            if(!cbAccept.isChecked){
                Toast.makeText(context,"Please accept terms & conditions",Toast.LENGTH_LONG).show()
                return
            }

            if(!selectedImagePath?.exists()!!){
                Toast.makeText(context,"Please choose image",Toast.LENGTH_LONG).show()
                return
            }

            val map: HashMap<String, RequestBody> = HashMap()
            map.put("userName",createRequestBody(uname_text.text.toString()))
            map.put("phone",createRequestBody(phone_text.text.toString()))
            map.put("email",createRequestBody(email_text.text.toString()))
            map.put("password",createRequestBody(confpassword_text.text.toString()))
            map.put("country_code",createRequestBody(ccp.selectedCountryCodeWithPlus.toString()))
            map.put("device_type",createRequestBody("1"))
            map.put("device_token",createRequestBody("12345"))
            map.put("description",createRequestBody(about_text.text.toString()))
            map.put("pushKitToken",createRequestBody("1234"))
            map.put("uuid",createRequestBody("1234"))

            var imagenPerfil: MultipartBody.Part? = null
            if (selectedImagePath != null) {

                Log.i("Register", selectedImagePath?.path!!)
                // create RequestBody instance from file
                val requestFile: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), selectedImagePath!!)
                // MultipartBody.Part is used to send also the actual file name
                imagenPerfil = MultipartBody.Part.createFormData("image", selectedImagePath?.name, requestFile)
            }

            //call api here
            signUpViewModel.signUpUser(getSecurityKey(context)!!,map,imagenPerfil)
            setObservables()
        }
    }

    private fun setObservables() {
        signUpViewModel.signUpLiveData.observe(this, Observer {

            if(it.code==200){

                saveUser(this,it.data!!)
                Log.i("TAG",it.data.toString())

                if(it.data.userType==0){
                    val intent = Intent(context,LoginActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
                else{
                    val intent= Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                }

            }

            else{
                Log.e("TAG",it.msg.toString())
            }
        })
    }

    fun createRequestBody(param:String):RequestBody{
      val request=  RequestBody.create("text/plain".toMediaTypeOrNull(),param)
        return request
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

    fun showSnackBarMessage(msg: String) {
        try {
            mSnackBar = Snackbar.make(
                getWindow().getDecorView().getRootView(),
                msg,
                Snackbar.LENGTH_LONG
            ) //Assume "rootLayout" as the root layout of every activity.
            mSnackBar?.duration = Snackbar.LENGTH_SHORT!!
            mSnackBar?.show()
        } catch (e: Exception) {
            Log.e("TAG",e.printStackTrace().toString())
        }
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