package com.bintyblackbook.ui.activities.home

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bintyblackbook.R
import com.bintyblackbook.util.*
import com.bintyblackbook.viewmodel.EventsViewModel
import com.bumptech.glide.Glide
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_add_event.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.business_signup_layout.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class AddEventActivity : ImagePickerUtility() {

    private val myCalendar = Calendar.getInstance()
    /*private val mCurrentTime = Calendar.getInstance()*/
    private lateinit var date: OnDateSetListener
    private lateinit var time: TimePickerDialog.OnTimeSetListener
    lateinit var eventsViewModel: EventsViewModel
    var mProgress: CustomProgressDialog? = null
    private var mSnackBar: Snackbar? = null
    var imageFile:File?=null
    var selectedImagePath:File?=null
    private var latitude = ""
    private var longitude = ""
    private val AUTOCOMPLETE_REQUEST_CODE = 1
    var heading=""
    var type = ""
    var image=""
    var event_id=""
    var dateStamp=""
    var timeStamp=""

    override fun selectedImage(imagePath: File?) {
        Glide.with(this).load(imagePath).into(riv)
        selectedImagePath=imagePath
    }

    override fun selectedVideoUri(imagePath: String?, videoPath: String?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        Places.initialize(this, "AIzaSyAd4ZzHfi-nAp-6IAm1YF5-pVxCAlzW4EA")

        initViews()
        getIntentData()

        rlBack.setOnClickListener {
            finish()
        }

        date = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateLabel()
        }

        time = TimePickerDialog.OnTimeSetListener { view, hour, minute ->
            myCalendar.set(Calendar.HOUR_OF_DAY, hour)
            myCalendar.set(Calendar.MINUTE, minute)
            updateTimeLabel()
        }

        riv.setOnClickListener {
            getImage(this, 0, false)
        }

        edtDate.setOnClickListener {
            datePicker()
        }

        edtTime.setOnClickListener {
            timePicker()
        }

        btnSave.setOnClickListener {
           checkValidations()
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
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(
                this
            )
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }
    }

    private fun getIntentData() {
        heading = intent.getStringExtra(AppConstant.HEADING).toString()
        if (heading!= null){
            tvHeading.text = heading
        }
        type= intent.getStringExtra("type").toString()
        val name= intent.getStringExtra("name").toString()
        val link= intent.getStringExtra("link").toString()
        val location= intent.getStringExtra("location").toString()
        val time= intent.getStringExtra("time").toString()
        val date= intent.getStringExtra("date").toString()
        val description= intent.getStringExtra("description").toString()
        val info= intent.getStringExtra("info").toString()
        image= intent.getStringExtra("image").toString()
        event_id= intent.getStringExtra("event_id").toString()

        Log.e("eventId",event_id.toString())
        if(type=="add"){
            edtEventName.setText("")
            edtLocation.setText("")
            edtDate.setText("")
            edtTime.setText("")
            edtLink.setText("")
            edtDesc.setText("")
            edtMoreInfo.setText("")
        }else{
            edtEventName.setText(name)
            edtLocation.setText(location)
            val date= MyUtils.getDate(date.toLong())
            edtDate.setText(date)
            val time= MyUtils.getTime(time.toLong())
            edtTime.setText(time)
            edtLink.setText(link)
            edtDesc.setText(description)
            edtMoreInfo.setText(info)
            Glide.with(this).load(image).into(riv)
        }
    }

    private fun initViews() {
        mProgress = CustomProgressDialog(this)
        eventsViewModel= EventsViewModel()
    }

    //pass date and time as timestamp
    private fun checkValidations() {
        if(InternetCheck.isConnectedToInternet(this)
            && Validations.isEmpty(this, edtEventName, getString(R.string.err_event_name))
            && Validations.isEmpty(this, edtLocation, getString(R.string.err_location))
            && Validations.isEmpty(this, edtDate, getString(R.string.err_date))
            && Validations.isEmpty(this, edtTime, getString(R.string.err_time))
            && Validations.isEmpty(this, edtLink, getString(R.string.err_web_link))
            && Validations.isEmpty(this, edtDesc, getString(R.string.err_description))
            && Validations.isEmpty(this, edtMoreInfo, getString(R.string.err_more_info))
        ) {

           // if(type.equals("add")){
                if(selectedImagePath?.absolutePath.isNullOrEmpty() && image.isNullOrEmpty()){
                    Toast.makeText(this, getString(R.string.please_choose_image), Toast.LENGTH_LONG).show()
                    return
                }
          /*  }
            else{
                if(image.isEmpty()){
                    Toast.makeText(this, getString(R.string.please_choose_image), Toast.LENGTH_LONG).show()
                    return
                }
            }*/

            timeStamp= MyUtils.currentTimeToLong(edtDate.text.toString() + " " + edtTime.text.toString())
            Log.i("timestamp", timeStamp)
            dateStamp = MyUtils.convertDateToLong(edtDate.text.toString())
            val map: HashMap<String, RequestBody> = HashMap()
            map.put("name", createRequestBody(edtEventName.text.toString()))
            map.put("location", createRequestBody(edtLocation.text.toString()))
            map.put("date", createRequestBody(dateStamp))
            map.put("time", createRequestBody(timeStamp))
            map.put("rsvp_link", createRequestBody(edtLink.text.toString()))
            map.put("description", createRequestBody(edtDesc.text.toString()))
            map.put("more_info", createRequestBody(edtMoreInfo.text.toString()))
            map.put("user_id", createRequestBody(getUser(this)?.id.toString()))
            map.put("latitude", createRequestBody(latitude))
            map.put("longitude", createRequestBody(longitude))

            var imagenPerfil: MultipartBody.Part? = null
            if (selectedImagePath != null) {

                // create RequestBody instance from file
                val requestFile: RequestBody = RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    selectedImagePath!!
                )
                // MultipartBody.Part is used to send also the actual file name
                imagenPerfil = MultipartBody.Part.createFormData(
                    "image",
                    selectedImagePath?.name,
                    requestFile
                )
            }

            if(type.equals("add")){
                eventsViewModel.addEvent(this, getSecurityKey(this)!!, getUser(this)?.authKey!!, map, imagenPerfil)
            } else{
                map.put("event_id", createRequestBody(event_id))
                eventsViewModel.editEvent(this, getSecurityKey(this)!!, getUser(this)?.authKey!!, map, imagenPerfil)
            }
            eventsViewModel.baseEventsLiveData.observe(this, {

                if (it.code == 200) {
                    showAlert(this, it.msg, getString(R.string.ok)) {
                        finish()
                    }
                }
            })
        }
    }

    private fun timePicker() {
        TimePickerDialog(this, time, myCalendar[Calendar.HOUR_OF_DAY], myCalendar[Calendar.MINUTE], false).show()
    }

    fun createRequestBody(param: String): RequestBody {
        val request=  RequestBody.create("text/plain".toMediaTypeOrNull(), param)
        return request
    }

    private fun datePicker() {

        val datePickerDialog = DatePickerDialog(this@AddEventActivity, date,  myCalendar[Calendar.YEAR],
            myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH])

        datePickerDialog.datePicker.minDate= System.currentTimeMillis() -1000
        datePickerDialog.show()

    }

    private fun updateDateLabel() {
        val dateFormat = "dd/MM/yyyy" //In which you need put here
        val sdf = SimpleDateFormat(dateFormat, Locale.US)
        edtDate.setText(sdf.format(myCalendar.time))

    }

    private fun updateTimeLabel() {
        val timeFormat = "hh:mm a" //In which you need put here
        val sdf = SimpleDateFormat(timeFormat, Locale.US)
        edtTime.setText(sdf.format(myCalendar.time))
    }

    fun showProgressDialog(){
        mProgress = CustomProgressDialog(this)
        mProgress?.show()
    }

    fun dismissProgressDialog(){

        mProgress?.dismiss()
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
           e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK && data != null) {
                val place = Autocomplete.getPlaceFromIntent(data)
                 edtLocation.setText(place.name.toString())
                latitude = place.latLng?.latitude.toString()
                longitude = place.latLng?.longitude.toString()
                Log.i(
                    "string",
                    place.address + place.toString() + "===lat" + latitude + "===long" + longitude
                )
            }

        }
    }
}