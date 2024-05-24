package com.example.weatherapp.presentation.screens.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.common.Resource
import com.example.weatherapp.data.remote.dto.WeatherDto
import com.example.weatherapp.data.remote.repository.WeatherRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel
@Inject constructor(private val repository: WeatherRepositoryImpl,
): ViewModel() {

    var weatherLiveData: LiveData<Resource<WeatherDto>> =
        repository.weatherLiveData

    fun fetchWeatherData(city:String) {
        viewModelScope.launch {
            repository.getWeatherData(city)
        }
    }
}