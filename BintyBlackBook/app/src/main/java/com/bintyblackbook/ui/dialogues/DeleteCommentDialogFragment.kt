package com.bintyblackbook.ui.dialogues

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bintyblackbook.R
import com.bintyblackbook.ui.activities.home.timeline.CommentsActivity
import kotlinx.android.synthetic.main.dialog_fragment_cancel.btnNo
import kotlinx.android.synthetic.main.dialog_fragment_cancel.btnYes
import kotlinx.android.synthetic.main.dialog_fragment_cancel.tvMessage
import kotlinx.android.synthetic.main.dialog_fragment_delete_comment.*


class DeleteCommentDialogFragment(var commentsActivity: CommentsActivity,var position: Int) : DialogFragment() {

    var status=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_fragment_delete_comment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        tvMsgComment.text = getString(R.string.delete_comment)

        btnYesComment.setOnClickListener {
           commentsActivity.deleteComment(position)
            dialog?.dismiss()
        }

        btnNoComment.setOnClickListener {
            dialog?.dismiss()
        }
    }
}