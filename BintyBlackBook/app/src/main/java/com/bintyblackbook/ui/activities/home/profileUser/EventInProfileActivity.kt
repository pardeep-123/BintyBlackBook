package com.bintyblackbook.ui.activities.home.profileUser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.EventInProfileAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.EventData
import com.bintyblackbook.ui.activities.home.AddEventActivity
import com.bintyblackbook.ui.dialogues.EventDeleteDialogFragment
import com.bintyblackbook.util.AppConstant
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.EventsViewModel
import kotlinx.android.synthetic.main.activity_event_in_profile.*

class EventInProfileActivity : BaseActivity(), EventInProfileAdapter.EventProfileInterface {

    lateinit var eventsViewModel: EventsViewModel
    var eventInProfileAdapter: EventInProfileAdapter? = null
    var myPopupWindow: PopupWindow? = null
    var arrayList= ArrayList<EventData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_in_profile)
        eventsViewModel= EventsViewModel()

        init()

        getMyEvents()
        rlBack.setOnClickListener {
            finish()
        }

        rlAdd.setOnClickListener {
            val intent= Intent(this, AddEventActivity::class.java)
            intent.putExtra(AppConstant.HEADING, "Add Event")
            intent.putExtra("type","add")
            startActivity(intent)
        }

    }

    private fun getMyEvents() {
        eventsViewModel.myEvents(context,getSecurityKey(this)!!, getUser(this)?.authKey!!, getUser(this)?.id.toString())
        eventsViewModel.eventsLiveData.observe(this, Observer {

            if(it.data.size==0){
                tvNoEvent.visibility=View.VISIBLE
                rvEvents.visibility=View.GONE
            }else{
                tvNoEvent.visibility=View.GONE
                rvEvents.visibility=View.VISIBLE
                arrayList.clear()
                arrayList.addAll(it.data)
                eventInProfileAdapter?.notifyDataSetChanged()
            }

        })
    }

    private fun init() {
        rvEvents.layoutManager = GridLayoutManager(this, 2)
        eventInProfileAdapter = EventInProfileAdapter(this)
        rvEvents.adapter = eventInProfileAdapter
        eventInProfileAdapter?.eventProfileInterface=this
        eventInProfileAdapter?.arrayList=arrayList
        //adapterItemClick()
    }

    fun deleteEvent(event_id:String,position:Int){
        eventsViewModel.deleteEvent(this, getSecurityKey(this)!!, getUser(this)?.authKey!!,event_id)
        eventsViewModel.baseEventsLiveData.observe(this, Observer {

            if(it.code==200){
                arrayList.removeAt(position)
                eventInProfileAdapter?.notifyDataSetChanged()
            }

        })
    }

    override fun onEditClick(data: EventData,position: Int) {
        val intent = Intent(this,AddEventActivity::class.java)
        intent.putExtra(AppConstant.HEADING, "Edit Event")
        intent.putExtra("event_id",data.id.toString())
        intent.putExtra("type","edit")
        intent.putExtra("name",data.name)
        intent.putExtra("location",data.location)
        intent.putExtra("time",data.time.toString())
        intent.putExtra("date",data.date.toString())
        intent.putExtra("description",data.description)
        intent.putExtra("info", data.moreInfo)
        intent.putExtra("link",data.rsvpLink)
        intent.putExtra("image",data.image)
        startActivity(intent)
    }

    override fun onDeleteClick(data: EventData, position: Int) {
        myPopupWindow?.dismiss()
        val eventDeleteDialog = EventDeleteDialogFragment(this, data.id.toString(),position)
        eventDeleteDialog.show(supportFragmentManager,"eventDelete")

    }

    override fun onRestart() {
        super.onRestart()
        getMyEvents()
    }
}