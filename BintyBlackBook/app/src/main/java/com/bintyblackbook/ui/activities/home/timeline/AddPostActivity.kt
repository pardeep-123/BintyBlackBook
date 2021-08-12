package com.bintyblackbook.ui.activities.home.timeline

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bintyblackbook.R
import com.bintyblackbook.util.*
import com.bintyblackbook.viewmodel.PostsViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_add_post.*
import kotlinx.android.synthetic.main.user_signup_layout.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class AddPostActivity : ImagePickerUtility() {

    var mProgress: CustomProgressDialog? = null
    private var mSnackBar: Snackbar? = null

    var imageFile:File?=null
    lateinit var postsViewModel: PostsViewModel

    override fun selectedImage(imagePath: File?) {
        Glide.with(this).load(imagePath).into(rivCamera)
        imageFile=imagePath!!
    }

    override fun selectedVideoUri(videoUri: Uri?) {

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
        setContentView(R.layout.activity_add_post)
        mProgress = CustomProgressDialog(this)
        postsViewModel= PostsViewModel(this)

        setOnClicks()

        setToolbar()

    }

    private fun setToolbar() {
        val heading = intent.getStringExtra(AppConstant.HEADING)
        if (heading != null){
            tvHeading.text = heading
            rivCamera.setImageResource(R.drawable.background)
            edtDesc.setText(R.string.dummy_text)
            btnPost.text = getString(R.string.save)
        }
    }

    private fun setOnClicks() {
        rlBack.setOnClickListener {
            finish()
        }
        rivCamera.setOnClickListener {
            getImage(this,0,false)
        }

        btnPost.setOnClickListener {

            checkValidations()
            finish()
        }

    }

    private fun checkValidations() {

        if(InternetCheck.isConnectedToInternet(this)
            && Validations.isEmpty(this,edtDesc,getString(R.string.err_add_desc))){

            if(!imageFile?.exists()!!){
                Toast.makeText(this,"Please choose an image",Toast.LENGTH_LONG).show()
                return
            }
            val map: HashMap<String, RequestBody> = HashMap()
            map.put("description",createRequestBody(edtDesc.text.toString()))

            var imagenPerfil: MultipartBody.Part? = null
            if (imageFile != null) {

                Log.i("Register", imageFile?.path!!)
                // create RequestBody instance from file
                val requestFile: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), imageFile!!)
                // MultipartBody.Part is used to send also the actual file name
                imagenPerfil = MultipartBody.Part.createFormData("image", imageFile?.name, requestFile)
            }

            postsViewModel.addPost(getSecurityKey(this)!!, getUser(this)?.authKey!!,map,imagenPerfil)
            postsViewModel.addPostLiveData.observe(this, Observer {
                if(it.code==200){
                    showAlert(this,it?.msg.toString(),getString(R.string.ok)) {
                        finish()
                    }
                }
                else{
                    showAlert(this,it.msg.toString(),getString(R.string.ok)){}
                }
            })
        }


    }

    fun createRequestBody(param:String): RequestBody {
        val request=  RequestBody.create("text/plain".toMediaTypeOrNull(),param)
        return request
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