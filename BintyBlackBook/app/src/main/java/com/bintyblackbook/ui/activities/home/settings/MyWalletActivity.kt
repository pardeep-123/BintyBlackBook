package com.bintyblackbook.ui.activities.home.settings

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.ReferAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.model.WalletData
import com.bintyblackbook.viewmodel.PaymentViewModel
import kotlinx.android.synthetic.main.activity_my_wallet.*

class MyWalletActivity : BaseActivity() {

    lateinit var paymentViewModel: PaymentViewModel
    var referAdapter: ReferAdapter? = null
    var walletList= ArrayList<WalletData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_wallet)

        rlBack.setOnClickListener {
            finish()
        }

        setAdapter()

    }

    private fun setAdapter() {
        rvRefer.layoutManager = LinearLayoutManager(this)
        referAdapter = ReferAdapter(this)
        rvRefer.adapter = referAdapter
        referAdapter?.walletList=walletList
    }
}