package com.example.weatherapp.presentation.screens.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.remote.repository.SearchRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepositoryImpl
) : ViewModel() {

    // LiveData to hold the location data, which can be observed by the UI
    val locationLiveData = MutableLiveData<String>()

    // Initializing block to check if there is any saved city when the ViewModel is created
    init {
        viewModelScope.launch {
            checkIfSavedCityAvailable()
        }
    }

    // Function to check if there is a saved city in the repository
    fun checkIfSavedCityAvailable() {
        viewModelScope.launch {
            // Retrieve the last searched city from the repository
            val lastSearchedCity = repository.getSavedCity()
            // If there is no saved city, load the current city weather information
            if (lastSearchedCity.isNullOrEmpty()) {
                loadWeatherInfo()
            } else {
                // If there is a saved city, post its value to locationLiveData
                locationLiveData.postValue(lastSearchedCity)
            }
        }
    }

    // Function to load the current city weather information
    fun loadWeatherInfo() {
        viewModelScope.launch {
            // Fetch the current city from the repository
            repository.fetchCurrentCity()?.let { city ->
                // Post the current city value to locationLiveData
                locationLiveData.postValue(city)
            }
        }
    }

    // Function to save the city name to the repository
    fun saveCityName(cityName: String) {
        viewModelScope.launch {
            repository.saveCityName(cityName)
        }
    }

    // Function to clear the last saved city in the repository
    fun clearLastSavedCity() {
        viewModelScope.launch {
            repository.clearSavedCity()
        }
    }
}