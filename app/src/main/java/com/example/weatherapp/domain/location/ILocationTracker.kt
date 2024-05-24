package com.example.weatherapp.domain.location

import android.location.Location

interface ILocationTracker {
    suspend fun getCurrentLocation(): Location?
}