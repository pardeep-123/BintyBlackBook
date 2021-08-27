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
import kotlinx.android.synthetic.main.dialog_fragment_un_loop.*

class UnLoopDialogFragment(val loopsActivity: MyLoopsActivity, val name: String,val position: Int) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_fragment_un_loop, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        tvDesc.text= getString(R.string.are_you_sure_want_to_unloop_robert_pattinson) +" "+ name +" ?"
        btnYes.setOnClickListener {
            loopsActivity.unLoopRequest(position)
            dismiss()
        }

        btnNo.setOnClickListener {
            dismiss()
        }
    }
}