package com.example.weatherapp.weatherDetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.weatherapp.data.common.Constants.API_KEY
import com.example.weatherapp.data.common.Resource
import com.example.weatherapp.data.remote.api.WeatherApi
import com.example.weatherapp.data.remote.dto.WeatherDto
import com.example.weatherapp.data.remote.repository.WeatherRepositoryImpl
import com.example.weatherapp.rule.CoroutineTestRule
import com.example.weatherapp.rule.MainDispatcherRule
import com.example.weatherapp.util.getOrAwaitValue
import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class WeatherRepositoryImplTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()


    @Mock
    private lateinit var api: WeatherApi

    @Mock
    private lateinit var weatherDto: WeatherDto

    @Mock
    private lateinit var observer: Observer<Resource<WeatherDto>>

    private lateinit var repository: WeatherRepositoryImpl



    private val city = "Los Angeles"

    @Before
    fun setUp() {
        repository = WeatherRepositoryImpl(api)
        repository.weatherLiveData.observeForever(observer)
    }

    @Test
    fun `getWeatherData should post loading and then success`() = runBlocking {
        // Arrange
        whenever(api.getWeatherData(city, API_KEY)).thenReturn(weatherDto)
        // Act
        repository.getWeatherData(city)
        val result = repository.weatherLiveData.getOrAwaitValue()
        assertTrue(result is Resource.Success && result.data == weatherDto)
    }

    @Test
    fun `handles exceptions during repository call`() = runTest {
        // Arrange
        val city = "Los Angeles"
        val exception = RuntimeException("Runtime Exception")
        whenever(api.getWeatherData(city, API_KEY)).thenThrow(exception)

        // Act
        repository.getWeatherData(city)

        // Assert
        val inOrder = inOrder(observer)
        inOrder.verify(observer).onChanged(argThat { this is Resource.Loading })
        inOrder.verify(observer).onChanged(argThat { this is Resource.Error && this.message == "Failed to load data: ${exception.message}" })
    }
}