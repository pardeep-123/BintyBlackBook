package com.bintyblackbook.ui.activities.home.message

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.ChatAdapter
import com.bintyblackbook.models.ChatModel
import com.bintyblackbook.ui.activities.home.CheckAvailabilityActivity
import com.bintyblackbook.ui.dialogues.BlockUserDialogFragment
import com.bintyblackbook.ui.dialogues.ReportUserDialogFragment
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    var myPopupWindow: PopupWindow? = null
    var chatAdapter: ChatAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        setPopUpWindow()

        rlBack.setOnClickListener {
            this.finish()
        }

        rlInfo.setOnClickListener {
            myPopupWindow?.showAsDropDown(it,-430,-15)
        }

        btnBookNow.setOnClickListener {
           startActivity(Intent(this,CheckAvailabilityActivity::class.java))
        }

        rvChat.layoutManager = LinearLayoutManager(this)
        val arrayList = ArrayList<ChatModel>()
        arrayList.add(ChatModel(R.drawable.shren,"hello",false,true))
        arrayList.add(ChatModel(R.drawable.shren,"How can i help you?",false,false))
        arrayList.add(ChatModel(R.drawable.user12,"Hello, James!",true,true))
        arrayList.add(ChatModel(R.drawable.user12,"How can i help you?",true,false))


        chatAdapter = ChatAdapter(this, arrayList)
        rvChat.adapter = chatAdapter
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
            myPopupWindow?.dismiss()
            val dialogFragment = ReportUserDialogFragment()
            dialogFragment.show(supportFragmentManager, "reportUser")
        }

        myPopupWindow =  PopupWindow (view, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
    }

}