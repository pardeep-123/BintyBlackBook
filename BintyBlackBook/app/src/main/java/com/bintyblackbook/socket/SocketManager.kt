package com.bintyblackbook.socket

import android.util.Log
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.BintyBookApplication.Companion.getInstance
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONException
import org.json.JSONObject
import java.net.URISyntaxException
import java.util.*

class SocketManager {
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

    // mSocket = IO.socket("http://192.168.1.122:3070/");
    val socket: Socket?
        get() {
            run {
                mSocket = try {
                    IO.socket("https://bintysblackbook.com:4509/")
                    // mSocket = IO.socket("http://192.168.1.122:3070/");
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

    fun saveVendorLocation(jsonObject: JSONObject?) {
        if (!mSocket?.connected()!!) {
            mSocket?.connect()
            mSocket?.emit(UPDATE_LOCATION, jsonObject)
            mSocket?.off(UPDATE_LOCATION_LISTENER)
            mSocket?.on(UPDATE_LOCATION_LISTENER, onSaveVendorLocListener)
        } else {
            mSocket?.emit(UPDATE_LOCATION, jsonObject)
            mSocket?.off(UPDATE_LOCATION_LISTENER)
            mSocket?.on(UPDATE_LOCATION_LISTENER, onSaveVendorLocListener)
        }
    }

    fun getVendorLocation(jsonObject: JSONObject?) {
        if (!mSocket?.connected()!!) {
            mSocket?.connect()
            mSocket?.emit(GET_LOCATION, jsonObject)
            mSocket?.off(GET_LOCATION_LISTENER)
            mSocket?.on(GET_LOCATION_LISTENER, onGetVendorLocListener)
        } else {
            mSocket?.emit(GET_LOCATION, jsonObject)
            mSocket?.off(GET_LOCATION_LISTENER)
            mSocket?.on(GET_LOCATION_LISTENER, onGetVendorLocListener)
        }
    }

    private val onGetChatListListener = Emitter.Listener { args ->
        Log.i("Socket", "onGetChatListener")
        for (observer in observerList!!) {
            observer.onResponse(GET_CHAT_LIST_LISTENER, *args)
        }
    }
    private val onSaveVendorLocListener = Emitter.Listener { args ->
        Log.i("Socket", "onSaveChatListener")
        for (observer in observerList!!) {
            observer.onResponse(UPDATE_LOCATION_LISTENER, *args)
        }
    }
    private val onGetVendorLocListener = Emitter.Listener { args ->
        Log.i("Socket", "onGetLocListener")
        for (observer in observerList!!) {
            observer.onResponse(GET_LOCATION_LISTENER, *args)
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
    private val recListener = Emitter.Listener { args ->
        Log.i("Socket", "recListener")
        for (observer in observerList!!) {
            observer.onResponse(RECEIVER_LISTENER, *args)
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
        const val GET_CHAT = "get_message"
        const val SEND_MESSAGE = "send_message"
        const val DELETE_CHAT="delete_chat"
        const val BLOCK_USER="block_user"
        const val UPDATE_LOCATION = "update_location_provider"
        const val GET_LOCATION = "get_location_provider"

        //listener
        const val CONNECT_LISTENER = "connect_listener"
        const val GET_CHAT_LIST_LISTENER = "chat_message"
        const val GET_CHAT_LISTENER = "get_data_message"
        const val DELETE_CHAT_LISTENER="delete_data"
        const val BODY_LISTENER = "new_message"
        const val RECEIVER_LISTENER = "new_message"
        const val UPDATE_LOCATION_LISTENER = "get_location"
        const val GET_LOCATION_LISTENER = "get_provider_location_info"
        const val BLOCK_USER_LISTENER="block_data"
    }
}