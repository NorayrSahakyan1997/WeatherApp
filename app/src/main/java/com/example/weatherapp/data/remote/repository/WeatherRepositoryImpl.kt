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
    private val api: WeatherApi // The WeatherApi instance injected by Hilt for making API calls
) : WeatherRepository {

    // Private MutableLiveData to hold the weather data
    private var _weatherLiveData = MutableLiveData<Resource<WeatherDto>>()
    // Public LiveData that can be observed by other components, exposing only the read-only view
    val weatherLiveData: LiveData<Resource<WeatherDto>> = _weatherLiveData

    // Override the getWeatherData function from the WeatherRepository interface
    override suspend fun getWeatherData(city: String) {
        try {
            // Set the value of _weatherLiveData to loading state before making the API call
            _weatherLiveData.value = Resource.Loading()
            // Make the API call to get weather data for the specified city
            val response = api.getWeatherData(city, API_KEY)
            // Post the success state with the retrieved data
            _weatherLiveData.postValue(Resource.Success(response))
        } catch (e: Exception) {
            // Post the error state with an error message if an exception occurs
            _weatherLiveData.postValue(Resource.Error("Failed to load data: ${e.message}", null))
        }
    }
}
