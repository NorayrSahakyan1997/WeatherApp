package com.example.weatherapp.presentation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.data.Constants.CITY_NAME
import com.example.weatherapp.data.Constants.SEARCH
import com.example.weatherapp.data.Constants.WEATHER
import com.example.weatherapp.presentation.screens.SearchScreen
import com.example.weatherapp.presentation.screens.WeatherScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherApp()
        }
    }
}

@Composable
fun WeatherApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = SEARCH) {
        composable("search") {
            SearchScreen(onSearch = { cityName ->
                if (cityName.isNotBlank()) {
                    // Pass the city name to the weather screen through navigation arguments
                    navController.navigate("$WEATHER/$cityName")
                }
            })
        }
        composable("$WEATHER/{$CITY_NAME}") { backStackEntry ->
            // Retrieve the city name passed as an argument
            val cityName = backStackEntry.arguments?.getString(CITY_NAME) ?: ""
            WeatherScreen(cityName = cityName)
        }
    }
}