package com.home.traker.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.telephony.CellLocation.requestLocationUpdate
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.home.traker.helper.GoogleMapHelper
import com.home.traker.helper.MarkerAnimationHelper
import com.home.traker.helper.UiHelper
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.FirebaseApp
import com.home.traker.R
import com.home.traker.helper.FireBaseHelper
import com.home.traker.navigators.IPositiveNegativeListener
import com.home.traker.navigators.LatLngInterpolator
import com.home.traker.utils.Constants
import kotlinx.android.synthetic.main.activity_attendance_list.*

class DriverRouteMapActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnPolylineClickListener, GoogleMap.OnPolygonClickListener {
    override fun onPolylineClick(p0: Polyline?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPolygonClick(p0: Polygon?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2200
    }

    private lateinit var googleMap: GoogleMap
    private lateinit var locationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var locationFlag = true
    private var driverOnlineFlag = false
    private var currentPositionMarker: Marker? = null
    private val googleMapHelper = GoogleMapHelper()
    private val firebaseHelper = FireBaseHelper("0000")
    private val markerAnimationHelper = MarkerAnimationHelper()
    private val uiHelper = UiHelper()
    private var driverLatLong: LatLng? = null
    private var isMapView: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_attendance_list)

            if (intent != null && intent.hasExtra(Constants.LAT)) {
                driverLatLong = LatLng(
                    intent.getStringExtra(Constants.LAT).toDouble(),
                    intent.getStringExtra(Constants.LONG).toDouble()
                )
                isMapView = intent.getBooleanExtra(Constants.IS_ROUTE_MAP_VIEW, false)
            }
            driveDate.text = "Vendor Visit"
            driveDateIn.visibility = View.GONE
            driveDateOut.visibility = View.GONE

            layoutMap.visibility = View.VISIBLE
            layoutList.visibility = View.GONE

            FirebaseApp.initializeApp(this)

            val mapFragment: SupportMapFragment =
                supportFragmentManager.findFragmentById(R.id.supportMap) as SupportMapFragment
            mapFragment.getMapAsync { googleMap = it }
            createLocationCallback()
            locationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            locationRequest = uiHelper.getLocationRequest()
            if (!uiHelper.isPlayServicesAvailable(this)) {
                Toast.makeText(this, "Play Services did not installed!", Toast.LENGTH_SHORT).show()
                finish()
            } else requestLocationUpdate()

            addDriver.setOnClickListener {
                startActivity(Intent(this, RegistrationActivity::class.java))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setMonth(monthOfYear: Int): String {
        when (monthOfYear) {
            1 -> return "Jan"
            2 -> return "Feb"
            3 -> return "Mar"
            4 -> return "Apr"
            5 -> return "May"
            6 -> return "Jun"
            7 -> return "Jul"
            8 -> return "Aug"
            9 -> return "Sep"
            10 -> return "Oct"
            11 -> return "Nov"
            12 -> return "Dec"
            else -> { // Note the block
                return "June"
            }
        }
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
                if (locationFlag) {
                    locationFlag = false

                    if (!isMapView) {
                        if (driverLatLong != null) {
                            animateCamera(driverLatLong!!)
                            showOrAnimateMarker(driverLatLong!!)
                        } else {
                            animateCamera(latLng)
                            showOrAnimateMarker(latLng)
                        }
                    } else {
                        val polyline1: Polyline = googleMap.addPolyline(
                            PolylineOptions()
                                .clickable(true)
                                .add(
                                    LatLng(28.6139, 77.2090),
                                    LatLng(28.5355, 77.3910),
                                    LatLng(28.4595, 77.0266),
                                    LatLng(28.7892, 77.5161)
                                )
                        )

                        animateCamera(LatLng(28.6139, 77.2090))
                        showOrAnimateMarker(LatLng(28.6139, 77.2090))
                    }
                }
            }
        }
    }

    private fun showOrAnimateMarker(latLng: LatLng) {
        if (currentPositionMarker == null)
            currentPositionMarker =
                googleMap.addMarker(googleMapHelper.getDriverMarkerOptions(latLng))
        else markerAnimationHelper.animateMarkerToGB(
            currentPositionMarker!!,
            latLng,
            LatLngInterpolator.Spherical()
        )
    }

    private fun animateCamera(latLng: LatLng) {
        val cameraUpdate = googleMapHelper.buildCameraUpdate(latLng)
        googleMap.animateCamera(cameraUpdate, 10, null)
    }

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

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * In this tutorial, we add polylines and polygons to represent routes and areas on the map.
     */
    override fun onMapReady(googleMap: GoogleMap) {

        /* // Add polylines to the map.
         // Polylines are useful to show a route or some other connection between points.
         val polyline1: Polyline = googleMap.addPolyline(
             PolylineOptions()
                 .clickable(true)
                 .add(
                     LatLng(-35.016, 143.321),
                     LatLng(-34.747, 145.592),
                     LatLng(-34.364, 147.891),
                     LatLng(-33.501, 150.217),
                     LatLng(-32.306, 149.248),
                     LatLng(-32.491, 147.309)
                 )
         )


         // Position the map's camera near Alice Springs in the center of Australia,
         // and set the zoom factor so most of Australia shows on the screen.
         googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-23.684, 133.903), 4f))

         // Set listeners for click events.
         googleMap.setOnPolylineClickListener(this@DriverRouteMapActivity)
         googleMap.setOnPolygonClickListener(this@DriverRouteMapActivity)*/
    }

    private val COLOR_BLACK_ARGB = -0x1000000
    private val POLYLINE_STROKE_WIDTH_PX = 12

    /**
     * Styles the polyline, based on type.
     * @param polyline The polyline object that needs styling.
     */
    private fun stylePolyline(polyline: Polyline) {
        // Get the data object stored with the polyline.
        val type = polyline.tag?.toString() ?: ""
        when (type) {
            "A" -> {
                // Use a custom bitmap as the cap at the start of the line.
                polyline.startCap = CustomCap(
                    BitmapDescriptorFactory.fromResource(R.drawable.ic_arrow_forward_black_24dp),
                    10f
                )
            }
            "B" -> {
                // Use a round cap at the start of the line.
                polyline.startCap = RoundCap()
            }
        }
        polyline.endCap = RoundCap()
        polyline.width = POLYLINE_STROKE_WIDTH_PX.toFloat()
        polyline.color = COLOR_BLACK_ARGB
        polyline.jointType = JointType.ROUND
    }
}