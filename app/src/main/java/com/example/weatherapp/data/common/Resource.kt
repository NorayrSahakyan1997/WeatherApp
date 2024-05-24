package com.example.weatherapp.data.common

// A sealed class to represent the state of a resource (e.g., data loading, success, error)
sealed class Resource<T>(val data: T? = null, val message: String? = null) {

    // A subclass to represent a successful state with the loaded data
    class Success<T>(data: T?) : Resource<T>(data)

    // A subclass to represent an error state with an error message and optionally the data
    class Error<T>(message: String?, data: T? = null) : Resource<T>(data, message)

    // A subclass to represent a loading state, optionally with some interim data
    class Loading<T>(data: T? = null) : Resource<T>(data)
}
