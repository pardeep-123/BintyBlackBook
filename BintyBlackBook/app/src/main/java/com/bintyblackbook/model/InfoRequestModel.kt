package com.bintyblackbook.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class InfoRequestModel:Serializable{

    @SerializedName("email")
    var email:String=""
    @SerializedName("name")
    var name:String=""
    @SerializedName("phone")
    var phone:String=""
    @SerializedName("country_code")
    var country_code:String=""
    @SerializedName("experience")
    var experience:String=""
    @SerializedName("website_link")
    var website_link:String=""
    @SerializedName("description")
    var description:String=""
    @SerializedName("address")
    var address:String=""
    @SerializedName("latitude")
    var latitude:String=""
    @SerializedName("longitude")
    var longitude:String=""
    @SerializedName("category_id")
    var category_id:String=""
    @SerializedName("sub_category_id")
    var sub_category_id:String=""
    @SerializedName("device_type")
    var device_type=""
    @SerializedName("device_token")
    var device_token=""
    @SerializedName("pushKitToken")
    var pushKitToken:String=""
    @SerializedName("uuid")
    var uuid:String=""
    @SerializedName("swapInMind")
    var swapInMind:String=""
    @SerializedName("isSwapSystem")
    var isSwapSystem:String=""
    @SerializedName("operationTime")
    var operationTime:String=""
    @SerializedName("isServiceProviding")
    var isServiceProviding:String=""
    @SerializedName("services")
    var services:String=""
    @SerializedName("socialMediaHandles")
    var socialMediaHandles:String=""

}