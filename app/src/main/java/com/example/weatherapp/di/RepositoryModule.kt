package com.example.weatherapp.di

import com.example.weatherapp.data.remote.repository.SearchRepositoryImpl
import com.example.weatherapp.data.remote.repository.WeatherRepositoryImpl
import com.example.weatherapp.domain.repository.SearchRepository
import com.example.weatherapp.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        weatherRepository: WeatherRepositoryImpl
    ): WeatherRepository


    @Binds
    abstract fun bindLocationRepository(repositoryImpl: SearchRepositoryImpl): SearchRepository
}