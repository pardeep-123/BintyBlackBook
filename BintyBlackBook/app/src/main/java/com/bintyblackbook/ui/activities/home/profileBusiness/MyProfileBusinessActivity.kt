package com.bintyblackbook.ui.activities.home.profileBusiness

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.HorizontalImagesAdapter
import com.bintyblackbook.ui.activities.home.profileUser.AvailabilityActivity
import com.bintyblackbook.ui.activities.home.profileUser.EventInProfileActivity
import kotlinx.android.synthetic.main.activity_my_profile_business.*

class MyProfileBusinessActivity : AppCompatActivity() {

    var horizontalImagesAdapter: HorizontalImagesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile_business)

        rlBack.setOnClickListener {
            onBackPressed()
        }

        init()

        btnSetAvailability.setOnClickListener {
            startActivity(Intent(this, AvailabilityActivity::class.java))
        }

        btnEditProfile.setOnClickListener {
            val intent = Intent(this, EditProfileBusinessActivity::class.java)
            startActivity(intent)
        }

        btnEvent.setOnClickListener {
            startActivity(Intent(this, EventInProfileActivity::class.java))
        }
    }

    private fun init() {
        val arrayList = ArrayList<Int>()
        arrayList.add(R.drawable.slider)
        arrayList.add(R.drawable.small2)

        horizontalImagesAdapter = HorizontalImagesAdapter(this, arrayList)
        rvImages.adapter = horizontalImagesAdapter
        rvImages.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        horizontalImagesAdapterClick()
    }

    private fun horizontalImagesAdapterClick() {
        /*horizontalImagesAdapter?.onItemClick = {image: Int ->
            riv1.setImageResource(image)
        }*/
    }
}