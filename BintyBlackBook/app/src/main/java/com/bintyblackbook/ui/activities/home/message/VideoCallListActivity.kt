package com.bintyblackbook.ui.activities.home.message

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.bintyblackbook.R
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.AllData
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.LoopsViewModel
import kotlinx.android.synthetic.main.activity_edit_message.*
import kotlinx.android.synthetic.main.activity_video_call_list.*

class VideoCallListActivity : BaseActivity() {

    lateinit var loopsViewModel: LoopsViewModel
    var videoCallListAdapter:VideoCallListAdapter?=null
    val videoList= ArrayList<AllData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_call_list)

        loopsViewModel= LoopsViewModel()

        setAdapter()
        getLoopList()
    }

    private fun getLoopList() {
        loopsViewModel.loopsList(this, getSecurityKey(this)!!, getUser(this)?.authKey!!)
        loopsViewModel.loopsLiveData.observe(this, Observer {

            if (it.code == 200) {
                if (it.data.allData.size == 0) {
                    rvVideoCall.visibility = View.GONE
                    // tvNoLoops.visibility=View.VISIBLE
                } else {
                    rvVideoCall.visibility = View.VISIBLE
                    //tvNoLoops.visibility=View.GONE
                    videoList.clear()
                    videoList.addAll(it.data.allData)
                    videoCallListAdapter?.notifyDataSetChanged()
                }
            }
        })
    }

    private fun setAdapter() {
        videoCallListAdapter = VideoCallListAdapter(this)
        rvVideoCall.adapter = videoCallListAdapter
        videoCallListAdapter?.arrayList=videoList
        //adapterItemClick()
    }
}