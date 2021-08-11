package com.bintyblackbook.ui.activities.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.bintyblackbook.R
import com.bintyblackbook.base.BaseActivity
import com.bintyblackbook.util.*
import com.bintyblackbook.viewmodel.ChangePasswordViewModel
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.toolbar.*

class ChangePasswordActivity : BaseActivity(), View.OnClickListener {

    lateinit var changePasswordViewModel: ChangePasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        headingText.setText("CHANGE PASSWORD")

        changePasswordViewModel= ChangePasswordViewModel(this)
        setOnClicks()

    }

    private fun setOnClicks() {
        iv_back.setOnClickListener(this)
        updateBtn.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_back -> {
                finish()
            }
            R.id.updateBtn -> {

                if(InternetCheck.isConnectedToInternet(this)
                    && Validations.isEmpty(this,etOldPass,getString(R.string.err_old_pass))
                    && Validations.isEmpty(this,etNewPass,getString(R.string.err_new_pass))
                    && Validations.confirmPassword(this,etNewPass,etConfirmPass,getString(R.string.pass_does_not_match))
                ){
                    changePasswordViewModel.changePassword(getSecurityKey(this)!!,
                        getUser(this)?.authKey.toString(),
                    etOldPass.text.toString(),etConfirmPass.text.toString())
                    setObservables()
                }
                finish()
            }
        }
    }

    private fun setObservables() {
        changePasswordViewModel.changePassLiveData.observe(this, Observer {
            if(it.code==200){
                showAlert(it.msg.toString())
                finish()
            }
            else{
                Log.e("TAG",it.msg.toString())
                showAlert(it.msg.toString())
            }
        })
    }
}