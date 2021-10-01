package com.bintyblackbook.ui.activities.home.notification

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.bintyblackbook.R
import com.bintyblackbook.adapters.NotificationAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.NotificationListData
import com.bintyblackbook.ui.activities.home.message.ChatActivity
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.NotificationViewModel
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationActivity : BaseActivity(),NotificationAdapter.NotificationAdapterInterface{

    lateinit var notificationViewModel: NotificationViewModel
    var notificationAdapter:NotificationAdapter? = null

    var arrayList= ArrayList<NotificationListData>()

    var notificationData:NotificationListData?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        notificationViewModel= NotificationViewModel(this)

        rlBack.setOnClickListener {
            finish()
        }

        setAdapter()
        getNotificationList()
    }

    private fun setAdapter() {
        notificationAdapter = NotificationAdapter(this)
        rvNotification.adapter = notificationAdapter
        notificationAdapter?.notificationAdapterInterface=this
        notificationAdapter?.arrayList=arrayList

    }

    private fun getNotificationList() {
        notificationViewModel.getNotificationList(getSecurityKey(this)!!, getUser(this)?.authKey!!)
        notificationViewModel.notificationLiveData.observe(this, Observer {

            arrayList.clear()
            if(it.data.size==0){
                rvNotification.visibility=View.GONE
                tvNoData.visibility=View.VISIBLE
            }
            else {
                rvNotification.visibility=View.VISIBLE
                tvNoData.visibility=View.GONE
                arrayList.addAll(it?.data!!)
                arrayList.reverse()
                notificationAdapter?.notifyDataSetChanged()
            }
        })
    }

    private fun updateNotificationSeen(data: NotificationListData) {
        notificationViewModel.updateNotiSeen(getSecurityKey(this)!!, getUser(this)?.authKey!!,data.id.toString())
        notificationViewModel.baseLiveData.observe(this, Observer {

            if(it.code==200){
                if (notificationData?.type == 2){
                    val intent= Intent(this, LoopRequestActivity::class.java)
                    intent.putExtra("message",data.message)
                    intent.putExtra("user_id",data.user2Id.toString())
                    startActivity(intent)
                }else if (notificationData?.type == 1){
                    startActivity(Intent(this,BookingRequestActivity::class.java))
                }
            }
        })
    }

    override fun onItemClick(data: NotificationListData, position: Int) {
        if(data.isSeen==1){
            if (data.type == 2){
                val intent= Intent(this, LoopRequestActivity::class.java)
                intent.putExtra("message",data.message)
                intent.putExtra("user_id",data.user2Id.toString())
                startActivity(intent)
            }else if (data.type == 1 || data.type==4){
                val intent= Intent(this,BookingRequestActivity::class.java)
                intent.putExtra("message",data.message)
                intent.putExtra("user_id",data.user2Id.toString())

                startActivity(intent)
            }
            else if(data.type==3){
                val intent=Intent(context, ChatActivity::class.java)
                intent.putExtra("type",data.type.toString())
               // intent.putExtra("isGroup",arrayList[pos].isGroup.toString())
                intent.putExtra("sender_id",data.userId.toString())
                intent.putExtra("name",data.userName)
                context.startActivity(intent)
            }
        }

        else{
            updateNotificationSeen(data)
        }

    }
}