package com.yeonghoon.heemo.couple

import com.yeonghoon.heemo.couple.dto.CoupleInfoResponse
import com.yeonghoon.heemo.user.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

@Service
class CoupleService(
    private val coupleRepository: CoupleRepository,
    private val coupleHistoryRepository: CoupleHistoryRepository,
    private val userRepository: UserRepository,
    private val eventPublisher: ApplicationEventPublisher
) {

    /**
     * 새로운 초대 코드 생성 (커플 대기 상태 생성)
     */
    @Transactional
    suspend fun createInviteCode(userId: Long): String = withContext(Dispatchers.IO) {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }
        
        if (user.couple != null) {
            throw IllegalStateException("User already belongs to a couple")
        }

        val couple = coupleRepository.save(Couple())
        user.couple = couple
        userRepository.save(user)
        
        couple.inviteCode
    }

    /**
     * 초대 코드로 커플 매칭
     */
    @Transactional
    suspend fun connectByInviteCode(userId: Long, inviteCode: String) = withContext(Dispatchers.IO) {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        if (user.couple != null) {
            throw IllegalStateException("User already belongs to a couple")
        }

        val couple = coupleRepository.findByInviteCode(inviteCode)
            .orElseThrow { IllegalArgumentException("Invalid invite code") }

        // 커플 인원 제한 (2명) 검증
        val currentMemberCount = userRepository.countByCoupleId(couple.id!!)
        if (currentMemberCount >= 2) {
            throw IllegalStateException("This couple is already full (2/2)")
        }

        user.couple = couple
        // 연결 시 오늘을 기념일로 자동 설정 (없을 경우)
        if (couple.anniversaryDate == null) {
            couple.anniversaryDate = LocalDate.now()
            user.anniversaryDate = LocalDate.now()
        }
        
        userRepository.save(user)

        // 매칭 완료 이벤트 발행 (상대방 찾기)
        val partner = userRepository.findPartner(couple.id!!, userId)
            .orElseThrow { IllegalStateException("Partner not found in couple") }

        val partnerId = partner.id!!
        val coupleId = couple.id!!

        // 상대방 기념일도 동기화
        if (partner.anniversaryDate == null) {
            partner.anniversaryDate = couple.anniversaryDate
            userRepository.save(partner)
        }

        eventPublisher.publishEvent(CoupleConnectedEvent(
            coupleId = coupleId,
            userId1 = userId,
            userId2 = partnerId
        ))
    }

    /**
     * 커플 연결 해제
     */
    @Transactional
    suspend fun disconnect(userId: Long) = withContext(Dispatchers.IO) {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        val couple = user.couple ?: throw IllegalStateException("User is not in a couple")
        val coupleId = couple.id!!

        // 1. 상대방 찾기
        val partner = userRepository.findPartner(coupleId, userId)
        val partnerId = partner.map { it.id }.orElse(null)

        // 2. 이력 저장 (History)
        val history = CoupleHistory(
            coupleId = coupleId,
            member1Id = userId,
            member2Id = partnerId,
            connectedAt = couple.createdAt
        )
        coupleHistoryRepository.save(history)

        // 3. 상대방 연결 해제
        partner.ifPresent {
            it.couple = null
            userRepository.save(it)
        }

        // 4. 내 연결 해제
        user.couple = null
        userRepository.save(user)

        // 5. 커플 상태 변경
        couple.status = CoupleStatus.DISCONNECTED
        coupleRepository.save(couple)

        // 6. 연결 해제 이벤트 발행
        eventPublisher.publishEvent(CoupleDisconnectedEvent(
            coupleId = coupleId,
            userId1 = userId,
            userId2 = partnerId
        ))
    }

    /**
     * 내 커플 정보 조회
     */
    @Transactional(readOnly = true)
    suspend fun getCoupleInfo(userId: Long): CoupleInfoResponse = withContext(Dispatchers.IO) {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }
        
        val couple = user.couple ?: throw IllegalStateException("User is not in a couple")
        val partner = userRepository.findPartner(couple.id!!, userId).orElse(null)

        val dDay = couple.anniversaryDate?.let { 
            ChronoUnit.DAYS.between(it, LocalDate.now()) + 1 
        }

        CoupleInfoResponse(
            coupleId = couple.id!!,
            inviteCode = couple.inviteCode,
            anniversaryDate = couple.anniversaryDate,
            myNickname = user.nickname,
            partnerNickname = partner?.nickname,
            dDay = dDay
        )
    }

    /**
     * 기념일 수정
     */
    @Transactional
    suspend fun updateAnniversary(userId: Long, newDate: LocalDate) = withContext(Dispatchers.IO) {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        val couple = user.couple ?: throw IllegalStateException("User is not in a couple")
        
        // 커플 엔티티 및 멤버들 업데이트
        couple.anniversaryDate = newDate
        user.anniversaryDate = newDate
        
        val partner = userRepository.findPartner(couple.id!!, userId)
        partner.ifPresent {
            it.anniversaryDate = newDate
            userRepository.save(it)
        }
        
        userRepository.save(user)
        coupleRepository.save(couple)
    }

    /**
     * [관리자] 커플 이력 전체 조회 (기간 필터링 포함)
     */
    @Transactional(readOnly = true)
    suspend fun getAllCoupleHistories(
        startDate: LocalDate?,
        endDate: LocalDate?
    ): List<CoupleHistory> = withContext(Dispatchers.IO) {
        if (startDate != null && endDate != null) {
            val start = startDate.atStartOfDay()
            val end = endDate.atTime(LocalTime.MAX)
            coupleHistoryRepository.findByDisconnectedAtBetween(start, end)
        } else {
            coupleHistoryRepository.findAll()
        }
    }

    /**
     * [사용자] 본인의 과거 커플 이력 조회
     */
    @Transactional(readOnly = true)
    suspend fun getMyCoupleHistory(userId: Long): List<CoupleHistory> = withContext(Dispatchers.IO) {
        coupleHistoryRepository.findByMemberId(userId)
    }
}