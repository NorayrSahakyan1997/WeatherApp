package com.example.weatherapp.presentation.screens.weather

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.data.common.Resource
import com.example.weatherapp.data.remote.dto.toWeatherDetail


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WeatherScreen(cityName: String, onBack: () -> Unit, viewModel: WeatherViewModel = hiltViewModel()) {
    val weatherDataState = viewModel.weatherLiveData.observeAsState()
    val context = LocalContext.current
    BackHandler(onBack = onBack)
    // Initiate data fetching
    LaunchedEffect(cityName) {
        viewModel.fetchWeatherData(cityName)
    }

    // Top bar with back button
        weatherDataState.value?.let { state ->
            when (state) {
                is Resource.Loading -> {
                    Text("Loading weather data...")
                }
                is Resource.Success -> {
                    state.data?.let { weatherData ->
                        WeatherInfoScreen(
                            context = context,
                            weatherData.toWeatherDetail()
                        )
                    }
                }
                is Resource.Error -> {
                    Text("Error loading weather data: ${state.message}")
                }
            }
        } ?: Text("Initializing...")
}

