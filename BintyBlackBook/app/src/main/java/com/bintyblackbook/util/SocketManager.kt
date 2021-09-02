package com.bintyblackbook.util

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import io.socket.client.Ack
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import java.net.URISyntaxException
import java.util.*

class SocketManager private constructor() {
    var isSocketConnected = false
    lateinit var mSocket: Socket
    private var mSocketInterface: SocketInterface? = null
    private var observerList: MutableList<SocketInterface>? = null
    private val TAG = SocketManager::class.java.canonicalName

    companion object {
        private var mSocketClass: SocketManager? = null

        @JvmStatic
        val socket: SocketManager?
            get() {
                if (mSocketClass == null) mSocketClass = SocketManager()
                return mSocketClass
            }

        //************************Listeners/Emitters************************
        const val SEND_MESSAGE_LISTENER = "send_message"
        const val LIKE_DISLIKE_LISTENER = "like_dislike_user"

        const val MESSAGE_LISTING="chat_listing"
        const val CHAT_LISTNER="chat_message"

    }


    /**
     *
     * No listener is used
     * Acknowledgement is used in [sendDataToServer]
     * */
    private val sendMessageListener = Emitter.Listener { args ->
        // Get a handler that can be used to post to the main thread
        Handler(Looper.getMainLooper()).post {
            Log.e(TAG + SEND_MESSAGE_LISTENER, args[0].toString())

            val obj = args[0] as JSONObject
            val mJsonString = obj.toString()
            val parser = JsonParser()
            val mJson = parser.parse(mJsonString)
            val gson = Gson()
//            val response = gson.fromJson(mJson, MyChatResponse.MyChatResponseItem::class.java)

            for (observer in observerList!!) {
//                observer.onSocketCall(NEW_MESSAGE_LISTENER, response);
            }
        }
    }


    /**
     * Default Listener
     * Define what you want to do when there's a connection error
     */
    private val onConnectError =
        Emitter.Listener { args -> // Get a handler that can be used to post to the main thread
            Handler(Looper.getMainLooper()).post {
                for (observer in observerList!!) {
                    observer.onError("ERROR", *args)
                }
                Log.e(TAG, "Run" + args[0])
                Log.e(TAG, "Error connecting")
            }
        }

    /**
     * Default Listener
     * Define what you want to do when connection is established
     */
    private val onConnect = Emitter.Listener { args ->
        // Get a handler that can be used to post to the main thread
        Handler(Looper.getMainLooper()).post {
            Log.e(TAG, "Connected SuccessFully")
            isSocketConnected = true
            if (mSocketInterface != null) mSocketInterface!!.onSocketConnect(*args)
/*            try {
                val user_id = getUser(App.getinstance())?.id.toString()
                if (user_id.isNotBlank()) {
                    val jsonObject = JSONObject()
                    jsonObject.put("sender_id", user_id)
                    jsonObject.put("message", "message")
                    jsonObject.put("receiver_id", "173")
                    sendDataToServer(SEND_MESSAGE_LISTENER, jsonObject)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }*/
            for (observer in observerList!!) {
                observer.onSocketConnect(*args)
            }
        }
    }


    private val onDisconnect = Emitter.Listener { args ->
        Log.e(TAG, "onDisconnect Listener")

        Handler(Looper.getMainLooper()).post {
            isSocketConnected = false
//            if (mSocketInterface != null)
//                mSocketInterface.onSocketDisconnect(args);
            for (observer in observerList!!) {
                observer.onSocketDisconnect(*args)
            }
        }
    }

    /*
     * Send Data to server by use of socket
     * */
    fun sendDataToServer(methodName: String, mObject: Any) {
        // Get a handler that can be used to post to the main thread
        Handler(Looper.getMainLooper()).post {
            mSocket.emit(methodName, mObject, object : Ack {
                override fun call(vararg args: Any?) {
                    Handler(Looper.getMainLooper()).post {
                        Log.e(TAG + SEND_MESSAGE_LISTENER, args[0].toString())
                        val obj = args[0] as JSONObject
                        val mJsonString = obj.toString()
                        val parser = JsonParser()
                        val mJson = parser.parse(mJsonString)
                        val gson = Gson()

                        if (methodName == MESSAGE_LISTING) {
//                            val response = gson.fromJson(mJson, SendMessageResponseSocket::class.java)
//                            for (observer in observerList!!) {
//                                observer.onSocketChatCall(SEND_MESSAGE_LISTENER, response)
//                            }
                        } else if (methodName == LIKE_DISLIKE_LISTENER) {
//                            val response = gson.fromJson(mJson, LikeDislikeResponseSocket::class.java)
//                            for (observer in observerList!!) {
//                                observer.onSocketCall(LIKE_DISLIKE_LISTENER, response)
//                            }
                        }
                    }
                }

            })
            Log.e("===emit==$methodName", mObject.toString())
        }
    }

    fun isConnected(): Boolean {
        return mSocket != null && mSocket.connected()
    }

    fun updateSocketInterface(mSocketInterface: SocketInterface?) {
        this.mSocketInterface = mSocketInterface
    }

    fun onRegister(observer: SocketInterface) {
        if (observerList != null && !observerList!!.contains(observer)) {
            observerList!!.add(observer)
        } else {
            observerList = ArrayList()
            (observerList as ArrayList<SocketInterface>).add(observer)
        }
    }

    fun unRegister(observer: SocketInterface) {
        if (observerList != null) {
            for (i in observerList!!.indices) {
                val model = observerList!![i]
                if (model === observer) {
                    observerList!!.remove(model)
                }
            }
        }
    }

    fun onConnect() {
        if (!mSocket.connected()) {
            Log.e(TAG, "Connecting Sockets")
            mSocket.on(Socket.EVENT_CONNECT, onConnect)
            mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect)
            mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError)
            mSocket.on(SEND_MESSAGE_LISTENER, sendMessageListener)
            mSocket.on(LIKE_DISLIKE_LISTENER, sendMessageListener)
            mSocket.connect()
        } else {
            Log.e(TAG, "Connecting Sockets Error!")
        }
    }

    fun onDisconnect() {
        Log.e(TAG, "Disconnecting Sockets")
        isSocketConnected = false
        mSocket.disconnect()
        mSocket.off(Socket.EVENT_CONNECT, onConnect)
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect)
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError)
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError)
        mSocket.off(SEND_MESSAGE_LISTENER, sendMessageListener)
        mSocket.off(LIKE_DISLIKE_LISTENER, sendMessageListener)

    }

    /*
     * Interface for Socket Callbacks
     * */
    interface SocketInterface {
        fun onSocketChatCall(event: String?,args: Any)
        fun onSocketCall(event: String?, args: Any)
        fun onSocketConnect(vararg args: Any?)
        fun onSocketDisconnect(vararg args: Any?)
        fun onError(event: String?, vararg args: Any?)
    }

    init {
        try {
            val options = IO.Options()
            options.reconnection = true
            options.reconnectionAttempts = Int.MAX_VALUE
          //  mSocket = IO.socket("https://admin.doggystyle.dog/?id=" + getUserId(App.getinstance()))
            if (observerList == null || observerList!!.size == 0) {
                observerList = ArrayList()
            }
//            onDisconnect()
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }
}

