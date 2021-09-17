package com.bintyblackbook.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bintyblackbook.R
import com.bintyblackbook.adapters.AdapterPromoteBusiness
import com.bintyblackbook.model.PromotionData
import com.bintyblackbook.ui.activities.home.promote.PaymentActivity
import com.bintyblackbook.ui.activities.home.promote.PromotePayActivity
import kotlinx.android.synthetic.main.fragment_promote_business.*
import kotlinx.android.synthetic.main.fragment_promote_event.*

class PromoteEventFragment:Fragment(), AdapterPromoteBusiness.PromoteAdapterInterface {

    var adapterPromoteBusiness: AdapterPromoteBusiness? = null

    var arrayList= ArrayList<PromotionData>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_promote_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arrayList.clear()
        arrayList.add(PromotionData("Daily Subscription","$9.99"))
        arrayList.add(PromotionData("Weekly Subscription","$49.99"))
        arrayList.add(PromotionData("Monthly Subscription","$149.99"))
        setAdapter()
    }

    private fun setAdapter() {
        val layoutManager= LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        rvPromoteEvents.layoutManager=layoutManager
        adapterPromoteBusiness = AdapterPromoteBusiness(requireContext(),"events")
        rvPromoteEvents.adapter = adapterPromoteBusiness
        adapterPromoteBusiness?.promoteAdapterInterface=this
        adapterPromoteBusiness?.arrayList=arrayList

    }

    override fun onItemClick(data: PromotionData, position: Int, screen_type: String) {
        val intent= Intent(requireContext(), PaymentActivity::class.java)
        intent.putExtra("promote_type",screen_type)
        intent.putExtra("price",data.price)
        intent.putExtra("subs_name",data.subscription_type)
        startActivity(intent)
    }


}