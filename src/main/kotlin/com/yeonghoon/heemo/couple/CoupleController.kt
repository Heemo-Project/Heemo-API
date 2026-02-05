package com.yeonghoon.heemo.couple

import com.yeonghoon.heemo.common.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@Tag(name = "Couple", description = "커플 매칭 관련 API")
@RestController
@RequestMapping("/api/v1/couples")
class CoupleController(
    private val coupleService: CoupleService
) {

    @Operation(summary = "초대 코드 생성", description = "커플 연결을 위한 새로운 초대 코드를 생성합니다.")
    @PostMapping("/invite")
    suspend fun createInviteCode(
        @AuthenticationPrincipal userId: Long
    ): ApiResponse<String> {
        val code = coupleService.createInviteCode(userId)
        return ApiResponse.success(code)
    }

    @Operation(summary = "초대 코드로 연결", description = "초대 코드를 입력하여 상대방과 커플로 연결합니다.")
    @PostMapping("/connect")
    suspend fun connect(
        @AuthenticationPrincipal userId: Long,
        @RequestBody request: ConnectRequest
    ): ApiResponse<Unit> {
        coupleService.connectByInviteCode(userId, request.inviteCode)
        return ApiResponse.success(Unit)
    }

    @Operation(summary = "커플 연결 해제", description = "현재 연결된 커플 관계를 해제합니다.")
    @DeleteMapping
    suspend fun disconnect(
        @AuthenticationPrincipal userId: Long
    ): ApiResponse<Unit> {
        coupleService.disconnect(userId)
        return ApiResponse.success(Unit)
    }
}

data class ConnectRequest(
    val inviteCode: String
)
