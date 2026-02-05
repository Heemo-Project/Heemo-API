package com.yeonghoon.heemo.couple

data class CoupleConnectedEvent(
    val coupleId: Long,
    val userId1: Long,
    val userId2: Long
)
