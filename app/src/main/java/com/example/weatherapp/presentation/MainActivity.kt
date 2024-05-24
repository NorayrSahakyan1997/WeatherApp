package com.example.weatherapp.presentation
import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.data.common.Constants
import com.example.weatherapp.data.common.Constants.CITY_NAME
import com.example.weatherapp.data.common.Constants.SEARCH
import com.example.weatherapp.data.common.Constants.WEATHER
import com.example.weatherapp.presentation.screens.search.SearchScreen
import com.example.weatherapp.presentation.screens.search.SearchViewModel
import com.example.weatherapp.presentation.screens.weather.WeatherScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor() : ComponentActivity() {

    // Declare a variable to hold the permission launcher
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    // Called when the activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askPermissionAndSetUpView()  // Request necessary permissions and set up the view
    }

    // Function to request permissions and set up the view
    private fun askPermissionAndSetUpView() {
        // Initialize the permission launcher with a callback for handling the result
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            // Check if all permissions are granted
            val allPermissionsGranted = permissions.all { it.value }
            if (!allPermissionsGranted) {
                // Show a toast message if any permission is denied
                Toast.makeText(this, Constants.PERMISSION_DENIED_MESSAGE, Toast.LENGTH_SHORT).show()
            }
            // Set the content of the activity to the WeatherApp composable
            setContent {
                WeatherApp()
            }
        }
        // Launch the permission request for location permissions
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )
    }
}

@Composable
fun WeatherApp() {
    // Create a navigation controller
    val navController = rememberNavController()
    // Get an instance of SearchViewModel using Hilt
    val searchViewModel: SearchViewModel = hiltViewModel()

    // Set up the navigation host
    NavHost(navController = navController, startDestination = SEARCH) {
        // Define the composable for the search screen
        composable(SEARCH) {
            SearchScreen(
                onSearch = { cityName ->
                    if (cityName.isNotBlank()) {
                        // Navigate to the weather screen with the city name if it's not blank
                        navController.navigate("$WEATHER/$cityName")
                    }
                },
                viewModel = searchViewModel  // Pass the SearchViewModel to the SearchScreen
            )
        }
        // Define the composable for the weather screen
        composable("$WEATHER/{$CITY_NAME}") { backStackEntry ->
            // Get the city name from the navigation arguments
            val cityName = backStackEntry.arguments?.getString(CITY_NAME) ?: ""
            WeatherScreen(
                cityName = cityName,
                onBack = {
                    // Clear the last saved city when navigating back
                    searchViewModel.clearLastSavedCity()
                    // Navigate back to the search screen
                    navController.popBackStack()
                }
            )
        }
    }
}