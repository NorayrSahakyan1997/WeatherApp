package com.example.weatherapp.data.remote.dto

import com.example.weatherapp.data.remote.model.WeatherDetail

data class WeatherDto(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)

fun WeatherDto.toWeatherDetail(): WeatherDetail {
    return WeatherDetail(
        cityName = this.name,
        temperatureKelvin = this.main.temp,
        weatherDescription = this.weather[0].description,
        humidity = this.main.humidity,
        windSpeed = this.wind.speed,
        iconCode = this.weather[0].icon
    )
}