package com.bintyblackbook.ui.activities.home.message

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bintyblackbook.R
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.MessageData
import com.bintyblackbook.ui.activities.home.videocall.accesstoken.VideoChatViewActivity
import com.bintyblackbook.util.AppConstant
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.ChatViewModel
import com.bintyblackbook.viewmodel.LoopsViewModel
import com.google.android.material.snackbar.Snackbar
import com.master.permissionhelper.PermissionHelper
import kotlinx.android.synthetic.main.activity_video_call_list.*
import kotlinx.android.synthetic.main.toolbar.*

class VideoCallListActivity : BaseActivity(), VideoCallListAdapter.VideoCallListInterface {

    lateinit var loopsViewModel: LoopsViewModel
    var videoCallListAdapter:VideoCallListAdapter?=null
    val videoList= ArrayList<MessageData>()
    var permissionHelper: PermissionHelper? = null
    lateinit var chatViewModel: ChatViewModel
    var receiverId=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_call_list)

        loopsViewModel= LoopsViewModel()
        chatViewModel= ChatViewModel()
        getIntentData()

        setAdapter()
       // getLoopList()
        setOnClicks()
    }

    private fun getIntentData() {
        val intent = intent
        val extras = intent.extras
        videoList.clear()
        var list= extras?.getSerializable("videoCallList") as ArrayList<MessageData>
        if(list.size!=0){
            for(i in 0 until list.size){
                if(list[i].isGroup==0){
                    videoList.add(list[i])
                }
            }
        }
      //  videoList.addAll()
        videoCallListAdapter?.notifyDataSetChanged()
    }

    private fun setOnClicks() {
        rl_Back.setOnClickListener {
            finish()
        }
    }

  /*  private fun getLoopList() {
        loopsViewModel.loopsList(this, getSecurityKey(this)!!, getUser(this)?.authKey!!)
        loopsViewModel.loopsLiveData.observe(this, Observer {

            if (it.code == 200) {
                if (it.data.allData.size == 0) {
                    rvVideoCall.visibility = View.GONE
                    // tvNoLoops.visibility=View.VISIBLE
                } else {
                    rvVideoCall.visibility = View.VISIBLE
                    //tvNoLoops.visibility=View.GONE
                    videoList.clear()
                    videoList.addAll(it.data.allData)
                    videoCallListAdapter?.notifyDataSetChanged()
                }
            }
        })
    }
*/
    private fun setAdapter() {
        videoCallListAdapter = VideoCallListAdapter(this)
        rvVideoCall.adapter = videoCallListAdapter
        videoCallListAdapter?.videoCallListInterface=this
        videoCallListAdapter?.arrayList=videoList

    }

    override fun onItemClick(data: MessageData, position: Int) {
        receiverId= data.user_id.toString()
        checkVideoPermission()
    }


    fun checkVideoPermission() {
        permissionHelper = PermissionHelper(
            this,
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
            ),
            100
        )
        permissionHelper?.denied {
            if (it) {
                Log.d("PermissionHelper", "Denied by system")
                permissionHelper?.openAppDetailsActivity()
            } else {
                Log.d("PermissionHelper", "Denied by user")
                Snackbar.make(
                    findViewById(android.R.id.content),
                    getString(R.string.permission_rationale),
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(
                    getString(R.string.ok)
                ) { v: View? ->
                    checkVideoPermission()
                }.show()
            }
        }

//Request all permission
        permissionHelper!!.requestAll {
            Log.d("PermissionHelper", "All granted")

            sendNotificationToUserApi()
//            var intent = Intent(this, VideoChatViewActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
//            startActivity(intent)
        }


    }

    private fun sendNotificationToUserApi() {
        chatViewModel.sendCallNotification(this, getSecurityKey(this)!!, getUser(this)?.authKey!!,receiverId)
        chatViewModel.notificationLiveData.observe(this, androidx.lifecycle.Observer {

            val intent= Intent(this,VideoChatViewActivity::class.java)
            intent.putExtra("videoToken",it.data?.token)
            intent.putExtra("channelName",it.data?.channelName)
            intent.putExtra("userId", getUser(this)?.id.toString())
            intent.putExtra("otheruserId",receiverId)
            startActivity(intent)

        })
    }
}