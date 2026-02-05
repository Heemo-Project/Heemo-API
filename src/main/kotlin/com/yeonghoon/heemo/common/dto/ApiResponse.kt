package com.yeonghoon.heemo.common.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: ApiError? = null
) {
    companion object {
        fun <T> success(data: T): ApiResponse<T> = ApiResponse(true, data)
        fun error(message: String, code: String): ApiResponse<Nothing> = 
            ApiResponse(false, error = ApiError(message, code))
    }
}

data class ApiError(
    val message: String,
    val code: String
)
