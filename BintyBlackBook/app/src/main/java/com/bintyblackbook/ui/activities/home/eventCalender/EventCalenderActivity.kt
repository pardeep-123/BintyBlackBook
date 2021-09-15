package com.bintyblackbook.ui.activities.home.eventCalender

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.EventAdapter
import com.bintyblackbook.adapters.EventCalenderPagerAdapter
import com.bintyblackbook.adapters.FavouriteEventAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.EventData
import com.bintyblackbook.model.FavEventData
import com.bintyblackbook.ui.activities.home.EventDetailActivity
import com.bintyblackbook.ui.fragments.AllEventFragment
import com.bintyblackbook.ui.fragments.FavouriteFragment
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.EventsViewModel
import kotlinx.android.synthetic.main.activity_event_calender.*


class EventCalenderActivity : BaseActivity(), TextWatcher, EventAdapter.EventAdapterInterface,
    FavouriteEventAdapter.FavEventInteface {
    var fragList: ArrayList<String> = ArrayList()

    var currentList="AllEvent"

    lateinit var eventsViewModel: EventsViewModel

    var eventAdapter: EventAdapter? = null
    var arrayList= ArrayList<EventData>()

    var favEventAdapter: FavouriteEventAdapter? = null

    var favEventList= ArrayList<FavEventData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_calender)
        eventsViewModel= EventsViewModel()
        setOnClicks()

        initAdapter()
        initFavEventAdapter()
        getAllEvents()
        /*setTabLayout()

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = fragList[position]
        }.attach()*/
    }

    private fun initFavEventAdapter() {
        rvFavEvents.layoutManager = GridLayoutManager(this, 2)
        favEventAdapter = FavouriteEventAdapter(this, favEventList)
        rvFavEvents.adapter = favEventAdapter
        favEventAdapter?.favEventInterface=this
        favEventAdapter?.arrayList=favEventList

    }

    private fun getAllEvents() {
        eventsViewModel.getOtherUserEvents(this, getSecurityKey(this)!!, getUser(this)?.authKey!!)

        eventsViewModel.eventsLiveData.observe(this, Observer {
            if (it.code==200){

                if(it.data.size!=0){
                    tvNoEventData.visibility= View.GONE
                    rvEvents.visibility= View.VISIBLE
                    arrayList.clear()
                    arrayList.addAll(it.data)
                    eventAdapter?.notifyDataSetChanged()
                }else{
                    tvNoEventData.visibility= View.VISIBLE
                    rvEvents.visibility= View.GONE
                }
            }
        })
    }

    private fun initAdapter() {
            rvEvents.layoutManager = GridLayoutManager(this,2)
            eventAdapter = EventAdapter(this,arrayList)
            rvEvents.adapter = eventAdapter
            eventAdapter?.eventAdapterInterface=this
            eventAdapter?.arrayList=arrayList
          //  adapterItemClick()

    }

   /* private fun setTabLayout() {
        val adapter = EventCalenderPagerAdapter(supportFragmentManager, this)
        fragList.clear()
        fragList.add("All Events")
        fragList.add("Favourite")
        adapter.addFragment(AllEventFragment(),"All Events")
        adapter.addFragment(FavouriteFragment(),"Favourite")
        viewPager.adapter = adapter
    }*/

    private fun setOnClicks() {

        edtSearchEvent.addTextChangedListener(this)
        tvAllEvent.setOnClickListener {
            currentList="AllEvent"
            tvFavEvents.setTextColor(ContextCompat.getColor(context, R.color.whiteColor))
            viewFavEvent.setBackgroundColor(ContextCompat.getColor(context,R.color.themeColor))
            tvAllEvent.setTextColor(ContextCompat.getColor(context, R.color.fadePink))
            viewAllEvent.setBackgroundColor(ContextCompat.getColor(context,R.color.fadePink))
            rvFavEvents.visibility=View.GONE
            rvEvents.visibility=View.VISIBLE
            getAllEvents()
        }

        tvFavEvents.setOnClickListener {
            currentList="FavEvent"
            tvFavEvents.setTextColor(ContextCompat.getColor(context, R.color.fadePink))
            viewFavEvent.setBackgroundColor(ContextCompat.getColor(context,R.color.fadePink))
            tvAllEvent.setTextColor(ContextCompat.getColor(context, R.color.whiteColor))
            viewAllEvent.setBackgroundColor(ContextCompat.getColor(context,R.color.themeColor))

            rvFavEvents.visibility=View.VISIBLE
            rvEvents.visibility=View.GONE
            getEventList()
        }

        rlBack.setOnClickListener {
            finish()
        }

        rlCalender.setOnClickListener {
            startActivity(Intent(this,CalenderActivity::class.java))
        }
    }

    private fun getEventList() {
        eventsViewModel.favEvents(this,getSecurityKey(this)!!, getUser(this)?.authKey!!)
        eventsViewModel.favEventsLiveData.observe(this, Observer {

            if(it?.data?.size!=0){
                tvNoEventData.visibility=View.GONE
                rvFavEvents.visibility=View.VISIBLE
                favEventList.clear()
                favEventList.addAll(it.data)
                favEventAdapter?.notifyDataSetChanged()

            }else{
                tvNoEventData.visibility=View.VISIBLE
                rvFavEvents.visibility=View.GONE
            }
        })
    }

    override fun onPause() {
        if (mProgress != null && mProgress?.isShowing!!) {
            mProgress?.dismiss()
        }
        super.onPause()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if(currentList == "AllEvent"){
            eventAdapter?.filter?.filter(s.toString().trim())
        } else{
         favEventAdapter?.filter?.filter(s.toString().trim())
        }
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun onSelectFav(data: EventData, status: String) {
        eventsViewModel.likeEvent(this, getSecurityKey(this)!!, getUser(context)?.authKey!!,data.id.toString(),status)
        eventsViewModel.baseEventsLiveData.observe(this, Observer {

            Log.i("TAG",it.msg)
        })
    }

    override fun onItemClick(data: EventData) {
//        val intent= Intent(this,EventDetailActivity::class.java)
//        startActivity(intent)
    }

    override fun onUnFavEvent(status: String, data: FavEventData,position:Int) {
        eventsViewModel.likeEvent(this, getSecurityKey(this)!!, getUser(context)?.authKey!!,data.id.toString(),status)
        eventsViewModel.baseEventsLiveData.observe(this, Observer {
            favEventList.removeAt(position)
            favEventAdapter?.notifyDataSetChanged()
        })

    }
}