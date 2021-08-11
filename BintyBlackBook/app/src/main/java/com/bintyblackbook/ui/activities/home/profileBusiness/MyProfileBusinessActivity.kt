package com.bintyblackbook.ui.activities.home.profileBusiness

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.HorizontalImagesAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.Data
import com.bintyblackbook.ui.activities.home.profileUser.AvailabilityActivity
import com.bintyblackbook.ui.activities.home.profileUser.EventInProfileActivity
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.activity_my_profile_business.*

class MyProfileBusinessActivity : BaseActivity() {

    lateinit var profileViewModel: ProfileViewModel

    var horizontalImagesAdapter: HorizontalImagesAdapter? = null
    val arrayList = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile_business)

        profileViewModel= ProfileViewModel(this)
        profileViewModel.userProfile(getSecurityKey(this)!!, getUser(this)?.authKey!!, getUser(this)?.id.toString())

        setObservables()

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

    private fun setObservables() {
        profileViewModel.profileObservable.observe(this, Observer {
            if (it.code==200){
                setData(it.data)
            }
        })
    }

    private fun setData(it: Data?) {
        tvName.text= it?.firstName
        tvLocation.text=it?.location
        tvUserAbout.text=it?.description
        tvExp.text=it?.experience
        tvWebLink.text=it?.websiteLink
       // arrayList.addAll(it?.userMedia)
    }

    private fun init() {

        arrayList.add(R.drawable.slider)
        arrayList.add(R.drawable.small2)

       //set adapter
        rvImages.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        horizontalImagesAdapter = HorizontalImagesAdapter(this)
        rvImages.adapter = horizontalImagesAdapter
        horizontalImagesAdapter?.arrayList=arrayList
        horizontalImagesAdapterClick()
    }

    private fun horizontalImagesAdapterClick() {
        /*horizontalImagesAdapter?.onItemClick = {image: Int ->
            riv1.setImageResource(image)
        }*/
    }
}