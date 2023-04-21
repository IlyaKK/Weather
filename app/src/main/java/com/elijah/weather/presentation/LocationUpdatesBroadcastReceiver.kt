package com.elijah.weather.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.elijah.weather.domain.AddCityByCoordinates
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationResult
import javax.inject.Inject

class LocationUpdatesBroadcastReceiver : BroadcastReceiver() {
    @Inject
    lateinit var addCityByCoordinates: AddCityByCoordinates

    override fun onReceive(context: Context, intent: Intent) {
        context.app.component.inject(this)
        if (intent.action == ACTION_LOCATION_PROCESS_UPDATES) {
            LocationAvailability.extractLocationAvailability(intent)?.let { locationAvailability ->
                if (!locationAvailability.isLocationAvailable) {
                    Log.d(TAG, "Location services are no longer available!")
                }
            }
            LocationResult.extractResult(intent)?.let { locationResult ->
                locationResult.lastLocation?.let {
                    addCityByCoordinates.invoke(it.latitude, it.longitude)
                }
            }
        }
    }

    companion object {
        const val ACTION_LOCATION_PROCESS_UPDATES = "ACTION_LOCATION_PROCESS_UPDATES"
        private const val TAG = "LUBroadcastReceiver"
    }
}