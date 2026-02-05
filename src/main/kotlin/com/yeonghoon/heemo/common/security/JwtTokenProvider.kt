package com.yeonghoon.heemo.common.security

import com.yeonghoon.heemo.user.Role
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}") private var secretKey: String,
    @Value("\${jwt.access-token-validity-in-seconds}") private val accessTokenValidityInSeconds: Long,
    @Value("\${jwt.refresh-token-validity-in-seconds}") private val refreshTokenValidityInSeconds: Long
) {

    private lateinit var key: SecretKey

    @PostConstruct
    fun init() {
        this.key = Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))
    }

    // 토큰 생성 (Access, Refresh)
    fun createToken(userId: Long, role: Role): TokenDto {
        val now = Date()
        val accessTokenExpiresIn = Date(now.time + accessTokenValidityInSeconds * 1000)
        val refreshTokenExpiresIn = Date(now.time + refreshTokenValidityInSeconds * 1000)

        val accessToken = Jwts.builder()
            .subject(userId.toString())
            .claim("role", role.name)
            .issuedAt(now)
            .expiration(accessTokenExpiresIn)
            .signWith(key)
            .compact()

        val refreshToken = Jwts.builder()
            .subject(userId.toString())
            .issuedAt(now)
            .expiration(refreshTokenExpiresIn)
            .signWith(key)
            .compact()

        return TokenDto(
            accessToken = accessToken,
            refreshToken = refreshToken,
            accessTokenExpiresIn = accessTokenValidityInSeconds
        )
    }

    // 토큰에서 인증 정보 추출
    fun getAuthentication(token: String): Authentication {
        val claims = getClaims(token)
        val userId = claims.subject
        val role = claims["role"] as String

        val authorities = listOf(SimpleGrantedAuthority("ROLE_$role"))
        val principal = User(userId, "", authorities)

        return UsernamePasswordAuthenticationToken(principal, "", authorities)
    }

    // 토큰 유효성 검증
    fun validateToken(token: String): Boolean {
        return try {
            getClaims(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun getClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
    }
}

data class TokenDto(
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresIn: Long
)
