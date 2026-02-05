package com.yeonghoon.heemo.couple

import com.yeonghoon.heemo.common.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tb_couple_history")
class CoupleHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val coupleId: Long,

    @Column(nullable = false)
    val member1Id: Long,

    @Column
    val member2Id: Long? = null,

    @Column
    val connectedAt: LocalDateTime? = null,

    @Column(nullable = false)
    val disconnectedAt: LocalDateTime = LocalDateTime.now()

) : BaseEntity()
