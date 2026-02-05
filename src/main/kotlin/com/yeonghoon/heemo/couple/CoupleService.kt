package com.yeonghoon.heemo.couple

import com.yeonghoon.heemo.user.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CoupleService(
    private val coupleRepository: CoupleRepository,
    private val userRepository: UserRepository
) {

    /**
     * 새로운 초대 코드 생성 (커플 대기 상태 생성)
     */
    @Transactional
    suspend fun createInviteCode(userId: Long): String = withContext(Dispatchers.IO) {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }
        
        if (user.couple != null) {
            throw IllegalStateException("User already belongs to a couple")
        }

        val couple = coupleRepository.save(Couple())
        user.couple = couple
        userRepository.save(user)
        
        couple.inviteCode
    }

    /**
     * 초대 코드로 커플 매칭
     */
    @Transactional
    suspend fun connectByInviteCode(userId: Long, inviteCode: String) = withContext(Dispatchers.IO) {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        if (user.couple != null) {
            throw IllegalStateException("User already belongs to a couple")
        }

        val couple = coupleRepository.findByInviteCode(inviteCode)
            .orElseThrow { IllegalArgumentException("Invalid invite code") }

        user.couple = couple
        userRepository.save(user)
    }
}
