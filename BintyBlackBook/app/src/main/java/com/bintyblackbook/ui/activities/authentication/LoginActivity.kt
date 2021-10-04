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
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.R
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.Data
import com.bintyblackbook.model.LoginSignUpModel
import com.bintyblackbook.ui.activities.home.HomeActivity
import com.bintyblackbook.ui.activities.welcome.WelcomeTutorial
import com.bintyblackbook.util.*
import com.bintyblackbook.viewmodel.LoginViewModel
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.email_text
import kotlinx.android.synthetic.main.activity_login.password_text
import kotlinx.android.synthetic.main.user_signup_layout.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.util.HashMap


class LoginActivity : BaseActivity(), View.OnClickListener, FacebookAuth.FbResult {

    lateinit var loginViewModel: LoginViewModel
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private lateinit var mCallbackManager: CallbackManager
    private lateinit var loginButton: LoginButton
    private lateinit var facebookAuth: FacebookAuth
    private val rcSignIn: Int = 231
    
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

        loginButton = LoginButton(this)
        loginButton.setPermissions(listOf("email", "public_profile"))
        mCallbackManager = CallbackManager.Factory.create()
        facebookAuth = FacebookAuth(loginButton, mCallbackManager, this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val account = GoogleSignIn.getLastSignedInAccount(this)  //Check last Signed account
        if (account != null) {
            signOut()
        }

        loginButton.setOnClickListener {
            LoginManager.getInstance().logOut()
            facebookAuth.allowUserToFacebookLogin()
        }

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

                   if (!getUserList(this).isNullOrEmpty()){
                       val aa = getUserList(this)
                       for(i in 0 until aa?.size!!){
                           if(response.userType==1 && aa[i].id !=response.id) {
                               aa.add(response)
                           }
                       }

                       saveUsers(this,aa)

                   }else{
                       val list = ArrayList<Data>()
                       if(response.userType==1){
                           list.add(response)
                       }

                       saveUsers(this,list)
                   }

                   BintyBookApplication.getInstance()?.setString(BintyBookApplication.USER_ID, response.id.toString())

                   if(response.userType==0){
                       MySharedPreferences.storeUserType(this,"User")
                   }
                   else{
                       MySharedPreferences.storeUserType(this,"Business")
                   }

                   if(response.userType==0){
                       val intent=Intent(this,WelcomeTutorial::class.java)
                       startActivity(intent)
                       finishAffinity()
                   }
                   else if(response.userType==1 && response.experience.isNullOrEmpty()){
                       val intent =Intent(this,InfoActivity::class.java)
                       startActivity(intent)
                       finishAffinity()
                   } else{
                       val intent =Intent(this,WelcomeTutorial::class.java)
                       startActivity(intent)
                       finishAffinity()
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
        fbLogin.setOnClickListener(this)
        googleLogin.setOnClickListener(this)
        cbRemember.setOnCheckedChangeListener { p0, isChecked ->
            if(isChecked){
                saveStatus(this,"1")
                saveEmail(this,email_text.text.toString())
                savePassword(this,password_text.text.toString())
            }
            else{
                saveStatus(this,"0")
//                saveEmail(this,email_text.text.toString())
//                savePassword(this,password_text.text.toString())
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
                intent.putExtra("screen_type","normal")
                startActivity(intent)
                finish()
            }
            R.id.googleLogin ->{
                signIn()
            }

            R.id.fbLogin ->{
                loginButton.performClick()
            }

        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient?.signInIntent
        startActivityForResult(signInIntent, rcSignIn)
    }

    private fun signOut() {
        mGoogleSignInClient?.signOut()?.addOnCompleteListener(this) {
            Log.e("JSON", "Logout")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (loginButton.hasOnClickListeners()) {
            mCallbackManager.onActivityResult(requestCode, resultCode, data)
        }

        if (requestCode == rcSignIn) {
            Log.e("SocialResultCode", "$resultCode---$requestCode")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
    }
    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        if (completedTask.isSuccessful) {
            Log.e("UserLoginActivity", "Google$completedTask")
            try {
                val account = completedTask.getResult(ApiException::class.java)
                Log.e(
                    "JSON",
                    account?.id + " " + account?.email + " " + account?.displayName + " " + account?.photoUrl
                            + "Token" + account?.idToken
                )

                //perform task here

                val map: HashMap<String, RequestBody> = HashMap()
                map.put("social_id",createRequestBody(account?.id.toString()))
                map.put("social_type",createRequestBody("2"))
                map.put("userName",createRequestBody(account?.displayName.toString()))
                map.put("email",createRequestBody(account?.email.toString()))
                map.put("device_type",createRequestBody("1"))
                map.put("device_token",createRequestBody(getToken(this)))
                map.put("pushKitToken",createRequestBody("1234"))
                map.put("uuid",createRequestBody("1234"))

                var imagenPerfil: MultipartBody.Part? = null

                // create RequestBody instance from file
                val requestFile: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), account?.photoUrl.toString())
                // MultipartBody.Part is used to send also the actual file name
                imagenPerfil = MultipartBody.Part.createFormData("image", account.photoUrl.toString(), requestFile)

                loginFnGoogle(map,imagenPerfil)

            } catch (e: ApiException) {
                Log.e("JSON", e.toString())
            }
        } else {
            Log.e("UserLoginActivity", "Google Cancel")
            showToast( "Google Cancel")
        }
    }

    override fun fbResult(token: String, jsonObj: JSONObject) {
        val facebookId = jsonObj.optString("id")
        val fbEmail = jsonObj.optString("email")
        val fbName = jsonObj.optString("name")
        val picObject: JSONObject = jsonObj.getJSONObject("picture")
        val data: JSONObject = picObject.getJSONObject("data")
        val imgUrl = data.getString("url")

        //perform task here
        val map: HashMap<String, RequestBody> = HashMap()
        map.put("social_id",createRequestBody(facebookId))
        map.put("social_type",createRequestBody("1"))
        map.put("userName",createRequestBody(fbName))
        map.put("email",createRequestBody(fbEmail))
        map.put("device_type",createRequestBody("1"))
        map.put("device_token",createRequestBody(getToken(this)))
        map.put("pushKitToken",createRequestBody("1234"))
        map.put("uuid",createRequestBody("1234"))

        var imagenPerfil: MultipartBody.Part? = null

            // create RequestBody instance from file
            val requestFile: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), imgUrl)
            // MultipartBody.Part is used to send also the actual file name
            imagenPerfil = MultipartBody.Part.createFormData("image", imgUrl, requestFile)

            loginFnGoogle(map,imagenPerfil)

    }

    private fun loginFnGoogle(map: HashMap<String, RequestBody>, imagenPerfil: MultipartBody.Part) {
        loginViewModel.socialLogin(getSecurityKey(context)!!,map,imagenPerfil)
        setSocialLoginObservables()
    }

    private fun setSocialLoginObservables() {
        loginViewModel.loginObservable.observe(this, Observer {
            saveUser(this,it.data!!)

            if(it?.data.phone.isNullOrEmpty()){
                val intent= Intent(this,SignupActivity::class.java)
                intent.putExtra("screen_type","social")
                startActivity(intent)
                finish()
            }else{
                val intent= Intent(this,HomeActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
        })
    }

    fun createRequestBody(param:String):RequestBody{
        val request=  RequestBody.create("text/plain".toMediaTypeOrNull(),param)
        return request
    }

    override fun onFbCancel() {
       showToast("cancel")
    }

}