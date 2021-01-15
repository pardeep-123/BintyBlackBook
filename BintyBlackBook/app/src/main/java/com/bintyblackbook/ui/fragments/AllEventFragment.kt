package com.bintyblackbook.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.EventAdapter
import com.bintyblackbook.models.EventsModel
import com.bintyblackbook.ui.activities.home.EventDetailActivity
import com.bintyblackbook.util.AppConstant
import kotlinx.android.synthetic.main.fragment_all_event.*


class AllEventFragment : Fragment() {

    var eventAdapter: EventAdapter? = null
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        eventAdapter = EventAdapter(activity!!,arrayList)
        rvAllEvents.adapter = eventAdapter
        rvAllEvents.layoutManager = GridLayoutManager(activity,2)
        adapterItemClick()
    }

    private fun adapterItemClick(){
        eventAdapter?.onItemClick = {eventsModel: EventsModel ->
            val intent = Intent(activity, EventDetailActivity::class.java)
            intent.putExtra(AppConstant.HEADING,eventsModel.name)
            startActivity(intent)
        }
    }
}