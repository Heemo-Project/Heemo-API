package com.yeonghoon.heemo.user.service

import com.yeonghoon.heemo.user.User
import com.yeonghoon.heemo.user.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository
) {

    @Transactional(readOnly = true)
    suspend fun getUser(id: Long): User? = withContext(Dispatchers.IO) {
        // JPA(Blocking I/O) 호출은 IO Dispatcher에서 실행하여 메인 스레드 차단 방지
        userRepository.findById(id).orElse(null)
    }

    @Transactional
    suspend fun registerUser(user: User): User = withContext(Dispatchers.IO) {
        userRepository.save(user)
    }
}
