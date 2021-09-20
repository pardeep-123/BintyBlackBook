package com.bintyblackbook.model

class AvailabilityResponse : BaseResponseModel(){
    val data: ArrayList<AvailabilityData> = ArrayList<AvailabilityData>()

}
class AvailabilityData{
    val date: Long?=null
    val id: Int= 0
    val providerId: Int= 0
    var isSelected=false
    val slots: ArrayList<Slot> = ArrayList<Slot>()
}

data class Slot(
    val availabilityId: Int,
    val id: Int,
    val slots: Long,
    val status: Int
)