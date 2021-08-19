package com.bintyblackbook.model

class BookingResponse:BaseResponseModel(){

    val data: BookingData?=null

}

 class BookingData{
    val pastBookings: ArrayList<PastBooking> = ArrayList<PastBooking>()
    val upcomingBookings: ArrayList<UpcomingBookings> = ArrayList<UpcomingBookings>()
}

data class PastBooking(
    val availabilityDate: String,
    val availabilityId: Int,
    val bookingId: String,
    val created: Int,
    val id: Int,
    val otherUserId: Int,
    val providerId: Int,
    val slots: ArrayList<Slot>,
    val status: Int,
    val userId: Int,
    val userImage: String,
    val userName: String
)

data class UpcomingBookings(
    val availabilityDate: String,
    val availabilityId: Int,
    val bookingId: String,
    val created: Int,
    val id: Int,
    val otherUserId: Int,
    val providerId: Int,
    val slots: ArrayList<Slot>,
    val status: Int,
    val userId: Int,
    val userImage: String,
    val userName: String
)

