package com.home.traker.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.home.traker.BuildConfig
import com.home.traker.R
import com.home.traker.api.ApiClient
import com.home.traker.api.ApiInterface
import com.home.traker.api.RequestModel
import com.home.traker.api.ResponseModelClasses
import com.home.traker.base.BaseActivity
import com.home.traker.databinding.ActivityDriverActivityLogBinding
import com.home.traker.model.ListItemModel
import com.home.traker.navigators.OnItemSelectionListener
import com.home.traker.utils.AppPrefences
import com.home.traker.utils.Constants
import com.home.traker.utils.Utils
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_driver_activity_log.*
import kotlinx.android.synthetic.main.item_dialog.view.*
import kotlinx.android.synthetic.main.topbar_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DriverActivityLogScreen : BaseActivity(),
    OnItemSelectionListener {

    private val TAG: String = DriverActivityLogScreen::class.java.getSimpleName()

    override fun getPosition(position: Int, isForTeamA: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemClickListener(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var foodsList = ArrayList<String>()
    var driverLocation = ArrayList<String>()
    private lateinit var binding: ActivityDriverActivityLogBinding
    private lateinit var viewModel: ListItemModel
    private var isPunchedIn = false

    lateinit var mFusedLocationClient: FusedLocationProviderClient

    private var mLocationCallback: LocationCallback? = null

    private var mLastUpdateTime: String? = null

    // location updates interval - 10sec
    val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000
    var startLocationString: String? = null

    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 5000
    private val REQUEST_CHECK_SETTINGS = 100

    // bunch of location related apis
    private var mSettingsClient: SettingsClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocationSettingsRequest: LocationSettingsRequest? = null
    private var mCurrentLocation: Location? = null

    // boolean flag to toggle the ui
    private var mRequestingLocationUpdates: Boolean? = null


    private var currentLat: String? = null
    private var currentLong: String? = null
    private var isFirstTime: Boolean = true

    private var attendance_id: String = ""
    private var user_id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver_activity_log)
        topbar.imageSetting.background = resources.getDrawable(R.drawable.ic_account_white_24dp)
        init()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        driverLocation.add("" + 0.0 + " - " + 0.0)
        viewModel =
            ListItemModel("", AppPrefences.getUserID(this), "", "", "", "", "", "", "", "", "")
        binding.viewmodel = viewModel
        try {
            topbar.setBackgroundColor(resources.getColor(R.color.colorComparePreviousYear))
            topbar.imageSetting.visibility = View.VISIBLE
            foodsList.add("Share")
            foodsList.add("Logout")

            val resultTypeAdapter = ArrayAdapter(this, R.layout.spinner_row, foodsList)
            resultTypeAdapter.setDropDownViewResource(R.layout.spinner_row)
            topbar.spinner.adapter = resultTypeAdapter

            try {
                topbar.imageSetting.setOnClickListener {
                    startActivity(
                        Intent(
                            this@DriverActivityLogScreen,
                            ShareMyLocationActivity::class.java
                        )
                    )
                }

                textLogout.setOnClickListener {
                    logoutAlertDialog()
                }
                showList.setOnClickListener {
                    openDialog()
                }

                textPunchIn.setOnClickListener {

                    if (!isPunchedIn) {

                        textPunchIn.alpha = 0.2f
                        textPunchIn.animate().apply {
                            interpolator = LinearInterpolator()
                            startDelay = 1000
                            duration = 500
                            alpha(1f)
                            start()
                        }

                        isPunchedIn = true
                        startLocationButtonClick()
                    } else {
                        Toast.makeText(this, "Already Punch IN", Toast.LENGTH_SHORT).show()
                    }
                }

                textPunchOut.setOnClickListener {

                    if (isPunchedIn) {
                        punchInOutApi(isPunchedIn)
                        isPunchedIn = false
                        //Toast.makeText(this, "Punch OUT", Toast.LENGTH_SHORT).show()
                        textPunchOut.alpha = 0.2f
                        textPunchOut.animate().apply {
                            interpolator = LinearInterpolator()
                            startDelay = 1000
                            duration = 500
                            alpha(1f)
                            start()
                        }

                        Handler().removeCallbacks(null)
                        stopService(Intent(this, ForegroundService::class.java))

                        stopLocationButtonClick()
                    } else {
                        Toast.makeText(this, "Already Punch OUT", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun init() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mSettingsClient = LocationServices.getSettingsClient(this)

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                // location is received
                mCurrentLocation = locationResult.lastLocation

                mLastUpdateTime = DateFormat.getTimeInstance().format(Date())
                updateLocationUI()
            }
        }

        mRequestingLocationUpdates = false

        mLocationRequest = LocationRequest()
        mLocationRequest!!.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS)
        mLocationRequest!!.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS)
        mLocationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        mLocationSettingsRequest = builder.build()


    }

    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    private fun startLocationUpdates() {
        mSettingsClient?.checkLocationSettings(mLocationSettingsRequest)
            ?.addOnSuccessListener(this) {
                Log.i(
                    TAG,
                    "All location settings are satisfied."
                )
                Toast.makeText(
                    applicationContext,
                    "Started location updates!",
                    Toast.LENGTH_SHORT
                ).show()
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    // return
                }
                mFusedLocationClient!!.requestLocationUpdates(
                    mLocationRequest,
                    mLocationCallback, Looper.myLooper()
                )
                updateLocationUI()
            }?.addOnFailureListener(this) { e ->
                val statusCode = (e as ApiException).statusCode
                when (statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        Log.i(
                            TAG,
                            "Location settings are not satisfied. Attempting to upgrade " +
                                    "location settings "
                        )
                        try { // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(
                                this@DriverActivityLogScreen, REQUEST_CHECK_SETTINGS
                            )
                        } catch (sie: IntentSender.SendIntentException) {
                            Log.i(
                                TAG,
                                "PendingIntent unable to execute request."
                            )
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        val errorMessage =
                            "Location settings are inadequate, and cannot be " +
                                    "fixed here. Fix in Settings."
                        Log.e(
                            TAG,
                            errorMessage
                        )
                        Toast.makeText(
                            this@DriverActivityLogScreen,
                            errorMessage,
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
                updateLocationUI()
            }
    }

    fun startLocationButtonClick() {
        Dexter.withActivity(this)

            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    mRequestingLocationUpdates = true
                    startLocationUpdates()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    if (response.isPermanentlyDenied) {
                        openSettings()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    private fun openSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts(
            "package",
            BuildConfig.APPLICATION_ID, null
        )
        intent.data = uri
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun punchInOutApi(isChecked: Boolean) = if (Utils.isConnected(this)) {
        showDialog()
        try {
            var inTime = getTime()
            var outTime = getTime()

            if (!isChecked)
                outTime = ""
            else
                inTime = ""


            val apiService =
                ApiClient.getClient(Constants.DRIVER_BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.PunchModel> =
                apiService.setPunchInOut(
                    RequestModel.PunchInOuRequestModel(
                        isChecked,
                        viewModel.user_id!!,
                        getDate(),
                        getTime(),
                        currentLat!!, currentLong!!
                    )
                )
            call.enqueue(object : Callback<ResponseModelClasses.PunchModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.PunchModel>,
                    response: Response<ResponseModelClasses.PunchModel>
                ) {
                    try {
                        dismissDialog()
                        Log.d("Response:", response.body().toString())
                        if (response.body() != null) {
                            if (response.body()!!.status == "0") {
                                showSuccessPopup(response.body()!!.message)

                            } else {
                                var msg = ""
                                if (isPunchedIn)
                                    msg = "User Punched IN Successfully"
                                else
                                    msg = "User Punched Out Successfully"

                                attendance_id = response.body()!!.data[0].attendance_id
                                showSuccessPopup(msg)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.PunchModel>,
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

    private fun periodicLocationUpdateApi(isChecked: Boolean) = if (Utils.isConnected(this)) {
        showDialog()
        try {
            val apiService =
                ApiClient.getClient(Constants.DRIVER_BASE_URL).create(ApiInterface::class.java)
            val call: Call<ResponseModelClasses.PunchModel> =
                apiService.setPunchInOut(
                    RequestModel.LocationUpdateRequestModel(
                        attendance_id, currentLat!!, currentLong!!,
                        getDate(), viewModel.user_id!!
                    )
                )
            call.enqueue(object : Callback<ResponseModelClasses.PunchModel> {
                override fun onResponse(
                    call: Call<ResponseModelClasses.PunchModel>,
                    response: Response<ResponseModelClasses.PunchModel>
                ) {
                    try {
                        dismissDialog()
                        Log.d("Response:", response.body().toString())
                        if (response.body() != null) {
                            if (response.body()!!.status == "0") {
                                //showSuccessPopup(response.body()!!.message)

                            } else {
                                var msg = ""
                                if (isPunchedIn)
                                    msg = "User Punched IN Successfully"
                                else
                                    msg = "User Punched Out Successfully"

                                //showSuccessPopup(msg)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseModelClasses.PunchModel>,
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

    fun getTime(): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        println("Current Time is: $date.time")
        return inputFormat.format(date.time)
    }

    fun getDate(): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        println("Current Date is: $date.date")
        return inputFormat.format(date.time)
    }


    override fun onResume() {
        super.onResume()
        // Resuming location updates depending on button state and
        // allowed permissions
        dismissDialog()
        if (mRequestingLocationUpdates!! && checkPermissions()) {
            startLocationUpdates()
        }
        //updateLocationUI()
    }


    private fun checkPermissions(): Boolean {
        val permissionState: Int = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    var location = StringBuilder()

    fun openDialog() {
        try {
            val dialog = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.item_dialog, null)
            dialog.setView(view)
            dialog.setCancelable(true)

            view.listRecyc.text = location.toString()
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateLocationUI() {

        var addresses: List<Address?>
        val geocoder: Geocoder
        geocoder = Geocoder(this, Locale.getDefault())

        Log.d("Update UI", "Executed")
        if (mCurrentLocation != null) {

            startLocationString =
                "Lat: " + mCurrentLocation!!.getLatitude() + ", " +
                        "Lng: " + mCurrentLocation!!.getLongitude()
            addresses = geocoder.getFromLocation(
                mCurrentLocation!!.getLatitude(),
                mCurrentLocation!!.getLongitude(),
                1
            );
            val address = addresses[0]!!.getAddressLine(0)
            var drvLoc =
                "\n" + " - " + mLastUpdateTime + " - " + mCurrentLocation!!.getLatitude() + " - " + mCurrentLocation!!.getLongitude()

            Log.d("Loc: ", drvLoc)

            driverLocation.add(drvLoc)
            // + " - " + mLastUpdateTime + " - " + address

            currentLat = mCurrentLocation!!.getLatitude().toString()
            currentLong = mCurrentLocation!!.longitude.toString()
            if (!isFirstTime)
                periodicLocationUpdateApi(true)
            else {
                //updateLocationUI()
                isFirstTime = false
                punchInOutApi(isPunchedIn)
            }

            location.append(
                "\n" + mCurrentLocation!!.getLatitude() + " - " + mCurrentLocation!!.getLongitude()
                        + " - " + mLastUpdateTime + " - " + address
            )
            // txtUpdatedOn!!.text = "Last updated on: $mLastUpdateTime,$address"
            /*Toast.makeText(applicationContext, "see status" + mCurrentLocation, Toast.LENGTH_SHORT)
                .show()*/

        }

    }

    fun stopLocationButtonClick() {
        mRequestingLocationUpdates = false
        stopLocationUpdates()
    }

    fun stopLocationUpdates() {
        val geocoder: Geocoder
        var addresses: List<Address?>
        geocoder = Geocoder(this, Locale.getDefault())

        addresses = geocoder.getFromLocation(
            mCurrentLocation!!.getLatitude(),
            mCurrentLocation!!.getLongitude(),
            1
        );
        val address = addresses[0]!!.getAddressLine(0)
        Toast.makeText(
            applicationContext,
            address,
            Toast.LENGTH_SHORT
        ).show()
        //txtUpdatedOn!!.text = "Last updated on:$address"
        // Removing location updates
        mFusedLocationClient?.removeLocationUpdates(mLocationCallback)
            ?.addOnCompleteListener(this) {
                Toast.makeText(
                    applicationContext,
                    "Location updates stopped!",
                    Toast.LENGTH_SHORT
                ).show()
                //toggleButtons()
            }

    }

    private fun logoutAlertDialog() {
        var alertDialog = android.app.AlertDialog.Builder(this)
        alertDialog.setTitle(getString(R.string.app_name))
        alertDialog.setMessage("Are you sure you want to logout? ")
        alertDialog.setNeutralButton("Cancel") { _, _ ->
        }

        alertDialog.setPositiveButton("Yes") { dialog, which ->
            dialog.dismiss()

            AppPrefences.setLogin(this, false)

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        alertDialog.show()

    }

}