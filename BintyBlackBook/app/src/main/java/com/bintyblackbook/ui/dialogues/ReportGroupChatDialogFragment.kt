package com.bintyblackbook.ui.dialogues

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bintyblackbook.R
import com.bintyblackbook.ui.activities.home.message.GroupChatActivity
import kotlinx.android.synthetic.main.dialog_fragment_block_user.btnNo
import kotlinx.android.synthetic.main.dialog_fragment_block_user.btnYes
import kotlinx.android.synthetic.main.dialog_fragment_block_user.rlCross
import kotlinx.android.synthetic.main.dialog_fragment_block_user.tvMessage
import kotlinx.android.synthetic.main.dialog_fragment_report_user.*

class ReportGroupChatDialogFragment(var name:String, val groupChatActivity: GroupChatActivity) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_fragment_report_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        tvMessage.text = getString(R.string.why_do_you_want_to_report_this_user)

        rlCross.setOnClickListener {
            dismiss()
        }

        btnYes.setOnClickListener {
            dismiss()
            //add action here
            if(edtReport.text.toString().trim().isNullOrEmpty()){
                Toast.makeText(context,"Please enter the reason",Toast.LENGTH_SHORT).show()
            }else{
                groupChatActivity.reportGroup(edtReport.text.toString())
            }
        }

        btnNo.setOnClickListener {
            dismiss()
        }
    }
}