package com.bintyblackbook.ui.activities.home.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.bintyblackbook.R
import com.bintyblackbook.adapters.EditMessagesAdapter
import com.bintyblackbook.model.AllData
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.LoopsViewModel
import kotlinx.android.synthetic.main.activity_edit_message.*
import kotlinx.android.synthetic.main.activity_video_call_list.*

class VideoCallActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_call)


    }


}