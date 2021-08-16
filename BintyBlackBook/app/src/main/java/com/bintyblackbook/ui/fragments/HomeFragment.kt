package com.bintyblackbook.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.HomeAdapter
import com.bintyblackbook.model.HomeData
import com.bintyblackbook.ui.activities.home.HomeItemClickActivity
import com.bintyblackbook.util.AppConstant
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    var homeAdapter: HomeAdapter? = null
    lateinit var homeViewModel: HomeViewModel
    var homeList=ArrayList<HomeData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel=HomeViewModel(context!!)

        init()

        //call api for home data

        if(!getUser(context!!)?.authKey.isNullOrEmpty()) {
            homeViewModel.homeList(getSecurityKey(context!!)!!, getUser(context!!)?.authKey!!)
            getHomeData()
        }
    }

    private fun getHomeData() {
        homeViewModel.homeLiveData.observe(activity!!, Observer {

         //   homeViewModel.homeList(it)

            if(it?.code==200){
                if(it.data.size==0){
                    rvHome.visibility=View.GONE
                    noHomeData.visibility=View.VISIBLE
                }
                else{
                    rvHome.visibility=View.VISIBLE
                    noHomeData.visibility=View.GONE
                    homeList.clear()
                    homeList.addAll(it.data)
                    homeAdapter?.notifyDataSetChanged()
                }

            }
        })
    }

    private fun init() {
        rvHome.layoutManager = LinearLayoutManager(activity)

        homeAdapter = HomeAdapter(activity!!)
        rvHome.adapter = homeAdapter
        homeAdapter?.arrayList=homeList
        adapterItemClick()
    }

    private fun adapterItemClick(){
        homeAdapter?.onItemClick = {homeModel: HomeData ->
            val intent = Intent(activity,HomeItemClickActivity::class.java)
            intent.putExtra(AppConstant.HEADING, homeModel.name)
            intent.putExtra("id",homeModel.id)
            startActivity(intent)
        }
    }
}