package com.bintyblackbook.ui.activities.authentication

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.bintyblackbook.R
import com.bintyblackbook.model.InfoRequestModel
import com.bintyblackbook.model.CategoryData
import com.bintyblackbook.model.SubCategories
import com.bintyblackbook.ui.activities.home.CheckAvailabilityActivity
import com.bintyblackbook.ui.activities.home.profileUser.SetAvailabilityActivity
import com.bintyblackbook.util.*
import com.bintyblackbook.viewmodel.InfoViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.android.synthetic.main.toolbar.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class InfoActivity : ImagePickerUtility(), CustomInterface {

    private val myCalendar = Calendar.getInstance()
    private lateinit var date: DatePickerDialog.OnDateSetListener

    var imageFile:File?=null
    var selectedVideoUri:Uri?=null
    var mProgress: CustomProgressDialog? = null
    private var mSnackBar: Snackbar? = null

    var categoryList = ArrayList<CategoryData>()

    lateinit var infoViewModel:InfoViewModel
    var swap_value=""
    var service_business =""

    var profile_image=""

    var categoryDialogFragment:CategoryDialogFragment?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyUtils.fullscreen(this)
        setContentView(R.layout.activity_info)
        initViews()

        setData()

        getCategoryData()
        setSpinnerBusinessCategory()
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

    private fun initViews() {
        mProgress = CustomProgressDialog(this)
        infoViewModel= InfoViewModel(this)
    }

    private fun getCategoryData() {

        infoViewModel.getCategories(getSecurityKey(this)!!, getUser(this)?.authKey!!)
        infoViewModel.categoryLiveData.observe(this, androidx.lifecycle.Observer {

            if(it.code==200){
                 categoryList.addAll(it?.data!!)
            }
            else{
                Log.i("TAG",it.msg.toString())
            }
        })
    }


    fun showProgressDialog(){
        mProgress = CustomProgressDialog(this)
        mProgress!!.show()
    }

    fun dismissProgressDialog(){

        mProgress!!.dismiss()
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
            val intent= Intent(this,SetAvailabilityActivity::class.java)
            startActivity(intent)
        }

        civ_profile.setOnClickListener {
            getImage(this,0,false)
        }

      /*  edtSetAvailability.setOnClickListener {
            datePicker()
        }*/

        riv_video.setOnClickListener {
            getImage(this,0,true)
        }

        riv_Picture.setOnClickListener {
            getImage(this,0,false)
        }

        btnSubmit.setOnClickListener {

            checkValidations()
//            val intent = Intent(this, HomeActivity::class.java)
//            startActivity(intent)
        }

        rgService.setOnCheckedChangeListener(object :RadioGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                when(checkedId){
                    R.id.rbNo ->{
                        service_business="1"
                        tvServiceType.visibility=View.GONE
                        edtServiceType.visibility=View.GONE
                        tvAvailability.visibility=View.GONE
                        edtSetAvailability.visibility=View.GONE
                    }

                    R.id.rbYes ->{
                        service_business="0"
                        tvServiceType.visibility=View.VISIBLE
                        edtServiceType.visibility=View.VISIBLE
                        tvAvailability.visibility=View.VISIBLE
                        edtSetAvailability.visibility=View.VISIBLE
                    }
                }
            }

        })

       rgSwap.setOnCheckedChangeListener(object :RadioGroup.OnCheckedChangeListener{
           override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
               when(checkedId){
                   R.id.rbYesSwap ->{
                        swap_value="1"
                   }

                   R.id.rbNoSwap ->{
                       swap_value="0"

                   }
               }
           }

       })

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

            val builder = MultipartBody.Builder().setType(MultipartBody.FORM)

            val request=InfoRequestModel().also { request->

            request.name=edtName.text.toString()
            request.email=edtEmail.text.toString()
            request.phone=etUserPhoneNumber.text.toString()
            request.country_code=ccp.selectedCountryCodeWithPlus
            request.experience=edtExperience.text.toString()
            request.website_link=edtWebsiteLink.text.toString()
            request.description=edtAboutMe.text.toString()
            request.address=edtLocation.text.toString()
            request.latitude=""
            request.longitude=""
            request.category_id=""
            request.sub_category_id=""
            request.device_type="1"
            request.device_token="12345"
            request.pushKitToken="1234"
            request.uuid="1234"
            request.swapInMind=edtSwaps.text.toString()
            request.isSwapSystem=swap_value
            request.operationTime=edtHrsDays.text.toString()
            request.isServiceProviding=service_business
            request.services= edtServiceType.text.toString()
            request.socialMediaHandles=edtSocialMedia.text.toString()
            }

            /*val map = request.serializeToMap()
            for (part in map) {
                builder.addFormDataPart(part.key, part.value)
            }*/

            val body = builder.build()

        }
    }

    private fun datePicker() {
        DatePickerDialog(
            this, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        ).show()
    }

    private fun updateDateLabel() {
        val dateFormat = "dd/MM/yy" //In which you need put here
        val sdf = SimpleDateFormat(dateFormat, Locale.US)
        edtSetAvailability.setText(sdf.format(myCalendar.time))
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setSpinnerBusinessCategory() {
        spinnerBusinessCategory.setOnTouchListener { view, motionEvent ->
            MyUtils.hideSoftKeyboard(this)
            false
        }

        val genderSpinnerArray =
            arrayOf("Select", "Business Category", "Business Category", "Business Category")
        val spinnerAdapter =
            ArrayAdapter<String>(this, R.layout.item_spinner, R.id.tvSpinner, genderSpinnerArray)
        spinnerBusinessCategory.adapter = spinnerAdapter

        spinnerBusinessCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                @RequiresApi(Build.VERSION_CODES.M)
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val tvSpinner =
                        (parent?.getChildAt(0) as View).findViewById<TextView>(R.id.tvSpinner)
                    tvSpinner.setTextColor(getColor(R.color.whiteColor))

                }

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


    override fun selectedImage(imagePath: File?) {
        Glide.with(this).load(imagePath).into(riv_Picture)
        imageFile=imagePath
        uploadMedia("0")
    }

    override fun selectedVideoUri(videoUri: Uri?) {
        Glide.with(this).load(videoUri).into(riv_video)
        selectedVideoUri=videoUri
        uploadMedia("1")

    }
    private fun uploadMedia(type: String) {
        var map=HashMap<String,RequestBody>()
        map.put("type",createRequestBody(type))

        var request: MultipartBody.Part? = null
        if (imageFile != null) {

            val requestFile: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), imageFile!!)

            request = MultipartBody.Part.createFormData("media", imageFile?.name, requestFile)
        }

        infoViewModel.uploadMedia(getSecurityKey(this)!!, getUser(this)?.authKey!!,map,request!!)
        infoViewModel.mediaLiveData.observe(this, androidx.lifecycle.Observer {
            if(it.code==200){
                Toast.makeText(this,it.msg,Toast.LENGTH_LONG).show()
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
        var categoryId=StringBuilder("")
        var arrayList=ArrayList<String>()
        data.forEach{
            if(it.isSelect){
                arrayList.add(it.id.toString())
            }
        }
        TextUtils.join(",",arrayList)
        Log.i("TAG",arrayList.toString())
    }

    override fun callbackSubCategories(data: ArrayList<SubCategories>) {
        var categoryId=StringBuilder("")
        var arrayList=ArrayList<String>()
        data.forEach{
            if(it.isSelect){
                arrayList.add(it.id.toString())
            }
        }
        TextUtils.join(",",arrayList)
        Log.i("TAG",arrayList.toString())
    }
}