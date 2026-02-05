package com.yeonghoon.heemo.common.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("!prod")
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Heemo API")
                    .description("커플 힐링 & 데이트 코스 서비스 'Heemo' API 문서")
                    .version("v0.0.1")
            )
    }
}
