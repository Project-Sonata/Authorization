package com.odeyalo.sonata.authorization.repository.user

import com.odeyalo.sonata.authorization.entity.User
import com.odeyalo.sonata.authorization.testing.faker.UserFaker
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName

/**
 * Tests for [InMemoryReactiveUserRepository] class
 */
class InMemoryReactiveUserRepositoryTest {

    companion object Constants {
        private const val  EXISTING_USERNAME = "mikunakano"
    }

    private val repository: InMemoryReactiveUserRepository = InMemoryReactiveUserRepository()

    private lateinit var existingUser: User


    @BeforeAll
    fun createUsers() {
        for (i in 0..30) {
            val user = UserFaker.createUser()
                .overrideId(i)
                .asInMemoryUser()
            repository.save(user).block()
        }
    }

    @BeforeEach
    fun prepareExistingUsers() {
        val inMemoryUser = UserFaker.createUser().overrideUsername(EXISTING_USERNAME).asInMemoryUser()

        existingUser = repository.save(inMemoryUser).block() as User
    }

    @AfterEach
    fun clearExistingUsers() {
        repository.deleteUserByUsername(EXISTING_USERNAME).block()
    }

    @Test
    @DisplayName("Find existing user by username and expect not null to be returned")
    fun findUserByUsername_andExpectNotNull() {
        val user = repository.findUserByUsername(EXISTING_USERNAME).block()
        assertNotNull(user, "If user with username exists in storage, then user must be returned");
    }

    @Test
    @DisplayName("Find existing user by username and expect not null to be returned")
    fun findUserByUsername_andExpectuser() {
        val actualUser = repository.findUserByUsername(EXISTING_USERNAME).block()
        assertEquals(existingUser, actualUser, "Exactly same user must be returned if user present in repository!")
    }

    @Test
    @DisplayName("Find user by non-existing username and expect null to be returned")
    fun findUserByNotExistingUsername_andExpectNull() {
        val user = repository.findUserByUsername("not existing").block()
        assertNull(user, "If user with username not exist in storage, then null must be returned");
    }

    @Test
    fun deleteUserByUsername() {
    }

    @Test
    fun findById() {
    }

    @Test
    fun findAll() {
    }

    @Test
    fun deleteById() {
    }

    @Test
    fun save() {
    }
}