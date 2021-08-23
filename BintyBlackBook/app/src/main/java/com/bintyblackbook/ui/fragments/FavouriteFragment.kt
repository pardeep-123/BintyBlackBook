package com.bintyblackbook.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.EventAdapter
import com.bintyblackbook.adapters.FavouriteEventAdapter
import com.bintyblackbook.model.FavEventData
import com.bintyblackbook.models.EventsModel
import com.bintyblackbook.ui.activities.home.EventDetailActivity
import com.bintyblackbook.util.AppConstant
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.EventsViewModel
import kotlinx.android.synthetic.main.fragment_favourite.*


class FavouriteFragment : Fragment() {

    var eventAdapter: FavouriteEventAdapter? = null
    lateinit var eventsViewModel:EventsViewModel
    var arrayList= ArrayList<FavEventData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eventsViewModel= EventsViewModel()

        init()
        getEventList()
    }

    /*override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        getEventList()
    }*/

    private fun getEventList() {
        eventsViewModel.favEvents(requireContext(),getSecurityKey(requireContext())!!, getUser(requireContext())?.authKey!!)
        eventsViewModel.favEventsLiveData.observe(requireActivity(), Observer {

            if(it?.data?.size!=0){
                tvNoFavData.visibility=View.GONE
                rvFavorites.visibility=View.VISIBLE
                arrayList.clear()
                arrayList.addAll(it.data)
                eventAdapter?.notifyDataSetChanged()
            }else{
                tvNoFavData.visibility=View.VISIBLE
                rvFavorites.visibility=View.GONE
            }
        })
    }

    private fun init() {
        eventAdapter = FavouriteEventAdapter(requireActivity(), arrayList)
        rvFavorites.adapter = eventAdapter
        rvFavorites.layoutManager = GridLayoutManager(activity, 2)
        adapterItemClick()
    }

    private fun adapterItemClick() {
        eventAdapter?.onItemClick = { eventsModel: FavEventData ->
            val intent = Intent(activity, EventDetailActivity::class.java)
            intent.putExtra(AppConstant.HEADING, eventsModel.name)
            startActivity(intent)
        }
    }
}