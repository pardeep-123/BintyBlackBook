package com.bintyblackbook.ui.activities.home.loop

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.adapters.SearchLoopsAdapter
import com.bintyblackbook.adapters.SearchRequestAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.AllUsersData
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.LoopsViewModel
import kotlinx.android.synthetic.main.activity_loop_search.*
import kotlinx.android.synthetic.main.toolbar.*

class LoopSearchActivity : BaseActivity(), SearchLoopsAdapter.SearchLoopInterface {

    lateinit var loopsViewModel: LoopsViewModel
    var arrayList= ArrayList<AllUsersData>()
    var searchLoopsAdapter: SearchLoopsAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loop_search)
        loopsViewModel= LoopsViewModel()
        headingText.text = "SEARCH"
        setLoopsAdapter()

        search_loop.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getUserList(search_loop.text.toString())
            }
            true
        }

        iv_back.setOnClickListener {
            finish()
        }
    }

    private fun setLoopsAdapter() {
        rvLoops.layoutManager= LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        searchLoopsAdapter= SearchLoopsAdapter(this)
        rvLoops.adapter= searchLoopsAdapter
        searchLoopsAdapter?.searchLoopInterface=this
        searchLoopsAdapter?.loopList= arrayList

    }


    private fun getUserList(s: String?) {
        loopsViewModel.getAllUsers(this, getSecurityKey(this)!!, getUser(this)?.authKey!!,s)
        loopsViewModel.allUsersLiveData.observe(this, Observer {

            if(it.data.size==0){
                tvNoSearchList.visibility=View.VISIBLE
                rvLoops.visibility=View.GONE
            }
            else {
                tvNoSearchList.visibility=View.GONE
                rvLoops.visibility=View.VISIBLE

                arrayList.clear()
                arrayList.addAll(it.data)
                searchLoopsAdapter?.notifyDataSetChanged()
            }
        })
    }

    override fun sendLoopRequest(data: AllUsersData,position: Int) {
        loopRequest(data.id.toString(),position)
    }


    override fun onUnLoopRequest(data: AllUsersData,position: Int) {
        unLoopRequest(data.id.toString(),position)
    }

    override fun acceptRejectRequest(data: AllUsersData, position: Int, status: String) {
        loopsViewModel.acceptRejectRequest(this, getSecurityKey(this)!!, getUser(this)?.authKey!!,data.id.toString(),status)
        loopsViewModel.baseLiveData.observe(this, Observer {

            if(status=="2"){
                arrayList[position].isLoop=2
                searchLoopsAdapter?.notifyDataSetChanged()
            }else if(status=="0"){
                arrayList[position].isLoop=0
                searchLoopsAdapter?.notifyDataSetChanged()
            }else{
                arrayList[position].isLoop=0
                searchLoopsAdapter?.notifyDataSetChanged()
            }

        })
    }

    fun loopRequest(user_id: String, position: Int){
        loopsViewModel.sendLoopReq(this, getSecurityKey(this)!!, getUser(this)?.authKey!!,user_id)
        loopsViewModel.baseLiveData.observe(this, Observer {

            if(it.code==200){
                arrayList[position].isLoop=1
                searchLoopsAdapter?.notifyDataSetChanged()
            }

        })
    }

    fun unLoopRequest(user_id: String, position: Int){
        loopsViewModel.unLoop(this, getSecurityKey(this)!!, getUser(this)?.authKey!!,user_id)
        loopsViewModel.baseLiveData.observe(this, Observer {

            arrayList[position].isLoop=0
            searchLoopsAdapter?.notifyDataSetChanged()

        })
    }

}