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

class ChatActivity : BaseActivity(),SocketManager.Observer, View.OnClickListener {

    var socketManager:SocketManager?=null

    var myPopupWindow: PopupWindow? = null
    var receiverId=""
    var message_type=""
    var listChat = ArrayList<Datum>()
    lateinit var adapter: ChatAdaptr

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        socketManager= BintyBookApplication.getSocketManager()
        receiverId= intent.getStringExtra("receiver_id").toString()
        message_type= intent.getStringExtra("message_type").toString()

        setPopUpWindow()

        initializeSocket()

        getFriendMessageList()

        setOnClicks()
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
            jsonObject.put("senderId", getUser(this)?.id.toString())
            jsonObject.put("receiverId", receiverId)
            socketManager?.getFriendMessage(jsonObject)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }


    private fun setPopUpWindow() {
        val inflater =
            applicationContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val  view = inflater.inflate(R.layout.popup_in_chat, null)

        val tvClear = view.findViewById<TextView>(R.id.tvClear)
        val tvBlock = view.findViewById<TextView>(R.id.tvBlock)
        val tvReport = view.findViewById<TextView>(R.id.tvReport)

        tvClear.setOnClickListener {
            myPopupWindow?.dismiss()
        }

        tvBlock.setOnClickListener {
            myPopupWindow?.dismiss()
            val dialogFragment = BlockUserDialogFragment("blockUser")
            dialogFragment.show(supportFragmentManager, "blockUser")
        }

        tvReport.setOnClickListener {
//            myPopupWindow?.dismiss()
//            val dialogFragment = ReportUserDialogFragment()
//            dialogFragment.show(supportFragmentManager, "reportUser")
        }

        myPopupWindow =  PopupWindow (view, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
    }

    override fun onError(event: String?, vararg args: Any?) {

    }

    override fun onResponse(event: String?, vararg args: Any?) {
        Log.e("EVENT", event.toString())
        when (event) {

            SocketManager.GET_CHAT_LISTENER -> {
               // dialogBox.dismiss()
                try {
                    runOnUiThread {
                        val data = args.get(0) as JSONArray
                        Log.e("GET_CHAT_LISTENER", data.toString())

                        Log.i("data_size",data.length().toString())
                          listChat = ArrayList()
//                        if (data.length() > 0) {
//                            // tv_notfound.visibility = View.GONE
//                        } else {
//                            //  tv_notfound.visibility = View.VISIBLE
//                        }
                        for (i in 0 until data.length()) {

                            val data = data.getJSONObject(i)
                            val chatModel = Datum()

                            chatModel.id = data.getInt("id")
                            chatModel.readStatus = data.getInt("readStatus")
                            chatModel.groupId = data.getInt("groupId")
                            chatModel.deletedId = data.getInt("deletedId")
                            chatModel.created = data.getString("created")
                            chatModel.updated = data.getString("updated")
                            chatModel.chatConstantId = data.getInt("chatConstantId")
                            chatModel.senderName = data.getString("senderName")
                            chatModel.message = data.getString("message")
                            chatModel.senderId = data.getInt("senderId")
                            chatModel.senderImage = data.getString("senderImage")
                            chatModel.recieverName = data.getString("recieverName")
                            chatModel.receiverId = data.getInt("receiverId")
                            chatModel.recieverImage = data.getString("recieverImage")
                            chatModel.messageType = data.getString("messageType")
                            listChat.add(chatModel)
                        }


                        runOnUiThread {
                            adapter = ChatAdaptr(context, listChat, getUser(this)?.id.toString())
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
                        chatModel.created = data.getString("created")
                        chatModel.updated = data.getString("updated")
                        chatModel.chatConstantId = data.getInt("chatConstantId")
                        chatModel.senderName = data.getString("senderName")
                        chatModel.message = data.getString("message")
                        chatModel.senderId = data.getInt("senderId")
                        chatModel.senderImage = data.getString("senderImage")
                        chatModel.recieverName = data.getString("recieverName")
                        chatModel.receiverId = data.getInt("receiverId")
                        chatModel.recieverImage = data.getString("recieverImage")
                        chatModel.messageType = data.getString("messageType")
                         listChat.add(chatModel)
                        /*if(listChat.size>0){
                            tv_notfound.visibility = View.GONE
                        }else{
                            tv_notfound.visibility = View.VISIBLE
                        }*/
                        viewLastMessage()
                    }
                } catch (ex: Exception) {
                    Log.e("BODY_LISTENER", ex.toString())
                    ex.printStackTrace()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        socketManager?.unRegister(this)
        socketManager?.onRegister(this)

    }

    @SuppressLint("WrongConstant")
    fun viewLastMessage(){
        runOnUiThread {
            val li = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
            li.setStackFromEnd(true);
            rvChat.layoutManager = li
            rvChat.adapter = adapter
            adapter.notifyDataSetChanged()

        }
    }

    fun initializeSocket(){
        socketManager = BintyBookApplication.getSocketManager()
        if(socketManager==null){
            socketManager?.initializeSocket()
        }

    }
    override fun onStop() {
        super.onStop()
        socketManager?.unRegister(this)
    }

    override fun onClick(v: View?) {

        when(v?.id){

            R.id.btnBookNow ->{
                val intent = Intent(this,CheckAvailabilityActivity::class.java)
                //intent.putExtra("user_id",userId)
                startActivity(intent)
            }

            R.id.rlBack ->{
                this.finish()
            }

            R.id.rlSend ->{
                if (edtMsg.text.toString().trim().isEmpty()) {
                    Toast.makeText(context,"Please enter a message!!", Toast.LENGTH_LONG).show()
                }
                else {
                    val jsonObject = JSONObject()
                    jsonObject.put("senderId", getUser(this)?.id.toString())
                    jsonObject.put("receiverId",receiverId)
                    jsonObject.put("messageType", message_type)
                    jsonObject.put("message",edtMsg.text.toString().trim() )

                    socketManager?.sendMessage(jsonObject)
                    edtMsg.setText("")
                    edtMsg.setSelection(0)
                  //  adapter.notifyDataSetChanged()
                }
            }

            R.id.rlInfo ->{
                myPopupWindow?.showAsDropDown(rlInfo,-430,-15)
            }
        }
    }
}