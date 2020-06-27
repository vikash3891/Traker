package com.home.traker.model

import android.content.Context
import android.text.SpannableStringBuilder
import android.view.View
import com.home.traker.base.BaseViewModel
import com.home.traker.navigators.LoginNavigator
import org.json.JSONObject

class LoginViewModel(var context: Context) : BaseViewModel<LoginNavigator>(),
    View.OnFocusChangeListener {
    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var spanTxt = SpannableStringBuilder()
    var apiErrorMessage = ""
    private var view: View? = null
    private var jsonObject = JSONObject()
    private var name = ""
    private var email = ""
    private var profileImage = ""


    init {
        createSpannableStringForLogin()
    }

    /*This method is used to create highlighted text span.*/
    private fun createSpannableStringForLogin() {
        /*spanTxt = SpannableStringBuilder(context.getString(R.string.dont_have_account))
        spanTxt.append(" ")
        spanTxt.append(context.getString(R.string.sign_up))
        spanTxt.setSpan(
            ForegroundColorSpan(utility.getColorWithApiCheck(R.color.colorAccent)),
            22,
            spanTxt.length,
            0
        )*/

    }

}
