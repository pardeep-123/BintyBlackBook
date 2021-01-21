package com.bintyblackbook.ui.activities.home.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bintyblackbook.R
import kotlinx.android.synthetic.main.activity_privacy_policy.*

class PrivacyPolicyActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)
        if (intent.getStringExtra("from").equals("terms")) {
            tvHeading.setText("TERMS & CONDITIONS")
        } else {
            tvHeading.setText("PRIVACY POLICY")
        }
        rlBack.setOnClickListener(this)

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_back -> {
                finish()
            }
        }
    }
}