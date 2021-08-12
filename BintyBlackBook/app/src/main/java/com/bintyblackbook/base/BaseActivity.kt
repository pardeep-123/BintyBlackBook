package com.bintyblackbook.base

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.MediaScannerConnection
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bintyblackbook.R
import com.bintyblackbook.util.CustomProgressDialog
import com.bintyblackbook.util.camera.CompressFile
import com.google.android.material.snackbar.Snackbar
import com.learnerdash.commons.camera.PermissionUtils
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

open class BaseActivity : AppCompatActivity() {
    private var mSnackBar: Snackbar? = null
    var mProgress: CustomProgressDialog? = null
    lateinit var context: Context
    private val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
    var imageuri1: Uri = Uri.EMPTY



    private val IMAGE_DIRECTORY = "/windealz"

    val TAG = "BaseActivity" // Activity Tag


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context = this
        mProgress = CustomProgressDialog(this)

    }


    private fun showMessage(isConnected: Boolean) {

        try {

            if (!isConnected) {

                val messageToUser = getString(R.string.internet_connection)

                mSnackBar = Snackbar.make(
                    getWindow().getDecorView().getRootView(),
                    messageToUser,
                    Snackbar.LENGTH_LONG
                ) //Assume "rootLayout" as the root layout of every activity.
                mSnackBar?.duration = Snackbar.LENGTH_INDEFINITE!!
                mSnackBar?.show()
            } else {
                mSnackBar?.dismiss()
            }

        } catch (e: Exception) {
Log.e("TAG",e.printStackTrace().toString())
        }
    }

    fun getContextT(): Context {
        return context
    }

    override fun onResume() {
        super.onResume()

      //  ConnectivityReceiver.connectivityReceiverListener = this



    }

 /*   *//**
     * Callback will be called when there is change
     *//*
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showMessage(isConnected)
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(ConnectivityReceiver())
        } catch (e: Exception) {
            Log.e("TAG",e.printStackTrace().toString())
        }
    }*/

    //Navigate screen with finish a activity
    fun navigatewithFinish(destination: Class<*>) {
        val intent = Intent(this@BaseActivity, destination)
        startActivity(intent)
        finish()
    }



    //Show alert message
    fun showAlertWithOk(message: String) {
        Handler(Looper.getMainLooper()).post(object : Runnable {
            public override fun run() {
                if (message != null && !message.isEmpty()) {
                    try {
                        AlertDialog.Builder(this@BaseActivity)
                            .setMessage(message)
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok, object : DialogInterface.OnClickListener {
                                override fun onClick(arg0: DialogInterface, arg1: Int) {
                                    arg0.dismiss()
                                }
                            }).create().show()

                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

            }
        })

    }



    //Show alert message
    fun showFinishAlert(message: String) {
        Handler(Looper.getMainLooper()).post(object : Runnable {
            public override fun run() {
                if (message != null && !message.isEmpty()) {
                    try {
                        AlertDialog.Builder(this@BaseActivity)
                            .setMessage(message)
                            .setPositiveButton(android.R.string.ok, object : DialogInterface.OnClickListener {
                                override fun onClick(arg0: DialogInterface, arg1: Int) {
                                    arg0.dismiss()
                                    finish()
                                }

                            }).create().show()

                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

            }
        })

    }


    //Navigate screen with finish a activity
    fun navigatewithFinishAffinity(destination: Class<*>) {
        val intent = Intent(this@BaseActivity, destination)
        startActivity(intent)
        finishAffinity()
    }

    //Navigate screen to another
    fun navigate(destination: Class<*>) {
        val intent = Intent(this@BaseActivity, destination)
        startActivity(intent)
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


    fun showProgressDialog(){
        mProgress = CustomProgressDialog(this)
        mProgress!!.show()
    }

    fun dismissProgressDialog(){

        mProgress!!.dismiss()
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }


    //Sjoe capture or pick a image dialouge
    public fun showPictureDialog() {
        if (checkAndRequestPermissions()) {
            showPopUpPhoto()
        } else {
            Log.e("TAG","Enable camera Permission")
        }
    }

    fun showPopUpPhoto(){

        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> camera()
            }
        }
        pictureDialog.show()

    }

    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, PermissionUtils.GALLERYREQUESTCODE)
    }


    fun checkAndRequestPermissions(): Boolean {

        val camerapermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val writepermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)


        val listPermissionsNeeded = ArrayList<String>()

        if (camerapermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toTypedArray(), REQUEST_ID_MULTIPLE_PERMISSIONS)
            return false
        }
        return true
    }



    fun showDialogOK(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", okListener)
            .create()
            .show()
    }




    fun explain(msg: String) {
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
        dialog.setMessage(msg)
            .setPositiveButton("Yes") { paramDialogInterface, paramInt ->
                //  permissionsclass.requestPermission(type,code);
                startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:cabuser.com.heyviplimo")))
            }
            .setNegativeButton("Cancel") { paramDialogInterface, paramInt -> finish() }
        dialog.show()
    }


    fun openGallery() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // Do something for lollipop and above versions
            if (isGalleryPermissions()) {
                val pickPhoto = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                pickPhoto.setType("image/*")
                startActivityForResult(
                    pickPhoto,
                    PermissionUtils.GALLERYREQUESTCODE
                )//one can be replaced with any action code
            }
        } else {
            // do something for phones running an SDK before lollipop
            val pickPhoto = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(
                pickPhoto,
                PermissionUtils.GALLERYREQUESTCODE
            )//one can be replaced with any action code
        }

    }





    public fun isGalleryPermissions(): Boolean {

        var isGranted: Boolean = false
        val permission = ContextCompat.checkSelfPermission(
            this@BaseActivity,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to get image from gallery denied")
            makeGalleryRequest()
            isGranted = false

        } else {
            val pickPhoto = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            pickPhoto.setType("image/*")
            startActivityForResult(
                pickPhoto,
                PermissionUtils.GALLERYREQUESTCODE
            )//one can be replaced with any action code
            isGranted = true

        }
        return isGranted
    }




    //open camera if permision granted
    private fun camera() {
        var values: ContentValues = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
        imageuri1 = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
        var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri1)
        startActivityForResult(intent, PermissionUtils.CAMERAPERMISSIONCODE)
    }




    public fun makeGalleryRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            PermissionUtils.READSTORAGEPERMISSIONCODE
        )
    }


    // save captured image from camera
    fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY
        )
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }
        try {
            val f = File(
                wallpaperDirectory, ((Calendar.getInstance()
                    .getTimeInMillis()).toString() + ".jpg")
            )
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(
                this,
                arrayOf(f.getPath()),
                arrayOf("image/jpeg"), null
            )
            fo.close()
            return f.getAbsolutePath()
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return ""
    }

    fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true)
    }

    @Nullable
    public fun getImagePathFromUri(@Nullable aUri: Uri): String {
        var imagePath: String? = null
        if (aUri == null) {
            return imagePath!!
        }
        if (DocumentsContract.isDocumentUri(this, aUri)) {
            val documentId = DocumentsContract.getDocumentId(aUri)
            if ("com.android.providers.media.documents" == aUri.getAuthority()) {
                val id = documentId.split((":").toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
                val selection = MediaStore.Images.Media._ID + "=" + id
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
            } else if ("com.android.providers.downloads.documents" == aUri.getAuthority()) {
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(documentId)
                )
                imagePath = getImagePath(contentUri, null!!)
            }
        } else if ("content".equals(aUri.getScheme(), ignoreCase = true)) {
            imagePath = getImagePath(aUri, null)
        } else if ("file".equals(aUri.getScheme(), ignoreCase = true)) {
            imagePath = aUri.getPath()
        }
        Log.e("GalleryPath : ", imagePath.toString())
        val file = CompressFile.getCompressed(this@BaseActivity, imagePath)
        Log.e("Galleryfile : ", file.absolutePath)
        return file.absolutePath!!
    }

    private fun getImagePath(aUri: Uri, aSelection: String?): String {
        var path: String? = null
        val cursor = getContentResolver()
            .query(aUri, null, aSelection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path!!
    }

    //Show progress untiol a image not load in Glide
    fun getImageLoader(): CircularProgressDrawable {
        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        return circularProgressDrawable
    }

    /*
    * Check Internet Connection Available or not
    * */
    fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetworkInfo: NetworkInfo? = null
        activeNetworkInfo = cm.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }







}