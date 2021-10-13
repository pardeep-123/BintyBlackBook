package com.bintyblackbook.ui.activities.home.eventCalender

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.EventAdapter
import com.bintyblackbook.adapters.FavouriteEventAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.EventData
import com.bintyblackbook.model.FavEventData
import com.bintyblackbook.ui.activities.home.EventDetailActivity
import com.bintyblackbook.util.AppConstant
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.EventsViewModel
import kotlinx.android.synthetic.main.activity_event_calender.*


class EventCalenderActivity : BaseActivity(), TextWatcher, EventAdapter.EventAdapterInterface, FavouriteEventAdapter.FavEventInteface {
    var fragList: ArrayList<String> = ArrayList()

    var currentList="AllEvent"

    lateinit var eventsViewModel: EventsViewModel

    var eventAdapter: EventAdapter? = null
    var arrayList= ArrayList<EventData>()
    var filteredList= ArrayList<EventData>()

    var favEventAdapter: FavouriteEventAdapter? = null

    var favEventList= ArrayList<FavEventData>()
    var favFilteredlist= ArrayList<FavEventData>()

    var launchSomeActivity:ActivityResultLauncher<Intent>?=null
    var selectedDate=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_calender)
        eventsViewModel= EventsViewModel()
        setOnClicks()

        initAdapter()
        initFavEventAdapter()
        getAllEvents()

       launchSomeActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                selectedDate = data?.getStringExtra("selectedDate")!!

                favFilteredlist= favEventList.filter {
                    it.date.toString()==selectedDate
                } as ArrayList<FavEventData>


                 filteredList= arrayList.filter {
                     it.date.toString() == selectedDate
                 } as ArrayList<EventData>

                Log.i("selected_date",filteredList.toString())
            }
        }
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
            if (it.code == 200) {

                if (it.data.size != 0) {
                    tvNoEventData.visibility = View.GONE
                    rvEvents.visibility = View.VISIBLE
                    arrayList.clear()
                    arrayList.addAll(it.data)
                    eventAdapter?.notifyDataSetChanged()
                } else {
                    tvNoEventData.visibility = View.VISIBLE
                    rvEvents.visibility = View.GONE
                }
            }
        })
    }

    private fun initAdapter() {
            rvEvents.layoutManager = GridLayoutManager(this, 2)
            eventAdapter = EventAdapter(this, arrayList)
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
            selectedDate=""
            currentList="AllEvent"
            tvFavEvents.setTextColor(ContextCompat.getColor(context, R.color.whiteColor))
            viewFavEvent.setBackgroundColor(ContextCompat.getColor(context, R.color.themeColor))
            tvAllEvent.setTextColor(ContextCompat.getColor(context, R.color.fadePink))
            viewAllEvent.setBackgroundColor(ContextCompat.getColor(context, R.color.fadePink))
            rvFavEvents.visibility=View.GONE
            rvEvents.visibility=View.VISIBLE
            if(selectedDate.equals("")){
                getAllEvents()
            }else{
                arrayList.addAll(filteredList)
                eventAdapter?.notifyDataSetChanged()
            }
        }

        tvFavEvents.setOnClickListener {
            selectedDate=""
            currentList="FavEvent"
            tvFavEvents.setTextColor(ContextCompat.getColor(context, R.color.fadePink))
            viewFavEvent.setBackgroundColor(ContextCompat.getColor(context, R.color.fadePink))
            tvAllEvent.setTextColor(ContextCompat.getColor(context, R.color.whiteColor))
            viewAllEvent.setBackgroundColor(ContextCompat.getColor(context, R.color.themeColor))

            rvFavEvents.visibility=View.VISIBLE
            rvEvents.visibility=View.GONE
            if(selectedDate.equals("")){
                getEventList()
            }else{
                favEventList.addAll(favFilteredlist)
                favEventAdapter?.notifyDataSetChanged()
            }

        }

        rlBack.setOnClickListener {
            finish()
        }

        rlCalender.setOnClickListener {
            launchSomeActivity?.launch(Intent(this, CalenderActivity::class.java))

        }
    }

    private fun getEventList() {
        eventsViewModel.favEvents(this, getSecurityKey(this)!!, getUser(this)?.authKey!!)
        eventsViewModel.favEventsLiveData.observe(this, Observer {
            if (it?.data?.size != 0) {
                tvNoEventData.visibility = View.GONE
                rvFavEvents.visibility = View.VISIBLE
                favEventList.clear()
                favEventList.addAll(it.data)
                favEventAdapter?.notifyDataSetChanged()

            } else {
                tvNoEventData.visibility = View.VISIBLE
                rvFavEvents.visibility = View.GONE
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
        eventsViewModel.likeEvent(
            this,
            getSecurityKey(this)!!,
            getUser(context)?.authKey!!,
            data.id.toString(),
            status
        )
        eventsViewModel.baseEventsLiveData.observe(this, Observer {

            Log.i("TAG", it.msg)
        })
    }

    override fun onItemClick(data: EventData) {
        val intent = Intent(this, EventDetailActivity::class.java)
        intent.putExtra(AppConstant.HEADING,data.name)
        intent.putExtra("user_id",data.userId)
        intent.putExtra("image",data.image)
        intent.putExtra("location",data.location)
        intent.putExtra("time",data.time.toString())
        intent.putExtra("date",data.date.toString())
        intent.putExtra("desc",data.description)
        intent.putExtra("web_link",data.rsvpLink)
        intent.putExtra("is_favourite",data.isFavourite.toString())
        startActivity(intent)
    }

    override fun onUnFavEvent(status: String, data: FavEventData, position: Int) {
        eventsViewModel.likeEvent(
            this,
            getSecurityKey(this)!!,
            getUser(context)?.authKey!!,
            data.eventId.toString(),
            status
        )
        eventsViewModel.baseEventsLiveData.observe(this, Observer {
            favEventList.removeAt(position)
            favEventAdapter?.notifyDataSetChanged()
        })

    }

    override fun onFavItemClick(data: FavEventData) {
        val intent = Intent(this, EventDetailActivity::class.java)
        intent.putExtra(AppConstant.HEADING,data.name)
        intent.putExtra("user_id",data.userId)
        intent.putExtra("image",data.image)
        intent.putExtra("location",data.location)
        intent.putExtra("time",data.time.toString())
        intent.putExtra("date",data.date.toString())
        intent.putExtra("desc",data.description)
        intent.putExtra("web_link",data.rsvpLink)
        intent.putExtra("is_favourite","1")
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        if(!selectedDate.equals("")){
            arrayList.clear()
            arrayList.addAll(filteredList)
            if(arrayList.size==0){
                rvEvents.visibility=View.GONE
                tvNoEventData.visibility=View.VISIBLE

            }else{
                rvEvents.visibility=View.VISIBLE
                tvNoEventData.visibility=View.GONE
                eventAdapter?.notifyDataSetChanged()
            }
        }
    }
}