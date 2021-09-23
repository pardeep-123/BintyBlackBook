package com.bintyblackbook.socket

import android.util.Log
import com.bintyblackbook.BintyBookApplication
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONException
import org.json.JSONObject
import java.net.URISyntaxException
import java.util.*

class SocketManager {

    var socket_live_url="https://bintysblackbook.com:4509/"
    var socket_dev_url="http://192.168.1.122:4509/"

    private var mSocket: Socket? = null
    private var observerList: MutableList<Observer>? = null

    fun initializeSocket() {
        mSocket = socket
        observerList = ArrayList()
        disconnect()
        mSocket?.on(Socket.EVENT_CONNECT, onConnect)
        mSocket?.on(Socket.EVENT_DISCONNECT, onDisconnect)
        mSocket?.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
        mSocket?.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError)
        mSocket?.connect()
    }

    val isConnected: Boolean
        get() = mSocket != null && mSocket?.connected()!!

    fun getmSocket(): Socket? {
        return mSocket
    }


    val socket: Socket?
        get() {
            run {
                mSocket = try {
                    IO.socket(socket_live_url)
                } catch (e: URISyntaxException) {
                    throw RuntimeException(e)
                }
            }
            return mSocket
        }

    private val onConnect = Emitter.Listener {
        Log.i("Socket", "CONNECTED")
        try {
            val jsonObject = JSONObject()
            jsonObject.put("userId", BintyBookApplication.getInstance()?.getString(BintyBookApplication.USER_ID))
            mSocket?.emit(CONNECT_USER, jsonObject)
            mSocket?.off(CONNECT_LISTENER)
            mSocket?.on(CONNECT_LISTENER, onConnectListener)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun disconnectAll() {
        if (mSocket != null) {
            mSocket?.off(Socket.EVENT_CONNECT, onConnect)
            mSocket?.off(Socket.EVENT_DISCONNECT, onDisconnect)
            mSocket?.off(Socket.EVENT_CONNECT_ERROR, onConnectError)
            mSocket?.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError)
            mSocket?.off()
        }
    }

    private fun disconnect() {
        if (mSocket != null) {
            mSocket?.off(Socket.EVENT_CONNECT, onConnect)
            mSocket?.off(Socket.EVENT_DISCONNECT, onDisconnect)
            mSocket?.off(Socket.EVENT_CONNECT_ERROR, onConnectError)
            mSocket?.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError)
            mSocket?.off()
            mSocket?.disconnect()
        }
    }

    private val onConnectListener = Emitter.Listener { args ->
        try {
            val jsonObject = args[0] as JSONObject
            Log.i("Socket", "onConnectListener$jsonObject")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onRegister(observer: Observer) {
        if (observerList != null && !observerList?.contains(observer)!!) {
            observerList?.clear()
            observerList?.add(observer)
        } else {
            observerList = ArrayList()
            observerList?.clear()
            observerList?.add(observer)
        }
    }

    fun unRegister(observer: Observer) {
        try {
            if (observerList != null) {
                for (i in 0 until observerList?.size!! - 1) {
                    val model = observerList!![i]
                    if (model === observer) {
                        observerList?.remove(model)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val onDisconnect = Emitter.Listener { Log.i("Socket", "DISCONNECTED") }
    private val onConnectError = Emitter.Listener { args ->
        Log.i("Socket", "CONNECTION ERROR")
        for (observer in observerList!!) {
            observer.onError("errorSocket", *args)
        }
    }

    fun getInboxMessage(jsonObject: JSONObject?) {
        if (!mSocket?.connected()!!) {
            mSocket?.connect()
            mSocket?.emit(GET_CHAT_LIST, jsonObject)
            mSocket?.off(GET_CHAT_LIST_LISTENER)
            mSocket?.on(GET_CHAT_LIST_LISTENER, onGetChatListListener)
        } else {
            mSocket?.emit(GET_CHAT_LIST, jsonObject)
            mSocket?.off(GET_CHAT_LIST_LISTENER)
            mSocket?.on(GET_CHAT_LIST_LISTENER, onGetChatListListener)
        }
    }


    private val onGetChatListListener = Emitter.Listener { args ->
        Log.i("Socket", "onGetChatListener")
        for (observer in observerList!!) {
            observer.onResponse(GET_CHAT_LIST_LISTENER, *args)
        }
    }


    fun sendMessage(jsonObject: JSONObject?) {
        if (!mSocket?.connected()!!) {
            mSocket?.connect()
            mSocket?.emit(SEND_MESSAGE, jsonObject)
            mSocket?.off(BODY_LISTENER)
            mSocket?.on(BODY_LISTENER, onBodyListener)
            mSocket?.off(RECEIVER_LISTENER)
            mSocket?.on(RECEIVER_LISTENER, recListener)
        } else {
            mSocket?.emit(SEND_MESSAGE, jsonObject)
            mSocket?.off(BODY_LISTENER)
            mSocket?.on(BODY_LISTENER, onBodyListener)
            mSocket?.off(RECEIVER_LISTENER)
            mSocket?.on(RECEIVER_LISTENER, recListener)
        }
    }

    fun sendGroupMessage(jsonObject: JSONObject?) {
        if (!mSocket?.connected()!!) {
            mSocket?.connect()
            mSocket?.emit(SEND_GROUP_MESSAGE, jsonObject)
            mSocket?.off(GROUP_BODY_LISTENER)
            mSocket?.on(GROUP_BODY_LISTENER, onBodyGroupListener)
            mSocket?.off(RECEIVER_LISTENER)
            mSocket?.on(RECEIVER_LISTENER, recListener)
        } else {
            mSocket?.emit(SEND_GROUP_MESSAGE, jsonObject)
            mSocket?.off(GROUP_BODY_LISTENER)
            mSocket?.on(GROUP_BODY_LISTENER, onBodyGroupListener)
            mSocket?.off(RECEIVER_LISTENER)
            mSocket?.on(RECEIVER_LISTENER, recListener)
        }
    }

    fun getFriendMessage(jsonObject: JSONObject?) {
        if (!mSocket?.connected()!!) {
            mSocket?.connect()
            mSocket?.emit(GET_CHAT, jsonObject)
            mSocket?.off(GET_CHAT_LISTENER)
            mSocket?.on(GET_CHAT_LISTENER, onGetChatListener)
            mSocket?.off(BODY_LISTENER)
            mSocket?.on(BODY_LISTENER, onBodyListener)
            mSocket?.off(RECEIVER_LISTENER)
            mSocket?.on(RECEIVER_LISTENER, recListener)
        } else {
            mSocket?.emit(GET_CHAT, jsonObject)
            mSocket?.off(GET_CHAT_LISTENER)
            mSocket?.on(GET_CHAT_LISTENER, onGetChatListener)
            mSocket?.off(BODY_LISTENER)
            mSocket?.on(BODY_LISTENER, onBodyListener)
            mSocket?.off(RECEIVER_LISTENER)
            mSocket?.on(RECEIVER_LISTENER, recListener)
        }
    }


    fun getChatMessage(jsonObject: JSONObject?) {
        if (!mSocket?.connected()!!) {
            mSocket?.connect()
            mSocket?.emit(GET_GROUP_MESSAGES, jsonObject)
            mSocket?.off(GET_GROUP_CHAT_LISTENER)
            mSocket?.on(GET_GROUP_CHAT_LISTENER, onGroupChatListener)
            mSocket?.off(GROUP_BODY_LISTENER)
            mSocket?.on(GROUP_BODY_LISTENER, onBodyGroupListener)
            mSocket?.off(RECEIVER_LISTENER)
            mSocket?.on(RECEIVER_LISTENER, recListener)
        } else {
            mSocket?.emit(GET_GROUP_MESSAGES, jsonObject)
            mSocket?.off(GET_GROUP_CHAT_LISTENER)
            mSocket?.on(GET_GROUP_CHAT_LISTENER, onGroupChatListener)
            mSocket?.off(GROUP_BODY_LISTENER)
            mSocket?.on(GROUP_BODY_LISTENER, onBodyGroupListener)
            mSocket?.off(RECEIVER_LISTENER)
            mSocket?.on(RECEIVER_LISTENER, recListener)
        }
    }

    fun getVideoCallStatus(jsonObject: JSONObject?){
        if (!mSocket?.connected()!!) {
            mSocket?.connect()
            mSocket?.emit(CALL_TO_USER, jsonObject)
            mSocket?.off(CALL_STATUS_LISTENER)
            mSocket?.on(CALL_STATUS_LISTENER,onCallStatusListener)

        } else {
            mSocket?.emit(CALL_TO_USER, jsonObject)
            mSocket?.off(CALL_STATUS_LISTENER)
            mSocket?.on(CALL_STATUS_LISTENER,onCallStatusListener)
        }
    }


    fun getBlockStatus(jsonObject: JSONObject?){
        if (!mSocket?.connected()!!) {
            mSocket?.connect()
            mSocket?.emit(BLOCK_STATUS, jsonObject)
            mSocket?.off(BLOCK_STATUS_LISTENER)
            mSocket?.on(BLOCK_STATUS_LISTENER,onBlockStatusListener)

        } else {
            mSocket?.emit(BLOCK_STATUS, jsonObject)
            mSocket?.off(BLOCK_STATUS_LISTENER)
            mSocket?.on(BLOCK_STATUS_LISTENER,onBlockStatusListener)
        }
    }

    fun clearChat(jsonObject: JSONObject?){
        if (!mSocket?.connected()!!) {
            mSocket?.connect()
            mSocket?.emit(DELETE_CHAT, jsonObject)
            mSocket?.off(DELETE_CHAT_LISTENER)
            mSocket?.on(DELETE_CHAT_LISTENER,onClearChatListener)

        } else {
            mSocket?.emit(DELETE_CHAT, jsonObject)
            mSocket?.off(DELETE_CHAT_LISTENER)
            mSocket?.on(DELETE_CHAT_LISTENER,onClearChatListener)
        }
    }

    fun clearGroupChat(jsonObject: JSONObject?){
        if (!mSocket?.connected()!!) {
            mSocket?.connect()
            mSocket?.emit(DELETE_GROUP_CHAT, jsonObject)
            mSocket?.off(DELETE_GROUP_CHAT_LISTENER)
            mSocket?.on(DELETE_GROUP_CHAT_LISTENER,onClearGroupChatListener)

        } else {
            mSocket?.emit(DELETE_GROUP_CHAT, jsonObject)
            mSocket?.off(DELETE_GROUP_CHAT_LISTENER)
            mSocket?.on(DELETE_GROUP_CHAT_LISTENER,onClearGroupChatListener)
        }
    }

    fun blockUser(jsonObject: JSONObject?){
        if (!mSocket?.connected()!!) {
            mSocket?.connect()
            mSocket?.emit(BLOCK_USER, jsonObject)
            mSocket?.off(BLOCK_USER_LISTENER)
            mSocket?.on(BLOCK_USER_LISTENER,onBlockUserListener)

        } else {
            mSocket?.emit(BLOCK_USER, jsonObject)
            mSocket?.off(BLOCK_USER_LISTENER)
            mSocket?.on(BLOCK_USER_LISTENER,onBlockUserListener)
        }
    }
    fun reportUser(jsonObject: JSONObject?){
        if (!mSocket?.connected()!!) {
            mSocket?.connect()
            mSocket?.emit(REPORT_USER, jsonObject)
            mSocket?.off(REPORT_USER_LISTENER)
            mSocket?.on(REPORT_USER_LISTENER,onReportUserListener)

        } else {
            mSocket?.emit(REPORT_USER, jsonObject)
            mSocket?.off(REPORT_USER_LISTENER)
            mSocket?.on(REPORT_USER_LISTENER,onReportUserListener)
        }
    }

    fun reportGroup(jsonObject: JSONObject?){
        if (!mSocket?.connected()!!) {
            mSocket?.connect()
            mSocket?.emit(REPORT_GROUP, jsonObject)
            mSocket?.off(REPORT_GROUP_LISTENER)
            mSocket?.on(REPORT_GROUP_LISTENER,onReportGroupListener)

        } else {
            mSocket?.emit(REPORT_GROUP, jsonObject)
            mSocket?.off(REPORT_GROUP_LISTENER)
            mSocket?.on(REPORT_GROUP_LISTENER,onReportGroupListener)
        }
    }

    fun leaveGroup(jsonObject: JSONObject?){
        if(!mSocket?.connected()!!){
            mSocket?.connect()
            mSocket?.emit(LEAVE_GROUP,jsonObject)
            mSocket?.off(LEAVE_GROUP_LISTENER)
            mSocket?.on(LEAVE_GROUP_LISTENER,onLeaveGroupListener)
        }else{
            mSocket?.emit(LEAVE_GROUP, jsonObject)
            mSocket?.off(LEAVE_GROUP_LISTENER)
            mSocket?.on(LEAVE_GROUP_LISTENER,onLeaveGroupListener)
        }
    }

    private val onGetChatListener = Emitter.Listener { args ->
        Log.i("Socket", "onGetChatListener")
        for (observer in observerList!!) {
            observer.onResponse(GET_CHAT_LISTENER, *args)
            Log.i("Socket", "inside for loop")
        }
    }
    private val onBodyListener = Emitter.Listener { args ->
        Log.i("Socket", "onBodyListener")
        for (observer in observerList!!) {
            observer.onResponse(BODY_LISTENER, *args)
        }
    }


    private val onGroupChatListener = Emitter.Listener { args ->
        Log.i("Socket", "onGetChatListener")
        for (observer in observerList!!) {
            observer.onResponse(GET_GROUP_CHAT_LISTENER, *args)
            Log.i("Socket", "inside for loop")
        }
    }
    private val onBodyGroupListener = Emitter.Listener { args ->
        Log.i("Socket", "onBodyListener")
        for (observer in observerList!!) {
            observer.onResponse(GROUP_BODY_LISTENER, *args)
        }
    }
    private val recListener = Emitter.Listener { args ->
        Log.i("Socket", "recListener")
        for (observer in observerList!!) {
            observer.onResponse(RECEIVER_LISTENER, *args)
        }
    }

    private val onBlockUserListener = Emitter.Listener { args ->
        Log.i("Socket", "onBlockUserListener")
        for (observer in observerList!!) {
            observer.onResponse(BLOCK_USER_LISTENER, *args)
            Log.i("Socket", "inside for loop")
        }
    }
    private val onReportGroupListener = Emitter.Listener { args ->
        Log.i("Socket", "onReportGroupListener")
        for (observer in observerList!!) {
            observer.onResponse(REPORT_GROUP_LISTENER, *args)
            Log.i("Socket", "inside for loop")
        }
    }

    private val onCallStatusListener = Emitter.Listener { args ->
        Log.i("Socket", "onBlockStatusListener")
        for (observer in observerList!!) {
            observer.onResponse(CALL_STATUS_LISTENER, *args)
            Log.i("Socket", "block status check")
        }
    }


    private val onBlockStatusListener = Emitter.Listener { args ->
        Log.i("Socket", "onBlockStatusListener")
        for (observer in observerList!!) {
            observer.onResponse(BLOCK_STATUS_LISTENER, *args)
            Log.i("Socket", "block status check")
        }
    }

    private val onClearChatListener= Emitter.Listener { args->
        Log.i("Socket", "onClearChatListener")
        for (observer in observerList!!) {
            observer.onResponse(DELETE_CHAT_LISTENER, *args)
            Log.i("Socket", "clear chat")
        }
    }

    private val onClearGroupChatListener= Emitter.Listener { args->
        Log.i("Socket", "onClearGroupChatListener")
        for (observer in observerList!!) {
            observer.onResponse(DELETE_GROUP_CHAT_LISTENER, *args)
            Log.i("Socket", "clear chat")
        }
    }

    private val onReportUserListener = Emitter.Listener { args ->
        Log.i("Socket", "onBlockUserListener")
        for (observer in observerList!!) {
            observer.onResponse(BLOCK_USER_LISTENER, *args)
            Log.i("Socket", "inside for loop")
        }
    }

    private val onLeaveGroupListener = Emitter.Listener { args ->
        Log.i("Socket", "onLeaveGroupListener")
        for (observer in observerList!!) {
            observer.onResponse(LEAVE_GROUP_LISTENER, *args)
            Log.i("Socket", "inside for loop")
        }
    }

    interface Observer {
        fun onError(event: String?, vararg args: Any?)
        fun onResponse(event: String?, vararg args: Any?)
    }

    companion object {
        // sockets events
        const val CONNECT_USER = "connect_user"
        const val GET_CHAT_LIST = "chat_listing"

        //for user
        const val GET_CHAT = "get_message"
        const val SEND_MESSAGE = "send_message"
        const val DELETE_CHAT="delete_chat"
        const val BLOCK_USER="block_user"
        const val BLOCK_STATUS="block_status"
        const val READ_UNREAD="read_unread"
        const val REPORT_USER="report_user"
        //for group
        const val SEND_GROUP_MESSAGE="send_group_message"
        const val GET_GROUP_MESSAGES="get_group_message"
        const val DELETE_GROUP_CHAT="delete_group_chat"
        const val REPORT_GROUP="report_group"
        const val LEAVE_GROUP="leave_group"
        //for video call
        const val CALL_TO_USER="call_to_user"



        //socket listener
        const val CONNECT_LISTENER = "connect_listener"
        const val GET_CHAT_LIST_LISTENER = "chat_message"
        //for user
        const val GET_CHAT_LISTENER = "get_data_message"
        const val DELETE_CHAT_LISTENER="delete_data"
        const val BODY_LISTENER = "new_message"
        const val RECEIVER_LISTENER = "new_message"
        const val BLOCK_USER_LISTENER="block_data"
        const val BLOCK_STATUS_LISTENER="block_data_status"
        const val READ_UNREAD_STATUS_LISTENER="read_data_status"
        const val REPORT_USER_LISTENER="report_data"
        //for group
        const val GROUP_BODY_LISTENER="new_message"
        const val GET_GROUP_CHAT_LISTENER="get_data_group_message"
        const val DELETE_GROUP_CHAT_LISTENER="clear_group_chat"
        const val REPORT_GROUP_LISTENER="report_group_data"
        const val LEAVE_GROUP_LISTENER="leave_group_chat"
        //for video call
        const val CALL_STATUS_LISTENER="call_connect_status"
    }
}