package com.example.weatherapp.data.common

import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.io.IOException
import java.util.Locale
object LocationHelper {
    fun getCityName(context: Context?, lat: Double, lon: Double): String {
        val geocoder = Geocoder(context!!, Locale.getDefault())
        val addresses: List<Address>?
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1)
            if (!addresses.isNullOrEmpty()) {
                val address: Address = addresses[0]
                // Check if an address is available
                if (address.locality != null && address.locality.isNotEmpty()) {
                    return address.locality
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return "Unknown location" // Default message if location is not found
    }
}