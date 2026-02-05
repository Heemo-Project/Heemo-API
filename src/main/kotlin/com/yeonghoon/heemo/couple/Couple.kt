package com.yeonghoon.heemo.couple

import com.yeonghoon.heemo.common.BaseEntity
import jakarta.persistence.*
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "tb_couple")
class Couple(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true, nullable = false, length = 36)
    val inviteCode: String = UUID.randomUUID().toString(),

    @Column
    var anniversaryDate: LocalDate? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: CoupleStatus = CoupleStatus.CONNECTED

) : BaseEntity()

enum class CoupleStatus {
    CONNECTED,      // 연결됨
    DISCONNECTED    // 연결 해제됨
}
