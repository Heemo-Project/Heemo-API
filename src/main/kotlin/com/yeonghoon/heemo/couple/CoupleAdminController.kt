package com.yeonghoon.heemo.couple

import com.yeonghoon.heemo.common.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Couple Admin", description = "관리자 전용 커플 데이터 관리 API")
@RestController
@RequestMapping("/api/v1/admin/couples")
class CoupleAdminController(
    private val coupleService: CoupleService
) {

    @Operation(summary = "커플 해제 이력 전체 조회", description = "시스템 내의 모든 커플 해제 이력을 조회합니다.")
    @GetMapping("/history")
    suspend fun getHistory(): ApiResponse<List<CoupleHistory>> {
        val history = coupleService.getAllCoupleHistories()
        return ApiResponse.success(history)
    }
}
