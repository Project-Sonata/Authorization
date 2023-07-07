package com.odeyalo.sonata.authorization.config

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule

@Configuration
open class ObjectMapperConfiguration {

    @Bean
    open fun objectMapper(): ObjectMapper {
        return ObjectMapper().registerModules(KotlinModule(), JavaTimeModule(), Jackson2HalModule())
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
    }
}