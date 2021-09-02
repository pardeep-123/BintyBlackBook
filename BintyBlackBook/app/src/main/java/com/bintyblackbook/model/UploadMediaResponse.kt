package com.bintyblackbook.model

class UploadMediaResponse:BaseResponseModel(){
    val data: ArrayList<MediaData> = ArrayList<MediaData>()
}

data class MediaData(
    val createdAt: String,
    val id: Int,
    val media: String,
    val type: Int,         //0-> image, 1-> video
    val updatedAt: String,
    val userId: Int
)