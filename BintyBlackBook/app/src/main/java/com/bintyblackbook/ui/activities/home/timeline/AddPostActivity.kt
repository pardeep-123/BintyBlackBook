package com.bintyblackbook.ui.activities.home.timeline

import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bintyblackbook.R
import com.bintyblackbook.util.AppConstant
import com.bintyblackbook.util.ImagePickerUtility
import kotlinx.android.synthetic.main.activity_add_post.*

class AddPostActivity : ImagePickerUtility() {

    override fun selectedImage(imagePath: String?) {

    }

    override fun selectedVideoUri(videoUri: Uri?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        rlBack.setOnClickListener {
            finish()
        }

        val heading = intent.getStringExtra(AppConstant.HEADING)
        if (heading != null){
            tvHeading.text = heading
            rivCamera.setImageResource(R.drawable.background)
            edtDesc.setText(R.string.dummy_text)
            btnPost.text = getString(R.string.save)
        }

        rivCamera.setOnClickListener {
            getImage(this,0,false)
        }

        btnPost.setOnClickListener {
            finish()
        }
    }
}