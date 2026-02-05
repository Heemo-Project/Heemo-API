package com.yeonghoon.heemo.common

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Common API Response Format")
data class ApiResponse<T>(
    @Schema(description = "Request success status", example = "true")
    val success: Boolean,
    
    @Schema(description = "Response data (on success)")
    val data: T? = null,
    
    @Schema(description = "Error information (on failure)")
    val error: ApiError? = null
) {
    companion object {
        fun <T> success(data: T): ApiResponse<T> = ApiResponse(true, data)
        fun error(message: String, code: String): ApiResponse<Nothing> = 
            ApiResponse(false, error = ApiError(message, code))
    }
}

@Schema(description = "Detailed API Error Information")
data class ApiError(
    @Schema(description = "Error message", example = "Invalid request parameters.")
    val message: String,
    
    @Schema(description = "Error code", example = "ERR_001")
    val code: String
)
