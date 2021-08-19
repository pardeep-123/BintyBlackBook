package com.bintyblackbook.model

class BlockListResponseModel:BaseResponseModel() {

    val data: ArrayList<BlockedData> = ArrayList<BlockedData>()
}

data class BlockedData(
    val id:Int

)