package com.yeonghoon.heemo.user.repository

import com.yeonghoon.heemo.user.domain.SocialProvider
import com.yeonghoon.heemo.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, Long> {
    fun findBySocialIdAndProvider(socialId: String, provider: SocialProvider): Optional<User>
}
