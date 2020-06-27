package com.home.traker.network

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast

class LocationTrackingService : JobService() {
    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show()
        return true
    }

    var locationManager: LocationManager? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onCreate() {
        if (locationManager == null)
            locationManager =
                applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        try {
            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                INTERVAL,
                DISTANCE,
                locationListeners[1]
            )
        } catch (e: SecurityException) {
            Log.e(TAG, "Fail to request location update", e)
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "Network provider does not exist", e)
        }

        try {
            locationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                INTERVAL,
                DISTANCE,
                locationListeners[0]
            )
        } catch (e: SecurityException) {
            Log.e(TAG, "Fail to request location update", e)
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "GPS provider does not exist", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (locationManager != null)
            for (i in 0..locationListeners.size) {
                try {
                    locationManager?.removeUpdates(locationListeners[i])
                } catch (e: Exception) {
                    Log.w(TAG, "Failed to remove location listeners")
                }
            }
    }


    companion object {
        val TAG = "LocationTrackingService"

        val INTERVAL = 1000.toLong() // In milliseconds
        val DISTANCE = 10.toFloat() // In meters

        val locationListeners = arrayOf(
            LTRLocationListener(LocationManager.GPS_PROVIDER),
            LTRLocationListener(LocationManager.NETWORK_PROVIDER)
        )

        class LTRLocationListener(provider: String) : android.location.LocationListener {

            val lastLocation = Location(provider)

            override fun onLocationChanged(location: Location?) {
                lastLocation.set(location)
                Log.d("Location: ", location.toString())
            }

            override fun onProviderDisabled(provider: String?) {
            }

            override fun onProviderEnabled(provider: String?) {
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

        }
    }

}