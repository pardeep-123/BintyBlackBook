package com.bintyblackbook.ui.activities.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.EventAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.models.EventsModel
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.EventsViewModel
import kotlinx.android.synthetic.main.activity_event.*

class EventActivity : BaseActivity() {

    var eventAdapter:EventAdapter? = null

    lateinit var eventsViewModel:EventsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        eventsViewModel= EventsViewModel(this)

        rlBack.setOnClickListener {
            finish()
        }

        init()

        getMyEvents()
    }

    private fun getMyEvents() {

        eventsViewModel.myEvents(getSecurityKey(this)!!, getUser(this)?.authKey!!, getUser(this)?.id.toString())

        eventsViewModel.eventsLiveData.observe(this, Observer {
            Log.i("TAG",it.msg)
        })

    }

    private fun init(){
        val arrayList = ArrayList<EventsModel>()
        arrayList.add(EventsModel(R.drawable.john,"John","Arizona, USA",false))
        arrayList.add(EventsModel(R.drawable.girl1,"John","Arizona, USA",false))
        arrayList.add(EventsModel(R.drawable.matinn,"John","Arizona, USA",false))
        arrayList.add(EventsModel(R.drawable.girl,"John","Arizona, USA",true))
        arrayList.add(EventsModel(R.drawable.girl1,"John","Arizona, USA",false))
        arrayList.add(EventsModel(R.drawable.mattrin,"John","Arizona, USA",false))
        arrayList.add(EventsModel(R.drawable.john,"John","Arizona, USA",false))
        arrayList.add(EventsModel(R.drawable.matinn,"John","Arizona, USA",true))
        arrayList.add(EventsModel(R.drawable.girl,"John","Arizona, USA",false))
        arrayList.add(EventsModel(R.drawable.mattrin,"John","Arizona, USA",false))

       // eventAdapter = EventAdapter(this,arrayList)
        rvEvents.layoutManager = GridLayoutManager(this,2)
        rvEvents.adapter = eventAdapter
        adapterItemClick()
    }

    private fun adapterItemClick(){
       /* eventAdapter?.onItemClick = {eventsModel: EventsModel ->
            val intent = Intent(this,EventDetailActivity::class.java)
            intent.putExtra(AppConstant.HEADING,eventsModel.name)
            startActivity(intent)
        }*/
    }
}