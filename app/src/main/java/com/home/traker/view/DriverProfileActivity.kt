package com.home.traker.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.home.traker.R
import com.home.traker.api.ApiClient
import com.home.traker.api.ApiInterface
import com.home.traker.api.ResponseModelClasses
import com.home.traker.base.BaseActivity
import com.home.traker.databinding.LayoutDriverProfileBinding
import com.home.traker.model.ListItemModel
import com.home.traker.utils.Constants
import com.home.traker.utils.Utils
import kotlinx.android.synthetic.main.layout_driver_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DriverProfileActivity : BaseActivity() {

    private lateinit var viewDataBinding: LayoutDriverProfileBinding
    private lateinit var driverDetails: ListItemModel

    var driver_id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_driver_profile)

        if (intent != null && intent.hasExtra(Constants.DRIVER_ID)) {
            driver_id = intent.getStringExtra(Constants.DRIVER_ID).toString()
            getDriverListAPI(driver_id)
        }

        checkAttendance.setOnClickListener {

            try {
                var intent1 = Intent(this, ListAttendanceActivity::class.java)
                intent1.putExtra(Constants.DRIVER_ID, driverDetails.user_id)
                intent1.putExtra(Constants.DRIVER_NAME, driverDetails.drive_name)
                startActivity(intent1)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        driverRoute.setOnClickListener {

            try {
                var intent1 = Intent(this, ListVendorRouteActivity::class.java)
                intent1.putExtra(Constants.DRIVER_ID, driverDetails.id)
                intent1.putExtra(Constants.DRIVER_NAME, driverDetails.drive_name)
                startActivity(intent1)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        editDriver.setOnClickListener {
            //startActivity(Intent(this, RegistrationActivity::class.java))

            var intent1 = Intent(this, RegistrationActivity::class.java)
            intent1.putExtra(Constants.DRIVER_ID, driverDetails.id)
            intent1.putExtra(Constants.DRIVER_NAME, driverDetails.drive_name)
            intent1.putExtra(Constants.PHONE, driverDetails.phone)
            intent1.putExtra(Constants.DETAILS, driverDetails.details)
            intent1.putExtra(Constants.PASSWORD, driverDetails.pasword)
            startActivity(intent1)
        }
    }

    private fun getDriverListAPI(driverID: String) = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.DRIVER_BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.DriverListResponseModel> =
                apiService.getDriverProfile(driverID)
            call.enqueue(object : Callback<ResponseModelClasses.DriverListResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.DriverListResponseModel>,
                    response: Response<ResponseModelClasses.DriverListResponseModel>
                ) {
                    try {
                        dismissDialog()
                        Log.d("Response:", response.body().toString())
                        if (response.body() != null) {
                            if (response.body()!!.status == "0") {
                                showSuccessPopup(response.body()!!.message)
                            } else {
                                driverDetails = response.body()!!.data[0]
                                updateUI()
                                //showSuccessPopup(response.body()!!.message)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.DriverListResponseModel>,
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

    private fun updateUI() {
        driverName.text = driverDetails.drive_name
        driverPhoneNumber.text = driverDetails.phone
        driverPhoneAddress.text = driverDetails.details
    }

}