package com.bintyblackbook.ui.fragments

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.HomeAdapter
import com.bintyblackbook.model.HomeData
import com.bintyblackbook.ui.activities.home.HomeActivity
import com.bintyblackbook.ui.activities.home.HomeItemClickActivity
import com.bintyblackbook.ui.activities.home.notification.NotificationActivity
import com.bintyblackbook.util.AppConstant
import com.bintyblackbook.util.MyUtils
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.HomeViewModel
import com.bintyblackbook.viewmodel.NotificationViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.io.Serializable


class HomeFragment : Fragment(), View.OnClickListener, TextWatcher {

    var searchClick = false
    var homeAdapter: HomeAdapter? = null
    var v:View?=null
    lateinit var homeViewModel: HomeViewModel
    lateinit var notificationViewModel: NotificationViewModel
    var homeList=ArrayList<HomeData>()

    var count=""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        v=  inflater.inflate(R.layout.fragment_home, container, false)
        homeViewModel=HomeViewModel()
        notificationViewModel= NotificationViewModel(requireContext())

        init()

        setOnClicks()

        //call api for home data
        if(!getUser(requireContext())?.authKey.isNullOrEmpty()) {
            homeViewModel.homeList(requireContext(),getSecurityKey(requireContext())!!, getUser(requireContext())?.authKey!!)
            getHomeData()
        }
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setOnClicks() {
        v!!.rootView.edtSearch.addTextChangedListener(this)
        v!!.rootView.ivSearch.setOnClickListener(this)

        v!!.rootView.rlMenu.setOnClickListener(this)

        v!!.rootView.rlBell.setOnClickListener(this)
    }

    private fun getHomeData() {
        homeViewModel.homeLiveData.observe(requireActivity(), Observer {

            if(it?.code==200){
                if(it.data.size==0){
                    v!!.rootView.rvHome.visibility=View.GONE
                    v!!.rootView.noHomeData.visibility=View.VISIBLE
                }
                else{
                    v!!.rootView.rvHome.visibility=View.VISIBLE
                    v!!.rootView.noHomeData.visibility=View.GONE
                    homeList.clear()
                    homeList.addAll(it.data)
                    homeAdapter?.notifyDataSetChanged()
                }
            }
            //call api for notification count
            getNotificationCount()
        })
    }

    private fun getNotificationCount() {
        notificationViewModel.getNotificationCount(getSecurityKey(requireContext())!!, getUser(requireContext())?.authKey!!)
        notificationViewModel.notiCountLiveData.observe(requireActivity(), Observer {

            if(it.code==200){
                try {
                    if (it.data?.count != 0) {
                        val mp = MediaPlayer.create(requireContext(), R.raw.noti)
                        mp.start()
                        mp?.setOnCompletionListener { media ->
                            v!!.rootView.tvCount.visibility = View.VISIBLE
                            v!!.rootView.tvCount.text = it.data?.count.toString()
                            media.release()
                        }

                    } else {
                        v!!.rootView.tvCount.visibility = View.GONE
                    }
                }catch (e:ExceptionInInitializerError){
                    e.printStackTrace()
                }
            }
        })
    }
    private fun init() {
        v!!.rootView.rvHome.layoutManager = LinearLayoutManager(activity)
        homeAdapter = HomeAdapter(requireActivity(),homeList)
        v!!.rootView.rvHome.adapter = homeAdapter
        homeAdapter?.arrayList=homeList
        adapterItemClick()
    }

    private fun adapterItemClick(){
        homeAdapter?.onItemClick = {homeModel: HomeData ->
            val intent = Intent(activity,HomeItemClickActivity::class.java)
            val args = Bundle()
            args.putSerializable("ARRAYLIST", homeModel.categoryName as Serializable?)
            args.putString(AppConstant.HEADING, homeModel.name)
            intent.putExtras(args)
//            intent.putExtra(AppConstant.HEADING, homeModel.name)
//            intent.putExtra("id",homeModel.id)
            startActivity(intent)
        }
    }

    override fun onClick(v: View?) {

        when(v?.id){
            R.id.ivSearch ->{
                if (searchClick){
                    v!!.rootView.edtSearch.visibility = View.GONE
                    searchClick = false
                    MyUtils.hideSoftKeyboard(requireActivity())
                }else{
                    v!!.rootView.edtSearch.visibility = View.VISIBLE
                    searchClick = true
                }
            }

            R.id.rlMenu ->{
                (activity as HomeActivity).openDrawer()
            }

            R.id.rlBell ->{
                val intent= Intent(context, NotificationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        homeAdapter?.filter?.filter(s.toString().trim())
    }

    override fun afterTextChanged(s: Editable?) {

    }

}