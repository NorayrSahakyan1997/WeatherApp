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

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askPermissionAndSetUpView()
    }

    private fun askPermissionAndSetUpView() {
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val allPermissionsGranted = permissions.all { it.value }
            if (!allPermissionsGranted) {
                Toast.makeText(this, Constants.PERMISSION_DENIED_MESSAGE, Toast.LENGTH_SHORT).show()
            }
            setContent {
                WeatherApp()
            }
        }
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
    val navController = rememberNavController()
    val searchViewModel: SearchViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = SEARCH) {
        composable(SEARCH) {
            SearchScreen(onSearch = { cityName ->
                if (cityName.isNotBlank()) {
                    navController.navigate("$WEATHER/$cityName")
                }
            }, viewModel = searchViewModel)
        }
        composable("$WEATHER/{$CITY_NAME}") { backStackEntry ->
            val cityName = backStackEntry.arguments?.getString(CITY_NAME) ?: ""
            WeatherScreen(
                cityName = cityName,
                onBack = {
                    searchViewModel.clearLastSavedCity()  // Clear the last saved city when navigating back
                    navController.popBackStack()  // Navigate back to the search screen
                }
            )
        }
    }
}