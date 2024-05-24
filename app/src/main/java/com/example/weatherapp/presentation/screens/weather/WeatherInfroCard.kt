package com.example.weatherapp.presentation.screens.weather

import android.content.Context
import android.widget.ImageView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.example.weatherapp.data.remote.model.WeatherDetail
import kotlin.math.roundToInt


@Composable
fun WeatherInfoScreen(
    context: Context,
    weatherDetail: WeatherDetail,
) {
    Column(modifier = Modifier.padding(16.dp)) {
        GlideImage(
            context = context,
            imageUrl = getWeatherIconUrl(weatherDetail.iconCode),
            modifier = Modifier.size(100.dp).padding(bottom = 8.dp)
        )
        Text(
            text = "Weather in ${weatherDetail.cityName}",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Temperature: ${kelvinToCelsius(weatherDetail.temperatureKelvin).roundToInt()}°C (${kelvinToFahrenheit(weatherDetail.temperatureKelvin).roundToInt()}°F)",
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Condition: ${weatherDetail.weatherDescription}",
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Humidity: ${weatherDetail.humidity}%",
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Wind Speed: ${weatherDetail.windSpeed} m/s",
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}


@Composable
fun GlideImage(
    context: Context,
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { ctx ->
            ImageView(ctx).apply {
                // Apply any ImageView properties if needed
            }
        },
        update = { imageView ->
            Glide.with(context)
                .load(imageUrl)
                .into(imageView)
        },
        modifier = modifier
    )
}

fun getWeatherIconUrl(iconCode: String): String {
    return "https://openweathermap.org/img/wn/$iconCode@2x.png"
}

// Helper functions for temperature conversion
fun kelvinToCelsius(kelvin: Double): Double = kelvin - 273.15
fun kelvinToFahrenheit(kelvin: Double): Double = (kelvin - 273.15) * 9/5 + 32