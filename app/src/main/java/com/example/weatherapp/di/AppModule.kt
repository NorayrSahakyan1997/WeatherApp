package com.example.weatherapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.weatherapp.data.common.Constants.BASE_URL
import com.example.weatherapp.data.common.Constants.MY_SHARED_PREF_KEY
import com.example.weatherapp.data.remote.api.WeatherApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Provides a singleton instance of WeatherApi using Retrofit for making network requests
    @Provides
    @Singleton
    fun provideWeatherApi(): WeatherApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL) // Set the base URL for the API
            .addConverterFactory(MoshiConverterFactory.create()) // Use Moshi for JSON conversion
            .build() // Build the Retrofit instance
            .create(WeatherApi::class.java) // Create the WeatherApi implementation
    }

    // Provides a singleton instance of FusedLocationProviderClient for accessing location services
    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(app: Application): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(app) // Get the FusedLocationProviderClient
    }

    // Provides a singleton instance of SharedPreferences for storing key-value pairs
    @Provides
    @Singleton
    fun provideSharedPreferences(app: Application): SharedPreferences {
        return app.getSharedPreferences(MY_SHARED_PREF_KEY, Context.MODE_PRIVATE) // Get the SharedPreferences instance
    }
}
