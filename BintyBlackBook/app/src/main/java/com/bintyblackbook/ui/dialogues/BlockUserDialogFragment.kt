package com.bintyblackbook.ui.dialogues

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bintyblackbook.R
import kotlinx.android.synthetic.main.dialog_fragment_block_user.*


class BlockUserDialogFragment(var clickOn: String) : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_fragment_block_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (clickOn=="unBlockUser"){
            tvMessage.text = getString(R.string.unblock_john)
        }

        rlCross.setOnClickListener {
            dismiss()
        }

        btnYes.setOnClickListener {
            dismiss()
        }

        btnNo.setOnClickListener {
            dismiss()
        }
    }
}