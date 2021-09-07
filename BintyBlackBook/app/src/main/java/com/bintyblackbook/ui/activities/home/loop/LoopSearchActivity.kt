package com.bintyblackbook.ui.activities.home.loop

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
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

class LoopSearchActivity : BaseActivity(), TextWatcher, SearchRequestAdapter.LoopRequestInterface,
    SearchLoopsAdapter.SearchLoopInterface {

    lateinit var loopsViewModel: LoopsViewModel
    var arrayList= ArrayList<AllUsersData>()
    var loopList= ArrayList<AllUsersData>()
    var searchRequestAdapter:SearchRequestAdapter?=null
    var searchLoopsAdapter: SearchLoopsAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loop_search)
        loopsViewModel= LoopsViewModel()
        headingText.text = "LOOP SEARCH"
        setLoopsAdapter()

        setFriendsAdapter()

        search_loop.addTextChangedListener(this)
        iv_back.setOnClickListener {
            finish()
        }
    }

    private fun setFriendsAdapter() {
        rvMyFriends.layoutManager= LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        searchRequestAdapter= SearchRequestAdapter(this)
        rvMyFriends.adapter= searchRequestAdapter
        searchRequestAdapter?.loopRequestInterface=this
        searchRequestAdapter?.loopList= arrayList
    }

    private fun setLoopsAdapter() {
        rvLoops.layoutManager= LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        searchLoopsAdapter= SearchLoopsAdapter(this)
        rvLoops.adapter= searchLoopsAdapter
        searchLoopsAdapter?.searchLoopInterface=this
        searchLoopsAdapter?.loopList= arrayList

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if(!s.isNullOrEmpty()){
            getUserList(s.toString())
        }
    }

    override fun afterTextChanged(s: Editable?) {

    }


    private fun getUserList(s: String?) {
        loopsViewModel.getAllUsers(this, getSecurityKey(this)!!, getUser(this)?.authKey!!,s)
        loopsViewModel.allUsersLiveData.observe(this, Observer {

            if(it.data.size==0){
                tvNoSearchList.visibility=View.VISIBLE
                rvLoops.visibility=View.GONE
                rvMyFriends.visibility=View.GONE

            }
            else {
                tvNoSearchList.visibility=View.GONE
                rvLoops.visibility=View.VISIBLE
                rvMyFriends.visibility=View.VISIBLE

                loopList.clear()
                loopList.addAll(it.data)
                searchLoopsAdapter?.notifyDataSetChanged()

                arrayList.clear()
                arrayList.addAll(it.data)
                searchRequestAdapter?.notifyDataSetChanged()
            }
        })
    }

    override fun sendLoopRequest(data: AllUsersData) {
        loopRequest(data.id.toString())
    }


    override fun onUnLoopRequest(data: AllUsersData) {
        unLoopRequest(data.id.toString())
    }

    override fun acceptRequest(status: String, data: AllUsersData) {
        accepCancelRequest(status,data)

    }

    override fun cancelRequest(status: String, data: AllUsersData) {
        accepCancelRequest(status, data)

    }
    private fun accepCancelRequest(status: String, data: AllUsersData) {
        loopsViewModel.acceptRejectRequest(this, getSecurityKey(this)!!, getUser(this)?.authKey!!,data.id.toString(),status)
        loopsViewModel.baseLiveData.observe(this, Observer {

            if(it.code==200){
                Log.i("===request",it.msg)
                searchLoopsAdapter?.notifyDataSetChanged()
                searchRequestAdapter?.notifyDataSetChanged()
            }

        })
    }

    fun loopRequest(user_id:String){
        loopsViewModel.sendLoopReq(this, getSecurityKey(this)!!, getUser(this)?.authKey!!,user_id)
        loopsViewModel.baseLiveData.observe(this, Observer {

            if(it.code==200){
                searchLoopsAdapter?.notifyDataSetChanged()
                searchRequestAdapter?.notifyDataSetChanged()
                Log.i("===loop",it.msg)
            }

        })
    }

    fun unLoopRequest(user_id:String){
        loopsViewModel.unLoop(this, getSecurityKey(this)!!, getUser(this)?.authKey!!,user_id)
        loopsViewModel.baseLiveData.observe(this, Observer {
            if(it.code==200){
                searchRequestAdapter?.notifyDataSetChanged()
                searchLoopsAdapter?.notifyDataSetChanged()
                Log.i("===loop",it.msg)
            }
        })
    }

}