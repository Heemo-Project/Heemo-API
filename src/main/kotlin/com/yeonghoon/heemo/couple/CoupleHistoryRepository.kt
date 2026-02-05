package com.yeonghoon.heemo.couple

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface CoupleHistoryRepository : JpaRepository<CoupleHistory, Long> {
    // 관리자: 기간별 조회
    fun findByDisconnectedAtBetween(start: LocalDateTime, end: LocalDateTime): List<CoupleHistory>

    // 사용자: 본인 참여 이력 조회 (member1 또는 member2)
    fun findByMember1IdOrMember2IdOrderByDisconnectedAtDesc(member1Id: Long, member2Id: Long): List<CoupleHistory>
}
