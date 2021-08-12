package com.bintyblackbook.api

import com.google.gson.JsonElement
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ApiCallInterface {

    /*
    Login API
    */

    @FormUrlEncoded
    @POST(ApiConstants.LOGIN)
    fun loginUser(
        @Header("security_key")security_key:String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("latitude") latitude:String,
        @Field("longitude") longitude:String,
        @Field("address") address:String,
        @Field("device_type") device_type:String,
        @Field("device_token") device_token:String
        ): Call<JsonElement>

    /*
       User signUp api
     */

    @Multipart
    @POST(ApiConstants.USER_SIGNUP)
    fun userSignUp(
        @Header ("security_key") security_key:String,
        @PartMap partMap: Map<String, RequestBody>,
        @Part file: MultipartBody.Part?
    ): Call<JsonElement>

    /*
    Business SignUpApi
     */
    @Multipart
    @POST(ApiConstants.BUSINESS_SIGNUP)
    fun businessSignUp(
        @Header ("security_key") security_key:String,
        @PartMap partMap: Map<String, RequestBody>,
        @Part file: MultipartBody.Part?
    ): Call<JsonElement>

    @POST(ApiConstants.HOME_LIST)
    fun homeList(@Header ("security_key") security_key:String,
    @Header("auth_key") auth_key:String):Call<JsonElement>

    @POST(ApiConstants.FORGOT_PASSWORD)
    fun forgotPassword(
        @Header("security_key")security_key:String,
        @Query("email") email:String): Call<JsonElement>

    @PUT(ApiConstants.CHANGE_PASSWORD)
    fun changePassword(
        @Header ("security_key") security_key:String,
        @Header("auth_key") auth_key:String,
        @Query("old_password") old_password:String,
        @Query("new_password") new_password:String
    ): Call<JsonElement>

    /*
    api for user profile
     */

    @FormUrlEncoded
    @POST(ApiConstants.USER_PROFILE)
    fun userProfile(@Header ("security_key") security_key:String,
                    @Header("auth_key") auth_key:String,
                    @Field("user_id") user_id:String):Call<JsonElement>

    /*
    api for business profile
     */
    @FormUrlEncoded
    @POST(ApiConstants.USER_PROFILE_BUSINESS)
    fun businessUserProfile(@Header ("security_key") security_key:String,
                    @Header("auth_key") auth_key:String,
                    @Field("user_id") user_id:String):Call<JsonElement>


    @Multipart
    @POST(ApiConstants.ADD_POST)
    fun addPost(
        @Header ("security_key") security_key:String,
        @Header ("auth_key") auth_key:String,
        @PartMap partMap: Map<String, RequestBody>,
        @Part file: MultipartBody.Part?
    ): Call<JsonElement>

}