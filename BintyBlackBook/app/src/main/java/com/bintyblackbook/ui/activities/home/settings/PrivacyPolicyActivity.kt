package com.bintyblackbook.ui.activities.home.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bintyblackbook.R
import kotlinx.android.synthetic.main.toolbar.*

class PrivacyPolicyActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)
        if(intent.getStringExtra("from").equals("terms")){
            headingText.setText("TERMS & CONDITIONS")
        }else{
            headingText.setText("PRIVACY POLICY")
        }
        iv_back.setOnClickListener(this)

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_back -> {
                finish()
            }
        }
    }
}