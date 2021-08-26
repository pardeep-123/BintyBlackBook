package com.bintyblackbook.ui.activities.home.notification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.bintyblackbook.R
import com.bintyblackbook.adapters.LoopRequestAdapter
import com.bintyblackbook.ui.activities.home.UserDetailActivity
import com.bintyblackbook.ui.dialogues.CancelDialogFragment
import com.bintyblackbook.util.AppConstant
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.util.showAlert
import com.bintyblackbook.viewmodel.LoopsViewModel
import kotlinx.android.synthetic.main.activity_loop_request.*

class LoopRequestActivity : AppCompatActivity() {

    var loopRequestAdapter:LoopRequestAdapter? = null

    lateinit var loopsViewModel: LoopsViewModel

    var msg =""
    var userId=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loop_request)

        loopsViewModel= LoopsViewModel(this)

        getIntentData()
        setAdapter()
        rlBack.setOnClickListener {
            onBackPressed()
        }

    }

    private fun getIntentData() {
        msg= intent.getStringExtra("message").toString()
        userId= intent.getStringExtra("user_id").toString()

    }

    private fun setAdapter() {

        loopRequestAdapter = LoopRequestAdapter(this)
        rvLoopRequest.adapter = loopRequestAdapter

        adapterItemBtnClick()
    }

    private fun adapterItemBtnClick(){
        loopRequestAdapter?.onItemBtnClick = {clickOn ->
            if (clickOn == "accept"){

                acceptRejectRequest("2")

                val intent = Intent(this, UserDetailActivity::class.java)
                intent.putExtra(AppConstant.SHOW_CHAT_AND_UN_LOOP_BTN,true)
                startActivity(intent)
            }else if (clickOn == "cancel"){
                acceptRejectRequest("0")
                val dialog  = CancelDialogFragment("cancelLoopRequest")
                dialog.show(supportFragmentManager,"cancelLoopRequest")
            }
        }
    }

    private fun acceptRejectRequest(status: String) {
        loopsViewModel.acceptRejectRequest(getSecurityKey(this)!!, getUser(this)?.authKey!!, getUser(this)?.id.toString(),status)

        loopsViewModel.baseLiveData.observe(this, Observer {
            if(it.code==200){
                showAlert(this,it.msg,getString(R.string.ok)){}
            }

        })
    }
}