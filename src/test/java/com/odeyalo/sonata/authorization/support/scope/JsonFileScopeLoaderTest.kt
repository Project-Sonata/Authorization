package com.odeyalo.sonata.authorization.support.scope

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.odeyalo.sonata.authorization.exception.ScopeLoadingFailedException
import com.odeyalo.sonata.authorization.support.scope.loader.JsonFileScopeLoader
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import reactor.test.StepVerifier
import java.io.File
import java.nio.file.Path

class JsonFileScopeLoaderTest {
    companion object Constants {
        val SCOPE_JSON_FILE_PATH =
            Path.of("").toAbsolutePath().toString() +
                    File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "scopes-test.json"
    }

    @Test
    @DisplayName("Get all scopes from file and expect parsed JSON as flux")
    fun loadScopesFromJsonFile_andExpectFluxWith2ScopesToBeReturned() {
        // given
        val testMapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule()).enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
        val loader = JsonFileScopeLoader(testMapper, SCOPE_JSON_FILE_PATH)
        // when
        val scopesPublisher = loader.loadScopes();
        // then
        StepVerifier.create(scopesPublisher)
            .expectNext(JsonScope("user-account-modify", "Read user profile information", setOf("guest", "admin", "user"), Scope.Type.PRIVATE))
            .expectNext(JsonScope("user-library-read", "Read user's saved music library", setOf("admin", "user"), Scope.Type.PUBLIC))
            .expectComplete()
            .verify();
    }

    @Test
    @DisplayName("Use non-existing JSON file as source and expect exception to be returned")
    fun useNonExistingJsonFile_andExpectException() {
        // given
        val notExistingPath = "mikuisthebest.json"
        val testMapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())

        assertThrows<ScopeLoadingFailedException> { JsonFileScopeLoader(testMapper, notExistingPath) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["i.txt", "love.png", "miku.gif", "nakano.pdf"])
    @DisplayName("Use non-json file and expect exception to be thrown")
    fun useNonJsonFile_andExpectException(fileName: String) {
        val testMapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())

        assertThrows<ScopeLoadingFailedException> { JsonFileScopeLoader(testMapper, fileName) }
    }
}