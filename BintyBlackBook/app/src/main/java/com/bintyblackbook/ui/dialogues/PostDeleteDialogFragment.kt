package com.bintyblackbook.ui.dialogues

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bintyblackbook.R
import com.bintyblackbook.ui.activities.home.timeline.TimelineActivity
import kotlinx.android.synthetic.main.dialog_fragment_post_delete.*


class PostDeleteDialogFragment(val timelineActivity: TimelineActivity, val position: Int) : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_fragment_post_delete, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btnYes.setOnClickListener {
            dismiss()
            timelineActivity.deletePost(position)
        }

        btnNo.setOnClickListener {
            dismiss()
        }
    }
}