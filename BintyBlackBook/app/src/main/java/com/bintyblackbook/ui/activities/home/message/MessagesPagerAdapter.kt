package com.bintyblackbook.ui.activities.home.message

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.adapter.FragmentStateAdapter


class MessagesPagerAdapter(manager: FragmentManager?, lifecycleOwner: LifecycleOwner) :
    FragmentStateAdapter(manager!!, lifecycleOwner.lifecycle) {

    private val mFragmentList: MutableList<Fragment> = ArrayList()
    private val mFragmentTitleList: MutableList<String> = ArrayList()

    override fun getItemCount(): Int {
        return mFragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }


    fun addFragment(
        fragment: Fragment,
        title: String
    ) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }


}