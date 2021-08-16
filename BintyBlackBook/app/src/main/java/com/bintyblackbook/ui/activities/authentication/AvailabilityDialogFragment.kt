package com.bintyblackbook.ui.activities.authentication

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bintyblackbook.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AvailabilityDialogFragment(var mContext:Context) :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_availability)
    }

}
