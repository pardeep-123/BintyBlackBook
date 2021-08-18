package com.bintyblackbook.ui.activities.home.loop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.adapters.AdapterMyLoops
import com.bintyblackbook.adapters.SuggestionsAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.AllData
import com.bintyblackbook.model.Suggested
import com.bintyblackbook.ui.activities.home.UserDetailActivity
import com.bintyblackbook.ui.dialogues.UnLoopDialogFragment
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.LoopsViewModel
import kotlinx.android.synthetic.main.activity_my_loops.*
import kotlinx.android.synthetic.main.toolbar.*

class MyLoopsActivity : BaseActivity(), View.OnClickListener, AdapterMyLoops.LoopsInterface {

    lateinit var loopsViewModel: LoopsViewModel
    var myLoopsAdapter:AdapterMyLoops?=null
    var suggestionAdapter:SuggestionsAdapter?=null

    var loopList = ArrayList<AllData>()
    var suggestList=ArrayList<Suggested>()

    var unloop_id=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_loops)

        setAdapter()
        setSuggestionAdapter()
        setOnClicks()
        loopsViewModel= LoopsViewModel(this)
        headingText.setText("MY LOOPS")

        getLoopsList()
    }

    private fun setSuggestionAdapter() {
        rvSuggestions.layoutManager=LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
        suggestionAdapter= SuggestionsAdapter(this,suggestList)
        rvSuggestions.adapter=suggestionAdapter

    }

    private fun getLoopsList() {
        loopsViewModel.loopsList(getSecurityKey(this)!!, getUser(this)?.authKey!!)
        loopsViewModel.loopsLiveData.observe(this, Observer {

            if(it.code==200){
                if(it.data.allData.size==0){
                    rvMyLoops.visibility=View.GONE
                    tvNoLoops.visibility=View.VISIBLE
                }
                else{
                    rvMyLoops.visibility=View.VISIBLE
                    tvNoLoops.visibility=View.GONE
                    loopList.addAll(it.data.allData)
                    myLoopsAdapter?.notifyDataSetChanged()
                }

                suggestList.addAll(it.data.suggested)
                suggestionAdapter?.notifyDataSetChanged()

            }
        })
    }

    private fun setOnClicks() {
        iv_back.setOnClickListener(this)
    }

    private fun setAdapter() {
        rvMyLoops.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)

        myLoopsAdapter = AdapterMyLoops(this)
        rvMyLoops.adapter = myLoopsAdapter
        myLoopsAdapter?.loopsInterface=this
        myLoopsAdapter?.list=loopList
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_back -> {
                finish()
            }
        }
    }

    override fun onItemClick(data: AllData, position: Int) {
        val intent= Intent(this,UserDetailActivity::class.java)
        intent.putExtra("user_id",data.user2_id)
        startActivity(intent)
    }

    override fun unLoop(data: AllData, position: Int) {
        unloop_id=data.id.toString()
        val fragmentDialog = UnLoopDialogFragment(this)
        fragmentDialog.show(this.supportFragmentManager,"LoopDialog")
    }

    fun unLoopRequest(){
        loopsViewModel.unLoop(getSecurityKey(context)!!, getUser(context)?.authKey!!,unloop_id)
        loopsViewModel.unLoopLiveData.observe(this, Observer {
            if(it.code==200){
                showAlertWithOk(it.msg)
            }
            else{
                Log.i("TAG",it?.msg.toString())
            }
        })
    }
}