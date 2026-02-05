package com.yeonghoon.heemo.couple

import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface CoupleRepository : JpaRepository<Couple, Long> {
    fun findByInviteCode(inviteCode: String): Optional<Couple>
}
