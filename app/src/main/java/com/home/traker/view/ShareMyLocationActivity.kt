package com.home.traker.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.FirebaseApp
import com.home.traker.R
import com.home.traker.api.ApiClient
import com.home.traker.api.ApiInterface
import com.home.traker.api.RequestModel
import com.home.traker.api.ResponseModelClasses
import com.home.traker.base.BaseActivity
import com.home.traker.helper.UiHelper
import com.home.traker.navigators.IPositiveNegativeListener
import com.home.traker.utils.Constants
import com.home.traker.utils.Utils
import kotlinx.android.synthetic.main.activity_share_live_location.*
import kotlinx.android.synthetic.main.layout_driver_profile.*
import kotlinx.android.synthetic.main.topbar_layout.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ShareMyLocationActivity : BaseActivity() {

    companion object {
        private const val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2200
    }

    private val uiHelper = UiHelper()
    private lateinit var locationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var locationFlag = true

    private var name = ""
    private var phone = ""
    private var details = ""
    private var lat = ""
    private var long = ""
    private var user_id = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_live_location)

        FirebaseApp.initializeApp(this)

        createLocationCallback()
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = uiHelper.getLocationRequest()
        if (!uiHelper.isPlayServicesAvailable(this)) {
            Toast.makeText(this, "Play Services did not installed!", Toast.LENGTH_SHORT).show()
            finish()
        } else requestLocationUpdate()

        lytAction.spinner.visibility = View.GONE
        lytAction.setBackgroundColor(resources.getColor(R.color.colorComparePreviousYear))

        clickPerform()
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
            if (vendorNameValue.text!!.isEmpty()) {
                showSuccessPopup("Please enter Vendor Name")
                !allValid
                return
            } else if (editMobileNumber.text!!.isEmpty() || editMobileNumber.length() < 10) {
                showSuccessPopup("Please enter Vendor's Phone Number")
                !allValid
                return
            } else if (editDriverDetails.text!!.isEmpty()) {
                showSuccessPopup("Please enter Driver Details")
                !allValid
                return
            } else if (allValid) {
                name = vendorNameValue.text.toString()
                phone = editMobileNumber.text.toString()
                details = editDriverDetails.text.toString()
                registerVendor()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun registerVendor() = if (Utils.isConnected(this)) {
        showDialog()
        try {

            val apiService =
                ApiClient.getClient(Constants.DRIVER_BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.RegistrationResponse> =
                apiService.shareLiveLocation(
                    RequestModel.setVendorDetailsRequestModel(
                        user_id,
                        name,
                        phone,
                        details,
                        getDateTime(),
                        "", lat, long
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
                                showSuccessPopup(response.body()!!.message)
                            } else {
                                var alertDialog = AlertDialog.Builder(this@ShareMyLocationActivity)
                                alertDialog.setTitle(getString(R.string.app_name))
                                alertDialog.setMessage("Live Location shared with Vendor Details")

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
        //showToast(getString(R.string.internet))
    }

    fun getDateTime(): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        println("Current Date and Time is: $date.time")
        return inputFormat.format(date.time)
    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdate() {
        if (!uiHelper.isHaveLocationPermission(this)) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
            return
        }
        if (uiHelper.isLocationProviderEnabled(this))
            uiHelper.showPositiveDialogWithListener(
                this,
                resources.getString(R.string.need_location),
                resources.getString(R.string.location_content),
                object :
                    IPositiveNegativeListener {
                    override fun onPositive() {
                        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                },
                "Turn On",
                false
            )
        locationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            val value = grantResults[0]
            if (value == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Location Permission denied", Toast.LENGTH_SHORT).show()
                finish()
            } else if (value == PackageManager.PERMISSION_GRANTED) requestLocationUpdate()
        }
    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                if (locationResult!!.lastLocation == null) return
                val latLng = LatLng(
                    locationResult.lastLocation.latitude,
                    locationResult.lastLocation.longitude
                )
                Log.e("Location", latLng.latitude.toString() + " , " + latLng.longitude)
                lat = latLng.latitude.toString()
                long = latLng.longitude.toString()
                if (locationFlag) {
                    locationFlag = false
                    //animateCamera(latLng)

                }
            }
        }
    }


}