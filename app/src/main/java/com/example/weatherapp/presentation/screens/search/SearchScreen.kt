package com.example.weatherapp.presentation.screens.search
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.data.common.Constants.EMPTY_CITY_NAME_ERROR_MESSAGE
import com.example.weatherapp.data.common.Constants.ENTER_CITY_NAME
import com.example.weatherapp.data.common.Constants.SEARCH_UPPER_CASE

@Composable
fun SearchScreen(
    onSearch: (String) -> Unit, // Callback function to handle the search action
    viewModel: SearchViewModel = hiltViewModel() // Default ViewModel provided by Hilt
) {
    // Observe the locationLiveData from the ViewModel
    val locationLiveData = viewModel.locationLiveData.observeAsState()
    // Get the current city name from the observed LiveData
    val city = locationLiveData.value ?: ""

    // Display the search view
    searchView(
        onSearch = onSearch, // Pass the search action callback
        currentCity = city, // Pass the current city name
        viewModel = viewModel // Pass the ViewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun searchView(
    onSearch: (String) -> Unit, // Callback function to handle the search action
    currentCity: String, // The current city name to display in the text field
    viewModel: SearchViewModel // The ViewModel to save the city name
) {
    // Get the current context
    val context = LocalContext.current

    // Remember the text state and initialize it with the current city name
    val (text, setText) = remember(currentCity) { mutableStateOf(currentCity) }

    // Layout the search view components in a row
    Row(
        modifier = Modifier
            .fillMaxWidth() // Make the row take the full width
            .padding(16.dp), // Add padding around the row
        horizontalArrangement = Arrangement.SpaceBetween // Space out the components horizontally
    ) {
        // Text field for entering the city name
        OutlinedTextField(
            value = text, // Bind the text state to the text field value
            onValueChange = setText, // Update the text state when the text field value changes
            label = { Text(ENTER_CITY_NAME) }, // Label for the text field
            modifier = Modifier
                .weight(1f) // Make the text field take up available space
                .padding(end = 12.dp), // Add padding to the end of the text field
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search), // Set the IME action to "Search"
            keyboardActions = KeyboardActions(onSearch = {
                if (text.isNotBlank()) {
                    // Perform the search action if the text is not blank
                    onSearch(text.trim())
                    setText("") // Clear the text field
                    viewModel.saveCityName(text.trim()) // Save the city name using the ViewModel
                }
            })
        )
        // Button for initiating the search action
        Button(
            onClick = {
                if (text.isNotBlank()) {
                    val city = text.trim()
                    // Perform the search action if the text is not blank
                    onSearch(city)
                    viewModel.saveCityName(city) // Save the city name using the ViewModel
                } else {
                    // Show a toast message if the text is blank
                    Toast.makeText(context, EMPTY_CITY_NAME_ERROR_MESSAGE, Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .alignByBaseline() // Align the button with the baseline of the text field
                .padding(top = 10.dp) // Add padding to the top of the button
        ) {
            Text(SEARCH_UPPER_CASE) // Text for the button
        }
    }
}
