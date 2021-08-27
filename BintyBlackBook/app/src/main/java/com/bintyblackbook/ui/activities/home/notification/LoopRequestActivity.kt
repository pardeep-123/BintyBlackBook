package com.bintyblackbook.ui.activities.home.notification

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.adapters.LoopRequestAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.LoopRequestData
import com.bintyblackbook.ui.activities.home.UserDetailActivity
import com.bintyblackbook.ui.dialogues.CancelDialogFragment
import com.bintyblackbook.util.AppConstant
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.util.showAlert
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

        loopsViewModel= LoopsViewModel(this)
        loopRequestViewModel= LoopRequestViewModel()

        getIntentData()
        setAdapter()
        getLoopRequest()
        rlBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getLoopRequest() {
        loopRequestViewModel.getLoopRequest(this, getSecurityKey(context)!!, getUser(context)?.authKey!!)
        loopRequestViewModel.loopReqLiveData.observe(this, Observer {
            Log.i("TAG",it.msg)
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

   /* private fun adapterItemBtnClick(){
        loopRequestAdapter?.onItemBtnClick = {clickOn ->
            if (clickOn == "2"){

                val dialog  = CancelDialogFragment(this,"acceptLoopRequest")
                dialog.show(supportFragmentManager,"acceptLoopRequest")
               *//* val intent = Intent(this, UserDetailActivity::class.java)
                intent.putExtra(AppConstant.SHOW_CHAT_AND_UN_LOOP_BTN,true)
                startActivity(intent)*//*
            }else if (clickOn == "0"){
                val dialog  = CancelDialogFragment(this,"cancelLoopRequest")
                dialog.show(supportFragmentManager,"cancelLoopRequest")
            }
        }
    }*/

     fun acceptRejectRequest(status: String) {
        loopsViewModel.acceptRejectRequest(getSecurityKey(this)!!, getUser(this)?.authKey!!,userId,status)
        loopsViewModel.baseLiveData.observe(this, Observer {

            if(it.code==200){
                showToast(it.msg)
            }

        })
    }

    override fun onItemClick(status: String, data: LoopRequestData) {
        userId= data.otherUserId.toString()
        if (status == "2"){
            val dialog  = CancelDialogFragment(this,"acceptLoopRequest")
            dialog.show(supportFragmentManager,"acceptLoopRequest")
        }else if (status == "0"){
            val dialog= CancelDialogFragment(this,"cancelLoopRequest")
            dialog.show(supportFragmentManager,"cancelLoopRequest")
        }
    }
}