
package com.bintyblackbook.ui.activities.home.notification

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.adapters.BookingRequestAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.LoopRequestData
import com.bintyblackbook.model.UpcomingBookings
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.BookingsViewModel
import com.bintyblackbook.viewmodel.LoopRequestViewModel
import com.bintyblackbook.viewmodel.LoopsViewModel
import kotlinx.android.synthetic.main.activity_booking_request.*
import kotlinx.android.synthetic.main.activity_booking_request.rlBack

class BookingRequestActivity : BaseActivity(), BookingRequestAdapter.LoopRequestInterface {

    var bookingRequestAdapter:BookingRequestAdapter? = null
    var loopList= ArrayList<UpcomingBookings>()
    var userId=""

    lateinit var bookingsViewModel: BookingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_request)

        bookingsViewModel= BookingsViewModel()
        rlBack.setOnClickListener {
           finish()
        }

        setAdapter()
        getLoopRequest()
    }

    private fun getLoopRequest() {
        bookingsViewModel.getAllBookings(this, getSecurityKey(context)!!, getUser(context)?.authKey!!)
        bookingsViewModel.bookingsLiveData.observe(this, Observer {
            if(it.data?.upcomingBookings?.size==0){
                rvBookingRequest.visibility= View.GONE
                tvNoRequest.visibility= View.VISIBLE
            }else {
                rvBookingRequest.visibility= View.VISIBLE
                tvNoRequest.visibility= View.GONE
                loopList.clear()
                loopList.addAll(it?.data?.upcomingBookings!!)
                bookingRequestAdapter?.notifyDataSetChanged()
            }
        })
    }

    private fun setAdapter() {
        rvBookingRequest.layoutManager= LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        bookingRequestAdapter = BookingRequestAdapter(this)
        rvBookingRequest.adapter = bookingRequestAdapter
        bookingRequestAdapter?.loopRequestInterface=this
        bookingRequestAdapter?.loopList= loopList

    }

    override fun onItemClick(status: String, data: UpcomingBookings,position: Int) {
      //  userId= data.user2Id.toString()

        acceptRejectRequest(status,position,)
        /*if (status == "2"){
            val dialog  = AcceptRejectDialogFragment(this,"acceptLoopRequest")
            dialog.show(supportFragmentManager,"acceptLoopRequest")
        }else if (status == "0"){
            val dialog= AcceptRejectDialogFragment(this,"cancelLoopRequest")
            dialog.show(supportFragmentManager,"cancelLoopRequest")
        }*/
    }

    fun acceptRejectRequest(status: String,position: Int) {
       /* bookingsViewModel.acceptRejectRequest(this,getSecurityKey(this)!!, getUser(this)?.authKey!!,userId,status)
        bookingsViewModel.baseLiveData.observe(this, Observer {

            if(it.code==200){
               loopList.removeAt(position)
                bookingRequestAdapter?.notifyDataSetChanged()
            }
        })*/
    }

}