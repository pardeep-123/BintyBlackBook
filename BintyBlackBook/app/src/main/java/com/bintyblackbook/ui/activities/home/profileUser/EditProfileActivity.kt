package com.bintyblackbook.ui.activities.home.profileUser

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.lifecycle.Observer
import com.bintyblackbook.R
import com.bintyblackbook.model.Data
import com.bintyblackbook.util.*
import com.bintyblackbook.viewmodel.ProfileViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class EditProfileActivity : ImagePickerUtility() {

    var selectedFile:File?=null
    lateinit var profileViewModel: ProfileViewModel
    var mProgress: CustomProgressDialog? = null
    private var mSnackBar: Snackbar? = null
    override fun selectedImage(imagePath: File?) {

        Glide.with(this).load(imagePath).into(civ_profile)
        selectedFile=imagePath
    }

    override fun selectedVideoUri(videoUri: Uri?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        mProgress = CustomProgressDialog(this)
        profileViewModel= ProfileViewModel(this)
        setOnClicks()

        headingText.text = getString(R.string.edit_profile)
        aboutTypingTimeScroll()

        setData(getUser(this))

    }

    private fun setData(user: Data?) {
        edtUserName.setText(user?.firstName)
        edtAbout.setText(user?.description)
        edtEmail.setText(user?.email)
        edtMobileNo.setText(user?.phone)
        if(!user?.image.isNullOrEmpty()){
            Glide.with(this).load(user?.image).into(civ_profile)
        }

    }

    private fun setOnClicks() {
        iv_back.setOnClickListener {
            finish()
        }

        btnSave.setOnClickListener {
           checkValidation()
        }
        civ_profile.setOnClickListener {
            getImage(this,0,false)
        }
    }

    private fun checkValidation() {
        if(InternetCheck.isConnectedToInternet(this)
            && Validations.isEmpty(this,edtUserName,getString(R.string.err_user_name))
            && Validations.isValidPhoneNumber(this,edtMobileNo,getString(R.string.err_valid_phone))
            && Validations.validateEmailAddress(this,edtEmail)
            && Validations.isEmpty(this,edtAbout,"About not be empty")){

            val map= HashMap<String,RequestBody>()
            map.put("name",createRequestBody(edtUserName.text.toString()))
            map.put("email",createRequestBody(edtEmail.text.toString()))
            map.put("country_code",createRequestBody(ccp.selectedCountryCodeWithPlus.toString()))
            map.put("phone", createRequestBody(edtMobileNo.text.toString()))
            map.put("description",createRequestBody(edtAbout.text.toString()))

            var imagenPerfil: MultipartBody.Part? = null
            if (selectedFile != null) {

                // create RequestBody instance from file
                val requestFile: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), selectedFile!!)
                // MultipartBody.Part is used to send also the actual file name
                imagenPerfil = MultipartBody.Part.createFormData("image", selectedFile?.name, requestFile)
            }

            profileViewModel.editProfile(getSecurityKey(this)!!, getUser(this)?.authKey!!,map,imagenPerfil!!)
            profileViewModel.profileObservable.observe(this, Observer {
                saveUser(this,it.data!!)
                showAlert(this,it.msg,getString(R.string.ok)){
                    finish()
                }

            })
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun aboutTypingTimeScroll() {
        edtAbout.setOnTouchListener { view, motionEvent ->
            if (view.id == R.id.edtAbout) {
                view.parent.requestDisallowInterceptTouchEvent(true)
                when (motionEvent.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> view.parent.requestDisallowInterceptTouchEvent(false)
                }
            }

            false
        }
    }

    fun createRequestBody(param:String): RequestBody {
        val request=  RequestBody.create("text/plain".toMediaTypeOrNull(),param)
        return request
    }

    fun showProgressDialog(){
        mProgress = CustomProgressDialog(this)
        mProgress!!.show()
    }

    fun dismissProgressDialog(){

        mProgress!!.dismiss()
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

}