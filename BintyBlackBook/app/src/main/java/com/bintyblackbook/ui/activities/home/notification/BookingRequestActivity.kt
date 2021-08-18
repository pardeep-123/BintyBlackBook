
package com.bintyblackbook.ui.activities.home.notification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bintyblackbook.R
import com.bintyblackbook.adapters.BookingRequestAdapter
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.ui.activities.home.UserDetailActivity
import com.bintyblackbook.ui.activities.home.bookings.MyBookingsActivity
import com.bintyblackbook.ui.dialogues.CancelDialogFragment
import kotlinx.android.synthetic.main.activity_booking_request.*

class BookingRequestActivity : BaseActivity() {

    var bookingRequestAdapter:BookingRequestAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_request)

        rlBack.setOnClickListener {
            onBackPressed()
        }
        bookingRequestAdapter = BookingRequestAdapter(this)
        rvBookingRequest.adapter = bookingRequestAdapter
        adapterItemBtnClick()
    }

    private fun adapterItemBtnClick(){
        bookingRequestAdapter?.onItemBtnClick = {clickOn ->
            if (clickOn == "accept"){
                startActivity(Intent(this, MyBookingsActivity::class.java))
            }else if (clickOn == "cancel"){
                val dialog  = CancelDialogFragment("cancelBookingRequest")
                dialog.show(supportFragmentManager,"cancelBookingRequest")
            }
        }
    }
}