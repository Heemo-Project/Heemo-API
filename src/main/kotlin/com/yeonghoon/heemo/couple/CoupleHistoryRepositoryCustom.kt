package com.yeonghoon.heemo.couple

import java.time.LocalDateTime

interface CoupleHistoryRepositoryCustom {
    fun findByDisconnectedAtBetween(start: LocalDateTime, end: LocalDateTime): List<CoupleHistory>
    fun findByMemberId(userId: Long): List<CoupleHistory>
}
