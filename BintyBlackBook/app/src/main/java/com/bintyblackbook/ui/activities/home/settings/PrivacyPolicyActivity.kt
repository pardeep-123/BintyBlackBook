package com.bintyblackbook.ui.activities.home.settings

import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bintyblackbook.R
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.util.getSecurityKey
import com.bintyblackbook.util.getUser
import com.bintyblackbook.viewmodel.SettingsViewModel
import kotlinx.android.synthetic.main.activity_privacy_policy.*

class PrivacyPolicyActivity : BaseActivity(), View.OnClickListener {

    lateinit var settingsViewModel: SettingsViewModel
    var type=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)
        settingsViewModel= SettingsViewModel(this)


        if (intent.getStringExtra("from").equals("terms")) {
            tvHeading.setText("TERMS & CONDITIONS")
            type="1"
        } else {
            tvHeading.setText("PRIVACY POLICY")
            type="2"
        }

        getContent()
        rlBack.setOnClickListener(this)

    }

    private fun getContent() {
        settingsViewModel.getContent(getSecurityKey(context)!!, getUser(context)?.authKey!!,type)
        settingsViewModel.contentLiveData.observe(this, Observer {

            if (it?.code==200){
                if(type.equals("1"))
                    tv_content.text = Html.fromHtml(it.data?.terms)
            }
            else{
                tv_content.text=Html.fromHtml(it.data?.privacy)
            }

        })
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.rlBack -> {
                finish()
            }
        }
    }
}