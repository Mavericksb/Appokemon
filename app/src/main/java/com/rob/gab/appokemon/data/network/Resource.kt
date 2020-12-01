package com.rob.gab.appokemon.data.network

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
data class Resource<out T>(
    val status: Status,
    val data: T? = null,
    val errorBody: T? = null,
    val errorMessage: String? = null
) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(errorBody: T?): Resource<T> {
            return Resource(Status.ERROR, null, errorBody)
        }

        fun error(errorMessage: String?): Resource<String> {
            return Resource(Status.ERROR, null, errorMessage = errorMessage)
        }

        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING, null, null)
        }
    }

    /**
     * Status of a resource that is provided to the UI.
     *
     *
     * These are usually created by the Repository classes where they return
     * `LiveData<Resource<T>>` to pass back the latest data to the UI with its fetch status.
     */
    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }
}