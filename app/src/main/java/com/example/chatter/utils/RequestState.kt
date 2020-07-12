package com.example.chatter.utils

data class RequestState(
    val status: Status,
    val message: String? = null
) {

    companion object {
        fun loading() : RequestState {
            return RequestState(status = Status.LOADING)
        }
        fun success() : RequestState {
            return RequestState(status = Status.SUCCESS)
        }
        fun error(message: String?) : RequestState {
            return RequestState(status = Status.ERROR, message = message)
        }
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}