package org.datepollsystems.waiterrobot.shared.core.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

sealed class Resource<T> {
    abstract val data: T?

    fun map(transform: (T?) -> T?): Resource<T> {
        @Suppress("UNCHECKED_CAST")
        return when (this) {
            is Error -> Error(exception, transform(data))
            is Loading -> Loading(transform(data))
            is Success -> Success(transform(data) as T)
        }
    }

    class Loading<T>(override val data: T? = null) : Resource<T>()
    class Success<T>(override val data: T) : Resource<T>()
    class Error<T>(val exception: Throwable, override val data: T? = null) : Resource<T>()
}

fun <T> Flow<Resource<T>>.mapResource(transform: (T?) -> T?): Flow<Resource<T>> =
    this.map { it.map(transform) }

fun <I, O> Resource<I>.mapType(transform: (I?) -> O?): Resource<O> {
    @Suppress("UNCHECKED_CAST")
    return when (this) {
        is Resource.Error -> Resource.Error(exception, transform(data))
        is Resource.Loading -> Resource.Loading(transform(data))
        is Resource.Success -> Resource.Success(transform(data) as O)
    }
}
