package com.example.weatherapp.data.remote.repository

import android.content.SharedPreferences
import android.location.Location
import com.example.weatherapp.data.common.Constants.LAST_SAVED_CITY_KEY
import com.example.weatherapp.data.location.LocationTracker
import com.example.weatherapp.domain.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepositoryImpl @OptIn(ExperimentalCoroutinesApi::class)
@Inject constructor(
    private val locationTracker: LocationTracker, // Instance of LocationTracker injected by Hilt
    private val sharedPreferences: SharedPreferences // SharedPreferences instance injected by Hilt
) : SearchRepository {

    // Retrieve the saved city name from SharedPreferences
    override suspend fun getSavedCity(): String? {
        return sharedPreferences.getString(LAST_SAVED_CITY_KEY, null)
    }

    // Save the city name to SharedPreferences
    override suspend fun saveCityName(cityName: String) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putString(LAST_SAVED_CITY_KEY, cityName).apply()
        }
    }

    // Clear the saved city name from SharedPreferences
    override suspend fun clearSavedCity() {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().remove(LAST_SAVED_CITY_KEY).apply()
        }
    }

    // Fetch the current city name using the LocationTracker
    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun fetchCurrentCity(): String? {
        // Get the current location from the LocationTracker
        val location = locationTracker.getCurrentLocation()
        // If the location is not null, get the city name from the location
        return location?.let { getCity(it) }
    }

    // Get the city name from a Location object
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getCity(location: Location): String {
        val lat = location.latitude
        val long = location.longitude
        return locationTracker.getCityName(lat, long)
    }
}
