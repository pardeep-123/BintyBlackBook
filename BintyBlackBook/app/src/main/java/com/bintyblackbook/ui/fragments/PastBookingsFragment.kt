package com.bintyblackbook.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.PastBookingAdapter
import com.bintyblackbook.model.PastBooking
import com.bintyblackbook.models.UpcomingBookingModel
import com.bintyblackbook.util.CustomProgressDialog
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.BookingsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_past_bookings.*


class PastBookingsFragment : Fragment() {

    private var mSnackBar: Snackbar? = null
    lateinit var bookingsViewModel:BookingsViewModel
    var pastBookingList = ArrayList<PastBooking>()
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
        bookingsViewModel= BookingsViewModel(requireActivity())

        setAdapter()

        getData()
    }

    private fun getData() {
        bookingsViewModel.getAllBookings(getSecurityKey(requireContext())!!, getUser(requireContext())?.authKey!!)
        bookingsViewModel.bookingsLiveData.observe(requireActivity(), {

            if(it.data?.pastBookings?.size==0){
                tvNoPastBookings.visibility=View.VISIBLE
                rvPastBooking.visibility=View.GONE
            }
            else {
                tvNoPastBookings.visibility=View.GONE
                rvPastBooking.visibility=View.VISIBLE
                pastBookingList.clear()
                pastBookingList.addAll(it?.data?.pastBookings!!)
                pastBookingAdapter?.notifyDataSetChanged()
            }

        })
    }

    private fun setAdapter() {
        rvPastBooking.layoutManager=LinearLayoutManager(activity)
        pastBookingAdapter= PastBookingAdapter(requireActivity())
        rvPastBooking.adapter=pastBookingAdapter
        pastBookingAdapter?.arrayList=pastBookingList
    }


}