package com.bintyblackbook.ui.activities.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.EventAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.EventData
import com.bintyblackbook.util.AppConstant
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.EventsViewModel
import kotlinx.android.synthetic.main.activity_event.*

class EventActivity : BaseActivity() {

    var eventAdapter:EventAdapter? = null

    lateinit var eventsViewModel:EventsViewModel
    var arrayList= ArrayList<EventData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        eventsViewModel= EventsViewModel()

        rlBack.setOnClickListener {
            finish()
        }

        init()

        getMyEvents()
    }

    private fun getMyEvents() {

        eventsViewModel.getOtherUserEvents(context,getSecurityKey(this)!!, getUser(this)?.authKey!!)

        eventsViewModel.eventsLiveData.observe(this, Observer {
            if(it.data.size==0){
                tvNoEvents.visibility=View.VISIBLE
                rvEvents.visibility=View.GONE
            }
            else{
                tvNoEvents.visibility=View.GONE
                rvEvents.visibility=View.VISIBLE
                arrayList.addAll(it.data)
                eventAdapter?.notifyDataSetChanged()
            }
        })

    }

    private fun init(){
        rvEvents.layoutManager = GridLayoutManager(this,2)
        eventAdapter= EventAdapter(this)
        rvEvents.adapter = eventAdapter
        eventAdapter?.arrayList=arrayList
        adapterItemClick()
    }

    private fun adapterItemClick(){
        eventAdapter?.onItemClick = {eventsModel: EventData ->
            val intent = Intent(this,EventDetailActivity::class.java)
            intent.putExtra(AppConstant.HEADING,eventsModel.name)
            intent.putExtra("post_id",eventsModel.id)
            startActivity(intent)
        }
    }
}