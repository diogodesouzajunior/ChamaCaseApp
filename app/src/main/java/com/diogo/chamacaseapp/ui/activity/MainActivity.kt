package com.diogo.chamacaseapp.ui.activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.diogo.chamacaseapp.R
import com.diogo.chamacaseapp.service.GPSMonitorService
import com.diogo.chamacaseapp.ui.adapter.MainAdapter
import com.diogo.chamacaseapp.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private val RC_HANDLE_LOCATION_PERM = 2

    var fragmentAdapter: MainAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (checkPermission(this)) {
            stopService(GPSMonitorService.getStartIntent(this))
            startService(GPSMonitorService.getStartIntent(this))
            setupViewPager()
        } else {
            requestLocationPermission()
        }
    }

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

    private fun setupViewPager() {

        fragmentAdapter = MainAdapter(supportFragmentManager)
        viewpager_main.adapter = fragmentAdapter

        tabs_main.setupWithViewPager(viewpager_main)

    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            RC_HANDLE_LOCATION_PERM
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode != RC_HANDLE_LOCATION_PERM) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }
        if (grantResults.size != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            stopService(GPSMonitorService.getStartIntent(this))
            startService(GPSMonitorService.getStartIntent(this))
            setupViewPager()
            return
        } else {
            requestLocationPermission()
        }
    }
}