package com.diogo.chamacaseapp.service

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import com.diogo.chamacaseapp.ChamaCaseApplication
import rx.Scheduler
import timber.log.Timber

class GPSMonitorService : Service() {

    private var worker: Scheduler.Worker? = null

    private val SERVICE_TAG = "BOOMBOOMTESTGPS"
    private val LOCATION_INTERVAL =
        1 * 60 * 1000

    private val LOCATION_DISTANCE =
        1f

    var mLocationListeners = arrayOf(
        LocationListener(LocationManager.GPS_PROVIDER),
        LocationListener(LocationManager.NETWORK_PROVIDER)
    )
    private var mLocationManager: LocationManager? = null

    fun checkPermission(context: Context?): Boolean {
        return (context?.let {
            ActivityCompat.checkSelfPermission(
                it,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } === PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) === PackageManager.PERMISSION_GRANTED)
    }
    override fun onBind(arg0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(SERVICE_TAG, "onStartCommand")

        try {
            initializeLocationManager()
            try {
                mLocationManager!!.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    LOCATION_INTERVAL.toLong(),
                    LOCATION_DISTANCE,
                    mLocationListeners[1]
                )
            } catch (ex: SecurityException) {
                Log.i(SERVICE_TAG, "fail to request location update, ignore", ex)
            } catch (ex: IllegalArgumentException) {
                Log.d(SERVICE_TAG, "network provider does not exist, " + ex.message)
            }
            try {
                mLocationManager!!.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL.toLong(), LOCATION_DISTANCE,
                    mLocationListeners[0]
                )
            } catch (ex: SecurityException) {
                Log.i(SERVICE_TAG, "fail to request location update, ignore", ex)
            } catch (ex: IllegalArgumentException) {
                Log.d(SERVICE_TAG, "gps provider does not exist " + ex.message)
            }
        } catch (t: Throwable) {
            Timber.e(t)
        }


        super.onStartCommand(intent, flags, startId)
        return Service.START_STICKY
    }

    override fun onCreate() {
        Log.e(SERVICE_TAG, "onCreate")

        (applicationContext as ChamaCaseApplication)
            .applicationComponent
            .inject(this)
    }

    override fun onDestroy() {
        Log.e(SERVICE_TAG, "onDestroy")
        super.onDestroy()
        if (mLocationManager != null) {
            for (i in mLocationListeners.indices) {
                try {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) !== PackageManager.PERMISSION_GRANTED
                    ) {
                    }
                    mLocationManager!!.removeUpdates(mLocationListeners[i])
                } catch (ex: Exception) {
                    Log.i(SERVICE_TAG, "fail to remove location listners, ignore", ex)
                }
            }
        }
    }

    private fun initializeLocationManager() {
        Log.e(SERVICE_TAG, "initializeLocationManager")

        if (mLocationManager == null) {
            mLocationManager =
                getApplicationContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        }
    }

    class LocationListener(provider: String) : android.location.LocationListener {
        var mLastLocation: Location
        override fun onLocationChanged(location: Location) {
            mLastLocation.set(location)
        }

        override fun onProviderDisabled(provider: String) {
        }

        override fun onProviderEnabled(provider: String) {
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        }

        init {
            mLastLocation = Location(provider)
        }
    }

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context!!, GPSMonitorService::class.java)
        }
    }

}