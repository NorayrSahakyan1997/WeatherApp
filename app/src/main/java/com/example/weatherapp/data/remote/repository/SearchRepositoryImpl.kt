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
    private val locationTracker: LocationTracker,
    private val sharedPreferences: SharedPreferences
) : SearchRepository {

    override suspend fun getSavedCity(): String? {
        return sharedPreferences.getString(LAST_SAVED_CITY_KEY, null)
    }

    override suspend fun saveCityName(cityName: String) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putString(LAST_SAVED_CITY_KEY, cityName).apply()
        }
    }

    override suspend fun clearSavedCity() {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().remove(LAST_SAVED_CITY_KEY).apply()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun fetchCurrentCity(): String? {
        val location = locationTracker.getCurrentLocation()
        return location?.let { getCity(it) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getCity(location: Location): String {
        val lat = location.latitude
        val long = location.longitude
        return locationTracker.getCityName(lat, long)
    }
}