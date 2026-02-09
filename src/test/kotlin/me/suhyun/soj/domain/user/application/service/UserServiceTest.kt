package me.suhyun.soj.domain.user.application.service

import me.suhyun.soj.domain.user.domain.model.User
import me.suhyun.soj.domain.user.domain.model.enums.AuthProvider
import me.suhyun.soj.domain.user.domain.model.enums.UserRole
import me.suhyun.soj.domain.user.domain.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var userService: UserService

    private val testUuid = UUID.randomUUID()

    private val testUser = User(
        id = 1L,
        uuid = testUuid,
        email = "test@test.com",
        nickname = "tester",
        password = "password",
        provider = AuthProvider.LOCAL,
        role = UserRole.USER
    )

    @BeforeEach
    fun setUp() {
        userService = UserService(userRepository)
    }

    @Nested
    inner class FindById {

        @Test
        fun `should find user by id`() {
            whenever(userRepository.findById(1L)).thenReturn(testUser)

            val result = userService.findById(1L)

            assertThat(result).isEqualTo(testUser)
        }

        @Test
        fun `should return null when user not found by id`() {
            whenever(userRepository.findById(999L)).thenReturn(null)

            val result = userService.findById(999L)

            assertThat(result).isNull()
        }
    }

    @Nested
    inner class FindByUuid {

        @Test
        fun `should find user by uuid`() {
            whenever(userRepository.findByUuid(testUuid)).thenReturn(testUser)

            val result = userService.findByUuid(testUuid)

            assertThat(result).isEqualTo(testUser)
        }

        @Test
        fun `should return null when user not found by uuid`() {
            val randomUuid = UUID.randomUUID()
            whenever(userRepository.findByUuid(randomUuid)).thenReturn(null)

            val result = userService.findByUuid(randomUuid)

            assertThat(result).isNull()
        }
    }

    @Nested
    inner class FindByEmail {

        @Test
        fun `should find user by email`() {
            whenever(userRepository.findByEmail("test@test.com")).thenReturn(testUser)

            val result = userService.findByEmail("test@test.com")

            assertThat(result).isEqualTo(testUser)
        }

        @Test
        fun `should return null when user not found by email`() {
            whenever(userRepository.findByEmail("notfound@test.com")).thenReturn(null)

            val result = userService.findByEmail("notfound@test.com")

            assertThat(result).isNull()
        }
    }

    @Nested
    inner class Save {

        @Test
        fun `should save user`() {
            whenever(userRepository.save(testUser)).thenReturn(testUser)

            val result = userService.save(testUser)

            assertThat(result).isEqualTo(testUser)
            verify(userRepository).save(testUser)
        }
    }

    @Nested
    inner class SoftDelete {

        @Test
        fun `should soft delete user by uuid`() {
            userService.softDelete(testUuid)

            verify(userRepository).softDelete(testUuid)
        }
    }
}
