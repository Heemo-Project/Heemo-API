package com.yeonghoon.heemo.couple

import com.yeonghoon.heemo.user.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CoupleService(
    private val coupleRepository: CoupleRepository,
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
        userRepository.save(user)

        // 매칭 완료 이벤트 발행 (상대방 찾기)
        // 현재 커플 ID를 가진 다른 유저(상대방)를 찾아서 이벤트에 포함
        // 주의: 위에서 save를 호출했지만 트랜잭션이 아직 커밋되지 않았을 수 있으므로
        // 현재 로직상 상대방은 DB에 이미 저장되어 있는 유일한 1명임.
        // 하지만 안전하게 로직을 구성하려면 쿼리가 필요할 수도 있음.
        // 여기서는 간단하게 '상대방이 누구인지'는 이벤트 리스너에서 조회하거나
        // 혹은 여기서 조회해서 넘겨줄 수 있음.
        
        // 현재 user(나)와 couple에 이미 연결된 다른 유저(상대방) 조회
        // (현재 유저는 아직 커밋 전이라 count에 포함 안 될 수도 있지만, save 호출 순서에 따라 다름)
        // JPA 영속성 컨텍스트 고려 시, 가장 확실한 건 couple 객체만 넘기거나
        // 여기서 상대방 ID를 찾는 것임. 하지만 여기서는 일단 이벤트를 발행하고
        // 이벤트 핸들러가 처리하도록 설계하는 것이 깔끔함.
        
        // 편의상 상대방 ID를 찾아서 이벤트에 담아줌 (이미 1명이 대기 중이므로)
        // *주의*: `user` 엔티티는 이 시점에 couple이 세팅되었지만 flush 전일 수 있음.
        // 기존에 등록된 유저를 찾음 (자신 제외)
        // 하지만 더 간단하게, 이벤트에는 '누가 연결되었는지'만 담고 
        // 알림 서비스가 알아서 '누가 이 커플에 있는지' 조회해서 알림을 보내는 게 나음.
        
        val partner = userRepository.findByCoupleIdAndIdNot(couple.id, userId)
            .orElseThrow { IllegalStateException("Partner not found in couple") }

        eventPublisher.publishEvent(CoupleConnectedEvent(
            coupleId = couple.id,
            userId1 = userId,
            userId2 = partner.id!!
        ))
    }
}
