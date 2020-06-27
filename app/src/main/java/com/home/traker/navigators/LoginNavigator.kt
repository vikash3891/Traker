package com.home.traker.navigators

import android.os.Bundle
import com.google.android.material.textfield.TextInputLayout
import android.view.View

/**
 * Created by bms on 27/2/18 added in com.sportsfanatiq.cricket.login
 */
interface LoginNavigator {

    fun onHover(view: View)
    fun validateFields()
    fun onTextChanged(view: TextInputLayout)
    fun launchActivity(activityCode: Int, bundle: Bundle = Bundle())
    fun updateApiError()
    fun animateSpan()
    fun googleSignUp()
    fun facebookSignUp()
    fun socialMediaLogOut()
}