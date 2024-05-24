package com.example.weatherapp.domain.repository

interface SearchRepository {
    suspend fun getSavedCity(): String?
    suspend fun saveCityName(cityName: String)
    suspend fun clearSavedCity()
    suspend fun fetchCurrentCity(): String?
}