package com.bintyblackbook.ui.activities.home.promote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintyblackbook.R
import com.bintyblackbook.adapters.PaymentAdapter
import com.bintyblackbook.models.PaymentModel
import com.bintyblackbook.ui.dialogues.PromoteSuccessfulDialogFragment
import kotlinx.android.synthetic.main.activity_payment.*

class PaymentActivity : AppCompatActivity() {

    var paymentAdapter:PaymentAdapter? = null
    lateinit var arrayList: ArrayList<PaymentModel>

    var promote_type=""
    var subs_name=""
    var price=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        getIntentData()

        rlBack.setOnClickListener {
            finish()
        }

        llAddNew.setOnClickListener {
            startActivity(Intent(this,AddCardActivity::class.java))
        }

        btnPay.setOnClickListener {
            val dialog = PromoteSuccessfulDialogFragment()
            dialog.show(supportFragmentManager,"successfulPromote")
        }

    /*    arrayList = ArrayList()

        arrayList.add(PaymentModel(R.drawable.credit_card,"Credit card", "123 **** ****", true))
        arrayList.add(PaymentModel(R.drawable.paypal,"Paypal", "Please Login", false))
        arrayList.add(PaymentModel(R.drawable.icon_wallet,"use wallet money", "", false))

        paymentAdapter = PaymentAdapter(this,arrayList)
        rvPayment.adapter = paymentAdapter
        rvPayment.layoutManager = LinearLayoutManager(this)*/

    }

    private fun getIntentData() {
        subs_name= intent.getStringExtra("subs_name").toString()
        price= intent.getStringExtra("price").toString()
        promote_type= intent.getStringExtra("promote_type").toString()

        if(promote_type == "business"){
            tvHeading.text= getString(R.string.promote_your_business)
        }else{
            tvHeading.text= getString(R.string.promote_your_events)
        }

        tvType.text= subs_name
        tvPrice.text= price

    }
}