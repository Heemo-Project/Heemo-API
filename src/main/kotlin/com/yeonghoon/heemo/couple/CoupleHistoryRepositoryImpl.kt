package com.yeonghoon.heemo.couple

import com.querydsl.jpa.impl.JPAQueryFactory
import com.yeonghoon.heemo.couple.QCoupleHistory.coupleHistory
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class CoupleHistoryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CoupleHistoryRepositoryCustom {

    override fun findByDisconnectedAtBetween(start: LocalDateTime, end: LocalDateTime): List<CoupleHistory> {
        return queryFactory
            .selectFrom(coupleHistory)
            .where(coupleHistory.disconnectedAt.between(start, end))
            .orderBy(coupleHistory.disconnectedAt.desc())
            .fetch()
    }

    override fun findByMemberId(userId: Long): List<CoupleHistory> {
        return queryFactory
            .selectFrom(coupleHistory)
            .where(
                coupleHistory.member1Id.eq(userId)
                    .or(coupleHistory.member2Id.eq(userId))
            )
            .orderBy(coupleHistory.disconnectedAt.desc())
            .fetch()
    }
}
