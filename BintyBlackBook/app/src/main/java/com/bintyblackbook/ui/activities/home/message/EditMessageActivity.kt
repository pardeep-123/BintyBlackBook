package com.bintyblackbook.ui.activities.home.message

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.EditMessagesAdapter
import com.bintyblackbook.adapters.HorizontalCircularImageAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.AllData
import com.bintyblackbook.models.EditMessageModel
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.LoopsViewModel
import kotlinx.android.synthetic.main.activity_edit_message.*
import kotlinx.android.synthetic.main.activity_my_loops.*

class EditMessageActivity : BaseActivity() {

    lateinit var loopsViewModel: LoopsViewModel
    var editMessageAdapter: EditMessagesAdapter? = null
    var horizontalCircularImageAdapter: HorizontalCircularImageAdapter? = null
     var arrayList= ArrayList<AllData>()
     var arrayList2= ArrayList<AllData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_message)

        loopsViewModel= LoopsViewModel()

        setAdapter()

        getLoopList()

        rlBack.setOnClickListener {
            finish()
        }

        btnNext.setOnClickListener {
            startActivity(Intent(this,NewGroupActivity::class.java))
            finish()
        }
    }

    private fun getLoopList() {
        loopsViewModel.loopsList(this, getSecurityKey(this)!!, getUser(this)?.authKey!!)
        loopsViewModel.loopsLiveData.observe(this, Observer {

            if(it.code==200){
                if(it.data.allData.size==0){
                    rvEditMessages.visibility=View.GONE
                   // tvNoLoops.visibility=View.VISIBLE
                }
                else{
                    rvEditMessages.visibility=View.VISIBLE
                    //tvNoLoops.visibility=View.GONE
                    arrayList2.clear()
                    arrayList2.addAll(it.data.allData)
                    editMessageAdapter?.notifyDataSetChanged()
                }
            }
        })
    }

    private fun setAdapter() {
        rvHorizontal.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        horizontalCircularImageAdapter = HorizontalCircularImageAdapter(this)
        rvHorizontal.adapter = horizontalCircularImageAdapter
        horizontalCircularImageAdapter?.arrayList=arrayList


        editMessageAdapter = EditMessagesAdapter(this)
        rvEditMessages.adapter = editMessageAdapter
        editMessageAdapter?.arrayList=arrayList2
        adapterItemClick()
    }

    private fun adapterItemClick() {

        editMessageAdapter?.onItemClick = { editMessageModel: AllData ->
            arrayList.clear()
            arrayList2.forEach { editMsgModel ->
                if (editMsgModel.selected) {
                    arrayList.add(editMsgModel)
                }
            }

            if (arrayList.size > 0){
                rvHorizontal.visibility = View.VISIBLE
                horizontalCircularImageAdapter?.notifyDataSetChanged()
            }else{
                rvHorizontal.visibility = View.GONE
            }


        }
    }

}