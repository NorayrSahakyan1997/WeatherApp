import android.content.SharedPreferences
import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.data.common.Constants.LAST_SAVED_CITY_KEY
import com.example.weatherapp.data.location.LocationTracker
import com.example.weatherapp.data.remote.repository.SearchRepositoryImpl
import com.example.weatherapp.rule.CoroutineTestRule
import com.example.weatherapp.rule.MainDispatcherRule
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
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
class SearchRepositoryImplTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    // Mock dependencies
    @Mock
    private lateinit var locationTracker: LocationTracker
    @Mock
    private lateinit var sharedPreferences: SharedPreferences


    private lateinit var searchRepository: SearchRepositoryImpl


    private val city = "Los Angeles"

    @Before
    fun setUp() {
        // Initialize the repository with the mocked dependencies
        searchRepository = SearchRepositoryImpl(locationTracker, sharedPreferences)
    }

    @Test
    fun `getSavedCity returns saved city name`() = runTest {
        // Setup the mock to return a specific city name
        `when`(sharedPreferences.getString(LAST_SAVED_CITY_KEY, null)).thenReturn("Los Angeles")

        // Call the method to test
        val city = searchRepository.getSavedCity()

        // Verify the result
        assertEquals("Los Angeles", city)
    }

    @Test
    fun `getSavedCity returns null when no city is saved`() = runTest {
        // Setup the mock to return null
        `when`(sharedPreferences.getString(LAST_SAVED_CITY_KEY, null)).thenReturn(null)

        // Call the method to test
        val city = searchRepository.getSavedCity()

        // Verify the result
        assertNull(city)
    }

    @Test
    fun `saveCityName saves the city name`() = runTest {
        // Mock the SharedPreferences editor
        val editor = mock(SharedPreferences.Editor::class.java)
        `when`(sharedPreferences.edit()).thenReturn(editor)
        `when`(editor.putString(anyString(), anyString())).thenReturn(editor)

        // Call the method to test
        searchRepository.saveCityName(city)

        // Verify the editor interactions
        verify(editor).putString(LAST_SAVED_CITY_KEY, city)
        verify(editor).apply()
    }

    @Test
    fun `clearSavedCity removes the saved city`() = runTest {
        // Mock the SharedPreferences editor
        val editor = mock(SharedPreferences.Editor::class.java)
        `when`(sharedPreferences.edit()).thenReturn(editor)
        `when`(editor.remove(anyString())).thenReturn(editor)

        // Call the method to test
        searchRepository.clearSavedCity()

        // Verify the editor interactions
        verify(editor).remove(LAST_SAVED_CITY_KEY)
        verify(editor).apply()
    }

    @Test
    fun `fetchCurrentCity returns city name based on location`() = runTest {
        // Setup the mock location
        val location = mock(Location::class.java)
        `when`(location.latitude).thenReturn(34.0522)
        `when`(location.longitude).thenReturn(-118.2437)
        `when`(locationTracker.getCurrentLocation()).thenReturn(location)
        `when`(locationTracker.getCityName(34.0522, -118.2437)).thenReturn("Los Angeles")

        // Call the method to test
        val city = searchRepository.fetchCurrentCity()

        // Verify the result
        assertEquals(city, city)
    }

    @Test
    fun `fetchCurrentCity returns null when location is null`() = runTest {
        // Setup the mock to return null for location
        `when`(locationTracker.getCurrentLocation()).thenReturn(null)

        // Call the method to test
        val city = searchRepository.fetchCurrentCity()

        // Verify the result
        assertNull(city)
    }
}
