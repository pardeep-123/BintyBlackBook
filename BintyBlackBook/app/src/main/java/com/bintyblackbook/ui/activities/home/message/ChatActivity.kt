package com.bintyblackbook.ui.activities.home.message

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.R
import com.bintyblackbook.adapters.ChatAdaptr
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.ChatData
import com.bintyblackbook.service.MyFirebaseMessagingService
import com.bintyblackbook.socket.SocketManager
import com.bintyblackbook.ui.activities.home.CheckAvailabilityActivity
import com.bintyblackbook.ui.dialogues.BlockUserDialogFragment
import com.bintyblackbook.ui.dialogues.ReportChatDialogFragment
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.ChatViewModel
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_chat.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ChatActivity : BaseActivity(), SocketManager.Observer, View.OnClickListener {

    lateinit var chatViewModel:ChatViewModel

    var layoutmanager:LinearLayoutManager?=null

    var socketManager: SocketManager? = null

    var myPopupWindow: PopupWindow? = null
    var receiverId = ""
    var type = ""
    var listChat = ArrayList<ChatData>()
    var chatAdapter: ChatAdaptr? = null
    var user_id: String = ""
    var name = ""
    var block_status = 0
    var isGroup = ""
    var token=""
    var channelName=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        socketManager = BintyBookApplication.getSocketManager()
        chatViewModel= ChatViewModel()

        setAdapter()
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

    private fun setAdapter() {
        layoutmanager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvChat.layoutManager = layoutmanager

    }

    fun scrollToBottom(){
        layoutmanager?.smoothScrollToPosition(rvChat, null, chatAdapter?.itemCount!!)
    }

    private fun setOnClicks() {
        rlSend.setOnClickListener(this)
        rlBack.setOnClickListener(this)
        rlInfo.setOnClickListener(this)
        btnBookNow.setOnClickListener(this)
        rlVideo.setOnClickListener(this)
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
        val inflater = applicationContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
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
            myPopupWindow?.dismiss()
            val dialogFragment = ReportChatDialogFragment(name, this)
            dialogFragment.show(supportFragmentManager, "reportUser")
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
                runOnUiThread {
                    try {
                        val data = args.get(0) as JSONObject
                        Log.i("=====", data.toString())
                        listChat.clear()
                        rvChat.visibility = View.GONE
                        tv_notfound.visibility = View.VISIBLE

                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
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
                try {
                    runOnUiThread {

                        val mObject = args[0] as JSONArray
                        Log.i("msgList", mObject.toString())
                        if (mObject.length() > 0) {
                            rvChat.visibility = View.VISIBLE
                            tv_notfound.visibility = View.GONE
                            listChat.clear()

                            for (i in 0 until mObject.length()) {
                                val jsonobj = mObject.getJSONObject(i)
                                val gson = GsonBuilder().create()
                                val data = gson.fromJson(jsonobj.toString(), ChatData::class.java)
                                listChat.add(data)
                            }
                            listChat.reverse()

                            chatAdapter = ChatAdaptr(this, listChat, getUser(this)?.id!!)
                            //viewLastMessage()
                            rvChat.adapter = chatAdapter
                            scrollToBottom()

                        } else {
                            rvChat.visibility = View.GONE
                            tv_notfound.visibility = View.VISIBLE
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            SocketManager.BODY_LISTENER -> {
                try {
                    runOnUiThread {
                        val mObject = args[0] as JSONObject

                        Log.i("msg", mObject.toString())
                        val gson = GsonBuilder().create()
                        val model = gson.fromJson(mObject.toString(), ChatData::class.java)

                        val senderId = model.senderId
                        val user2Id = model.receiverId
                        if (model.senderId != getUser(context)?.id) {
                            model.recieverImage = mObject.getString("senderImage")
                        }

                        if (user2Id == receiverId.toInt() || senderId == receiverId.toInt()) {
                            listChat.add(model)

                            if (listChat.size > 0) {
                                tv_notfound.visibility = View.GONE
                                rvChat.visibility = View.VISIBLE
                                chatAdapter?.notifyDataSetChanged()
                                rvChat.scrollToPosition(listChat.size - 1)
                            } else {
                                rvChat.visibility = View.GONE
                                tv_notfound.visibility = View.VISIBLE
                            }
                        }

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            SocketManager.BLOCK_USER_LISTENER -> {
                try {
                    val data = args.get(0) as JSONObject
                    Log.e("BLOCK_LISTENER", data.toString())
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        socketManager?.onRegister(this)
        MyFirebaseMessagingService.isChatNotOpened = false
    }

    fun initializeSocket() {
        socketManager = BintyBookApplication.getSocketManager()
        if (socketManager == null) {
            socketManager?.initializeSocket()
        }

    }

    override fun onPause() {
        super.onPause()
        MyFirebaseMessagingService.isChatNotOpened = true
    }
    override fun onStop() {
        super.onStop()
        socketManager?.unRegister(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.rlVideo -> {
                // startActivity(Intent(this,VideoCallActivity::class.java))

                //ios channel name= $mUserId-|-remoteNumber-|-date-|-video-|-name
                var mUserId = getUser(this)?.id.toString()
                var mUserName = getUser(this)?.firstName

                var timeInterval=0L
                val toyBornTime = "2001-01-01"
                val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                dateFormat.timeZone = TimeZone.getTimeZone("UTC")

                try {
                    val oldDate: Date = dateFormat.parse(toyBornTime)
                    System.out.println(oldDate)
                    val currentDate = Date()
                    val diff = currentDate.time - oldDate.time
                    val seconds = diff / 1000
                    val milli= seconds/1000
                    val minutes = seconds / 60
                    val hours = minutes / 60
                    val days = hours / 24
                    if (oldDate.before(currentDate)) {
                        timeInterval=seconds
                        Log.e("oldDate", "is previous date")
                        Log.e("Difference: ", " seconds: " + milli + " minutes: " + minutes
                                    + " hours: " + hours + " days: " + days
                        )
                    }

                    // Log.e("toyBornTime", "" + toyBornTime);
                } catch (e: ParseException) {
                    e.printStackTrace()
                }


                var mChannelName = "$mUserId-|-$receiverId-|-$timeInterval-|-video-|-$mUserName"
                //"$mUserId-|-$receiverId-|-${System.currentTimeMillis()/1000}-|-video-|-$mUserName"

                Log.d("=====", mChannelName + "====date" + timeInterval)


                chatViewModel.sendCallNotification(
                    this,
                    getSecurityKey(this)!!,
                    getUser(this)?.authKey!!,
                    receiverId
                )
                chatViewModel.notificationLiveData.observe(this, {

                    val intent = Intent(this, VideoCallActivity::class.java)
                    intent.putExtra("videoToken", it.data?.token)
                    intent.putExtra("channelName", mChannelName)
                    intent.putExtra("userId", getUser(this)?.id.toString())
                    intent.putExtra("otheruserId", receiverId)
                    intent.putExtra("otherUserName", name)
                    startActivity(intent)

                })
            }

            R.id.btnBookNow -> {
                val intent = Intent(this, CheckAvailabilityActivity::class.java)
                intent.putExtra("user_id", receiverId)
                intent.putExtra("screen_type", "chat")
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

    fun reportUser(report: String){
        try{
            val jsonObject= JSONObject()
            jsonObject.put("message", report)
            jsonObject.put("userId", getUser(this)?.id.toString())
            jsonObject.put("user2Id", receiverId)
            socketManager?.reportUser(jsonObject)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }


}