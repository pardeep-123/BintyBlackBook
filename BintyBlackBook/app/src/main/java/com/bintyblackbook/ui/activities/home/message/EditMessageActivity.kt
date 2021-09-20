package com.bintyblackbook.ui.activities.home.message

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.EditMessagesAdapter
import com.bintyblackbook.adapters.HorizontalCircularImageAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.AllData
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.LoopsViewModel
import kotlinx.android.synthetic.main.activity_edit_message.*
import java.io.Serializable

class EditMessageActivity : BaseActivity(),
    HorizontalCircularImageAdapter.HorizontalAdapterInterface {

    lateinit var loopsViewModel: LoopsViewModel
    var editMessageAdapter: EditMessagesAdapter? = null
    var horizontalCircularImageAdapter: HorizontalCircularImageAdapter? = null
     var selectedList= ArrayList<AllData>()
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
            val intent= Intent(this, NewGroupActivity::class.java)
            val args = Bundle()
            args.putSerializable("ARRAYLIST", selectedList as Serializable?)
            intent.putExtra("BUNDLE", args)
            startActivity(intent)
            finish()
        }
    }

    private fun getLoopList() {
        loopsViewModel.loopsList(this, getSecurityKey(this)!!, getUser(this)?.authKey!!)
        loopsViewModel.loopsLiveData.observe(this, Observer {

            if (it.code == 200) {
                if (it.data.allData.size == 0) {
                    rvEditMessages.visibility = View.GONE
                    // tvNoLoops.visibility=View.VISIBLE
                } else {
                    rvEditMessages.visibility = View.VISIBLE
                    //tvNoLoops.visibility=View.GONE
                    arrayList2.clear()
                    arrayList2.addAll(it.data.allData)
                    editMessageAdapter?.notifyDataSetChanged()
                }
            }
        })
    }

    private fun setAdapter() {
        //set horizontal adapter
        rvHorizontal.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        horizontalCircularImageAdapter = HorizontalCircularImageAdapter(this)
        rvHorizontal.adapter = horizontalCircularImageAdapter
        horizontalCircularImageAdapter?.horizontalAdapterInterface=this
        horizontalCircularImageAdapter?.arrayList=selectedList

        //set adapter for all users
        editMessageAdapter = EditMessagesAdapter(this)
        rvEditMessages.adapter = editMessageAdapter
        editMessageAdapter?.arrayList=arrayList2
        adapterItemClick()
    }

    private fun adapterItemClick() {

        editMessageAdapter?.onItemClick = { editMessageModel: AllData ->
            selectedList.clear()
            arrayList2.forEach { editMsgModel ->
                if (editMsgModel.selected) {
                    selectedList.add(editMsgModel)
                }
            }

            if (selectedList.size > 0){
                rvHorizontal.visibility = View.VISIBLE
                horizontalCircularImageAdapter?.notifyDataSetChanged()
            }else{
                rvHorizontal.visibility = View.GONE
            }
        }
    }

    override fun onDelete(data: AllData, position: Int) {
//        selectedList.removeAt(position)
//        horizontalCircularImageAdapter?.notifyDataSetChanged()
    }

}