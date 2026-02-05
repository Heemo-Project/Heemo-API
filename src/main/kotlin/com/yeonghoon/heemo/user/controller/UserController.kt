package com.yeonghoon.heemo.user.controller

import com.yeonghoon.heemo.common.ApiResponse
import com.yeonghoon.heemo.user.User
import com.yeonghoon.heemo.user.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "User", description = "사용자 관련 API")
@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {

    @Operation(summary = "사용자 조회", description = "ID를 통해 사용자 정보를 조회합니다.")
    @GetMapping("/{id}")
    suspend fun getUser(@PathVariable id: Long): ApiResponse<User> {
        val user = userService.getUser(id)
            ?: throw IllegalArgumentException("User not found with id: $id")
        return ApiResponse.success(user)
    }
}
