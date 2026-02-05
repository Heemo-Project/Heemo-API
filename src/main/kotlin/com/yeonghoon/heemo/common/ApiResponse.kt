package com.yeonghoon.heemo.common

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "공통 API 응답 포맷")
data class ApiResponse<T>(
    @Schema(description = "요청 성공 여부", example = "true")
    val success: Boolean,
    
    @Schema(description = "응답 데이터 (성공 시)")
    val data: T? = null,
    
    @Schema(description = "에러 정보 (실패 시)")
    val error: ApiError? = null
) {
    companion object {
        fun <T> success(data: T): ApiResponse<T> = ApiResponse(true, data)
        fun error(message: String, code: String): ApiResponse<Nothing> = 
            ApiResponse(false, error = ApiError(message, code))
    }
}

@Schema(description = "API 에러 상세 정보")
data class ApiError(
    @Schema(description = "에러 메시지", example = "잘못된 요청입니다.")
    val message: String,
    
    @Schema(description = "에러 코드", example = "ERR_001")
    val code: String
)
