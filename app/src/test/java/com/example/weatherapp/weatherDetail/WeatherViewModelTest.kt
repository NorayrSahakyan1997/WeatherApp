package com.example.weatherapp.weatherDetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.weatherapp.data.common.Resource
import com.example.weatherapp.data.remote.dto.WeatherDto
import com.example.weatherapp.data.remote.repository.WeatherRepositoryImpl
import com.example.weatherapp.presentation.screens.weather.WeatherViewModel
import com.example.weatherapp.rule.CoroutineTestRule
import com.example.weatherapp.rule.MainDispatcherRule
import com.example.weatherapp.util.getOrAwaitValue
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class WeatherViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()


    private lateinit var viewModel: WeatherViewModel

    @Mock
    private lateinit var repository: WeatherRepositoryImpl

    @Mock
    private lateinit var weatherDto: WeatherDto

    @Mock
    private lateinit var observer: Observer<Resource<WeatherDto>>

    private val city = "Los Angeles"
    private lateinit var liveData: MutableLiveData<Resource<WeatherDto>>

    @Before
    fun setUp() {
        liveData = MutableLiveData<Resource<WeatherDto>>()
        whenever(repository.weatherLiveData).thenReturn(liveData)
        viewModel = WeatherViewModel(repository)
        viewModel.weatherLiveData.observeForever(observer)
    }

    @Test
    fun `fetchWeatherData should post loading and then success`() = runTest {
        // Arrange

        val expectedResource =
            Resource.Success(weatherDto)
        // Act
        viewModel.fetchWeatherData(city)
        advanceUntilIdle()
        verify(repository).getWeatherData(city)

        // Simulate the repository posting data
        (repository.weatherLiveData as MutableLiveData).postValue(expectedResource)
        // Assert
        verify(observer).onChanged(expectedResource)
//        assertEquals(expectedResource, viewModel.weatherLiveData.value)

    }


    @Test
    fun `test live data error state`() = runTest {
        val errorMessage = "Error fetching details"

        viewModel.fetchWeatherData("error_id")
        liveData.postValue(Resource.Error(errorMessage, null))

        // Advance time until all coroutines are idle
        advanceUntilIdle()

        // Assert the LiveData updates as expected
        val capturedValue =
            liveData.getOrAwaitValue()  // Using an extension function to await the value
        Assert.assertTrue(capturedValue is Resource.Error)
        Assert.assertEquals(errorMessage, (capturedValue as Resource.Error).message)
    }


    @Test
    fun `test loading state when fetching  data` () = runTest {
        val loadingResource = Resource.Loading<WeatherDto>()
        liveData.postValue(loadingResource)

        viewModel.fetchWeatherData(city)
        advanceUntilIdle()

        Mockito.verify(observer).onChanged(loadingResource)
        Assert.assertTrue(viewModel.weatherLiveData.value is Resource.Loading)
    }

    @Test
    fun `fetchWeatherData should update liveData with loading state`() = runTest {
        // Arrange
        val expectedResource = Resource.Loading<WeatherDto>()

        // Act
        viewModel.fetchWeatherData(city)
        liveData.postValue(expectedResource)
        advanceUntilIdle()

        // Assert
        verify(observer).onChanged(expectedResource)
        verify(repository).getWeatherData(city)
    }


}