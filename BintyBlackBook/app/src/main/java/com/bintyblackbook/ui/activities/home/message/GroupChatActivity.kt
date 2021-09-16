package com.bintyblackbook.ui.activities.home.message

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.R
import com.bintyblackbook.adapters.GroupChatAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.GroupChatData
import com.bintyblackbook.socket.SocketManager
import com.bintyblackbook.ui.dialogues.LeaveGroupDialogFragment
import com.bintyblackbook.ui.dialogues.ReportChatDialogFragment
import com.bintyblackbook.util.getUser
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_group_chat.*
import kotlinx.android.synthetic.main.activity_group_chat.rlMsgInput
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.util.*

class GroupChatActivity : BaseActivity(), View.OnClickListener, SocketManager.Observer {

    var type=""
    var name=""
    var groupId=""
    var socketManager: SocketManager? = null
    var listChat = ArrayList<GroupChatData>()
    var chatAdapter: GroupChatAdapter?=null
    var myPopupWindow: PopupWindow? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat)
        socketManager = BintyBookApplication.getSocketManager()
        setPopUpWindow()
        setAdapter()
        getIntentData()
        initializeSocket()

        setOnClicks()
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

    private fun setAdapter() {
        val layoutmanager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        layoutmanager.stackFromEnd = true
        layoutmanager.isSmoothScrollbarEnabled = true
        rvGroupChat.layoutManager = layoutmanager
    }

    private fun setOnClicks() {
        rlGroupInfo.setOnClickListener(this)
        rl_back.setOnClickListener(this)
        rlSendMsg.setOnClickListener(this)
    }

    private fun getIntentData() {
        type= intent?.getStringExtra("type").toString()
        name= intent?.getStringExtra("name").toString()
        groupId= intent?.getStringExtra("groupId").toString()
        txtHeading.text= name

        getGroupChatList()
    }

    private fun getGroupChatList() {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("userId", getUser(context)?.id)
            jsonObject.put("groupId", groupId)
            jsonObject.put("type", type)
            socketManager?.getChatMessage(jsonObject)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun setPopUpWindow() {
        val inflater = applicationContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_group_chat, null)

        val tvClear = view.findViewById<TextView>(R.id.tvClearChat)
        val tvLeaveGroup = view.findViewById<TextView>(R.id.tvLeaveGroup)
        val tvReport = view.findViewById<TextView>(R.id.tvReportGroup)

        tvClear.setOnClickListener {
            myPopupWindow?.dismiss()

            clearChat()
        }

        tvLeaveGroup.setOnClickListener {
            myPopupWindow?.dismiss()
            val dialogFragment = LeaveGroupDialogFragment("leaveGroup", this)
            dialogFragment.show(supportFragmentManager, "leaveGroup")
        }

        tvReport.setOnClickListener {
            myPopupWindow?.dismiss()
            val dialogFragment = ReportChatDialogFragment("reportUser",this)
            dialogFragment.show(supportFragmentManager, "reportUser")
        }

        myPopupWindow = PopupWindow(view,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            true
        )
    }

    private fun clearChat() {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("userId", getUser(context)?.id.toString())
            jsonObject.put("groupId", groupId)
            socketManager?.clearGroupChat(jsonObject)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.rlGroupInfo ->{
                myPopupWindow?.showAsDropDown(rlGroupInfo, -430, -15)
            }

            R.id.rl_back->{
                finish()
            }

            R.id.rlSendMsg ->{
                if (edtGroupMsg.text.toString().trim().isEmpty()) {
                    Toast.makeText(context, "Please enter a message", Toast.LENGTH_LONG).show()
                } else {
                    val jsonObject = JSONObject()
                    jsonObject.put("senderId", getUser(context)?.id.toString())
                    jsonObject.put("groupId", groupId)
                    jsonObject.put("messageType", "0")
                    jsonObject.put("message", edtGroupMsg.text.toString().trim())
                    jsonObject.put("type", type)
                    socketManager?.sendGroupMessage(jsonObject)
                    edtGroupMsg.setText("")
                    edtGroupMsg.setSelection(0)
                    //  adapter.notifyDataSetChanged()
                }
            }
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
        when(event){
            SocketManager.GET_GROUP_CHAT_LISTENER -> {
                runOnUiThread {
                    Log.e("fgfdgg", "mychar")
                    val mObject = args[0] as JSONArray
                    listChat.clear()
                    for (i in 0 until mObject.length()) {
                        val jsonobj = mObject.getJSONObject(i)
                        val gson = GsonBuilder().create()
                        val data = gson.fromJson(jsonobj.toString(), GroupChatData::class.java)
                        listChat.add(data)
                    }

                    chatAdapter = GroupChatAdapter(this, listChat, getUser(this)?.id!!)
                    rvGroupChat.adapter = chatAdapter
                    if (listChat.isNullOrEmpty()) {
                        tvNoChat.visibility = View.VISIBLE
                    } else {
                        tvNoChat.visibility = View.GONE
                    }
                }
            }

            SocketManager.LEAVE_GROUP_LISTENER ->{
                runOnUiThread {
                    val mObject = args[0] as JSONObject
                    mObject.getString("success_message")
                    rlMsgInput.visibility=View.GONE
                }
            }

            SocketManager.GROUP_BODY_LISTENER -> {
                runOnUiThread {
                    val mObject = args[0] as JSONObject
                    val gson = GsonBuilder().create()
                    val model = gson.fromJson(mObject.toString(), GroupChatData::class.java)
                    listChat.add(model)
                    if (listChat.size>0) {
                        tvNoChat.visibility = View.GONE
                    } else {
                        tvNoChat.visibility = View.VISIBLE
                    }
                    chatAdapter = GroupChatAdapter(context, listChat,getUser(this)?.id!!)

                    rvGroupChat.adapter = chatAdapter
                }
            }

            SocketManager.DELETE_GROUP_CHAT_LISTENER->{
                try {
                    val data = args.get(0) as JSONObject
                    Log.i("=====", data.toString())
                    listChat.clear()
                    rvGroupChat.visibility = View.GONE
                    tvNoChat.visibility = View.VISIBLE
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun leaveGroup(){
        try {
            val jsonObject = JSONObject()
            jsonObject.put("userId", getUser(this)?.id.toString())
            jsonObject.put("groupId", groupId)
            socketManager?.leaveGroup(jsonObject)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun reportGroup(report_text: String) {
        try{
            val jsonObject= JSONObject()
            jsonObject.put("message",report_text)
            jsonObject.put("userId", getUser(this)?.id.toString())
            jsonObject.put("groupId", groupId)
            socketManager?.reportGroup(jsonObject)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        socketManager?.onRegister(this)
    }
}