package com.yeonghoon.heemo.user.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.yeonghoon.heemo.user.domain.QUser.user
import com.yeonghoon.heemo.user.domain.User
import org.springframework.stereotype.Repository

@Repository
class UserQueryRepository(
    private val queryFactory: JPAQueryFactory
) {
    fun findByNickname(nickname: String): User? {
        return queryFactory.selectFrom(user)
            .where(user.nickname.eq(nickname))
            .fetchOne()
    }
}
