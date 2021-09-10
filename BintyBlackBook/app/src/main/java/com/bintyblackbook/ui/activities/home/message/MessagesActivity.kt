package com.bintyblackbook.ui.activities.home.message

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.R
import com.bintyblackbook.adapters.MessagesAdapter
import com.bintyblackbook.adapters.SwapsAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.MessageData
import com.bintyblackbook.models.EditMessageModel
import com.bintyblackbook.socket.SocketManager
import com.bintyblackbook.util.getMsgType
import com.bintyblackbook.util.getUser
import kotlinx.android.synthetic.main.activity_messages.*
import org.json.JSONException
import org.json.JSONObject

class MessagesActivity : BaseActivity(), View.OnClickListener,SocketManager.Observer {

    lateinit var arrayList: ArrayList<EditMessageModel>
    private var socketManager: SocketManager? = null
    var reciever_id=""

    var type="msg"
    val list = ArrayList<MessageData>()
    var swapList= ArrayList<MessageData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)
        showProgressDialog()
        socketManager= BintyBookApplication.getSocketManager()

        setOnClicks()

        getChatData()

    }



    private fun getChatData() {
        val userId: String = getUser(context)?.id.toString()
        val jsonObject = JSONObject()
        try {
            jsonObject.put("userId", userId)
            jsonObject.put("type", getMsgType(context))
            socketManager?.getInboxMessage(jsonObject)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun setOnClicks() {
        rlBack.setOnClickListener(this)
        rlEdit.setOnClickListener(this)
        tvMsgs.setOnClickListener(this)
        tvSwaps.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.rlBack -> {
                finish()
            }
            R.id.rlEdit -> {
                startActivity(Intent(this,EditMessageActivity::class.java))
            }

            R.id.tvMsgs ->{
                type="msg"
                tvSwaps.setTextColor(ContextCompat.getColor(context, R.color.whiteColor))
                viewSwaps.setBackgroundColor(ContextCompat.getColor(context,R.color.themeColor))
                tvMsgs.setTextColor(ContextCompat.getColor(context, R.color.fadePink))
                viewMsgs.setBackgroundColor(ContextCompat.getColor(context,R.color.fadePink))
                rvSwaps.visibility=View.GONE
                rvAllMsg.visibility=View.VISIBLE
                tvNoMsgs.visibility=View.GONE
                showProgressDialog()
                getChatData()
            }

            R.id.tvSwaps ->{
                type="swap"
                tvSwaps.setTextColor(ContextCompat.getColor(context, R.color.fadePink))
                viewSwaps.setBackgroundColor(ContextCompat.getColor(context,R.color.fadePink))
                tvMsgs.setTextColor(ContextCompat.getColor(context, R.color.whiteColor))
                viewMsgs.setBackgroundColor(ContextCompat.getColor(context,R.color.themeColor))
                rvSwaps.visibility=View.VISIBLE
                rvAllMsg.visibility=View.GONE
                tvNoMsgs.visibility=View.GONE
                showProgressDialog()
                getChatData()
            }
        }
    }

    override fun onError(event: String?, vararg args: Any?) {
        when(event){

            "errorSocket" -> {
               dismissProgressDialog()
            }
        }
    }

    override fun onResponse(event: String?, vararg args: Any?) {
        when (event) {
            SocketManager.GET_CHAT_LIST_LISTENER -> {
                try {
                    dismissProgressDialog()
                    runOnUiThread {
                        val data = args.get(0) as JSONObject
                        val chatListArray = data.getJSONArray("chatListing")
                        val swapListArray = data.getJSONArray("swapListing")

                        list.clear()
                        swapList.clear()

                        if (type == "msg") {
                            if (chatListArray.length() != 0) {
                                /*rvAllMsg.visibility=View.VISIBLE
                            rvSwaps.visibility=View.GONE*/
                                for (i in 0 until chatListArray.length()) {
                                    val objects = chatListArray.getJSONObject(i)

                                    Log.i("objects", objects.toString())
                                    val getInboxMessageListResponse = MessageData()
                                    getInboxMessageListResponse.isGroup = objects.getInt("isGroup")
                                    val isGroup = getInboxMessageListResponse.isGroup
                                    if (isGroup == 0) {
                                        getInboxMessageListResponse.id = objects.getInt("id")
                                        getInboxMessageListResponse.senderId =
                                            objects.getInt("senderId")
                                        getInboxMessageListResponse.receiverId =
                                            objects.getInt("receiverId")
                                        getInboxMessageListResponse.lastMessageId =
                                            objects.getInt("lastMessageId")
                                        getInboxMessageListResponse.deletedId =
                                            objects.getInt("deletedId")
                                        getInboxMessageListResponse.created =
                                            objects.getInt("created")
                                        getInboxMessageListResponse.updated =
                                            objects.getInt("updated")
                                        getInboxMessageListResponse.user_id =
                                            objects.getInt("user_id")
                                        getInboxMessageListResponse.lastMessage =
                                            objects.getString("lastMessage")
                                        getInboxMessageListResponse.userName =
                                            objects.getString("userName")
                                        getInboxMessageListResponse.userImage =
                                            objects.getString("userImage")
                                        getInboxMessageListResponse.created_at =
                                            objects.getInt("created_at")
                                        getInboxMessageListResponse.unreadcount =
                                            objects.getInt("unreadcount")
                                        getInboxMessageListResponse.messageType =
                                            objects.getInt("messageType")
                                        getInboxMessageListResponse.isOnline =
                                            objects.getInt("isOnline")
                                        getInboxMessageListResponse.groupId =
                                            objects.getInt("groupId")
                                        getInboxMessageListResponse.type = objects.getInt("type")
                                        getInboxMessageListResponse.pushKitToken =
                                            objects.getString("pushKitToken")
                                    } else {
                                        getInboxMessageListResponse.id = objects.getInt("id")
                                        getInboxMessageListResponse.userName =
                                            objects.getString("name")
                                        getInboxMessageListResponse.userImage =
                                            objects.getString("image")
                                        getInboxMessageListResponse.created =
                                            objects.getInt("createdAt")
                                        getInboxMessageListResponse.totalUsers =
                                            objects.getInt("totalUsers")
                                        getInboxMessageListResponse.unreadcount =
                                            objects.getInt("unreadcount")
                                        getInboxMessageListResponse.lastMessage =
                                            objects.getString("lastMessage")
                                        getInboxMessageListResponse.lastMessageCreated =
                                            objects.getInt("lastMessageCreated")
                                        getInboxMessageListResponse.messageType =
                                            objects.getInt("messageType")
                                        getInboxMessageListResponse.adminId =
                                            objects.getString("adminId")
                                        getInboxMessageListResponse.groupMessagesCount =
                                            objects.getInt("groupMessagesCount")
                                        getInboxMessageListResponse.deleteMessagesCount =
                                            objects.getInt("deleteMessagesCount")
                                        getInboxMessageListResponse.userId =
                                            objects.getString("userId")
                                        getInboxMessageListResponse.groupId =
                                            objects.getInt("groupId")
                                        getInboxMessageListResponse.isGroup =
                                            objects.getInt("isGroup")
                                    }

                                    list.add(getInboxMessageListResponse)
                                }
                                Log.i("sizecheck", "" + list.size)
                                runOnUiThread {
                                    rvAllMsg.adapter = MessagesAdapter(this, list)
                                }
                            } else {
                                tvNoMsgs.visibility = View.VISIBLE
                                /*rvSwaps.visibility=View.GONE
                            rvAllMsg.visibility=View.GONE
                           */
                            }
                        } else {
                            if (swapListArray.length() != 0) {
                                /*rvAllMsg.visibility=View.GONE
                            tvNoMsgs.visibility=View.GONE
                            rvSwaps.visibility=View.VISIBLE*/
                                for (i in 0 until swapListArray.length()) {
                                    val objects = swapListArray.getJSONObject(i)
                                    val getInboxMessageListResponse = MessageData()
                                    getInboxMessageListResponse.id = objects.getInt("id")
                                    getInboxMessageListResponse.senderId =
                                        objects.getInt("senderId")
                                    getInboxMessageListResponse.receiverId =
                                        objects.getInt("receiverId")
                                    getInboxMessageListResponse.lastMessageId =
                                        objects.getInt("lastMessageId")
                                    getInboxMessageListResponse.deletedId =
                                        objects.getInt("deletedId")
                                    getInboxMessageListResponse.created = objects.getInt("created")
                                    getInboxMessageListResponse.updated = objects.getInt("updated")
                                    getInboxMessageListResponse.user_id = objects.getInt("user_id")
                                    getInboxMessageListResponse.lastMessage =
                                        objects.getString("lastMessage")
                                    getInboxMessageListResponse.userName =
                                        objects.getString("userName")
                                    getInboxMessageListResponse.userImage =
                                        objects.getString("userImage")
                                    getInboxMessageListResponse.created_at =
                                        objects.getInt("created_at")
                                    getInboxMessageListResponse.unreadcount =
                                        objects.getInt("unreadcount")
                                    getInboxMessageListResponse.messageType =
                                        objects.getInt("messageType")
                                    getInboxMessageListResponse.isOnline =
                                        objects.getInt("isOnline")
                                    getInboxMessageListResponse.groupId = objects.getInt("groupId")
                                    getInboxMessageListResponse.type = objects.getInt("type")
                                    getInboxMessageListResponse.isGroup = objects.getInt("isGroup")
                                    getInboxMessageListResponse.pushKitToken =
                                        objects.getString("pushKitToken")
                                    swapList.add(getInboxMessageListResponse)
                                }
                                Log.i("sizecheck", "" + swapList.size)

                                runOnUiThread {
                                    rvSwaps.adapter = SwapsAdapter(this, swapList)
                                }
                            } else {
                                /* rvSwaps.visibility=View.GONE
                            rvAllMsg.visibility=View.GONE*/
                                tvNoMsgs.visibility = View.VISIBLE
                            }
                        }
                    }
                } catch (e: Exception) {
                   dismissProgressDialog()
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        socketManager?.unRegister(this)
    }

    override fun onResume() {
        super.onResume()
        socketManager?.onRegister(this)
      //  getChatData()

    }
}