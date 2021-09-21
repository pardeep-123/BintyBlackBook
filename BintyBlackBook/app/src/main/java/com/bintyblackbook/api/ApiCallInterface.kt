package com.bintyblackbook.api

import com.google.gson.JsonElement
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
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

    @FormUrlEncoded
    @POST(ApiConstants.FORGOT_PASSWORD)
    fun forgotPassword(
        @Header("security_key")security_key:String,
        @Field("email") email:String): Call<JsonElement>

    @FormUrlEncoded
    @PUT(ApiConstants.CHANGE_PASSWORD)
    fun changePassword(
        @Header ("security_key") security_key:String,
        @Header("auth_key") auth_key:String,
        @Field("old_password") old_password:String,
        @Field("new_password") new_password:String
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

    @Multipart
    @PUT(ApiConstants.EDIT_POST)
    fun editPost(
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
    @Multipart
    @PUT(ApiConstants.EDIT_BUSINESS_PROFILE)
    fun addEditInfo(
        @Header("security_key") security_key: String,
        @Header("auth_key") auth_key: String,
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part file: MultipartBody.Part?
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


    //delete post
    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "delete_post", hasBody = true)
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

    @GET(ApiConstants.GET_NOTIFICATION_LIST)
    fun getNotificationList(
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

    // my events
    @FormUrlEncoded
    @POST(ApiConstants.MY_EVENTS)
    fun myEvents(
        @Header("security_key") security_key: String,
        @Header("auth_key") auth_key: String,
        @Field("user_id") userId:String
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

    //post detail

    @FormUrlEncoded
    @POST(ApiConstants.POST_DETAIL)
    fun getPostDetail(
        @Header("security_key") security_key:String,
        @Header("auth_key") auth_key: String,
        @Field("post_id") post_id: String
    ) : Call<JsonElement>

    //get availability slots

    @FormUrlEncoded
    @POST(ApiConstants.GET_AVAILABILITY)
    fun getAvailableSlots(
        @Header("security_key") security_key: String,
        @Header("auth_key") auth_key: String,
        @Field("user_id") user_id: String
    ) : Call<JsonElement>


    @FormUrlEncoded
    @POST(ApiConstants.SEND_LOOP_REQUEST)
    fun sendLoopReq(
        @Header("security_key") security_key: String,
        @Header("auth_key") auth_key: String,
        @Field("userId") userId:String
    ) : Call<JsonElement>


    @FormUrlEncoded
    @POST(ApiConstants.UNSEND_LOOP_REQUEST)
    fun unSendLoopReq(
        @Header("security_key") security_key: String,
        @Header("auth_key") auth_key: String,
        @Field("userId") userId:String
    ) : Call<JsonElement>

    @FormUrlEncoded
    @POST(ApiConstants.ACCEPT_REJECT_REQUEST)
    fun acceptRejectRequest(
        @Header("security_key") security_key: String,
        @Header("auth_key") auth_key: String,
        @Field("user_id") user_id:String,
        @Field("status") status:String
    ) : Call<JsonElement>

    @Multipart
    @PUT(ApiConstants.EDIT_PROFILE)
    fun editProfile(
        @Header("security_key") security_key: String,
        @Header("auth_key") auth_key: String,
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part file: MultipartBody.Part?
    ) : Call<JsonElement>

    @GET(ApiConstants.GET_ALL_BOOKINGS)
    fun getAllBookings(
        @Header("security_key") security_key: String,
        @Header("auth_key") auth_key: String
    ): Call<JsonElement>

    @GET(ApiConstants.GET_BLOCKED_USERS)
    fun getBlockedUsers(
        @Header("security_key") security_key: String,
        @Header("auth_key") auth_key: String
    ): Call<JsonElement>

    @FormUrlEncoded
    @POST(ApiConstants.UNBLOCK_USER)
    fun unBlockUser(
        @Header("security_key") security_key: String,
        @Header("auth_key") auth_key: String,
        @Field("loop_id") loop_id:String
    ): Call<JsonElement>


    @Multipart
    @POST(ApiConstants.ADD_EVENT)
    fun addEvent(
        @Header("security_key") security_key: String,
        @Header("auth_key") auth_key: String,
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part file: MultipartBody.Part?
    ) : Call<JsonElement>


    @Multipart
    @PUT(ApiConstants.EDIT_EVENT)
    fun editEvent(
        @Header("security_key") security_key: String,
        @Header("auth_key") auth_key: String,
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part file: MultipartBody.Part?
    ) : Call<JsonElement>

    @FormUrlEncoded
    @POST(ApiConstants.LOGOUT)
    fun logout(
        @Header("security_key") security_key: String,
        @Header("auth_key") auth_key: String,
        @Field("user_id") userId:String
    ): Call<JsonElement>


    @GET(ApiConstants.GET_NOTIFICATION_COUNT)
    fun getNotificationCount(
        @Header("security_key") security_key: String,
        @Header("auth_key") auth_key: String
    ) : Call<JsonElement>

    @FormUrlEncoded
    @POST(ApiConstants.NOTIFICATION_SEEN)
    fun getNotificationSeen(
        @Header("security_key") security_key: String,
        @Header("auth_key") auth_key: String,
        @Field("notification_id") notification_id:String
    ) : Call<JsonElement>

    @GET(ApiConstants.GET_All_LOOP_REQUEST)
    fun getAllRequest(
        @Header("security_key") security_key: String,
        @Header("auth_key") auth_key: String
    ) : Call<JsonElement>

    @FormUrlEncoded
    @POST(ApiConstants.REPORT_POST)
    fun reportPost(
        @Header("security_key") security_key: String,
        @Header("auth_key") auth_key: String,
        @Field("post_id") post_id: String,
        @Field("description") description:String
    ) : Call<JsonElement>


    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "delete_event", hasBody = true)
 //   @DELETE(ApiConstants.DELETE_EVENT)
    fun deleteEvent(
        @Header("security_key") security_key: String,
        @Header("auth_key") auth_key: String,
        @Field("event_id") event_id: String
    ) : Call<JsonElement>

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = ApiConstants.DELETE_MEDIA, hasBody = true)
    fun deleteMedia(
        @Header("security_key") security_key: String,
        @Header("auth_key") auth_key: String,
        @Field("media_id") media_id:String
    ) : Call<JsonElement>

    @FormUrlEncoded
    @POST(ApiConstants.LIKE_DISLIKE_POST)
    fun likeDislikePost(
        @Header("security_key") security_key: String,
        @Header("auth_key") auth_key: String,
        @Field("status") status:String,
        @Field("post_id") post_id:String
    ) : Call<JsonElement>

    @FormUrlEncoded
    @POST(ApiConstants.GET_ALL_USERS)
    fun getAllUsers(
        @Header("security_key") security_key: String,
        @Header("auth_key") auth_key: String,
        @Field("keyword") keyword:String?
    ): Call<JsonElement>

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = ApiConstants.DELETE_COMMENT, hasBody = true)
    fun deleteComment(
        @Header("security_key") security_key: String,
        @Header("auth_key") auth_key: String,
        @Field("comment_id") comment_id:String
    ) : Call<JsonElement>

    @FormUrlEncoded
    @POST(ApiConstants.ADD_BOOKING)
    fun addBooking(
        @Header("security_key") security_key: String,
        @Header("auth_key") auth_key: String,
        @Field("providerId") provider_id:String,
        @Field("availabilityId") availabilityId:String,
        @Field("slots") slots:String
    ) : Call<JsonElement>

    @FormUrlEncoded
    @POST(ApiConstants.ADD_GROUP)
    fun addGroup(
        @Header("security_key") security_key: String,
        @Header("auth_key") auth_key: String,
        @Field("name") name:String,
        @Field("group_users") group_users:String
    ): Call<JsonElement>

    @GET(ApiConstants.MY_WALLET)
        fun getWalletList(
            @Header("security_key") security_key: String,
            @Header("auth_key") auth_key: String
        ):Call<JsonElement>

    @FormUrlEncoded
    @POST(ApiConstants.SET_AVAILABILITY)
    fun setAvailability(
        @Header("security_key") security_key: String,
        @Header("auth_key") auth_key: String,
        @Field("selectedSlots") slectedSlots: JSONArray,
    ) : Call<JsonElement>


    @FormUrlEncoded
    @POST(ApiConstants.SEND_CALL_NOTIFICATION)
    fun sendCallNotification(
        @Header("security_key") security_key: String,
        @Header("auth_key") auth_key: String,
        @Field("receiverId") receiverId: String,
    ) : Call<JsonElement>

}