package com.bintyblackbook.ui.activities.home

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.PhotoAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.CategoryName
import com.bintyblackbook.util.AppConstant
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_home_item_click.*
import kotlinx.android.synthetic.main.toolbar.*

class HomeItemClickActivity : BaseActivity(), TextWatcher {

    var photoAdapter:PhotoAdapter? = null
    lateinit var homeViewModel: HomeViewModel
    var list=ArrayList<CategoryName>()

    var id=0
    var userId=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_item_click)

        homeViewModel = HomeViewModel(this)

        val getHeading = intent.getStringExtra(AppConstant.HEADING)
        id = intent.getIntExtra("id", 0)

        iv_back.setOnClickListener {
            finish()
        }

        if (getHeading != null) {
            headingText.text = getHeading
        }

        setAdapter()
        getData()

        edtSearch.addTextChangedListener(this)
    }

    private fun getData() {
        homeViewModel.homeList(getSecurityKey(context)!!, getUser(context)?.authKey!!)
        homeViewModel.homeListLiveData.observe(this, Observer {

            for (i in 0 until it.data.size){
                if(it.data[i].id==id){
                    list.addAll(it.data[i].categoryName)
                    photoAdapter?.notifyDataSetChanged()
                }
            }
        })
    }

    private fun setAdapter(){
        rvPhotos.layoutManager = GridLayoutManager(this, 2)
        photoAdapter = PhotoAdapter(list,this)
        rvPhotos.adapter = photoAdapter
        photoAdapter?.arrayList=list
        adapterItemClick()
    }

    private fun adapterItemClick(){
        photoAdapter?.onItemClick = { photosModel ->
                val intent = Intent(this,OtherUserProfileActivity::class.java)
                intent.putExtra("user_id",photosModel.userId.toString())
                startActivity(intent)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        photoAdapter!!.filter.filter(s.toString().trim())
    }

    override fun afterTextChanged(s: Editable?) {

    }
}