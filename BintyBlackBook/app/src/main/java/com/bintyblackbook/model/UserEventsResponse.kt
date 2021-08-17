package com.bintyblackbook.model

 class UserEventsResponse:BaseResponseModel(){

    val data: ArrayList<EventData> = ArrayList<EventData>()

}

data class EventData(
    val createdTime: Int,
    val date: Int,
    val description: String,
    val id: Int,
    val image: String,
    var isFavourite: Int,
    val isPromoted: Int,
    val latitude: String,
    val location: String,
    val longitude: String,
    val moreInfo: String,
    val name: String,
    val rsvpLink: String,
    val time: Int,
    val userId: Int
)


