package com.odeyalo.sonata.authorization.support.scope

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.odeyalo.sonata.authorization.support.scope.loader.JsonFileScopeLoader
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

class JsonFileScopeLoaderTest {

    @Test
    @DisplayName("Get all scopes from file and expect parsed JSON as flux")
    fun getScopes() {
        // given
        val testMapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())

        val path =
            Path.of("").toAbsolutePath().toString() +
            File.separator +"src" + File.separator + "test" + File.separator + "/resources" + File.separator + "scopes-test.json"

        val loader = JsonFileScopeLoader(testMapper, path)

        loader.loadScopes()
            .log()
            .subscribe();
    }
}