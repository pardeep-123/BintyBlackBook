package com.bintyblackbook.ui.activities.home.notification

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.adapters.LoopRequestAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.LoopRequestData
import com.bintyblackbook.ui.dialogues.CancelDialogFragment
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.LoopRequestViewModel
import com.bintyblackbook.viewmodel.LoopsViewModel
import kotlinx.android.synthetic.main.activity_loop_request.*

class LoopRequestActivity : BaseActivity(), LoopRequestAdapter.LoopRequestInterface {

    var loopRequestAdapter:LoopRequestAdapter? = null

    lateinit var loopsViewModel: LoopsViewModel
    lateinit var loopRequestViewModel: LoopRequestViewModel

    var msg =""
    var userId=""
    var loopList= ArrayList<LoopRequestData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loop_request)

        loopsViewModel= LoopsViewModel()
        loopRequestViewModel= LoopRequestViewModel()

        getIntentData()
        setAdapter()
        getLoopRequest()

        rlBack.setOnClickListener {
          finish()
        }
    }

    private fun getLoopRequest() {
        loopRequestViewModel.getLoopRequest(this, getSecurityKey(context)!!, getUser(context)?.authKey!!)
        loopRequestViewModel.loopReqLiveData.observe(this, Observer {
            if(it.data.size==0){
                rvLoopRequest.visibility=View.GONE
                noReqFound.visibility=View.VISIBLE
            }
            else {
                rvLoopRequest.visibility=View.VISIBLE
                noReqFound.visibility=View.GONE
                loopList.clear()
                loopList.addAll(it?.data!!)
                loopRequestAdapter?.notifyDataSetChanged()
            }
        })
    }

    private fun getIntentData() {
        msg= intent.getStringExtra("message").toString()
        userId= intent.getStringExtra("user_id").toString()

    }

    private fun setAdapter() {
        rvLoopRequest.layoutManager= LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        loopRequestAdapter = LoopRequestAdapter(this)
        rvLoopRequest.adapter = loopRequestAdapter
        loopRequestAdapter?.loopRequestInterface=this
        loopRequestAdapter?.loopList= loopList
       // adapterItemBtnClick()
    }

     fun acceptRejectRequest(status: String) {

    }

    override fun onItemClick(status: String, data: LoopRequestData,position:Int) {
        userId= data.otherUserId.toString()
        loopsViewModel.acceptRejectRequest(this,getSecurityKey(this)!!, getUser(this)?.authKey!!,userId,status)
        loopsViewModel.baseLiveData.observe(this, Observer {
            loopList.removeAt(position)
            loopRequestAdapter?.notifyDataSetChanged()
            if(loopList.size==0){
                rvLoopRequest.visibility=View.GONE
                noReqFound.visibility=View.VISIBLE
            }

        })



       /* if (status == "2"){
            val dialog  = CancelDialogFragment(this,"acceptLoopRequest")
            dialog.show(supportFragmentManager,"acceptLoopRequest")
        }else if (status == "0"){
            val dialog= CancelDialogFragment(this,"cancelLoopRequest")
            dialog.show(supportFragmentManager,"cancelLoopRequest")
        }*/
    }
}