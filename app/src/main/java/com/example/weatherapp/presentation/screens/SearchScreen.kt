package com.example.weatherapp.presentation.screens
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.Constants.EMPTY_CITY_NAME_ERROR_MESSAGE
import com.example.weatherapp.data.Constants.ENTER_CITY_NAME
import com.example.weatherapp.data.Constants.SEARCH_UPPER_CASE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(onSearch: (String) -> Unit) {
    val (text, setText) = remember { mutableStateOf("") }
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween // This arranges children to space between them
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = setText,
            label = { Text(ENTER_CITY_NAME) },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                if (text.isNotBlank()) {
                    onSearch(text.trim())
                    setText("")
                }
            })
        )
        Button(
            onClick = {
                if (text.isNotBlank()) {
                    onSearch(text.trim())
                    setText("")
                } else {
                    Toast.makeText(context, EMPTY_CITY_NAME_ERROR_MESSAGE, Toast.LENGTH_SHORT)
                        .show()
                }
            },
            modifier = Modifier
                .alignByBaseline()
                .padding(top = 8.dp) // Ensures the button is centered relative to the baseline of the text field
        ) {
            Text(SEARCH_UPPER_CASE)
        }
    }
}
