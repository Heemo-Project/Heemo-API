package com.yeonghoon.heemo.user.controller

import com.yeonghoon.heemo.common.dto.ApiResponse
import com.yeonghoon.heemo.user.domain.User
import com.yeonghoon.heemo.user.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {

    @GetMapping("/{id}")
    suspend fun getUser(@PathVariable id: Long): ApiResponse<User> {
        val user = userService.getUser(id)
            ?: throw IllegalArgumentException("User not found with id: $id")
        return ApiResponse.success(user)
    }
}
