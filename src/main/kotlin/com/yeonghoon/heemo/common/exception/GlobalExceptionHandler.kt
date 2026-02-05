package com.yeonghoon.heemo.common.exception

import com.yeonghoon.heemo.common.ApiResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * 비즈니스 로직 상의 잘못된 인자 예외 (400 Bad Request)
     */
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ApiResponse<Nothing>> {
        log.warn("Illegal Argument: {}", e.message)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(e.message ?: "Invalid Argument", "BAD_REQUEST"))
    }

    /**
     * 객체 상태가 요청을 수행하기에 적절하지 않은 경우 (400 Bad Request)
     * 예: 이미 커플인 유저가 매칭 시도
     */
    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(e: IllegalStateException): ResponseEntity<ApiResponse<Nothing>> {
        log.warn("Illegal State: {}", e.message)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(e.message ?: "Invalid State", "ILLEGAL_STATE"))
    }

    /**
     * @Valid 검증 실패 예외 (400 Bad Request)
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Nothing>> {
        val fieldError = e.bindingResult.fieldError
        val message = "[${fieldError?.field}] ${fieldError?.defaultMessage ?: "Validation Error"}"
        log.warn("Validation Error: {}", message)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(message, "VALIDATION_FAILED"))
    }

    /**
     * 메소드 인자 타입이 일치하지 않는 경우 (400 Bad Request)
     * 예: ID 자리에 문자열이 들어오는 경우
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatchException(e: MethodArgumentTypeMismatchException): ResponseEntity<ApiResponse<Nothing>> {
        val message = "${e.name} field type mismatch (expected: ${e.requiredType?.simpleName})"
        log.warn("Type Mismatch: {}", message)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(message, "TYPE_MISMATCH"))
    }

    /**
     * 지원하지 않는 HTTP Method 호출 시 (405 Method Not Allowed)
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleMethodNotSupportedException(e: HttpRequestMethodNotSupportedException): ResponseEntity<ApiResponse<Nothing>> {
        val message = "HTTP Method '${e.method}' is not supported."
        log.warn(message)
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
            .body(ApiResponse.error(message, "METHOD_NOT_ALLOWED"))
    }

    /**
     * 인가 실패 (403 Forbidden)
     */
    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(e: AccessDeniedException): ResponseEntity<ApiResponse<Nothing>> {
        log.warn("Access Denied: {}", e.message)
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ApiResponse.error("Access is denied", "FORBIDDEN"))
    }

    /**
     * 최상위 예외 처리 (500 Internal Server Error)
     */
    @ExceptionHandler(Exception::class)
    fun handleGeneralException(e: Exception): ResponseEntity<ApiResponse<Nothing>> {
        log.error("Unhandled Internal Server Error: ", e)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error("An unexpected error occurred", "INTERNAL_SERVER_ERROR"))
    }
}