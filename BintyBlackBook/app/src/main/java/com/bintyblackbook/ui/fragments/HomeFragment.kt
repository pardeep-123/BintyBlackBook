package com.bintyblackbook.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.HomeAdapter
import com.bintyblackbook.models.HomeModel
import com.bintyblackbook.ui.activities.home.HomeItemClickActivity
import com.bintyblackbook.util.AppConstant
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    var homeAdapter: HomeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

    }

    private fun init() {
        rvHome.layoutManager = LinearLayoutManager(activity)

        val arrayListImages = ArrayList<Int>()
        arrayListImages.add(R.drawable.john)
        arrayListImages.add(R.drawable.matinn)
        arrayListImages.add(R.drawable.mattrin)

        val arrayListImages1 = ArrayList<Int>()
        arrayListImages1.add(R.drawable.slider)
        arrayListImages1.add(R.drawable.small)
        arrayListImages1.add(R.drawable.small1)

        val arrayListImages2 = ArrayList<Int>()
        arrayListImages2.add(R.drawable.small2)
        arrayListImages2.add(R.drawable.small3)
        arrayListImages2.add(R.drawable.small4)

        val arrayList = ArrayList<HomeModel>()
        arrayList.add(HomeModel("Photography", arrayListImages))
        arrayList.add(HomeModel("DJS", arrayListImages1))
        arrayList.add(HomeModel("Music", arrayListImages2))
        arrayList.add(HomeModel("Events", arrayListImages))


        homeAdapter = HomeAdapter(activity!!, arrayList)
        rvHome.adapter = homeAdapter
        adapterItemClick()
    }

    private fun adapterItemClick(){
        homeAdapter?.onItemClick = {homeModel: HomeModel ->
            val intent = Intent(activity,HomeItemClickActivity::class.java)
            intent.putExtra(AppConstant.HEADING, homeModel.title)
            startActivity(intent)
        }
    }
}