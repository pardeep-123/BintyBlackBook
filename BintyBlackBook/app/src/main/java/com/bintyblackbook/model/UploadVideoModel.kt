package com.bintyblackbook.model

import android.net.Uri
import java.io.File

data class UploadVideoModel(
    var type:String,
    var video_url:String?,
    var id:Int?
)