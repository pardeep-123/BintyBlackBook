package com.bintyblackbook.ui.activities.home.message

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.HorizontalCircularImageAdapter
import com.bintyblackbook.model.AllData
import com.bintyblackbook.util.ImagePickerUtility
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.ChatViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_new_group.*
import java.io.File

class NewGroupActivity : ImagePickerUtility() {

    lateinit var chatViewModel: ChatViewModel
    var selectedImagePath:File?=null
    var arrayList= ArrayList<AllData>()
    var horizontalCircularImageAdapter: HorizontalCircularImageAdapter? = null

    var userIds=""

    override fun selectedImage(imagePath: File?) {
        Glide.with(context).load(imagePath).into(civGroup)
        selectedImagePath=imagePath
    }

    override fun selectedVideoUri(imagePath: String?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_group)

        getIntentData()
        setAdapter()

        chatViewModel= ChatViewModel()

        rlBack.setOnClickListener {
            onBackPressed()
        }

        civGroup.setOnClickListener {
            getImage(this, 0, false)
        }

        btnNext.setOnClickListener {
            if(edtGroupName.text.toString().isNullOrEmpty()){
                Toast.makeText(this,"Please enter group name",Toast.LENGTH_SHORT).show()
            }else{
                addGroup()
            }
        }
    }

    private fun setAdapter() {
        rvParticipants.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        horizontalCircularImageAdapter = HorizontalCircularImageAdapter(this)
        rvParticipants.adapter = horizontalCircularImageAdapter
        horizontalCircularImageAdapter?.arrayList=arrayList
    }

    private fun getIntentData() {
        val intent = intent
        val args = intent.getBundleExtra("BUNDLE")
        arrayList = args?.getSerializable("ARRAYLIST") as ArrayList<AllData>
        Log.i("groupUserList",arrayList.size.toString())

        tvParticipants.text= getString(R.string.participants)+" "+(arrayList.size +1).toString()

        val usersId=ArrayList<String>()
        arrayList.forEach {
            usersId.add(it.id.toString())
        }
        userIds= TextUtils.join(",",usersId)

    }

    private fun addGroup() {
        chatViewModel.addGroup(this, getSecurityKey(this)!!, getUser(this)?.authKey.toString(), edtGroupName.text.toString(),userIds
        )
        chatViewModel.addGroupLiveData.observe(this, Observer {
            val intent= Intent(this,MessagesActivity::class.java)
            startActivity(intent)
            finish()
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, EditMessageActivity::class.java))
    }
}