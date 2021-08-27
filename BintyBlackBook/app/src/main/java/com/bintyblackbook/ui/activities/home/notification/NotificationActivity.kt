package com.bintyblackbook.ui.activities.home.notification

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.bintyblackbook.R
import com.bintyblackbook.adapters.NotificationAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.NotificationListData
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.NotificationViewModel
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationActivity : BaseActivity(){


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
        notificationAdapter?.arrayList=arrayList
        adapterItemClick()
    }

    private fun getNotificationList() {
        notificationViewModel.getNotificationList(getSecurityKey(this)!!, getUser(this)?.authKey!!)
        notificationViewModel.notificationLiveData.observe(this, Observer {

            if(it.data.size==0){
                rvNotification.visibility=View.GONE
                tvNoData.visibility=View.VISIBLE
            }
            else {
                rvNotification.visibility=View.VISIBLE
                tvNoData.visibility=View.GONE
                arrayList.clear()
                arrayList.addAll(it?.data!!)
                notificationAdapter?.notifyDataSetChanged()
            }
        })
    }

    private fun adapterItemClick(){
        notificationAdapter?.onItemClick = {notificationModel: NotificationListData ->
            notificationData=notificationModel
            updateNotificationSeen(notificationModel.id.toString())
        }
    }

    private fun updateNotificationSeen(id: String) {
        notificationViewModel.updateNotiSeen(getSecurityKey(this)!!, getUser(this)?.authKey!!,id)
        notificationViewModel.baseLiveData.observe(this, Observer {

            if(it.code==200){
                if (notificationData?.type == 2){
                    val intent= Intent(this, LoopRequestActivity::class.java)
                    intent.putExtra("message",notificationData?.message)
                    intent.putExtra("user_id",notificationData?.user2Id.toString())
                    startActivity(intent)
                }else if (notificationData?.type == 1){
                    startActivity(Intent(this,BookingRequestActivity::class.java))
                }
            }

        })
    }
}