package com.example.weatherapp.search
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.data.common.Constants
import com.example.weatherapp.data.location.LocationTracker
import com.example.weatherapp.data.remote.repository.SearchRepositoryImpl
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class SearchRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var editor: SharedPreferences.Editor


    @OptIn(ExperimentalCoroutinesApi::class)
    @Mock
    private lateinit var locationTracker: LocationTracker

    private lateinit var repository: SearchRepositoryImpl

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)  // Manual initialization of mocks
        whenever(sharedPreferences.edit()).thenReturn(editor)
        whenever(editor.putString(anyString(), anyString())).thenReturn(editor)
        whenever(editor.remove(anyString())).thenReturn(editor)
        repository = SearchRepositoryImpl(locationTracker, sharedPreferences)
    }

    @Test
    fun saveCityName_savesCorrectly() = runBlocking {
        repository.saveCityName("New York")
        verify(editor).putString(Constants.LAST_SAVED_CITY_KEY, "New York")
        verify(editor).apply()
    }

    @Test
    fun getSavedCity_returnsCity() = runBlocking {
        `when`(sharedPreferences.getString(Constants.LAST_SAVED_CITY_KEY, null)).thenReturn("New York")
        val cityName = repository.getSavedCity()
        assert(cityName == "New York")
    }

    @Test
    fun clearSavedCity_clearsCorrectly() = runBlocking {
        repository.clearSavedCity()
        verify(editor).remove(Constants.LAST_SAVED_CITY_KEY)
        verify(editor).apply()
    }
}