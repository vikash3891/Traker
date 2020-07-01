package com.home.traker.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.home.traker.R
import com.home.traker.api.ApiClient
import com.home.traker.api.ApiInterface
import com.home.traker.api.RequestModel
import com.home.traker.api.ResponseModelClasses
import com.home.traker.base.BaseActivity
import com.home.traker.utils.Constants
import com.home.traker.utils.Utils
import kotlinx.android.synthetic.main.activity_registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationActivity : BaseActivity() {

    private var user_id = ""
    private var name = ""
    private var phone = ""
    private var details = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        try {
            if (intent != null && intent.hasExtra(Constants.DRIVER_ID)) {
                user_id = intent.getStringExtra(Constants.DRIVER_ID).toString()
                name = intent.getStringExtra(Constants.DRIVER_NAME).toString()
                phone = intent.getStringExtra(Constants.PHONE).toString()
                details = intent.getStringExtra(Constants.DETAILS).toString()
                if (intent.getStringExtra(Constants.PASSWORD) != null)
                    password = intent.getStringExtra(Constants.PASSWORD).toString()
                btnSubmitRegister.text = "Save"
            }

            clickPerform()
            updateUI()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun updateUI() {
        try {
            if (name != null)
                editName.setText(name)
            if (phone != null)
                editMobileNumber.setText(phone)
            if (details != null)
                editDriverDetails.setText(details)
            if (password != null)
                editPassword.setText(password)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Utils.isRegisterSuccess)
            finish()
    }

    private fun clickPerform() {
        try {
            btnSubmitRegister.setOnClickListener {
                //Toast.makeText(this, "Registration Submitted.", Toast.LENGTH_SHORT).show()
                validationField()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun validationField() {
        try {
            var allValid = true
            if (editName.text!!.isEmpty()) {
                showRegSuccessPopup("Please enter Driver Name")
                !allValid
                return
            } else if (editMobileNumber.text!!.isEmpty() || editMobileNumber.length() < 10) {
                showRegSuccessPopup("Please enter Driver's Phone Number")
                !allValid
                return
            } else if (editDriverDetails.text!!.isEmpty()) {
                showRegSuccessPopup("Please enter Driver Details")
                !allValid
                return
            } else if (allValid) {
                name = editName.text.toString()
                phone = editMobileNumber.text.toString()
                details = editDriverDetails.text.toString()
                password = editPassword.text.toString()
                registerDriver()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun registerDriver() = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.DRIVER_BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.RegistrationResponse> =
                apiService.registerUser(
                    RequestModel.getDriverRegistrationRequestModel(
                        user_id,
                        name,
                        phone,
                        details,
                        password,
                        ""
                    )
                )
            call.enqueue(object : Callback<ResponseModelClasses.RegistrationResponse> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.RegistrationResponse>,
                    response: Response<ResponseModelClasses.RegistrationResponse>
                ) {
                    try {
                        dismissDialog()

                        if (response.body() != null) {
                            if (response.body()!!.status == "0") {
                                showRegSuccessPopup(response.body()!!.message)
                            } else {

                                var alertDialog = AlertDialog.Builder(this@RegistrationActivity)
                                alertDialog.setTitle(getString(R.string.app_name))
                                alertDialog.setMessage("Driver Registered Successfully")

                                alertDialog.setPositiveButton("OK") { dialog, which ->
                                    dialog.dismiss()
                                    finish()
                                }

                                alertDialog.show()
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.RegistrationResponse>,
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
        showToast(getString(R.string.internet))
    }

    fun showRegSuccessPopup(message: String) {
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(getString(R.string.app_name))
        alertDialog.setMessage(message)

        alertDialog.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
        }

        alertDialog.show()
    }
}