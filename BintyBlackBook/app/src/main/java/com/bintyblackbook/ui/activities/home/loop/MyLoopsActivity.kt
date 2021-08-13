package com.bintyblackbook.ui.activities.home.loop

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.AdapterMyLoops
import com.bintyblackbook.adapters.HomeAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.Suggested
import com.bintyblackbook.ui.activities.home.UserDetailActivity
import com.bintyblackbook.ui.dialogues.UnLoopDialogFragment
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.LoopsViewModel
import kotlinx.android.synthetic.main.activity_my_loops.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar.*

class MyLoopsActivity : BaseActivity(), View.OnClickListener, AdapterMyLoops.LoopsInterface {

    lateinit var loopsViewModel: LoopsViewModel
    var myLoopsAdapter:AdapterMyLoops?=null

    var loopList=ArrayList<Suggested>()

    var unloop_id=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_loops)

        setAdapter()
        setOnClicks()
        loopsViewModel= LoopsViewModel(this)
        headingText.setText("MY LOOPS")

        getLoopsList()
    }

    private fun getLoopsList() {
        loopsViewModel.loopsList(getSecurityKey(this)!!, getUser(this)?.authKey!!)
        loopsViewModel.loopsLiveData.observe(this, Observer {

            if(it.code==200){
                loopList.addAll(it.data.suggested)
                myLoopsAdapter?.notifyDataSetChanged()
            }
        })
    }

    private fun setOnClicks() {
        iv_back.setOnClickListener(this)
    }

    private fun setAdapter() {
        rvMyLoops.layoutManager = LinearLayoutManager(this)

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

    override fun onItemClick(data: Suggested, position: Int) {
        startActivity(Intent(context, UserDetailActivity::class.java))
    }

    override fun unLoop(data: Suggested, position: Int) {
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