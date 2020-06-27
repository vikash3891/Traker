package com.home.traker.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.home.traker.R
import com.home.traker.api.ApiClient
import com.home.traker.api.ApiInterface
import com.home.traker.api.RequestModel
import com.home.traker.api.ResponseModelClasses
import com.home.traker.base.BaseActivity
import com.home.traker.utils.AppPrefences
import com.home.traker.utils.Constants
import com.home.traker.utils.Utils
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {

    var isAdmin = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        try {
            clickPerform()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onResume() {
        super.onResume()
        if (AppPrefences.getRememberMe(this)!!) {
            userName.setText(AppPrefences!!.getUserID(this))
            editPassword.setText(AppPrefences!!.getPassword(this))

        }
    }

    private fun validationFields() {
        try {
            var isValid = true

            if (userName.text!!.isEmpty() && editPassword.text!!.isEmpty()) {
                showSuccessPopup("Please enter Email/Password")
                !isValid
                return
            } else if (userName.text!!.isEmpty()) {
                showSuccessPopup("Please enter valid Email")
                !isValid
                return
            } else if (editPassword.text!!.isEmpty()) {
                showSuccessPopup("Password cannot be empty")
                !isValid
                return
            } else if (isValid) {

                loginApi(false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun clickPerform() {
        try {
            loginAsDriver.setOnClickListener {
                Toast.makeText(this, "Login as Driver", Toast.LENGTH_SHORT).show()

                header.setBackgroundColor(resources.getColor(R.color.colorComparePreviousYear))
                login.background = resources.getDrawable(R.drawable.drawable_round_corner_red)

                loginTitle.setText("Traker")

                loginAsDriver.visibility = View.GONE
                loginAsAdmin.visibility = View.VISIBLE

                isAdmin = false
            }

            loginAsAdmin.setOnClickListener {
                Toast.makeText(this, "Login as Admin", Toast.LENGTH_SHORT).show()

                header.setBackgroundColor(resources.getColor(R.color.colorAccent))
                login.background = resources.getDrawable(R.drawable.drawable_round_corner_blue)
                loginTitle.setText("Traker-Admin")

                loginAsAdmin.visibility = View.GONE
                loginAsDriver.visibility = View.VISIBLE

                isAdmin = true
            }

            login.setOnClickListener {
                validationFields()

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loginApi(isChecked: Boolean) = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.LoginResponseModel> =
                apiService.getAdminLogin(
                    RequestModel.getLoginRequestModel(
                        isAdmin,
                        userName.text.toString(),
                        editPassword.text.toString()
                    )
                )
            call.enqueue(object : Callback<ResponseModelClasses.LoginResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.LoginResponseModel>,
                    response: Response<ResponseModelClasses.LoginResponseModel>
                ) {
                    try {
                        dismissDialog()
                        Log.d("Response:", response.body().toString())
                        if (response.body() != null) {
                            if (response.body()!!.status == "0") {
                                showSuccessPopup(response.body()!!.message)
                            } else {
                                /*AppPrefences.setLoginUserInfo(
                                    this@LoginActivity,
                                    response.body()!!.data
                                )
*/
                                AppPrefences.setLogin(this@LoginActivity, true)
                                AppPrefences.setRememberMe(this@LoginActivity, true)
                                AppPrefences.setUserID(
                                    this@LoginActivity,
                                    userName.text.toString()
                                )
                                AppPrefences.setPassword(
                                    this@LoginActivity,
                                    editPassword.text.toString()
                                )
                                AppPrefences.setIsAdmin(
                                    this@LoginActivity,
                                    isAdmin
                                )
                                if (isAdmin) {
                                    Constants.isAdmin = true
                                    startActivity(
                                        Intent(
                                            this@LoginActivity,
                                            ListActivity::class.java
                                        )
                                    )
                                } else {
                                    Constants.isAdmin = false
                                    startActivity(
                                        Intent(
                                            this@LoginActivity,
                                            DriverActivityLogScreen::class.java
                                        )
                                    )
                                }
                                finish()
                                //showSuccessPopup(response.body()!!.message)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.LoginResponseModel>,
                    t: Throwable
                ) {
                    Log.d("Throws:", t.message.toString())
                    dismissDialog()
                }
            })

        } catch (e: Exception) {
            e.printStackTrace()
            dismissDialog()
        }

    } else {
        dismissDialog()
        showToast(getString(R.string.internet))
    }
}