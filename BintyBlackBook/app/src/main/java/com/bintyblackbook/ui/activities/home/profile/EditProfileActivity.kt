package com.bintyblackbook.ui.activities.home.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bintyblackbook.R
import com.bintyblackbook.util.ImagePickerUtility
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.toolbar.*

class EditProfileActivity : ImagePickerUtility() {

    override fun selectedImage(imagePath: String?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        iv_back.setOnClickListener {
            finish()
        }

        btnSave.setOnClickListener {
            finish()
        }

        headingText.text = getString(R.string.edit_profile)
        aboutTypingTimeScroll()

        edtUserName.setText(R.string.john)
        edtMobileNo.setText("9876543210")
        edtEmail.setText("John@gmail.com")
        edtAbout.setText(R.string.dummy_text)

        civ_profile.setOnClickListener {
            getImage(this,0)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun aboutTypingTimeScroll() {
        edtAbout.setOnTouchListener { view, motionEvent ->
            if (view.id == R.id.edtAbout) {
                view.parent.requestDisallowInterceptTouchEvent(true)
                when (motionEvent.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> view.parent.requestDisallowInterceptTouchEvent(false)
                }
            }

            false
        }
    }
}