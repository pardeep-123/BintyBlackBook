package com.bintyblackbook.ui.activities.home.timeline

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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class AddPostActivity : ImagePickerUtility() {

    var mProgress: CustomProgressDialog? = null
    private var mSnackBar: Snackbar? = null

    var imageFile:File?=null
    lateinit var postsViewModel: PostsViewModel

    var type=""
    var post_id=""
    var description=""
    var image=""

    override fun selectedImage(imagePath: File?) {
        Glide.with(this).load(imagePath).into(rivCamera)
        imageFile=imagePath!!
    }

    override fun selectedVideoUri(imagePath: String?) {

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
        postsViewModel= PostsViewModel()

        type= intent.getStringExtra("screen_type").toString()


        getData()

        setOnClicks()

        setToolbar()

    }

    private fun getData() {

        if(type.equals("Edit Post")){
            post_id=intent?.getStringExtra("post_id").toString()
            description=intent?.getStringExtra("description").toString()
            image=intent?.getStringExtra("image").toString()
            Glide.with(this).load(image).into(rivCamera)
            edtDesc.setText(description)
            btnPost.text = getString(R.string.save)
//            val uri=Uri.parse(image)
//            imageFile= saveImage(MediaStore.Images.Media.getBitmap(contentResolver, uri), this)!!

        }

       /* type=intent?.getStringExtra(AppConstant.HEADING).toString()

        if(type.equals("Edit Past")){
            edtDesc.setText(description)
            Glide.with(this).load(image).into(rivCamera)
        }*/
    }

    private fun setToolbar() {
        val heading = intent.getStringExtra(AppConstant.HEADING)
        if (heading != null){
            tvHeading.text = heading

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

        }

    }

    private fun checkValidations() {

        //imageFile is optional
        if(InternetCheck.isConnectedToInternet(this)
            && Validations.isEmpty(this,edtDesc,getString(R.string.err_add_desc))){

          /*  if(imageFile?.absolutePath.isNullOrEmpty() && image.isEmpty()){
                Toast.makeText(this,"Please choose an image",Toast.LENGTH_LONG).show()
                return
            }*/
            val map: HashMap<String, RequestBody> = HashMap()
             map.put("description",createRequestBody(edtDesc.text.toString()))

            var imagenPerfil: MultipartBody.Part? = null
            if (imageFile != null) {

                val requestFile: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), imageFile!!)

                imagenPerfil = MultipartBody.Part.createFormData("image", imageFile?.name, requestFile)
            }

            if(type.equals("Edit Post")){
                map.put("post_id",createRequestBody(post_id))
                postsViewModel.editPost(this,getSecurityKey(this)!!, getUser(this)?.authKey!!,map,imagenPerfil)
            }
            else{
                postsViewModel.addPost(this,getSecurityKey(this)!!, getUser(this)?.authKey!!,map,imagenPerfil)
            }

            postsViewModel.baseResponseLiveData.observe(this, Observer {
                if(it.code==200){
                    showAlert(this,it?.msg.toString(),getString(R.string.ok)) {
                        finish()
                    }
                } else{
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
            mSnackBar?.duration = Snackbar.LENGTH_SHORT
            mSnackBar?.show()
        } catch (e: Exception) {
            Log.e("TAG",e.printStackTrace().toString())
        }
    }
}