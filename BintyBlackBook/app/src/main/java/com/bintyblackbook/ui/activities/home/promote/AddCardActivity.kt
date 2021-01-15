package com.bintyblackbook.ui.activities.home.promote

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bintyblackbook.R
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment
import kotlinx.android.synthetic.main.activity_add_card.*
import java.text.SimpleDateFormat
import java.util.*

class AddCardActivity : AppCompatActivity() {

    lateinit var monthYearPickerDialog: MonthYearPickerDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        datePickDialog()
        rlBack.setOnClickListener {
            finish()
        }

        edtDate.setOnClickListener {
            monthYearPickerDialog.show(supportFragmentManager, "DateYearPicker")
        }

        btnSave.setOnClickListener {
            finish()
        }
    }

    private fun datePickDialog() {
        monthYearPickerDialog = MonthYearPickerDialogFragment
            .getInstance(
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.YEAR)
            )


        monthYearPickerDialog.setOnDateSetListener { year, monthOfYear ->
            val calendar = Calendar.getInstance()
            calendar[Calendar.YEAR] = year
            calendar[Calendar.MONTH] = monthOfYear

            val dateFormat = SimpleDateFormat("MMM yyyy")
            val date = dateFormat.format(calendar.time)
            edtDate.setText(date)

        }
    }
}