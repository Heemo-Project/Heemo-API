package com.yeonghoon.heemo.user

import java.util.Optional

interface UserQueryRepository {
    fun findPartner(coupleId: Long, myId: Long): Optional<User>
}