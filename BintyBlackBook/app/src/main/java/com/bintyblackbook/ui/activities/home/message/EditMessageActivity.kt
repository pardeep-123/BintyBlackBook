package com.bintyblackbook.ui.activities.home.message

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.EditMessagesAdapter
import com.bintyblackbook.adapters.HorizontalCircularImageAdapter
import com.bintyblackbook.models.EditMessageModel
import kotlinx.android.synthetic.main.activity_edit_message.*

class EditMessageActivity : AppCompatActivity() {

    var editMessageAdapter: EditMessagesAdapter? = null
    var horizontalCircularImageAdapter: HorizontalCircularImageAdapter? = null
    lateinit var arrayList: ArrayList<EditMessageModel>
    lateinit var arrayList2: ArrayList<EditMessageModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_message)

        arrayList2 = ArrayList()

        rlBack.setOnClickListener {
            finish()
        }

        btnNext.setOnClickListener {
            startActivity(Intent(this,NewGroupActivity::class.java))
            finish()
        }

        rvHorizontal.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        horizontalCircularImageAdapter = HorizontalCircularImageAdapter(this,arrayList2)
        rvHorizontal.adapter = horizontalCircularImageAdapter

        arrayList = ArrayList()
        arrayList.add(EditMessageModel(R.drawable.bamie, "John","10 min ago", false))
        arrayList.add(EditMessageModel(R.drawable.soph, "Jian","10 min ago", false))
        arrayList.add(EditMessageModel(R.drawable.robert, "Malli","10 min ago", false))

        editMessageAdapter = EditMessagesAdapter(this, arrayList)
        rvEditMessages.adapter = editMessageAdapter
        adapterItemClick()
    }

    private fun adapterItemClick() {

        editMessageAdapter?.onItemClick = { editMessageModel: EditMessageModel ->
            arrayList2.clear()
            arrayList.forEach { editMsgModel ->
                if (editMsgModel.selected) {
                    arrayList2.add(editMsgModel)
                }
            }

            if (arrayList2.size > 0){
                rvHorizontal.visibility = View.VISIBLE
            }else{
                rvHorizontal.visibility = View.GONE
            }

            horizontalCircularImageAdapter?.notifyDataSetChanged()
        }
    }

}