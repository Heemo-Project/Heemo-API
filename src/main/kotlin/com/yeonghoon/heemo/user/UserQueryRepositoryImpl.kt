package com.yeonghoon.heemo.user

import com.querydsl.jpa.impl.JPAQueryFactory
import com.yeonghoon.heemo.user.QUser.user
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
class UserQueryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : UserQueryRepository {

    override fun findPartner(coupleId: Long, myId: Long): Optional<User> {
        val foundUser = queryFactory
            .selectFrom(user)
            .where(
                user.couple.id.eq(coupleId),
                user.id.ne(myId)
            )
            .fetchOne()
        
        return Optional.ofNullable(foundUser)
    }
}
