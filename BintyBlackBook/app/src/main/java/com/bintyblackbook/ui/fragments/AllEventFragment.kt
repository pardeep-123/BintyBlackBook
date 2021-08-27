package com.bintyblackbook.ui.fragments

import android.content.Intent
import android.os.Bundle
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
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.EventsViewModel
import kotlinx.android.synthetic.main.fragment_all_event.*


class AllEventFragment : Fragment() {

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

  /*  override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser)
        getAllEvents()
    }
*/
    private fun getAllEvents() {
        eventsViewModel.getOtherUserEvents(requireContext(),getSecurityKey(requireContext())!!, getUser(requireContext())?.authKey!!)

        eventsViewModel.eventsLiveData.observe(requireActivity(), Observer {
            if (it.code==200){

                (context as EventCalenderActivity).dismissProgressDialog()
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
        eventAdapter = EventAdapter(requireContext())
        rvAllEvents.adapter = eventAdapter
        eventAdapter?.arrayList=arrayList
        adapterItemClick()
    }

    private fun adapterItemClick(){
        eventAdapter?.onItemClick = {eventsModel: EventData ->
            val intent = Intent(activity, EventDetailActivity::class.java)
            intent.putExtra(AppConstant.HEADING,eventsModel.name)
            intent.putExtra("post_id",eventsModel.id.toString())
            startActivity(intent)
        }
    }

    fun onSelectFavourite(){
        eventAdapter?.onSelectFav = {eventsModel: EventData ->
         //   eventsViewModel.likeEvent(getSecurityKey(context!!)!!, getUser(context!!)?.authKey)
        }
    }
}