package com.bintyblackbook.ui.activities.home.profileUser

import android.content.Intent
import android.os.Bundle
import android.view.View
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

class EventInProfileActivity : BaseActivity() {

    lateinit var eventsViewModel: EventsViewModel
    var eventInProfileAdapter: EventInProfileAdapter? = null

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
                arrayList.addAll(it.data)
                eventInProfileAdapter?.notifyDataSetChanged()
            }

        })
    }

    private fun init() {
        rvEvents.layoutManager = GridLayoutManager(this, 2)
        eventInProfileAdapter = EventInProfileAdapter(this)
        rvEvents.adapter = eventInProfileAdapter
        eventInProfileAdapter?.arrayList=arrayList
        adapterItemClick()
    }

    private fun adapterItemClick() {

        eventInProfileAdapter?.onItemClick = {eventsModel: EventData, clickOn: String,position:Int ->
            if (clickOn == "editClick"){
                val intent = Intent(this,AddEventActivity::class.java)
                intent.putExtra(AppConstant.HEADING, "Edit Event")
                intent.putExtra("type","edit")
                intent.putExtra("name",eventsModel.name)
                intent.putExtra("location",eventsModel.location)
                intent.putExtra("time",eventsModel.time.toString())
                intent.putExtra("date",eventsModel.date.toString())
                intent.putExtra("description",eventsModel.description)
                intent.putExtra("info", eventsModel.moreInfo)
                intent.putExtra("link",eventsModel.rsvpLink)
                startActivity(intent)
            }else if(clickOn == "deleteClick"){
                val eventDeleteDialog = EventDeleteDialogFragment(this, eventsModel.id.toString(),position)
                eventDeleteDialog.show(supportFragmentManager,"eventDelete")
            }
        }
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

}