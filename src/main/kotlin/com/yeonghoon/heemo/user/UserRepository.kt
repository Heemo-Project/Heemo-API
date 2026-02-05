package com.yeonghoon.heemo.user

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, Long>, UserQueryRepository {
    fun findBySocialIdAndProvider(socialId: String, provider: SocialProvider): Optional<User>
    fun countByCoupleId(coupleId: Long): Long
}
