package com.bintyblackbook.ui.activities.authentication

import android.os.Bundle
import android.util.Log
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import org.json.JSONObject


class FacebookAuth(
        private val loginButton: LoginButton,
        private val mCallbackManager: CallbackManager,
        private val fbResult: FbResult
) {

    /**
     * function for allow user to handle facebook login
     */
    fun allowUserToFacebookLogin() {
      //  loginButton.loginBehavior = LoginBehavior.WEB_VIEW_ONLY
        loginButton.registerCallback(mCallbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(result: LoginResult?) {
                val request = GraphRequest.newMeRequest(result?.accessToken) { `object`, response ->
                    try {
                        if (`object`.has("id")) {
                            result?.accessToken?.let {
                                fbResult.fbResult(it.token, `object`)
                            }
                            Log.e("FBLOGIN_SUCCESS", `object`.toString())
                        } else {
                            Log.e("FBLOGIN_FAILD", `object`.toString())
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.e("FBLOGIN_FAILD", "E", e)
                    }
                }

                val parameters = Bundle()
                parameters.putString("fields", "name,email,id,picture.type(large)")
                request.parameters = parameters
                request.executeAsync()
            }

            override fun onCancel() {
                fbResult.onFbCancel()
            }

            override fun onError(error: FacebookException) {
                Log.d("FacebookAuth", "---fb_error----" + error.message)
            }

        })
    }

    /**
     * function for handle facebook token
     */
    interface FbResult {
        fun fbResult(token: String, jsonObj: JSONObject)
        fun onFbCancel()
    }
}