package com.bintyblackbook.model

class ContentResponse :BaseResponseModel(){

    val data:ContentData?=null
}

class ContentData {
    val id:String?=null
    val privacy:String?=null
    val term:String?=null
}
