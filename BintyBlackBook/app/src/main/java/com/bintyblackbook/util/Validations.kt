package com.bintyblackbook.util

import android.content.Context
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.bintyblackbook.R
import java.util.regex.Pattern

/*
 * Created by neha on 1/9/2018.
 */
class Validations {

    val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"

    companion object {
        /**
         * @param editText
         * - EditText field which need to be validated
         * @return - Returns true if editText is Null or empty
         */
        fun isNullOrEmpty(editText: EditText): Boolean {
            return editText.text == null || editText.text.toString().trim { it <= ' ' }.length == 0
        }

        private fun setError(context: Context, et: View, s: String) {

            if (et is EditText) {
                Toast.makeText(context, s, Toast.LENGTH_LONG).show()
                et.requestFocus()
            } else if (et is EditText) {
                Toast.makeText(context, s, Toast.LENGTH_LONG).show()

                et.requestFocus()
            }
        }

        fun isNullOrEmpty(textView: TextView): Boolean {
            return textView.text == null || textView.text.toString().trim { it <= ' ' }.length == 0
        }


        fun isContainSpecialCharacter(string: String): Boolean {
            return string.matches("[a-zA-Z0-9.? ]*".toRegex())
        }

        private fun validateFullnameLength(
            applicationContext: Context,
            mEtUsername: EditText,
            errMessage: String
        ): Boolean {
            if (isNullOrEmpty(mEtUsername) && mEtUsername.text.toString()
                    .trim { it <= ' ' }.length < 2
            ) {
                Toast.makeText(applicationContext, errMessage, Toast.LENGTH_LONG).show()
                //CommonUtils.showSnackbarMessage(applicationContext, errMessage, R.color.colorAccent)
                requestFocus(applicationContext, mEtUsername)
                return false
            }
            return true
        }

        private fun requestFocus(applicationContext: Context, view: View) {
            if (view.requestFocus()) {
//                val imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        private fun validatePasswordContainSpecialCharacter(
            applicationContext: Context,
            mEtPassword: EditText,
            errMessage: String
        ): Boolean {
            if (isContainSpecialCharacter(mEtPassword.text.toString().trim { it <= ' ' })) {
                Toast.makeText(applicationContext, errMessage, Toast.LENGTH_LONG).show()

                //CommonUtils.showSnackbarMessage(applicationContext, errMessage, R.color.colorAccent)
                mEtPassword.requestFocus()
                return false
            }
            return true
        }

        public fun validateEmailAddress(
            applicationContext: Context,
            view: View
        ): Boolean {
            val email = (view as EditText).text.toString().trim { it <= ' ' }
            if (email.isEmpty()) {

                Toast.makeText(applicationContext, applicationContext.resources.getString(R.string.err_email), Toast.LENGTH_LONG).show()

                requestFocus(applicationContext, view)
                return false
            } else if (!isValidEmail(email)) {
                Toast.makeText(applicationContext, applicationContext.resources.getString(R.string.err_valid_email_address), Toast.LENGTH_LONG).show()

                requestFocus(applicationContext, view)
                return false
            } else {
                return true
            }
        }


        public fun isValidEmail(email: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }


        private fun validateConfirmPassword(
            applicationContext: Context,
            mEtPassword: EditText,
            mEtCnfPassword: EditText,
            errMessage: String
        ): Boolean {
            if (!mEtPassword.text.toString().trim().equals(mEtCnfPassword.text.toString().trim())) {
                Toast.makeText(applicationContext, errMessage, Toast.LENGTH_LONG).show()
                //CommonUtils.showSnackbarMessage(applicationContext, errMessage, R.color.colorAccent)
                mEtCnfPassword.requestFocus()
                return false
            }
            return true
        }

        private fun validatePasswordLength(
            applicationContext: Context,
            mEtPassword: EditText,
            errMessage: String
        ): Boolean {
            if (mEtPassword.text.toString().trim { it <= ' ' }.length <= 6) {
                Toast.makeText(applicationContext, errMessage, Toast.LENGTH_LONG).show()
                mEtPassword.requestFocus()
                return false
            }
            return true
        }

        private fun validatePinLength(
            applicationContext: Context,
            mEtPassword: EditText,
            errMessage: String
        ): Boolean {
            if (mEtPassword.text.toString().trim { it <= ' ' }.length != 4) {
                Toast.makeText(applicationContext, errMessage, Toast.LENGTH_LONG).show()

                //CommonUtils.showSnackbarMessage(applicationContext, errMessage, R.color.colorAccent)
                mEtPassword.requestFocus()
                return false
            }
            return true
        }

        public fun validateCurrentPassword(
            applicationContext: Context,
            mEtCurrentPassword: EditText,
            errMessage: String
        ): Boolean {
            if (isNullOrEmpty(mEtCurrentPassword)) {
                Toast.makeText(applicationContext, errMessage, Toast.LENGTH_LONG).show()

                mEtCurrentPassword.requestFocus()
                return false

            }
            return true
        }

        fun validatePhoneNumber(
            applicationContext: Context,
            mEdPhoneNumber: EditText
        ): Boolean {
            if (isNullOrEmpty(mEdPhoneNumber)) {
                Toast.makeText(
                    applicationContext,
                    applicationContext.resources.getString(R.string.err_phone),
                    Toast.LENGTH_LONG
                ).show()

                /*CommonUtils.showSnackbarMessage(
                    applicationContext,
                    applicationContext.resources.getString(R.string.err_phone),
                    R.color.colorAccent
                )*/
                mEdPhoneNumber.requestFocus()
                return false
            } else if (mEdPhoneNumber.length() < 10 || mEdPhoneNumber.length() > 15) {
                Toast.makeText(
                    applicationContext,
                    applicationContext.resources.getString(R.string.err_valid_phone),
                    Toast.LENGTH_LONG
                ).show()

                /*CommonUtils.showSnackbarMessage(
                    applicationContext,
                    applicationContext.resources.getString(R.string.err_valid_phone),
                    R.color.colorAccent
                )*/
                mEdPhoneNumber.requestFocus()
                return false
            }

            return true
        }



        private fun validateTerms(
            applicationContext: Context,
            cb_signup: CheckBox,
            errMessage: String?
        ): Boolean {
            if (!cb_signup.isChecked) {
                Toast.makeText(applicationContext, errMessage, Toast.LENGTH_LONG).show()


                cb_signup.requestFocus()
                return false
            }
            return true
        }

        /*fun isValidateForgotPassword(applicationContext: Context, mEtEmail: EditText): Boolean {
            if (!validateEmailAddress(
                    applicationContext,
                    mEtEmail
                )
            ) {
                return false
            }
            if (!CommonUtils.isInternetConnection(applicationContext)) {
                CommonUtils.showSnackbarMessage(
                    applicationContext,
                    applicationContext.resources.getString(R.string.err_msg_internet),
                    R.color.colorAccent
                )
                return false
            }
            return true
        }

*/

        fun isEmpty(context: Context, et: EditText, hint: String): Boolean {
            val data = et.text.toString().trim { it <= ' ' }

            if (data.isEmpty()) {

                if (hint.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray().size > 1) {
                    setError(
                        context,
                        et, hint.toLowerCase()
                    )
                } else {
                    setError(
                        context,
                        et, hint.toLowerCase()
                    )
                }

                return false
            }
            return true
        }


        fun confirmPassword(
            context: Context,
            etNewPass: EditText,
            etConfirmPass: EditText,
            hint: String
        ): Boolean {

            val oldPassword = etNewPass.text.toString().trim { it <= ' ' }
            val newPassword = etConfirmPass.text.toString().trim { it <= ' ' }

            if (!oldPassword.matches(newPassword.toRegex())) {
                setError(
                    context,
                    etConfirmPass, hint.toLowerCase()
                )
               // Toasty.error(context!!, context.getString(R.string.passMatch)).show()
                return false
            }
            return true
        }


        fun isValidPassword(context: Context, et: EditText): Boolean {
            if (!isEmpty(context, et, "Please enter Valid Password")) {
                return false
            }
            val password = et.text.toString().trim { it <= ' ' }

            if (password.length > 7) {
                return true
            } else {
                setError(context, et, context.getString(R.string.validPass))
                return false
            }
        }

        fun isAlphaNumeric(context: Context, et: EditText): Boolean {

            var containsAlphabet = false
            var containsNumber = false

            containsAlphabet = containsAlphabets(et.text.toString().trim())
            containsNumber = containsNumber(et.text.toString().trim())

            if (containsAlphabet && containsNumber) {

                return true
            }
           // Toasty.error(context, context.getString(R.string.pass_alpha_numeric)).show()

            return false


        }

        fun containsAlphabets(string: String): Boolean {


            var pattern = Pattern.compile(".*[a-zA-Z]+.*")

            var matcher = pattern.matcher(string)

            if (matcher.matches()) {
                return true
            }

            return false

        }


        fun containsNumber(string: String): Boolean {


            var pattern = Pattern.compile("(.)*(\\d)(.)*")

            var matcher = pattern.matcher(string)

            if (matcher.matches()) {
                return true
            }

            return false

        }


        fun isValidPhoneNumber(context: Context, et: EditText, hint: String): Boolean {
            if (!isEmpty(context, et, "Please enter phone number.")) {
                return false
            }
            val number = et.text.toString().trim { it <= ' ' }

            try {
                if (!number.matches("[0]*[1-9][0-9]*".toRegex())) {
                    setError(
                        context,
                        et, hint.toLowerCase()
                    )
                    return false
                } else if (number.length < 8 || number.length > 16) {
                    setError(
                        context,
                        et, hint.toLowerCase()
                    )
                    return false
                }
                return true
            } catch (ex: NumberFormatException) {
                setError(
                    context,
                    et, hint.toLowerCase()
                )
                return false
            }
        }


    }

}