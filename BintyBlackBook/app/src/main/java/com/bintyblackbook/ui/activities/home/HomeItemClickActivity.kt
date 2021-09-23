package com.bintyblackbook.ui.activities.home

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.bintyblackbook.R
import com.bintyblackbook.adapters.PhotoAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.CategoryName
import com.bintyblackbook.util.AppConstant
import com.bintyblackbook.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_home_item_click.*
import kotlinx.android.synthetic.main.toolbar.*

class HomeItemClickActivity : BaseActivity(), TextWatcher {

    val spanCount=0
    var photoAdapter:PhotoAdapter? = null
    lateinit var homeViewModel: HomeViewModel
    var list=ArrayList<CategoryName>()

    var id=0
    var userId=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_item_click)

        homeViewModel = HomeViewModel()
        setAdapter()
        getIntentData()
        setOnClicks()

    }

    private fun setOnClicks() {
        iv_back.setOnClickListener {
            finish()
        }
        edtSearch.addTextChangedListener(this)
    }

    private fun getIntentData() {
        val intent = intent
        val extras = intent.extras
        val getHeading = extras?.getString(AppConstant.HEADING).toString()
        if (getHeading != null) {
            headingText.text = getHeading
        }
        list.clear()
        list.addAll(extras?.getSerializable("ARRAYLIST") as ArrayList<CategoryName>)
        photoAdapter?.notifyDataSetChanged()
    }


    private fun setAdapter(){

        val layoutManager = GridLayoutManager(this, 2)
        rvPhotos.layoutManager = layoutManager
        photoAdapter = PhotoAdapter(list, this)
        rvPhotos.adapter = photoAdapter
        photoAdapter?.arrayList=list
        adapterItemClick()
    }

    private fun adapterItemClick(){
        photoAdapter?.onItemClick = { photosModel ->
            val intent = Intent(this, OtherUserProfileActivity::class.java)
            intent.putExtra("user_id", photosModel.userId.toString())
            startActivity(intent)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        photoAdapter?.filter?.filter(s.toString().trim())
    }

    override fun afterTextChanged(s: Editable?) {

    }
}