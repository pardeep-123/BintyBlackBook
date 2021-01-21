package com.bintyblackbook.ui.activities.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.HorizontalImagesAdapter
import com.bintyblackbook.ui.activities.home.message.ChatActivity
import com.bintyblackbook.ui.dialogues.FragmentDialog
import com.bintyblackbook.util.AppConstant
import kotlinx.android.synthetic.main.activity_user_detail.*
import kotlinx.android.synthetic.main.toolbar.*

class UserDetailActivity : AppCompatActivity() {

    var horizontalImagesAdapter:HorizontalImagesAdapter? = null
    var loopBtnClick = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)
        rvImages.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val showChatBtn = intent.getBooleanExtra(AppConstant.SHOW_CHAT_BTN,false)
        if (showChatBtn){
            btnChat.visibility = View.VISIBLE
        }

        iv_back.setOnClickListener {
            finish()
        }

        headingText.visibility = View.GONE

        init()

        btnAvailability.setOnClickListener {
            val intent = Intent(this,CheckAvailabilityActivity::class.java)
            startActivity(intent)
        }

        btnChat.setOnClickListener {
            val intent = Intent(this,ChatActivity::class.java)
            startActivity(intent)
        }

        btnLoop.setOnClickListener {
            if (loopBtnClick){
                loopBtnClick = false
                btnLoop.text = getString(R.string.cancel_loop_request)
                val fragmentDialog = FragmentDialog("LoopRequest")
                fragmentDialog.show(supportFragmentManager,"LoopDialog")
            }else{
                loopBtnClick = true
                btnLoop.text = getString(R.string.loop)
                val fragmentDialog = FragmentDialog("LoopRequestCancel")
                fragmentDialog.show(supportFragmentManager,"LoopDialog")
            }

        }

        btnEvent.setOnClickListener {
            val intent = Intent(this,EventActivity::class.java)
            startActivity(intent)
        }
    }

    private fun init(){
        val arrayList = ArrayList<Int>()
        arrayList.add(R.drawable.slider)
        arrayList.add(R.drawable.small2)
        arrayList.add(R.drawable.small3)
        arrayList.add(R.drawable.small4)
        arrayList.add(R.drawable.small2)
        arrayList.add(R.drawable.small3)
        arrayList.add(R.drawable.small4)

        horizontalImagesAdapter = HorizontalImagesAdapter(this,arrayList)
        rvImages.adapter = horizontalImagesAdapter
        horizontalImagesAdapterClick()
    }

    private fun horizontalImagesAdapterClick(){
        /*horizontalImagesAdapter?.onItemClick = {image: Int ->
            riv1.setImageResource(image)
        }*/
    }
}