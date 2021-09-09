package com.bintyblackbook.model

import java.io.File

data class UploadPhotoModel(
    var type:String,
    var photo_url:File?,
    var image:String?,
    var id: Int,

)