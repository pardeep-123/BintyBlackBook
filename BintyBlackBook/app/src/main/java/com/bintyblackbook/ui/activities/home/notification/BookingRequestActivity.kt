
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
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.LoopRequestViewModel
import com.bintyblackbook.viewmodel.LoopsViewModel
import kotlinx.android.synthetic.main.activity_booking_request.*
import kotlinx.android.synthetic.main.activity_booking_request.rlBack

class BookingRequestActivity : BaseActivity(), BookingRequestAdapter.LoopRequestInterface {

    var bookingRequestAdapter:BookingRequestAdapter? = null
    var loopList= ArrayList<LoopRequestData>()
    var userId=""
    lateinit var loopsViewModel: LoopsViewModel
    lateinit var loopRequestViewModel: LoopRequestViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_request)

        loopsViewModel= LoopsViewModel()
        loopRequestViewModel= LoopRequestViewModel()
        rlBack.setOnClickListener {
           finish()
        }

        setAdapter()
        getLoopRequest()
    }

    private fun getLoopRequest() {
        loopRequestViewModel.getLoopRequest(this, getSecurityKey(context)!!, getUser(context)?.authKey!!)
        loopRequestViewModel.loopReqLiveData.observe(this, Observer {
            if(it.data.size==0){
                rvBookingRequest.visibility= View.GONE
                tvNoRequest.visibility= View.VISIBLE
            }else {
                rvBookingRequest.visibility= View.VISIBLE
                tvNoRequest.visibility= View.GONE
                loopList.clear()
                loopList.addAll(it?.data!!)
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

    override fun onItemClick(status: String, data: LoopRequestData,position: Int) {
        userId= data.user2Id.toString()

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
        loopsViewModel.acceptRejectRequest(this,getSecurityKey(this)!!, getUser(this)?.authKey!!,userId,status)
        loopsViewModel.baseLiveData.observe(this, Observer {

            if(it.code==200){
               loopList.removeAt(position)
                bookingRequestAdapter?.notifyDataSetChanged()
            }
        })
    }

}