package com.bintyblackbook.api

import com.google.gson.JsonElement
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

    @FormUrlEncoded
    @POST(ApiConstants.USER_SIGNUP)
    fun userSignUp(
        @Header("security_key") security_key:String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("userName") userName:String,
        @Field("phone") phone:String,
        @Field("country_code") country_code:String,
        @Field("description") description:String,
        @Field("latitude") latitude:String,
        @Field("longitude") longitude:String,
        @Field("address") address:String,
        @Field("device_type") device_type:String,
        @Field("device_token") device_token:String,
        @Field("image") image:String
    ):Call<JsonElement>


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
    @GET(ApiConstants.USER_PROFILE)
    fun userProfile(@Header ("security_key") security_key:String,
                    @Header("auth_key") auth_key:String,
                    @Field("user_id") user_id:String):Call<JsonElement>

    /*
    api for business profile
     */
    @FormUrlEncoded
    @GET(ApiConstants.USER_PROFILE)
    fun businessUserProfile(@Header ("security_key") security_key:String,
                    @Header("auth_key") auth_key:String,
                    @Field("user_id") user_id:String):Call<JsonElement>

}