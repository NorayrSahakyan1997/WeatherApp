package com.example.weatherapp.presentation.screens
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


@Composable
fun WeatherScreen(cityName: String) {
    Text("Weather information for $cityName")
}