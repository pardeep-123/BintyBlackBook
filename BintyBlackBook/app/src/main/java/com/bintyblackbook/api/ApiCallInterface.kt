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
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part file: MultipartBody.Part?
    ): Call<JsonElement>

    /*
    Business SignUp Api
     */
    @Multipart
    @POST(ApiConstants.BUSINESS_SIGNUP)
    fun businessSignUp(
        @Header ("security_key") security_key:String,
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
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
    fun businessUserProfile(
        @Header ("security_key") security_key:String,
        @Header("auth_key") auth_key:String,
        @Field("user_id") user_id:String
    ):Call<JsonElement>


    @Multipart
    @POST(ApiConstants.ADD_POST)
    fun addPost(
        @Header ("security_key") security_key:String,
        @Header ("auth_key") auth_key:String,
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part file: MultipartBody.Part?
    ): Call<JsonElement>

    @GET(ApiConstants.GET_CATEGORIES)
    fun getCategories(
        @Header("security_key") securityKey:String,
        @Header("auth_key") auth_key:String
    ): Call<JsonElement>

    @Multipart
    @POST(ApiConstants.UPLOAD_MEDIA)
    fun uploadMedia(
        @Header("security_key") security_key:String,
        @Header("auth_key") auth_key:String,
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part file: MultipartBody.Part?
    ): Call<JsonElement>

    /*
    add or edit business profile
     */

    @PUT(ApiConstants.EDIT_BUSINESS_PROFILE)
    fun addEditInfo(
        @Header("security_key") security_key: String,
        @Header("auth_key") auth_key: String,
        @Body requestBody: RequestBody
    ):Call<JsonElement>

    /*
    get loops list
     */
    @POST(ApiConstants.GET_LOOPS)
    fun getLoops(
        @Header("security_key") security_key:String,
        @Header("auth_key") auth_key:String
    ): Call<JsonElement>


    @FormUrlEncoded
    @POST(ApiConstants.UNLOOP)
    fun unLoop(
        @Header("security_key") security_key: String,
        @Header("auth_key") auth_key:String,
        @Field("userId") userId:String
    ): Call<JsonElement>

    @GET(ApiConstants.GET_ALL_POSTS)
    fun getPosts(
        @Header("security_key") security_key: String,
        @Header("auth_key") auth_key:String
    ) : Call<JsonElement>

    @DELETE(ApiConstants.DELETE_POST)
    fun deletePost(
        @Header("security_key") security_key:String,
        @Header("auth_key") auth_key: String,
        @Field("post_id") post_id:String
    ): Call<JsonElement>

    @FormUrlEncoded
    @PUT(ApiConstants.NOTIFICATION_STATUS)
    fun addNotificationStatus(
        @Header("security_key") security_key:String,
        @Header("auth_key") auth_key: String,
        @Field("status") status:String
    ) : Call<JsonElement>

    @GET(ApiConstants.GET_NOTIFICATION_STATUS)
    fun getNotificationStatus(
        @Header("security_key") security_key:String,
        @Header("auth_key") auth_key: String
    ) : Call<JsonElement>

    @FormUrlEncoded
    @POST(ApiConstants.ALL_CONTENT)
    fun allContent(
        @Header("security_key") security_key:String,
        @Header("auth_key") auth_key: String,
        @Field("type") type:String
    ) : Call<JsonElement>

    @FormUrlEncoded
    @POST(ApiConstants.GET_COMMENTS)
    fun getComments(
        @Header("security_key") security_key:String,
        @Header("auth_key") auth_key: String,
        @Field("post_id") post_id:String
    ) : Call<JsonElement>

    @GET(ApiConstants.GET_OTHER_USER_EVENTS)
    fun getOtherUserEvents(
        @Header("security_key") security_key:String,
        @Header("auth_key") auth_key: String
    ) : Call<JsonElement>

    @GET(ApiConstants.FAVOURITE_EVENTS)
    fun favEvents(
        @Header("security_key") security_key:String,
        @Header("auth_key") auth_key: String
    ) :Call<JsonElement>


    @FormUrlEncoded
    @POST(ApiConstants.ADD_COMMENT)
    fun addComment(
        @Header("security_key") security_key:String,
        @Header("auth_key") auth_key: String,
        @Field("post_id") post_id:String,
        @Field("comment") comment:String
    ) : Call<JsonElement>

    @FormUrlEncoded
    @POST(ApiConstants.LIKE_EVENT)
    fun likeEvent(
        @Header("security_key") security_key:String,
        @Header("auth_key") auth_key: String,
        @Field("event_id") event_id:String,
        @Field("status") status:String
    ) : Call<JsonElement>


}