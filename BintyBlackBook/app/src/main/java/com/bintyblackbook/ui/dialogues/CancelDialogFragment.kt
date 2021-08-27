package com.bintyblackbook.ui.dialogues

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bintyblackbook.R
import com.bintyblackbook.ui.activities.home.loop.MyLoopsActivity
import com.bintyblackbook.ui.activities.home.notification.LoopRequestActivity
import kotlinx.android.synthetic.main.dialog_fragment_cancel.*


class CancelDialogFragment(var loopsActivity:LoopRequestActivity, var clickOn: String) : DialogFragment() {

    var status=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_fragment_cancel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (clickOn=="cancelBookingRequest"){
            status= "0"
            tvMessage.text = getString(R.string.are_you_sure_want_to_cancel_booking)
        }
        else if(clickOn=="acceptLoopRequest"){
            status="2"
            tvMessage.text= getString(R.string.are_you_sure_ypu_want_to_accept_request)
        }

        rlCross.setOnClickListener {
            dialog?.dismiss()
        }

        btnYes.setOnClickListener {
            loopsActivity.acceptRejectRequest(status)
            dialog?.dismiss()
        }

        btnNo.setOnClickListener {
            dialog?.dismiss()
        }
    }
}