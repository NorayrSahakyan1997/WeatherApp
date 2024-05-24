import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.data.remote.repository.SearchRepositoryImpl
import com.example.weatherapp.presentation.screens.search.SearchViewModel
import com.example.weatherapp.rule.CoroutineTestRule
import com.example.weatherapp.rule.MainDispatcherRule
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {

    // Rule to ensure LiveData updates are instant for testing
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    // Rule to set the main dispatcher for coroutines to a test dispatcher
    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()


    // Mock dependencies
    @Mock
    private lateinit var repository: SearchRepositoryImpl

    @Mock
    private lateinit var observer: Observer<String>

    // ViewModel instance to test
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        // Initialize the ViewModel with the mocked repository
        viewModel = SearchViewModel(repository)
        // Observe the LiveData to verify changes
        viewModel.locationLiveData.observeForever(observer)
    }

    @Test
    fun `initialization test - saved city available`() = runTest {
        // Setup the repository mock to return a saved city
        `when`(repository.getSavedCity()).thenReturn("Los Angeles")

        // Call the method to test (checkIfSavedCityAvailable is called in init block)
        viewModel.checkIfSavedCityAvailable()

        // Verify the LiveData is updated with the saved city
        verify(observer).onChanged("Los Angeles")
    }

    @Test
    fun `initialization test - no saved city`() = runTest {
        // Setup the repository mock to return null for the saved city
        `when`(repository.getSavedCity()).thenReturn(null)
        // Setup the repository mock to return a current city
        `when`(repository.fetchCurrentCity()).thenReturn("Los Angeles")

        // Call the method to test (checkIfSavedCityAvailable is called in init block)
        viewModel.checkIfSavedCityAvailable()

        // Verify the LiveData is updated with the fetched city
        verify(observer).onChanged("Los Angeles")
    }

    @Test
    fun `loadWeatherInfo test`() = runTest {
        // Setup the repository mock to return a current city
        `when`(repository.fetchCurrentCity()).thenReturn("Los Angeles")

        // Call the method to test
        viewModel.loadWeatherInfo()

        // Verify the LiveData is updated with the fetched city
        verify(observer).onChanged("Los Angeles")
    }

    @Test
    fun `saveCityName test`() = runTest {
        // Call the method to test
        viewModel.saveCityName("Los Angeles")

        // Verify the repository's saveCityName method is called with the correct argument
        verify(repository).saveCityName("Los Angeles")
    }

    @Test
    fun `clearLastSavedCity test`() = runTest {
        // Call the method to test
        viewModel.clearLastSavedCity()

        // Verify the repository's clearSavedCity method is called
        verify(repository).clearSavedCity()
    }
}