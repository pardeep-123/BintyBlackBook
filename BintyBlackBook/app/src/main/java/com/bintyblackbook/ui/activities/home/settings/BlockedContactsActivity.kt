package com.bintyblackbook.ui.activities.home.settings

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bintyblackbook.R
import com.bintyblackbook.adapters.AdapterBlockedContacts
import kotlinx.android.synthetic.main.activity_blocked_contacts.*
import kotlinx.android.synthetic.main.toolbar.*

class BlockedContactsActivity : AppCompatActivity(), View.OnClickListener {
    val context: Context =this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blocked_contacts)
        headingText.setText("Blocked Contacts")
        iv_back.setOnClickListener(this)
        rvBlockedContacts.adapter = AdapterBlockedContacts(context)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_back -> {
                finish()
            }
        }
    }
}