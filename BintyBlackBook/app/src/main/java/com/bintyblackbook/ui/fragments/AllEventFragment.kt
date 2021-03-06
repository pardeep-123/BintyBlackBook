package com.bintyblackbook.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.EventAdapter
import com.bintyblackbook.model.EventData
import com.bintyblackbook.ui.activities.home.EventDetailActivity
import com.bintyblackbook.ui.activities.home.eventCalender.EventCalenderActivity
import com.bintyblackbook.util.AppConstant
import com.bintyblackbook.util.CustomProgressDialog
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.EventsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_all_event.*


class AllEventFragment : Fragment(), EventAdapter.EventAdapterInterface {

    lateinit var eventsViewModel: EventsViewModel

    var eventAdapter: EventAdapter? = null
    var arrayList= ArrayList<EventData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)

        eventsViewModel= EventsViewModel()
        init()
        getAllEvents()
    }

    private fun getAllEvents() {
        eventsViewModel.getOtherUserEvents(requireContext(),getSecurityKey(requireContext())!!, getUser(requireContext())?.authKey!!)

        eventsViewModel.eventsLiveData.observe(requireActivity(), Observer {
            if (it.code==200){

                if(it.data.size!=0){
                    tvNoEvent.visibility=View.GONE
                    rvAllEvents.visibility=View.VISIBLE
                    arrayList.clear()
                    arrayList.addAll(it.data)
                    eventAdapter?.notifyDataSetChanged()
                }else{
                    tvNoEvent.visibility=View.VISIBLE
                    rvAllEvents.visibility=View.GONE
                }
            }
        })
    }

    private fun init(){

        rvAllEvents.layoutManager = GridLayoutManager(activity,2)
        eventAdapter = EventAdapter(requireContext(),arrayList)
        rvAllEvents.adapter = eventAdapter
        eventAdapter?.eventAdapterInterface=this
        eventAdapter?.arrayList=arrayList

    }

    override fun onSelectFav(data: EventData, status: String) {
        eventsViewModel.likeEvent(requireContext(), getSecurityKey(requireContext())!!, getUser(requireContext())?.authKey!!,data.id.toString(),status)
        eventsViewModel.baseEventsLiveData.observe(this, Observer {

            Log.i("TAG",it.msg)
        })
    }

    override fun onItemClick(data: EventData) {
        val intent = Intent(activity, EventDetailActivity::class.java)
        intent.putExtra(AppConstant.HEADING,data.name)
        intent.putExtra("user_id",data.userId)
        intent.putExtra("image",data.image)
        intent.putExtra("location",data.location)
        intent.putExtra("date",data.date)
        intent.putExtra("desc",data.description)
        intent.putExtra("web_link",data.rsvpLink)
        intent.putExtra("is_favourite",data.isFavourite.toString())
        startActivity(intent)
    }

}