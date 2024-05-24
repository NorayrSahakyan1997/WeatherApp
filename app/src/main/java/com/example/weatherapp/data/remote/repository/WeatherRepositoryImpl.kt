package com.example.weatherapp.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.data.common.Constants.API_KEY
import com.example.weatherapp.data.common.Resource
import com.example.weatherapp.data.remote.api.WeatherApi
import com.example.weatherapp.data.remote.dto.WeatherDto
import com.example.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi
) : WeatherRepository {
    private var _weatherLiveData = MutableLiveData<Resource<WeatherDto>>()
    val weatherLiveData: LiveData<Resource<WeatherDto>> = _weatherLiveData
    override suspend fun getWeatherData(city: String) {
        _weatherLiveData.value = Resource.Loading()
        try {
            val response = api.getWeatherData(city, API_KEY)
            _weatherLiveData.postValue(Resource.Success(response))
        } catch (e: Exception) {
            _weatherLiveData.postValue(Resource.Error("Failed to load data: ${e.message}", null))
        }
    }
}