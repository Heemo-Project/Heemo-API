package com.yeonghoon.heemo.auth

import com.yeonghoon.heemo.common.JwtTokenProvider
import com.yeonghoon.heemo.user.SocialProvider
import com.yeonghoon.heemo.user.UserRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.util.*

@Component
class OAuth2LoginSuccessHandler(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userRepository: UserRepository,
    @Value("\${app.oauth2.authorized-redirect-uri}") private val redirectUri: String
) : SimpleUrlAuthenticationSuccessHandler() {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val oAuth2User = authentication.principal as OAuth2User
        val oauthToken = authentication as OAuth2AuthenticationToken
        
        // CustomOAuth2UserService에서 provider를 추출하거나 token에서 추출
        // registrationId는 구글, 카카오 등 소문자
        val registrationId = oauthToken.authorizedClientRegistrationId
        val provider = SocialProvider.valueOf(registrationId.uppercase(Locale.getDefault()))
        
        // OAuthAttributes 로직과 동일하게 socialId 추출 필요
        // 하지만 여기서는 이미 loadUser를 거쳐왔으므로 attributes에 값이 있음
        // 구글: sub, 카카오: id
        val attributes = oAuth2User.attributes
        val socialId = when (provider) {
            SocialProvider.GOOGLE -> attributes["sub"] as String
            SocialProvider.KAKAO -> attributes["id"].toString()
        }

        val user = userRepository.findBySocialIdAndProvider(socialId, provider)
            .orElseThrow { IllegalArgumentException("User not found") }

        val tokenDto = jwtTokenProvider.createToken(user.id!!, user.role)

        val targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
            .queryParam("accessToken", tokenDto.accessToken)
            .queryParam("refreshToken", tokenDto.refreshToken)
            .build().toUriString()

        clearAuthenticationAttributes(request)
        redirectStrategy.sendRedirect(request, response, targetUrl)
    }
}
