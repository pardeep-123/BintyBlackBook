package com.bintyblackbook.ui.activities.home.promote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bintyblackbook.R
import kotlinx.android.synthetic.main.activity_promote_pay.*

class PromotePayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promote_pay)

        rlBack.setOnClickListener {
            finish()
        }

        btnPay.setOnClickListener {
            startActivity(Intent(this, PaymentActivity::class.java))
        }

    }
}