package com.bintyblackbook.model

 class FavEventsResponse:BaseResponseModel() {

     val data: ArrayList<FavEventData> = ArrayList<FavEventData>()

 }

data class FavEventData(
    val date: Int,
    val description: String,
    val eventId: Int,
    val id: Int,
    val image: String,
    val latitude: String,
    val location: String,
    val longitude: String,
    val moreInfo: String,
    val name: String,
    val rsvpLink: String,
    val status: Int,
    val time: Int,
    val userId: Int
)