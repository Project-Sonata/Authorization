package com.odeyalo.sonata.authorization.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ObjectMapperConfiguration {

    @Bean
    open fun scopeLoaderObjectMapper(): ObjectMapper {
        return ObjectMapper().registerModule(KotlinModule())
    }
}