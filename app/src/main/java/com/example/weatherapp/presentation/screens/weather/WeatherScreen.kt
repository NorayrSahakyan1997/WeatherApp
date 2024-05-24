package com.example.weatherapp.presentation.screens.weather

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.data.common.Resource
import com.example.weatherapp.data.remote.dto.toWeatherDetail


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WeatherScreen(
    cityName: String, // The name of the city to fetch weather data for
    onBack: () -> Unit, // Callback function to handle the back button press
    viewModel: WeatherViewModel = hiltViewModel() // Default ViewModel provided by Hilt
) {
    // Observe the weatherLiveData from the ViewModel
    val weatherDataState = viewModel.weatherLiveData.observeAsState()
    // Get the current context
    val context = LocalContext.current
    // Handle the back button press
    BackHandler(onBack = onBack)

    // Launch an effect to fetch weather data when the cityName changes
    LaunchedEffect(cityName) {
        viewModel.fetchWeatherData(cityName)
    }

    // Display content based on the weather data state
    weatherDataState.value?.let { state ->
        when (state) {
            is Resource.Loading -> {
                // Show loading state
                Text("Loading weather data...")
            }
            is Resource.Success -> {
                // Show weather data when successfully loaded
                state.data?.let { weatherData ->
                    WeatherInfoScreen(
                        context = context,
                        weatherData.toWeatherDetail() // Convert to detailed weather data
                    )
                }
            }
            is Resource.Error -> {
                // Show error message if data loading fails
                Text("Error loading weather data: ${state.message}")
            }
        }
    } ?: Text("Initializing...") // Show initializing state before data is loaded
}
