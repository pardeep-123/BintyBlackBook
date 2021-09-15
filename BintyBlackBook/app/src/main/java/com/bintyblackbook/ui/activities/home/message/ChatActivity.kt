package com.bintyblackbook.ui.activities.home.message

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.R
import com.bintyblackbook.adapters.ChatAdaptr
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.Datum
import com.bintyblackbook.socket.SocketManager
import com.bintyblackbook.ui.activities.home.CheckAvailabilityActivity
import com.bintyblackbook.ui.dialogues.BlockUserDialogFragment
import com.bintyblackbook.util.getUser
import kotlinx.android.synthetic.main.activity_chat.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class ChatActivity : BaseActivity(), SocketManager.Observer, View.OnClickListener {

    var socketManager: SocketManager? = null

    var myPopupWindow: PopupWindow? = null
    var receiverId = ""
    var sender_id = ""
    var user_receiverId = ""
    var type = ""
    var listChat = ArrayList<Datum>()
    var adapter: ChatAdaptr? = null
    var user_id: String = ""
    var name = ""
    var block_status = 0
    var isGroup = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        socketManager = BintyBookApplication.getSocketManager()
        getIntentData()

        initializeSocket()

        setOnClicks()
    }

    private fun getIntentData() {

        receiverId = intent.getStringExtra("sender_id").toString()
        type = intent.getStringExtra("type").toString()
        name = intent.getStringExtra("name").toString()
        isGroup = intent.getStringExtra("isGroup").toString()

        if (isGroup == "0") {
            rlVideo.visibility = View.VISIBLE
        } else {
            rlVideo.visibility = View.GONE
        }

        tvHeading.text = name
        getFriendMessageList()
        getBlockUserStatus()

    }

    private fun setOnClicks() {
        rlSend.setOnClickListener(this)
        rlBack.setOnClickListener(this)
        rlInfo.setOnClickListener(this)
        btnBookNow.setOnClickListener(this)

    }

    fun getFriendMessageList() {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("senderId", getUser(context)?.id)
            jsonObject.put("receiverId", receiverId)
            jsonObject.put("type", type)
            socketManager?.getFriendMessage(jsonObject)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }


    fun getBlockUserStatus() {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("userId", getUser(context)?.id.toString())
            jsonObject.put("user2Id", receiverId)
            socketManager?.getBlockStatus(jsonObject)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    private fun setPopUpWindow() {
        val inflater =
            applicationContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_in_chat, null)

        val tvClear = view.findViewById<TextView>(R.id.tvClear)
        val tvBlock = view.findViewById<TextView>(R.id.tvBlock)
        val tvReport = view.findViewById<TextView>(R.id.tvReport)
        if (block_status == 1) {
            tvBlock.visibility = View.GONE
        } else {
            tvBlock.visibility = View.VISIBLE
        }


        tvClear.setOnClickListener {
            myPopupWindow?.dismiss()

            clearChat()
        }

        tvBlock.setOnClickListener {
            myPopupWindow?.dismiss()
            val dialogFragment = BlockUserDialogFragment("blockUser", this)
            dialogFragment.show(supportFragmentManager, "blockUser")
        }

        tvReport.setOnClickListener {
//            myPopupWindow?.dismiss()
//            val dialogFragment = ReportUserDialogFragment()
//            dialogFragment.show(supportFragmentManager, "reportUser")
        }

        myPopupWindow = PopupWindow(
            view,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            true
        );
    }

    //for clear chat
    private fun clearChat() {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("userId", getUser(context)?.id.toString())
            jsonObject.put("user2Id", receiverId)
            socketManager?.clearChat(jsonObject)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

    }

    override fun onError(event: String?, vararg args: Any?) {
        when (event) {

            "errorSocket" -> {
                dismissProgressDialog()
            }
        }
    }

    override fun onResponse(event: String?, vararg args: Any?) {
        Log.e("EVENT", event.toString())
        when (event) {

            SocketManager.DELETE_CHAT_LISTENER -> {
                try{
                    runOnUiThread {
                    val data = args.get(0) as JSONObject
                    Log.i("=====", data.toString())
                    listChat.clear()
                    rvChat.visibility = View.GONE
                    tv_notfound.visibility = View.VISIBLE
                   }
                }catch (e:java.lang.Exception){
                    e.printStackTrace()
                }
            }

            SocketManager.BLOCK_STATUS_LISTENER -> {

                try {
                        val data = args.get(0) as JSONObject
                        Log.i("Block_status_listener", data.toString())
                        // block_data_status=1(blocked),block_data_status=0(unblocked)
                        if (data.getInt("block_data_status") == 1) {
                            block_status = 1
                            llBottom.visibility = View.GONE
                            if (isGroup == "0") {
                                rlVideo.visibility = View.GONE
                            }

                        } else {
                            block_status = 0
                            if (isGroup == "0") {
                                rlVideo.visibility = View.VISIBLE
                            }
                            llBottom.visibility = View.VISIBLE
                        }
                        setPopUpWindow()


                } catch (e: java.lang.Exception) {
                    e.printStackTrace()

                }


            }

            SocketManager.GET_CHAT_LISTENER -> {
                // dialogBox.dismiss()
                try {
                    runOnUiThread {
                        val data = args.get(0) as JSONArray
                        Log.e("GET_CHAT_LISTENER", data.toString())

                        Log.i("data_size", data.length().toString())
                        listChat = ArrayList()
                        if (data.length() > 0) {
                            tv_notfound.visibility = View.GONE
                        } else {
                            tv_notfound.visibility = View.VISIBLE
                        }
                        for (i in 0 until data.length()) {

                            val data = data.getJSONObject(i)
                            val chatModel = Datum()

                            chatModel.id = data.getInt("id")
                            user_id = chatModel.id.toString()
                            chatModel.readStatus = data.getInt("readStatus")
                            chatModel.groupId = data.getInt("groupId")
                            chatModel.deletedId = data.getInt("deletedId")
                            chatModel.created = data.getLong("created")
                            chatModel.updated = data.getLong("updated")
                            chatModel.chatConstantId = data.getInt("chatConstantId")
                            chatModel.senderName = data.getString("senderName")
                            chatModel.message = data.getString("message")
                            chatModel.senderId = data.getInt("senderId")
                            chatModel.senderImage = data.getString("senderImage")
                            chatModel.recieverName = data.getString("recieverName")
                            chatModel.receiverId = data.getInt("receiverId")
                            if (chatModel.receiverId!! == getUser(this)?.id) {
                                user_receiverId = chatModel.receiverId.toString()
                            } else {
                                user_receiverId = chatModel.senderId.toString()
                            }

                            chatModel.recieverImage = data.getString("recieverImage")
                            chatModel.messageType = data.getString("messageType")
                            listChat.add(chatModel)
                        }

                        runOnUiThread {
                            listChat.reverse()
                            adapter = ChatAdaptr(context, listChat, user_receiverId)
                            viewLastMessage()

                        }
                    }
                } catch (ex: Exception) {
                    Log.e("BODY_LISTENER", ex.toString())
                    ex.printStackTrace()
                }
            }

            SocketManager.BODY_LISTENER -> {

                try {
                    runOnUiThread {
                        val data = args.get(0) as JSONObject
                        Log.e("BODY_LISTENER", data.toString())
                        val chatModel = Datum()

                        chatModel.id = data.getInt("id")
                        chatModel.readStatus = data.getInt("readStatus")
                        chatModel.groupId = data.getInt("groupId")
                        chatModel.deletedId = data.getInt("deletedId")
                        chatModel.created = data.getLong("created")
                        chatModel.updated = data.getLong("updated")
                        chatModel.chatConstantId = data.getInt("chatConstantId")
                        chatModel.senderName = data.getString("senderName")
                        chatModel.message = data.getString("message")
                        chatModel.senderId = data.getInt("senderId")
                        chatModel.senderImage = data.getString("senderImage")
                        chatModel.recieverName = data.getString("recieverName")
                        chatModel.receiverId = data.getInt("receiverId")
                        //   receiverId= chatModel.receiverId.toString()
                        chatModel.recieverImage = data.getString("recieverImage")
                        chatModel.messageType = data.getString("messageType")
                        listChat.add(chatModel)
                        if (listChat.size > 0) {
                            tv_notfound.visibility = View.GONE
                        } else {
                            tv_notfound.visibility = View.VISIBLE
                        }
                        if (chatModel.receiverId!! == getUser(this)?.id) {
                            adapter = ChatAdaptr(context, listChat, chatModel.receiverId.toString())
                        } else {
                            adapter = ChatAdaptr(context, listChat, chatModel.senderId.toString())
                        }

                        viewLastMessage()
                    }
                } catch (ex: Exception) {
                    Log.e("BODY_LISTENER", ex.toString())
                    ex.printStackTrace()
                }
            }

            SocketManager.BLOCK_USER_LISTENER -> {
                try {
                    runOnUiThread {
                        val data = args.get(0) as JSONObject
                        Log.e("BLOCK_LISTENER", data.toString())
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        socketManager?.onRegister(this)
    }

    @SuppressLint("WrongConstant")
    fun viewLastMessage() {
        runOnUiThread {
            val li = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
            li.stackFromEnd = true;
            rvChat.layoutManager = li
            rvChat.adapter = adapter
            adapter?.notifyDataSetChanged()
        }
    }

    fun initializeSocket() {
        socketManager = BintyBookApplication.getSocketManager()
        if (socketManager == null) {
            socketManager?.initializeSocket()
        }

    }

    override fun onStop() {
        super.onStop()
        socketManager?.unRegister(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.btnBookNow -> {
                val intent = Intent(this, CheckAvailabilityActivity::class.java)
                intent.putExtra("user_id", user_id)
                startActivity(intent)
            }

            R.id.rlBack -> {
                finish()
            }

            R.id.rlSend -> {
                if (edtMsg.text.toString().trim().isEmpty()) {
                    Toast.makeText(context, "Please enter a message", Toast.LENGTH_LONG).show()
                } else {
                    val jsonObject = JSONObject()
                    jsonObject.put("senderId", getUser(context)?.id.toString())
                    jsonObject.put("receiverId", receiverId)
                    jsonObject.put("messageType", "0")
                    jsonObject.put("message", edtMsg.text.toString().trim())
                    jsonObject.put("type", type)
                    socketManager?.sendMessage(jsonObject)
                    edtMsg.setText("")
                    edtMsg.setSelection(0)
                    //  adapter.notifyDataSetChanged()
                }
            }

            R.id.rlInfo -> {
                myPopupWindow?.showAsDropDown(rlInfo, -430, -15)
            }
        }
    }


    fun blockUser() {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("userId", getUser(context)?.id)
            jsonObject.put("user2Id", receiverId)
            jsonObject.put("status", "0")
            socketManager?.blockUser(jsonObject)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}