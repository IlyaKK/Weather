package com.elijah.weather.data.repository

import android.Manifest
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import com.elijah.weather.presentation.LocationUpdatesBroadcastReceiver
import com.elijah.weather.util.hasPermission
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LocationManager @Inject constructor(
    private val application: Application
) {
    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(application)
    }

    private val locationRequest by lazy {
        LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            TimeUnit.MINUTES.toMillis(30)
        ).build()
    }

    private val locationUpdatePendingIntent: PendingIntent by lazy {
        val intent = Intent(application, LocationUpdatesBroadcastReceiver::class.java)
        intent.action = LocationUpdatesBroadcastReceiver.ACTION_LOCATION_PROCESS_UPDATES
        var existPending =
            PendingIntent.getBroadcast(
                application,
                0,
                intent,
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_NO_CREATE
            )
        if (existPending == null) {
            existPending =
                PendingIntent.getBroadcast(
                    application,
                    0,
                    intent,
                    PendingIntent.FLAG_MUTABLE
                )
        }
        existPending
    }

    fun startLocationUpdates(): Boolean {
        if (!application.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            return false
        }

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationUpdatePendingIntent
            )
        } catch (_: SecurityException) {
            return false
        }
        return true
    }
}