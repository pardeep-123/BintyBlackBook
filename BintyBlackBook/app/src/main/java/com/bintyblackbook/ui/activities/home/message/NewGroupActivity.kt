package com.bintyblackbook.ui.activities.home.message

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.HorizontalCircularImageAdapter
import com.bintyblackbook.model.AllData
import com.bintyblackbook.util.ImagePickerUtility
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_new_group.*
import kotlinx.android.synthetic.main.activity_signup.*
import java.io.File

class NewGroupActivity : ImagePickerUtility() {

    var selectedImagePath:File?=null
    var arrayList= ArrayList<AllData>()
    var horizontalCircularImageAdapter: HorizontalCircularImageAdapter? = null

    override fun selectedImage(imagePath: File?) {
        Glide.with(context).load(imagePath).into(civGroup)
        selectedImagePath=imagePath
    }

    override fun selectedVideoUri(imagePath: String?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_group)

        tvParticipants.text= getString(R.string.participants)+" "+(arrayList.size +1).toString()

        rlBack.setOnClickListener {
            onBackPressed()
        }

        civGroup.setOnClickListener {
            getImage(this,0,false)
        }

        btnNext.setOnClickListener {
            finish()
        }


        rvParticipants.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        horizontalCircularImageAdapter = HorizontalCircularImageAdapter(this)
        rvParticipants.adapter = horizontalCircularImageAdapter
        horizontalCircularImageAdapter?.arrayList=arrayList

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,EditMessageActivity::class.java))
    }
}