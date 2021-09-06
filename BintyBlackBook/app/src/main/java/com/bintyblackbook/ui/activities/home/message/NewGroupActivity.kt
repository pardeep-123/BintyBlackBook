package com.bintyblackbook.ui.activities.home.message

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.HorizontalCircularImageAdapter
import com.bintyblackbook.models.EditMessageModel
import com.bintyblackbook.util.ImagePickerUtility
import kotlinx.android.synthetic.main.activity_new_group.*
import java.io.File

class NewGroupActivity : ImagePickerUtility() {

    lateinit var arrayList: ArrayList<EditMessageModel>
    var horizontalCircularImageAdapter: HorizontalCircularImageAdapter? = null

    override fun selectedImage(imagePath: File?) {

    }

    override fun selectedVideoUri(imagePath: String?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_group)

        rlBack.setOnClickListener {
            onBackPressed()
        }

        civGroup.setOnClickListener {
            getImage(this,0,false)
        }

        btnNext.setOnClickListener {
            finish()
        }

        arrayList = ArrayList()
        arrayList.add(EditMessageModel(R.drawable.bamie, "John","10 min ago", false))
        arrayList.add(EditMessageModel(R.drawable.soph, "Jian","10 min ago", false))
        arrayList.add(EditMessageModel(R.drawable.robert, "Malli","10 min ago", false))

        rvParticipants.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        horizontalCircularImageAdapter = HorizontalCircularImageAdapter(this, arrayList)
        rvParticipants.adapter = horizontalCircularImageAdapter


    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,EditMessageActivity::class.java))
    }
}