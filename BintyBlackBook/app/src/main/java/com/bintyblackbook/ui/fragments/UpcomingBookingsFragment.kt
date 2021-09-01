package com.bintyblackbook.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.UpcomingBookingAdapter
import com.bintyblackbook.model.UpcomingBookings
import com.bintyblackbook.util.CustomProgressDialog
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.BookingsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_upcoming_bookings.*
import java.util.*


class UpcomingBookingsFragment : Fragment() {

    private var mSnackBar: Snackbar? = null
    var mProgress: CustomProgressDialog? = null
    lateinit var bookingsViewModel: BookingsViewModel
    var upcomingArrayList = ArrayList<UpcomingBookings>()
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
        mProgress = CustomProgressDialog(requireContext())
        bookingsViewModel= BookingsViewModel()

        setAdapter()

        getData()

    }

    private fun getData() {
        bookingsViewModel.getAllBookings(requireActivity(),getSecurityKey(requireContext())!!, getUser(requireContext())?.authKey!!)
        bookingsViewModel.bookingsLiveData.observe(requireActivity(), {

            if(it?.data?.upcomingBookings?.size==0){
                tvNoBookings.visibility=View.VISIBLE
                rvUpcomingBookings.visibility=View.GONE
            }
            else {
                tvNoBookings.visibility=View.GONE
                rvUpcomingBookings.visibility=View.VISIBLE
                upcomingArrayList.clear()
                upcomingArrayList.addAll(it?.data?.upcomingBookings!!)
                upcomingBookingAdapter?.notifyDataSetChanged()
            }

        })
    }

    private fun setAdapter() {
        rvUpcomingBookings.layoutManager = LinearLayoutManager(activity)
        upcomingBookingAdapter = UpcomingBookingAdapter(requireActivity())
        rvUpcomingBookings.adapter = upcomingBookingAdapter
        upcomingBookingAdapter?.arrayList=upcomingArrayList
    }

    fun showProgressDialog() {
        mProgress = CustomProgressDialog(requireContext())
        mProgress?.show()
    }

    fun dismissProgressDialog() {
        mProgress?.dismiss()
    }
}