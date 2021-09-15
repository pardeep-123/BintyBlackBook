package com.bintyblackbook.ui.activities.home.settings

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.BintyBookApplication
import com.bintyblackbook.R
import com.bintyblackbook.adapters.AdapterBlockedContacts
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.BlockedData
import com.bintyblackbook.socket.SocketManager
import com.bintyblackbook.ui.dialogues.UnBlockUserDialogFragment
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.BlockUserViewModel
import kotlinx.android.synthetic.main.activity_blocked_contacts.*
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONObject
import java.lang.Exception

class BlockedContactsActivity : BaseActivity(), View.OnClickListener, AdapterBlockedContacts.BlockAdapterInterface,
    SocketManager.Observer {

    var socketManager:SocketManager?=null

    var data_position=0
    lateinit var blockUserViewModel: BlockUserViewModel
    var arrayList= ArrayList<BlockedData>()
    var adapterBlockedContacts:AdapterBlockedContacts?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blocked_contacts)

        socketManager= BintyBookApplication.getSocketManager()
        blockUserViewModel= BlockUserViewModel()
        headingText.text = "Blocked Contacts"
        iv_back.setOnClickListener(this)
        setAdapter()
        getBlockUserList()
        initializeSocket()
    }

    private fun initializeSocket() {
        socketManager = BintyBookApplication.getSocketManager()
        if(socketManager==null){
            socketManager?.initializeSocket()
        }
    }

    private fun getBlockUserList() {
        blockUserViewModel.getBlockUserList(this,getSecurityKey(context)!!, getUser(context)?.authKey!!)
        blockUserViewModel.blockListLiveData.observe(this, Observer {

            if(it.data.size==0){
                tvNoBlockUser.visibility=View.VISIBLE
                rvBlockedContacts.visibility=View.GONE
            } else{
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
        adapterBlockedContacts?.blockInterface=this
        adapterBlockedContacts?.arrayList=arrayList
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_back -> {
                finish()
            }
        }
    }

    override fun onUnblockUser(data: BlockedData, position: Int, status: String) {
        data_position=position
        val dialogFragment = UnBlockUserDialogFragment(data.otherUserName,data.user2Id.toString(),this)
        dialogFragment.show(supportFragmentManager, "blockUser")
    }

    fun unBlockUser(user2Id: String) {
        val jsonObject = JSONObject()
        try{
            jsonObject.put("userId", getUser(this)?.id.toString())
            jsonObject.put("user2Id",user2Id)
            jsonObject.put("status","0")
            socketManager?.blockUser(jsonObject)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        socketManager?.onRegister(this)

    }
    override fun onStop() {
        super.onStop()
        socketManager?.unRegister(this)
    }

    override fun onError(event: String?, vararg args: Any?) {
        when(event){
            "errorSocket" -> {
                dismissProgressDialog()
            }
        }
    }

    override fun onResponse(event: String?, vararg args: Any?) {
        when(event){
            SocketManager.BLOCK_USER_LISTENER -> {
                try {
                    runOnUiThread {
                        val data = args.get(0) as JSONObject
                        arrayList.removeAt(data_position)
                        getBlockUserList()
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }

    }
}