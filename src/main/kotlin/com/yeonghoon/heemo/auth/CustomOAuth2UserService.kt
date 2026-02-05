package com.yeonghoon.heemo.auth

import com.yeonghoon.heemo.auth.dto.OAuthAttributes
import com.yeonghoon.heemo.user.SocialProvider
import com.yeonghoon.heemo.user.User
import com.yeonghoon.heemo.user.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CustomOAuth2UserService(
    private val userRepository: UserRepository
) : OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Transactional
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val delegate = DefaultOAuth2UserService()
        val oAuth2User = delegate.loadUser(userRequest)

        // kakao, google 등 구분
        val registrationId = userRequest.clientRegistration.registrationId
        val userNameAttributeName = userRequest.clientRegistration.providerDetails
            .userInfoEndpoint.userNameAttributeName

        val provider = SocialProvider.valueOf(registrationId.uppercase(Locale.getDefault()))
        
        val attributes = OAuthAttributes.of(provider, userNameAttributeName, oAuth2User.attributes)
        val user = saveOrUpdate(attributes)

        return DefaultOAuth2User(
            Collections.singleton(SimpleGrantedAuthority("ROLE_${user.role.name}")),
            attributes.attributes,
            attributes.nameAttributeKey
        )
    }

    private fun saveOrUpdate(attributes: OAuthAttributes): User {
        val user = userRepository.findBySocialIdAndProvider(attributes.socialId, attributes.provider)
            .map { entity ->
                entity.nickname = attributes.nickname
                entity.profileImageUrl = attributes.profileImageUrl
                entity
            }
            .orElse(attributes.toEntity())

        return userRepository.save(user)
    }
}
