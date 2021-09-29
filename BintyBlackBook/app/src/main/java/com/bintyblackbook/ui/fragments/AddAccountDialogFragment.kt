package com.bintyblackbook.ui.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bintyblackbook.R
import com.bintyblackbook.ui.activities.authentication.LoginActivity
import com.bintyblackbook.ui.activities.home.HomeActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.layout_add_account.*

class AddAccountDialogFragment(var homeActivity: HomeActivity) : BottomSheetDialogFragment() {
    private lateinit var behavior: BottomSheetBehavior<View>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return inflater.inflate(R.layout.layout_add_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val displayMetrics = DisplayMetrics()

        dialog?.setOnShowListener {
            val dialog = it as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
            behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.peekHeight = displayMetrics.heightPixels
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheet.layoutParams.width = ViewGroup.LayoutParams.FILL_PARENT
//            bottomSheet.layoutParams.height=ViewGroup.LayoutParams.WRAP_CONTENT

        }
    }

    override fun onActivityCreated(arg0: Bundle?) {
        super.onActivityCreated(arg0)

        setAdapter()
        tvAddAcc.setOnClickListener {
            dialog?.dismiss()
            startActivity(Intent(requireActivity(),LoginActivity::class.java))
            requireActivity()?.finishAffinity()
            //callback.callbackInterest(listOf)
        }
    }

    private fun setAdapter() {
//        rvInterests.layoutManager = LinearLayoutManager(requireContext())
//        rvInterests.adapter = sortAdapter
        //sortAdapter.recyclerItemClick=this
    }


}