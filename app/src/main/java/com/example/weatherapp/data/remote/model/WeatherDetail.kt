package com.example.weatherapp.data.remote.model

data class WeatherDetail(
    val cityName: String,
    val temperatureKelvin: Double,
    val weatherDescription: String,
    val humidity: Int,
    val windSpeed: Double,
    val iconCode: String
)
