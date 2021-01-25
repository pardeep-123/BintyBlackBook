package com.bintyblackbook.ui.activities.home.notification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bintyblackbook.R
import com.bintyblackbook.adapters.LoopRequestAdapter
import com.bintyblackbook.ui.activities.home.UserDetailActivity
import com.bintyblackbook.ui.dialogues.CancelDialogFragment
import com.bintyblackbook.util.AppConstant
import kotlinx.android.synthetic.main.activity_loop_request.*

class LoopRequestActivity : AppCompatActivity() {

    var loopRequestAdapter:LoopRequestAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loop_request)

        rlBack.setOnClickListener {
            onBackPressed()
        }

        loopRequestAdapter = LoopRequestAdapter(this)
        rvLoopRequest.adapter = loopRequestAdapter
        adapterItemBtnClick()
    }

    private fun adapterItemBtnClick(){
        loopRequestAdapter?.onItemBtnClick = {clickOn ->
            if (clickOn == "accept"){
                val intent = Intent(this, UserDetailActivity::class.java)
                intent.putExtra(AppConstant.SHOW_CHAT_AND_UN_LOOP_BTN,true)
                startActivity(intent)
            }else if (clickOn == "cancel"){
                val dialog  = CancelDialogFragment("cancelLoopRequest")
                dialog.show(supportFragmentManager,"cancelLoopRequest")
            }
        }
    }
}