package com.bintyblackbook.ui.activities.home.settings

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.adapters.AdapterBlockedContacts
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.BlockedData
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.BlockUserViewModel
import kotlinx.android.synthetic.main.activity_blocked_contacts.*
import kotlinx.android.synthetic.main.toolbar.*

class BlockedContactsActivity : BaseActivity(), View.OnClickListener {

    lateinit var blockUserViewModel: BlockUserViewModel
    var arrayList= ArrayList<BlockedData>()
    var adapterBlockedContacts:AdapterBlockedContacts?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blocked_contacts)

        blockUserViewModel= BlockUserViewModel()
        headingText.text = "Blocked Contacts"
        iv_back.setOnClickListener(this)
        setAdapter()
        getBlockUserList()
    }

    private fun getBlockUserList() {
        blockUserViewModel.getBlockUserList(this,getSecurityKey(context)!!, getUser(context)?.authKey!!)
        blockUserViewModel.blockListLiveData.observe(this, Observer {

            if(it.data.size==0){
                tvNoBlockUser.visibility=View.VISIBLE
                rvBlockedContacts.visibility=View.GONE
            }
            else{
                tvNoBlockUser.visibility=View.GONE
                rvBlockedContacts.visibility=View.VISIBLE
                arrayList.addAll(it.data)
                adapterBlockedContacts?.notifyDataSetChanged()
            }
        })
    }

    private fun setAdapter() {
        rvBlockedContacts.layoutManager=LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        adapterBlockedContacts= AdapterBlockedContacts(context)
        rvBlockedContacts.adapter = adapterBlockedContacts
        adapterBlockedContacts?.arrayList=arrayList
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_back -> {
                finish()
            }
        }
    }
}