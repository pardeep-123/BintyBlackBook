package com.bintyblackbook.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.PastBookingAdapter
import com.bintyblackbook.models.UpcomingBookingModel
import kotlinx.android.synthetic.main.fragment_past_bookings.*


class PastBookingsFragment : Fragment() {

    lateinit var upcomingArrayList:ArrayList<UpcomingBookingModel>
    var pastBookingAdapter:PastBookingAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_past_bookings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvPastBooking.layoutManager = LinearLayoutManager(activity)
        upcomingArrayList = ArrayList()

        upcomingArrayList.add(UpcomingBookingModel(R.drawable.alina,"Alisha","Date: 20/01/2021","Time: 9:00 AM","Status: Accepted"))
        upcomingArrayList.add(UpcomingBookingModel(R.drawable.girl1,"Alisha","Date: 20/01/2021","Time: 9:00 AM","Status: Declined"))

        pastBookingAdapter = PastBookingAdapter(activity!!,upcomingArrayList)
        rvPastBooking.adapter = pastBookingAdapter
    }
}