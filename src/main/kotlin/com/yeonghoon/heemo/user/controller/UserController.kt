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

@Tag(name = "User", description = "User Management API")
@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {

    @Operation(summary = "Get User Profile", description = "Retrieve user information by ID.")
    @GetMapping("/{id}")
    suspend fun getUser(@PathVariable id: Long): ApiResponse<User> {
        val user = userService.getUser(id)
            ?: throw IllegalArgumentException("User not found with id: $id")
        return ApiResponse.success(user)
    }
}
