package com.bintyblackbook.ui.dialogues

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bintyblackbook.R
import com.bintyblackbook.ui.activities.home.message.ChatActivity
import kotlinx.android.synthetic.main.dialog_fragment_block_user.*


class BlockUserDialogFragment(var name: String,var chatActivity: ChatActivity) : DialogFragment() {


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

        rlCross.visibility=View.GONE

        tvMessage.text = getString(R.string.are_you_sure_want_to_block_john)+" "+name+"?"

        rlCross.setOnClickListener {
            dismiss()
        }

        btnYes.setOnClickListener {
            dismiss()
            chatActivity.blockUser()
        }

        btnNo.setOnClickListener {
            dismiss()
        }
    }
}