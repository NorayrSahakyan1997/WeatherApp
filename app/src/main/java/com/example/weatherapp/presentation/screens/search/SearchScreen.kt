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
    onSearch: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val locationLiveData = viewModel.locationLiveData.observeAsState()
    val city = locationLiveData.value?:""

    searchView(
        onSearch = onSearch,
        currentCity = city,
        viewModel = viewModel
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun searchView(onSearch: (String) -> Unit, currentCity: String, viewModel: SearchViewModel) {
    val context = LocalContext.current

    // Update text state when currentCity changes
    val (text, setText) = remember(currentCity) { mutableStateOf(currentCity) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = setText,
            label = { Text(ENTER_CITY_NAME) },
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                if (text.isNotBlank()) {
                    onSearch(text.trim())
                    setText("")
                    viewModel.saveCityName(text.trim()) // Consider saving only on specific actions if needed
                }
            })
        )
        Button(
            onClick = {
                if (text.isNotBlank()) {
                    val city = text.trim()
                    onSearch(city)
                    viewModel.saveCityName(city)
                } else {
                    Toast.makeText(context, EMPTY_CITY_NAME_ERROR_MESSAGE, Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .alignByBaseline()
                .padding(top = 10.dp)
        ) {
            Text(SEARCH_UPPER_CASE)
        }
    }
}
