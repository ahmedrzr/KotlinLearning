package com.example.kotlinlearning.network.apihelpers

class Resource<out T>(val status: Status, val data: T? = null, val message: String? = null) {

    companion object {
        fun <T> loading(data: T?): Resource<T> =
            Resource(Status.LOADING, null, null)

        fun <T> success(data: T?): Resource<T> =
            Resource(Status.SUCCESS, data, null)

        fun <T> error(msg: String, data: T?): Resource<T> =
            Resource(Status.ERROR, null, msg)
    }
}