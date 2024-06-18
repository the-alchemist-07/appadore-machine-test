package com.appadore.test.core.common

sealed class Resource<out T> {
    data class Success<out T>(val value: T) : Resource<T>()
    data class Error(val error: String) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
}