package com.example.weatherapp.data.common

object Constants {

    //Remote
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val API_KEY = "59a608e87a293c0325a087132ad7b87e"

    //Screen names, Keys
    const val SEARCH = "search"
    const val SEARCH_UPPER_CASE = "Search"
    const val WEATHER = "weather"
    const val CITY_NAME = "cityName"
    const val ENTER_CITY_NAME = "Enter a city name"


    //Toast, error message
    const val EMPTY_CITY_NAME_ERROR_MESSAGE = "Please enter a city name to search."
    const val PERMISSION_DENIED_MESSAGE =
        "Unfortunately, we cannot receive your current location, you should type it manually."

    //Shared preference
    const val LAST_SAVED_CITY_KEY = "last_searched_city"
    const val MY_SHARED_PREF_KEY = "MySharedPref"

    //Location, Default
    const val LAT: Double = 44.34
    const val LONG: Double = 10.99
}