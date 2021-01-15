package com.bintyblackbook.ui.activities.home.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.ReferAdapter
import kotlinx.android.synthetic.main.activity_my_wallet.*

class MyWalletActivity : AppCompatActivity() {

    var referAdapter: ReferAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_wallet)

        rlBack.setOnClickListener {
            finish()
        }

        referAdapter = ReferAdapter(this)
        rvRefer.adapter = referAdapter
        rvRefer.layoutManager = LinearLayoutManager(this)
    }
}