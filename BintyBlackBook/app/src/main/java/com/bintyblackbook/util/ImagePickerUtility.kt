package com.bintyblackbook.util

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.Gravity
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bintyblackbook.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


abstract class ImagePickerUtility : AppCompatActivity() {

    val REQUEST_CODE = 100
    private val GALLERY_PIC_REQUEST_CODE = 101
    private val CAMERA_REQUEST_CODE = 102
    var mVideoDialog: Boolean = false

    private val GALLERY_VIDEO_REQUEST_CODE = 1001
    private val REQUEST_VIDEO_CAPTURE = 1002

    var imageAbsolutePath = ""
    private var mActivity: Activity? = null
    private var mCode = 0


    open fun getImage(activity: Activity, code: Int, videoDialog: Boolean) {
        mActivity = activity
        mCode = code
        mVideoDialog = videoDialog

        //*****videoDialog -> put false for pick the Image.*****
        //*****videoDialog -> put true for pick the Video.*****

        if (!cameraPermission(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                )
            )
        ) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                checkPermissionDenied(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                checkPermissionDenied(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            } else {
                requestPermission()
            }
        } else {
            imageDialog()

        }
    }

    private fun imageDialog() {
        val dialog = Dialog(mActivity!!)
        dialog.setContentView(R.layout.camera_gallery_popup)
//        dialog.window!!.attributes.windowAnimations = R.style.Theme_Dialog
        val window = dialog.window
        window!!.setGravity(Gravity.BOTTOM)
        window!!.setLayout(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val camera = dialog.findViewById<TextView>(R.id.tvCamera)
        val cancel = dialog.findViewById<TextView>(R.id.tv_cancel)
        val gallery = dialog.findViewById<TextView>(R.id.tvGallery)
        cancel.setOnClickListener { dialog.dismiss() }

        camera.setOnClickListener {
            dialog.dismiss()
            if (mVideoDialog) {
                captureVideo(mActivity!!)
            } else {
                captureImage(mActivity!!)
            }

        }

        gallery.setOnClickListener {
            dialog.dismiss()
            if (mVideoDialog) {
                openGalleryForVideo(mActivity!!)
            } else {
                openGallery(mActivity!!)
            }
        }
        dialog.show()
    }

    open fun captureImage(activity: Activity) {
        mActivity = activity


        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile = createImageFile()

        val fileProvider =
            activity?.let { FileProvider.getUriForFile(it, "com.bintyblackbook", photoFile) }
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
        } else {
            Toast.makeText(activity, "Unable to open camera", Toast.LENGTH_LONG).show()
        }


        /* try {
             val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
             val photoFile: File = createFileForImage()!!
             imageAbsolutePath = photoFile.absolutePath
             intent.putExtra(
                 MediaStore.EXTRA_OUTPUT,
                 FileProvider.getUriForFile(this, "com.android.cameraclick", photoFile)
             )
             startActivityForResult(intent, CAMERA_REQUEST_CODE)
         } catch (e: IOException) {
             e.printStackTrace()
         }*/
    }

    open fun captureVideo(activity: Activity) {
        mActivity = activity

        val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        if (takeVideoIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
        }


    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
//        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {

            imageAbsolutePath = absolutePath
        }
    }


    open fun openGallery(activity: Activity) {
        mActivity = activity
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(
            Intent.createChooser(intent, "Select a File"), GALLERY_PIC_REQUEST_CODE
        )
    }

    open fun openGalleryForVideo(activity: Activity) {
        mActivity = activity

        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY_VIDEO_REQUEST_CODE)

    }

    private fun cameraPermission(permissions: Array<String>): Boolean {
        return ContextCompat.checkSelfPermission(
            mActivity!!,
            permissions[0]
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            mActivity!!,
            permissions[1]
        ) == PackageManager.PERMISSION_GRANTED
    }

    open fun requestPermission() {
        ActivityCompat.requestPermissions(
            mActivity!!, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ), REQUEST_CODE
        )
    }

    private fun checkPermissionDenied(permissions: Array<out String>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(permissions[0])) {
                val mBuilder = AlertDialog.Builder(mActivity)
                val dialog: AlertDialog =
                    mBuilder.setTitle(R.string.alert).setMessage(R.string.permissionRequired)
                        .setPositiveButton(
                            R.string.ok
                        ) { dialog, which -> requestPermission() }
                        .setNegativeButton(
                            R.string.cancel
                        ) { dialog, which ->

                        }.create()
                dialog.setOnShowListener {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                        ContextCompat.getColor(
                            mActivity!!, R.color.colorPrimary
                        )
                    )
                }
                dialog.show()
            } else {
                val builder = AlertDialog.Builder(mActivity)
                val dialog: AlertDialog =
                    builder.setTitle(R.string.alert).setMessage(R.string.permissionRequired)
                        .setCancelable(
                            false
                        )
                        .setPositiveButton(R.string.openSettings) { dialog, which ->
                            finish()
                            val intent = Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", packageName, null)
                            )
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }.create()
                dialog.setOnShowListener {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                        ContextCompat.getColor(
                            mActivity!!, R.color.colorPrimary
                        )
                    )
                }
                dialog.show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getImage(mActivity!!, mCode, mVideoDialog)
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                checkPermissionDenied(permissions)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            selectedImage(imageAbsolutePath)

        } else if (requestCode == GALLERY_PIC_REQUEST_CODE && resultCode == RESULT_OK) {

        } else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            val videoUri = intent.data
            /*videoView.setVideoURI(videoUri)*/
            selectedVideoUri(videoUri)
        } else if (requestCode == GALLERY_VIDEO_REQUEST_CODE && resultCode == RESULT_OK) {
            val videoUri = intent.data
            /*videoView.setVideoURI(videoUri)*/
            selectedVideoUri(videoUri)
        }
    }


    abstract fun selectedImage(imagePath: String?)

    abstract fun selectedVideoUri(videoUri: Uri?)
}