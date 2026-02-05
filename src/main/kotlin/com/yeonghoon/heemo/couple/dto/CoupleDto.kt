package com.yeonghoon.heemo.couple.dto

import java.time.LocalDate

data class CoupleInfoResponse(
    val coupleId: Long,
    val inviteCode: String,
    val anniversaryDate: LocalDate?,
    val myNickname: String,
    val partnerNickname: String?,
    val dDay: Long?
)

data class UpdateAnniversaryRequest(
    val date: LocalDate
)
