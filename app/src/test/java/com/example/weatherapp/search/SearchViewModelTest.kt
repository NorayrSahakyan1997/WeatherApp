import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.weatherapp.data.remote.repository.SearchRepositoryImpl
import com.example.weatherapp.presentation.screens.search.SearchViewModel
import com.example.weatherapp.rule.CoroutineTestRule
import com.example.weatherapp.rule.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

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

    private val city = "Los Angeles"


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
        `when`(repository.getSavedCity()).thenReturn(city)

        // Call the method to test (checkIfSavedCityAvailable is called in init block)
        viewModel.checkIfSavedCityAvailable()

        // Verify the LiveData is updated with the saved city
        verify(observer).onChanged(city)
    }

    @Test
    fun `initialization test - no saved city`() = runTest {
        // Setup the repository mock to return null for the saved city
        `when`(repository.getSavedCity()).thenReturn(null)
        // Setup the repository mock to return a current city
        `when`(repository.fetchCurrentCity()).thenReturn(city)

        // Call the method to test (checkIfSavedCityAvailable is called in init block)
        viewModel.checkIfSavedCityAvailable()

        // Verify the LiveData is updated with the fetched city
        verify(observer).onChanged(city)
    }

    @Test
    fun `loadWeatherInfo test`() = runTest {
        // Setup the repository mock to return a current city
        `when`(repository.fetchCurrentCity()).thenReturn(city)

        // Call the method to test
        viewModel.loadWeatherInfo()

        // Verify the LiveData is updated with the fetched city
        verify(observer).onChanged(city)
    }

    @Test
    fun `saveCityName test`() = runTest {
        // Call the method to test
        viewModel.saveCityName(city)

        // Verify the repository's saveCityName method is called with the correct argument
        verify(repository).saveCityName(city)
    }

    @Test
    fun `clearLastSavedCity test`() = runTest {
        // Call the method to test
        viewModel.clearLastSavedCity()

        // Verify the repository's clearSavedCity method is called
        verify(repository).clearSavedCity()
    }
}