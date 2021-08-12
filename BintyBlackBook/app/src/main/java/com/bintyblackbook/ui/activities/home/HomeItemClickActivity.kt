package com.bintyblackbook.ui.activities.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.PhotoAdapter
import com.bintyblackbook.model.CategoryName
import com.bintyblackbook.models.PhotosModel
import com.bintyblackbook.util.AppConstant
import com.bintyblackbook.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_home_item_click.*
import kotlinx.android.synthetic.main.toolbar.*

class HomeItemClickActivity : AppCompatActivity() {

    var photoAdapter:PhotoAdapter? = null
    lateinit var homeViewModel: HomeViewModel
    var list=ArrayList<CategoryName>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_item_click)
        rvPhotos.layoutManager = GridLayoutManager(this, 2)

        homeViewModel= HomeViewModel(this)

        val getHeading = intent.getStringExtra(AppConstant.HEADING)

        iv_back.setOnClickListener {
            finish()
        }

        if (getHeading != null) {
            headingText.text = getHeading
        }

        init()
        homeViewModel.homeLiveData.observe(this, Observer {

            for(i in 0 until it?.data?.size!!){
                if(it.data[i].name.equals(getHeading)){
                    list.addAll(it.data[i].categoryName)
                    photoAdapter?.notifyDataSetChanged()
                }
            }
        })
    }

    private fun init(){
//        val arrayList = ArrayList<PhotosModel>()
//        arrayList.add(PhotosModel(R.drawable.john,"John","Arizona, USA"))
//        arrayList.add(PhotosModel(R.drawable.girl1,"John","Arizona, USA"))
//        arrayList.add(PhotosModel(R.drawable.matinn,"John","Arizona, USA"))
//        arrayList.add(PhotosModel(R.drawable.girl,"John","Arizona, USA"))
//        arrayList.add(PhotosModel(R.drawable.girl1,"John","Arizona, USA"))
//        arrayList.add(PhotosModel(R.drawable.mattrin,"John","Arizona, USA"))
//        arrayList.add(PhotosModel(R.drawable.john,"John","Arizona, USA"))
//        arrayList.add(PhotosModel(R.drawable.matinn,"John","Arizona, USA"))
//        arrayList.add(PhotosModel(R.drawable.girl,"John","Arizona, USA"))
//        arrayList.add(PhotosModel(R.drawable.mattrin,"John","Arizona, USA"))

        photoAdapter = PhotoAdapter(this,list)
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