package com.bintyblackbook.ui.activities.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.PhotoAdapter
import com.bintyblackbook.models.PhotosModel
import com.bintyblackbook.util.AppConstant
import kotlinx.android.synthetic.main.activity_home_item_click.*
import kotlinx.android.synthetic.main.toolbar.*

class HomeItemClickActivity : AppCompatActivity() {

    var photoAdapter:PhotoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_item_click)
        rvPhotos.layoutManager = GridLayoutManager(this, 2)

        val getHeading = intent.getStringExtra(AppConstant.HEADING)

        iv_back.setOnClickListener {
            finish()
        }

        if (getHeading != null) {
            headingText.text = getHeading
        }

        init()
    }

    private fun init(){
        val arrayList = ArrayList<PhotosModel>()
        arrayList.add(PhotosModel(R.drawable.john,"John","Arizona, USA"))
        arrayList.add(PhotosModel(R.drawable.girl1,"John","Arizona, USA"))
        arrayList.add(PhotosModel(R.drawable.matinn,"John","Arizona, USA"))
        arrayList.add(PhotosModel(R.drawable.girl,"John","Arizona, USA"))
        arrayList.add(PhotosModel(R.drawable.girl1,"John","Arizona, USA"))
        arrayList.add(PhotosModel(R.drawable.mattrin,"John","Arizona, USA"))
        arrayList.add(PhotosModel(R.drawable.john,"John","Arizona, USA"))
        arrayList.add(PhotosModel(R.drawable.matinn,"John","Arizona, USA"))
        arrayList.add(PhotosModel(R.drawable.girl,"John","Arizona, USA"))
        arrayList.add(PhotosModel(R.drawable.mattrin,"John","Arizona, USA"))

        photoAdapter = PhotoAdapter(this,arrayList)
        rvPhotos.adapter = photoAdapter
        adapterItemClick()
    }

    private fun adapterItemClick(){
        photoAdapter?.onItemClick = { photosModel ->
             val intent = Intent(this,UserDetailActivity::class.java)
            startActivity(intent)
        }
    }
}