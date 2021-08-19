package com.bintyblackbook.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.UpcomingBookingAdapter
import com.bintyblackbook.models.UpcomingBookingModel
import com.bintyblackbook.viewmodel.BookingsViewModel
import kotlinx.android.synthetic.main.fragment_upcoming_bookings.*
import java.util.*


class UpcomingBookingsFragment : Fragment() {

    lateinit var bookingsViewModel: BookingsViewModel
    lateinit var upcomingArrayList:ArrayList<UpcomingBookingModel>
    var upcomingBookingAdapter:UpcomingBookingAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upcoming_bookings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookingsViewModel= BookingsViewModel(requireActivity())

        upcomingArrayList = ArrayList()

        upcomingArrayList.add(UpcomingBookingModel(R.drawable.alina,"Alisha","Date: 20/01/2021","Time: 9:00 AM","Status: Accepted"))
        upcomingArrayList.add(UpcomingBookingModel(R.drawable.girl1,"Alisha","Date: 20/01/2021","Time: 9:00 AM","Status: Declined"))
        upcomingArrayList.add(UpcomingBookingModel(R.drawable.bamie,"Alisha","Date: 20/01/2021","Time: 9:00 AM","Status: Inprogress"))
        setAdapter()

    }

    private fun setAdapter() {
        rvUpcomingBookings.layoutManager = LinearLayoutManager(activity)
        upcomingBookingAdapter = UpcomingBookingAdapter(requireActivity(),upcomingArrayList)
        rvUpcomingBookings.adapter = upcomingBookingAdapter
    }
}