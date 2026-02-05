package com.yeonghoon.heemo.auth.dto

import com.yeonghoon.heemo.user.Role
import com.yeonghoon.heemo.user.SocialProvider
import com.yeonghoon.heemo.user.User

data class OAuthAttributes(
    val attributes: Map<String, Any>,
    val nameAttributeKey: String,
    val socialId: String,
    val nickname: String,
    val email: String?,
    val profileImageUrl: String?,
    val provider: SocialProvider
) {
    companion object {
        fun of(
            provider: SocialProvider,
            userNameAttributeName: String,
            attributes: Map<String, Any>
        ): OAuthAttributes {
            return when (provider) {
                SocialProvider.GOOGLE -> ofGoogle(userNameAttributeName, attributes)
                SocialProvider.KAKAO -> ofKakao(userNameAttributeName, attributes)
            }
        }

        private fun ofGoogle(
            userNameAttributeName: String,
            attributes: Map<String, Any>
        ): OAuthAttributes {
            return OAuthAttributes(
                attributes = attributes,
                nameAttributeKey = userNameAttributeName,
                socialId = attributes["sub"] as String,
                nickname = attributes["name"] as String,
                email = attributes["email"] as String?,
                profileImageUrl = attributes["picture"] as String?,
                provider = SocialProvider.GOOGLE
            )
        }

        private fun ofKakao(
            userNameAttributeName: String,
            attributes: Map<String, Any>
        ): OAuthAttributes {
            val kakaoAccount = attributes["kakao_account"] as Map<*, *>
            val profile = kakaoAccount["profile"] as Map<*, *>?
            
            return OAuthAttributes(
                attributes = attributes,
                nameAttributeKey = userNameAttributeName,
                socialId = attributes["id"].toString(),
                nickname = profile?.get("nickname") as String? ?: "Unknown",
                email = kakaoAccount["email"] as String?,
                profileImageUrl = profile?.get("profile_image_url") as String?,
                provider = SocialProvider.KAKAO
            )
        }
    }

    fun toEntity(): User {
        return User(
            socialId = socialId,
            provider = provider,
            nickname = nickname,
            profileImageUrl = profileImageUrl,
            role = Role.USER
        )
    }
}
