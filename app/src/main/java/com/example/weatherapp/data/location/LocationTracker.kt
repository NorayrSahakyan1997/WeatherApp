package com.example.weatherapp.data.location

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.example.weatherapp.domain.location.ILocationTracker
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.IOException
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.resume

@ExperimentalCoroutinesApi
class LocationTracker @Inject constructor(
    private val locationClient: FusedLocationProviderClient, // Client for accessing the location
    private val application: Application // Application context
) : ILocationTracker {

    // Override function to get the current location
    override suspend fun getCurrentLocation(): Location? {
        // Check if the app has permission to access fine location
        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        // Check if the app has permission to access coarse location
        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        // Get the location manager from the system service
        val locationManager =
            application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // Check if either the network provider or GPS provider is enabled
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        // Return null if permissions are not granted or GPS is not enabled
        if (!hasAccessCoarseLocationPermission || !hasAccessFineLocationPermission || !isGpsEnabled) {
            return null
        }

        // Use a cancellable coroutine to get the last known location
        return suspendCancellableCoroutine { cont ->
            locationClient.lastLocation.apply {
                if (isComplete) {
                    // If the task is complete, resume the coroutine with the result or null
                    if (isSuccessful) {
                        cont.resume(result)
                    } else {
                        cont.resume(null)
                    }
                    return@suspendCancellableCoroutine
                }
                // Add success listener to resume the coroutine with the location result
                addOnSuccessListener {
                    cont.resume(it)
                }
                // Add failure listener to resume the coroutine with null
                addOnFailureListener {
                    cont.resume(null)
                }
                // Add cancel listener to cancel the coroutine
                addOnCanceledListener {
                    cont.cancel()
                }
            }
        }
    }

    // Function to get the city name from latitude and longitude
    fun getCityName(lat: Double, lon: Double): String {
        // Create a Geocoder instance to get the address from latitude and longitude
        val geocoder = Geocoder(application, Locale.getDefault())
        val addresses: List<Address>?
        try {
            // Get the address list from the geocoder
            addresses = geocoder.getFromLocation(lat, lon, 1)
            if (!addresses.isNullOrEmpty()) {
                val address: Address = addresses[0]
                // Check if the locality (city name) is available
                if (address.locality != null && address.locality.isNotEmpty()) {
                    return address.locality
                }
            }
        } catch (e: IOException) {
            e.printStackTrace() // Print the stack trace in case of an IOException
        }
        return "Unknown location" // Default message if location is not found
    }
}
