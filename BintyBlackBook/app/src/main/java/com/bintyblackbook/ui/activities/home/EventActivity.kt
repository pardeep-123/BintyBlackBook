package com.bintyblackbook.ui.activities.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.EventAdapter
import com.bintyblackbook.models.EventsModel
import com.bintyblackbook.models.PhotosModel
import com.bintyblackbook.util.AppConstant
import kotlinx.android.synthetic.main.activity_event.*
import kotlinx.android.synthetic.main.toolbar.*

class EventActivity : AppCompatActivity() {

    var eventAdapter:EventAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        rvEvents.layoutManager = GridLayoutManager(this,2)

        rlBack.setOnClickListener {
            finish()
        }

        init()
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

        eventAdapter = EventAdapter(this,arrayList)
        rvEvents.adapter = eventAdapter
        adapterItemClick()
    }

    private fun adapterItemClick(){
        eventAdapter?.onItemClick = {eventsModel: EventsModel ->
            val intent = Intent(this,EventDetailActivity::class.java)
            intent.putExtra(AppConstant.HEADING,eventsModel.name)
            startActivity(intent)
        }
    }
}