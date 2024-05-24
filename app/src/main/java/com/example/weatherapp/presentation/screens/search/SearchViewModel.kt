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

    val locationLiveData = MutableLiveData<String>()

    init {
        checkIfSavedCityAvailable()
    }

    private fun checkIfSavedCityAvailable() {
        viewModelScope.launch {
            val lastSearchedCity = repository.getSavedCity()
            if (lastSearchedCity.isNullOrEmpty()) {
                loadWeatherInfo()
            } else {
                locationLiveData.postValue(lastSearchedCity)
            }
        }
    }

    private fun loadWeatherInfo() {
        viewModelScope.launch {
            repository.fetchCurrentCity()?.let { city ->
                locationLiveData.postValue(city)
            }
        }
    }

    fun saveCityName(cityName: String) {
        viewModelScope.launch {
            repository.saveCityName(cityName)
        }
    }

    fun clearLastSavedCity() {
        viewModelScope.launch {
            repository.clearSavedCity()
        }
    }
}