package com.bintyblackbook.ui.activities.authentication

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.adapters.UploadPhotoAdapter
import com.bintyblackbook.adapters.UploadVideoAdapter
import com.bintyblackbook.model.*
import com.bintyblackbook.util.*
import com.bintyblackbook.viewmodel.InfoViewModel
import com.bumptech.glide.Glide
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.android.synthetic.main.activity_info.edtLocation
import kotlinx.android.synthetic.main.toolbar.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.lang.StringBuilder
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class InfoActivity : ImagePickerUtility(), CustomInterface,
    UploadVideoAdapter.UploadVideoInterface, UploadPhotoAdapter.UploadPhotoInterface {

    var mediaList= ArrayList<MediaData>()
    var categoty_id=""
    var category_name=""
    var subCategory_id=""
    var subCategory_name=""


    var imageFile:File?=null
    var selectedVideoFile:String?=null
    private var mSnackBar: Snackbar? = null
    var list_count=1
    var categoryList = ArrayList<CategoryData>()
    var videoAdapter:UploadVideoAdapter?=null
    var videoList= ArrayList<UploadVideoModel>()
    var uploadPhotoAdapter:UploadPhotoAdapter?=null
    var photoList= ArrayList<UploadPhotoModel>()

    lateinit var infoViewModel:InfoViewModel
    var swap_value=""
    var service_business =""

    var profile_image=""

    var categoryDialogFragment:CategoryDialogFragment?=null

    private var latitude = ""
    private var longitude = ""
    private val AUTOCOMPLETE_REQUEST_CODE = 1

    var black_owned=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyUtils.fullscreen(this)
        setContentView(R.layout.activity_info)

        Places.initialize(this, "AIzaSyAd4ZzHfi-nAp-6IAm1YF5-pVxCAlzW4EA")
        initViews()
        setVideoAdapter()
        setPhotoAdapter()
        setData()

        getCategoryData()

        aboutMeTypingTimeScroll()

        headingText.text = getString(R.string.tell_us_about_business)
        clickHandles()

//        date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
//            myCalendar.set(Calendar.YEAR, year)
//            myCalendar.set(Calendar.MONTH, monthOfYear)
//            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//            updateDateLabel()
//        }

    }

    private fun setPhotoAdapter() {
        rvUploadPhoto.layoutManager= LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        uploadPhotoAdapter= UploadPhotoAdapter(this)
        rvUploadPhoto.adapter= uploadPhotoAdapter
        uploadPhotoAdapter?.uploadPhotoInterface=this
        uploadPhotoAdapter?.arrayList=photoList
        photoList.add(UploadPhotoModel("undefined",imageFile,"",0))
        uploadPhotoAdapter?.notifyDataSetChanged()
    }

    private fun setVideoAdapter() {
        rvUploadVideo.layoutManager=LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        videoAdapter= UploadVideoAdapter(this)
        rvUploadVideo.adapter= videoAdapter
        videoAdapter?.uploadVideoInterface =this
        videoAdapter?.arrayList=videoList
        videoList.add(UploadVideoModel("undefined",selectedVideoFile,0))
        videoAdapter?.notifyDataSetChanged()
    }

    private fun initViews() {
        mProgress = CustomProgressDialog(this)
        infoViewModel= InfoViewModel()
    }

    private fun getCategoryData() {

        infoViewModel.getCategories(this,getSecurityKey(this)!!, getUser(this)?.authKey!!)
        infoViewModel.categoryLiveData.observe(this, androidx.lifecycle.Observer {

            if(it.code==200){
                 categoryList.addAll(it?.data!!)
            }
            else{
                Log.i("TAG",it.msg.toString())
            }
        })
    }

    private fun setData() {
        edtName.setText(getUser(this)?.businessName)
        edtEmail.setText(getUser(this)?.email)
        etUserPhoneNumber.setText(getUser(this)?.phone)
        Glide.with(this).load(getUser(this)?.image).into(civ_profile)
        profile_image= getUser(this)?.image!!
    }

    private fun clickHandles() {

        etSelectCategory.setOnClickListener {
            runOnUiThread {
                categoryDialogFragment = CategoryDialogFragment(categoryList, this, this)
                categoryDialogFragment?.show(this.supportFragmentManager, "dialog")
            }
        }
        iv_back.setOnClickListener {
            finish()
        }

        edtSetAvailability.setOnClickListener {
            val intent= Intent(this,SetUserAvailabilityActivity::class.java)
            startActivity(intent)
        }

        civ_profile.setOnClickListener {
            getImage(this,0,false)
        }

        addNewVideo.setOnClickListener {
            list_count += 1
            videoList.add(UploadVideoModel("undefined",selectedVideoFile,0))
            videoAdapter?.notifyDataSetChanged()
        }
        ivAddImage.setOnClickListener {
            list_count += 1
            photoList.add(UploadPhotoModel("undefined",imageFile,"",0))
            uploadPhotoAdapter?.notifyDataSetChanged()
        }

        btnSubmit.setOnClickListener {
            checkValidations()
        }

        rgService.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbNo -> {
                    service_business = "1"
                    tvServiceType.visibility = View.GONE
                    edtServiceType.visibility = View.GONE
                    tvAvailability.visibility = View.GONE
                    edtSetAvailability.visibility = View.GONE
                }

                R.id.rbYes -> {
                    service_business = "0"
                    tvServiceType.visibility = View.VISIBLE
                    edtServiceType.visibility = View.VISIBLE
                    tvAvailability.visibility = View.VISIBLE
                    edtSetAvailability.visibility = View.VISIBLE
                }
            }
        }

        rgSwap.setOnCheckedChangeListener { group, checkedId ->
           when (checkedId) {
               R.id.rbYesSwap -> {
                   swap_value = "1"
                   edtSwaps.visibility = View.VISIBLE
                   tvSwap.visibility = View.VISIBLE
               }

               R.id.rbNoSwap -> {
                   swap_value = "0"
                   edtSwaps.visibility = View.GONE
                   tvSwap.visibility = View.GONE
               }
           }
       }

        edtLocation.setOnClickListener {
            val fields = listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS_COMPONENTS,
                Place.Field.ADDRESS
            )
            // Start the autocomplete intent.
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }

    }

    private fun checkValidations() {
        if(InternetCheck.isConnectedToInternet(this)
            && Validations.isEmpty(this,edtName,getString(R.string.err_business_name))
            && Validations.validateEmailAddress(this,edtEmail)
            && Validations.validatePhoneNumber(this,etUserPhoneNumber)
            && Validations.isEmpty(this,edtExperience,getString(R.string.err_experience))
            && Validations.isEmpty(this,edtLocation,getString(R.string.err_location))
            && Validations.isEmpty(this,edtSocialMedia,getString(R.string.err_social_media))
            && Validations.isEmpty(this,edtWebsiteLink,getString(R.string.err_website_link))){

                if(service_business.isNullOrEmpty()){
                    Toast.makeText(this,"Please choose the service",Toast.LENGTH_LONG).show()
                    return
                }

            if(swap_value.isNullOrEmpty()){
                Toast.makeText(this,"Please choose the swap system",Toast.LENGTH_LONG).show()
                return
            }

            val map= HashMap<String,RequestBody>()

            map.put("email",createRequestBody(edtEmail.text.toString()))
            map.put("name",createRequestBody(edtName.text.toString()))
            map.put("phone",createRequestBody(etUserPhoneNumber.text.toString()))
            map.put("country_code",createRequestBody(ccp.selectedCountryCodeWithPlus.toString()))
            map.put("experience",createRequestBody(edtExperience.text.toString()))
            map.put("website_link",createRequestBody(edtWebsiteLink.text.toString()))
            map.put("description",createRequestBody(edtAboutMe.text.toString()))
            map.put("address",createRequestBody(edtLocation.text.toString()))
            map.put("latitude",createRequestBody(latitude))
            map.put("longitude",createRequestBody(longitude))
            map.put("category_id",createRequestBody(categoty_id))
            map.put("sub_category_id",createRequestBody(subCategory_id))
            map.put("device_type",createRequestBody("1"))
            map.put("device_token",createRequestBody("12345"))
            map.put("pushKitToken",createRequestBody("12345"))
            map.put("uuid",createRequestBody("12345"))
            map.put("availability",createRequestBody(""))
            map.put("swapInMind",createRequestBody(edtSwaps.text.toString()))
            map.put("isSwapSystem",createRequestBody(swap_value))
            map.put("operationTime",createRequestBody(edtHrsDays.text.toString()))
            map.put("isServiceProviding",createRequestBody(service_business))
            map.put("services",createRequestBody(edtServiceType.text.toString()))
            map.put("socialMediaHandles",createRequestBody(edtSocialMedia.text.toString()))
            map.put("black_owned",createRequestBody(black_owned))

            var imagenPerfil: MultipartBody.Part?=null

            if (imageFile != null) {

                // create RequestBody instance from file
                val requestFile: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), imageFile!!)
                // MultipartBody.Part is used to send also the actual file name
                imagenPerfil = MultipartBody.Part.createFormData("image", imageFile?.name, requestFile)
            }

            infoViewModel.addEditInfo(this,getSecurityKey(this)!!, getUser(this)?.authKey!!,map,imagenPerfil)
            infoViewModel.infoLiveData.observe(this, androidx.lifecycle.Observer {

            })

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun aboutMeTypingTimeScroll() {
        edtAboutMe.setOnTouchListener { view, motionEvent ->
            if (view.id == R.id.edtAboutMe) {
                view.parent.requestDisallowInterceptTouchEvent(true)
                when (motionEvent.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> view.parent.requestDisallowInterceptTouchEvent(false)
                }
            }

            false
        }
    }

    override fun selectedImage(imagePath: File?) {
        refereshImageArray()
        photoList.add(UploadPhotoModel("upload",imagePath,"",0))
        uploadPhotoAdapter?.notifyDataSetChanged()
//        Glide.with(this).load(imagePath).into(riv_Picture)
//        ivDeletePhoto.visibility=View.VISIBLE
        imageFile=imagePath
        uploadMedia("0")

    }

    override fun selectedVideoUri(imagePath: String?) {
        refereshVideoArray()
        Glide.with(this).load(imagePath).into(riv_video)
        videoList.add(UploadVideoModel("upload",imagePath,0))
        selectedVideoFile=imagePath
        uploadMedia("1")

    }
    private fun uploadMedia(type: String) {
        val map=HashMap<String,RequestBody>()
        map.put("type",createRequestBody(type))

        var request: MultipartBody.Part? = null

        if(type == "0"){
            if (imageFile != null) {

                val requestFile: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), imageFile!!)

                request = MultipartBody.Part.createFormData("media", imageFile?.name, requestFile)
            }
        } else{
            if (selectedVideoFile != null) {

                val requestFile: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), selectedVideoFile!!)

                request = MultipartBody.Part.createFormData("media", selectedVideoFile, requestFile)
            }
        }

        infoViewModel.uploadMedia(this,getSecurityKey(this)!!, getUser(this)?.authKey!!,map,request!!)
        infoViewModel.mediaLiveData.observe(this, androidx.lifecycle.Observer {
            if(it.code==200){
                Toast.makeText(this,"Uploaded media successfully",Toast.LENGTH_LONG).show()
                mediaList.addAll(it.data)
            }

            else{
                Toast.makeText(this,it.msg.toString(),Toast.LENGTH_LONG).show()
            }
        })

    }

    fun createRequestBody(param:String): RequestBody {
        val request= param.toRequestBody("text/plain".toMediaTypeOrNull())
        return request
    }

    override fun callbackMethod(data: ArrayList<CategoryData>) {

        val arrayList=ArrayList<String>()
        val categoryList= ArrayList<String>()
        data.forEach{
            if(it.isSelect){
                categoryList.add(it.name.toString())
                arrayList.add(it.id.toString())
            }
        }
        categoty_id = TextUtils.join(",",arrayList)
        category_name= TextUtils.join(",",categoryList)
        etSelectCategory.setText(category_name)
        Log.i("TAG",arrayList.toString())
    }

    override fun callbackSubCategories(data: ArrayList<SubCategories>) {

        val id_list=ArrayList<String>()
        val name_list=ArrayList<String>()
        data.forEach{
            if(it.isSelect){
                id_list.add(it.id.toString())
                name_list.add(it.name)
            }
        }
        subCategory_id= TextUtils.join(",",id_list)
        subCategory_name= TextUtils.join(",",name_list)
        Log.i("subactegory_ids","====ids"+ subCategory_id +"====names"+subCategory_name)
    }

    override fun onVideoUpload(data:UploadVideoModel,position: Int) {
        getImage(this,0,true)
    }

    override fun deleteVideo(data:UploadVideoModel,position: Int) {
        infoViewModel.deleteMedia(this, getSecurityKey(this)!!, getUser(this)?.authKey!!,data.id.toString())
        infoViewModel.mediaLiveData.observe(this,  {

            if(it.code==200){
                Log.i("====",it.msg)
            }
        })
    }

    override fun onPhotoUpload(data:UploadPhotoModel,position: Int) {
        getImage(this,0,false)
    }

    override fun onDeletePhoto(data:UploadPhotoModel,position: Int) {
        photoList.removeAt(position)
        Log.i("list_size",photoList.toString())
        uploadPhotoAdapter?.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK && data != null) {
                val place = Autocomplete.getPlaceFromIntent(data)
                edtLocation.setText(place.name.toString())
                latitude = place.latLng?.latitude.toString()
                longitude = place.latLng?.longitude.toString()
            }

        }
    }

    fun refereshVideoArray(){
        for (m in videoList){
            if(m.type.equals("undefined")){
                videoList.remove(m)
                break
            }
        }
    }

    fun refereshImageArray(){
        for (m in photoList){
            if(m.type.equals("undefined")){
                photoList.remove(m)
                break
            }
        }
    }
}