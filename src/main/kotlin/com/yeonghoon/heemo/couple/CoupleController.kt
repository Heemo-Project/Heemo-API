package com.yeonghoon.heemo.couple

import com.yeonghoon.heemo.common.ApiResponse
import com.yeonghoon.heemo.couple.dto.CoupleInfoResponse
import com.yeonghoon.heemo.couple.dto.UpdateAnniversaryRequest
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

    @Operation(summary = "내 커플 정보 조회", description = "현재 연결된 커플 정보를 조회합니다.")
    @GetMapping
    suspend fun getCoupleInfo(
        @AuthenticationPrincipal userId: Long
    ): ApiResponse<CoupleInfoResponse> {
        val info = coupleService.getCoupleInfo(userId)
        return ApiResponse.success(info)
    }

    @Operation(summary = "기념일 수정", description = "커플 기념일을 수정합니다. (상대방 정보도 함께 업데이트됨)")
    @PatchMapping("/anniversary")
    suspend fun updateAnniversary(
        @AuthenticationPrincipal userId: Long,
        @RequestBody request: UpdateAnniversaryRequest
    ): ApiResponse<Unit> {
        coupleService.updateAnniversary(userId, request.date)
        return ApiResponse.success(Unit)
    }
}

data class ConnectRequest(
    val inviteCode: String
)
