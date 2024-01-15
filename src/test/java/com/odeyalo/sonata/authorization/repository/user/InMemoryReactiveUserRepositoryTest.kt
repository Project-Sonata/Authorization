package com.odeyalo.sonata.authorization.repository.user

import com.odeyalo.sonata.authorization.entity.User
import com.odeyalo.sonata.authorization.repository.memory.InMemoryReactiveUserRepository
import com.odeyalo.sonata.authorization.testing.faker.UserFaker
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestInstance

/**
 * Tests for [InMemoryReactiveUserRepository] class
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InMemoryReactiveUserRepositoryTest {

    companion object Constants {
        private const val EXISTING_USERNAME = "mikunakano"
    }

    private val repository: InMemoryReactiveUserRepository =
        InMemoryReactiveUserRepository()

    private lateinit var existingUser: User


    @BeforeAll
    fun createUsers() {
        for (i in 0..30) {
            val user = UserFaker.createUser()
                .overrideId(i.toLong())
                .asInMemoryUser()
            repository.save(user).block()
        }
    }

    @BeforeEach
    fun prepareExistingUsers() {
        val inMemoryUser = UserFaker.createUser().overrideUsername(EXISTING_USERNAME).asInMemoryUser()

        existingUser = repository.save(inMemoryUser).block()!!
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
    fun findUserByUsername_andExpectUser() {
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
    @DisplayName("Delete user by existing username and expect user to be deleted")
    fun deleteUserByExistingUsername_andExpectUserToBeDeleted() {
        repository.deleteUserByUsername(EXISTING_USERNAME).block()
        val user = repository.findUserByUsername(EXISTING_USERNAME).block()
        assertNull(user, "User must be null if existing user was deleted!")
    }

    @Test
    @DisplayName("Delete user by not existing username and expect nothing to be deleted")
    fun deleteUserByNotExistingUsername_andExpectNothingToBeDeleted() {
        val beforeDeletion = repository.findAll().toIterable().toList()

        repository.deleteUserByUsername("not existing").block()

        val afterDeletion = repository.findAll().toIterable().toList()

        assertEquals(beforeDeletion, afterDeletion, "If user does not exist by username, then nothing must be deleted!")
    }

    @Test
    @DisplayName("Find existing user by existed ID in repository and expect user to be returned")
    fun findUserByExistingId_andExpectUserToBeReturned() {
        val actualUser = repository.findById(existingUser.id).block()
        assertEquals(
            existingUser, actualUser, "If user exists in repository, then exactly the same user must be returned!"
        )
    }


    @Test
    @DisplayName("Find user by non-existed ID in repository and expect null to be returned")
    fun findUserByNotExistingId_andExpectEmptyMonoToBeReturned() {
        val user = repository.findById(-1L).block()
        assertNull(user, "If ID does not presented in repository, then null must be returned!")
    }


    @Test
    @DisplayName("Find all users and expect all users to be returned")
    fun findAllUsers_andExpectAllUsersToBeReturned() {
        val preparedUsers = listOf(
            UserFaker.createUser().asInMemoryUser(),
            UserFaker.createUser().asInMemoryUser(),
            UserFaker.createUser().asInMemoryUser()
        )

        val repo = InMemoryReactiveUserRepository(
            preparedUsers
        )
        val foundUsers = repo.findAll().collectList().block()

        assertThat(preparedUsers)
            .describedAs("The users must be equal!")
            .hasSameElementsAs(foundUsers)
    }

    @Test
    @DisplayName("Delete by existing user id and expect user to be deleted")
    fun deleteByExistingId_andExpectUserToBeDeleted() {
        repository.deleteById(existingUser.id).block()

        val listOfUsers = repository.findAll().toIterable().toList()

        assertFalse(listOfUsers.contains(existingUser), "If existing id is used, then user must be deleted!")
    }


    @Test
    @DisplayName("Delete by not existing user id and expect nothing to be deleted")
    fun deleteByNotExistingId_andExpectUserToBeDeleted() {
        val beforeDeletion = repository.findAll().toIterable().toList()
        repository.deleteById(-1L).block()
        val afterDeletion = repository.findAll().toIterable().toList()
        assertEquals(beforeDeletion, afterDeletion, "If id does not exist, then nothing must be deleted!")
    }


    @Test
    @DisplayName("Should generate user id if ID was null")
    fun saveUser_andExpectUserIdToBeGenerated() {
        // given
        val user = UserFaker.createUser().overrideId(null).asInMemoryUser()
        // when
        val savedUser = repository.save(user).block()
        // then
        assertNotNull(savedUser.id, "Id must be generated if ID was set to null")
    }

    @Test
    @DisplayName("Should only save user if id is already set")
    fun shouldOnlySaveUser_ifIdIsSet() {
        val user = UserFaker.createUser().asInMemoryUser()
        val savedUser = repository.save(user).block()
        assertEquals(user, savedUser, "If ID is set, then nothing must be generated!")
    }
}