package com.odeyalo.sonata.authorization.support.scope.loader

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.odeyalo.sonata.authorization.exception.ScopeLoadingFailedException
import com.odeyalo.sonata.authorization.support.scope.JsonScope
import com.odeyalo.sonata.authorization.support.scope.Scope
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path

/**
 * Load the scopes from Json file
 */
@Component
class JsonFileScopeLoader(
    private val scopeLoaderObjectMapper: ObjectMapper,
    @Value("\${sonata.authorization.scope.placeholder.json}") private val fileName: String
) : ScopeLoader {

    private val logger: Logger = LoggerFactory.getLogger(JsonFileScopeLoader::class.java)

    init {
        if (!fileName.endsWith(".json")) {
            throw ScopeLoadingFailedException("The file is not JSON file and cannot be processed by this implementation")
        }
        if (Files.exists(Path.of(fileName)).not()) {
            throw ScopeLoadingFailedException(
                "The file $fileName does not exist!",
                FileNotFoundException("File name with with name $fileName does not exist")
            )
        }
        try {
            scopeLoaderObjectMapper.readTree(File(fileName));
        } catch (ex: Exception) {
            throw ScopeLoadingFailedException("The file contains not valid JSON!", ex)
        }
    }

    @Throws(ScopeLoadingFailedException::class)
    override fun loadScopes(): Flux<Scope> {
        return try {
            val container: ScopeContainer = scopeLoaderObjectMapper.readValue(File(fileName))
            this.logger.info("Loaded scopes from: $fileName. Final size of scopes is ${container.scopes.size}")
            Flux.fromIterable(container.scopes)
        } catch (e: Exception) {
            this.logger.error("Failed to load scopes from json file from path: $fileName.", e)
            Flux.error(ScopeLoadingFailedException("Failed to load scopes using JSON file.", e))
        }
    }

    private data class ScopeContainer(@JsonProperty("scopes") val scopes: List<JsonScope>)
}