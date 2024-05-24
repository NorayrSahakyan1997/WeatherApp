package com.example.weatherapp.domain.repository


interface WeatherRepository {
    suspend fun getWeatherData(city:String)

}