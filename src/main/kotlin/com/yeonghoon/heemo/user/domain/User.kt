package com.yeonghoon.heemo.user.domain

import com.yeonghoon.heemo.common.BaseEntity
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(
    name = "tb_user",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["socialId", "provider"])
    ]
)
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val socialId: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val provider: SocialProvider,

    @Column(nullable = false, length = 50)
    var nickname: String,

    @Column
    var birthday: LocalDate? = null,

    @Column(length = 4)
    var mbti: String? = null,

    @Column
    var anniversaryDate: LocalDate? = null, // 연인이 된 일자 (디데이 기준일)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: Role = Role.USER

) : BaseEntity()
