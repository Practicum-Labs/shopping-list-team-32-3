package ru.practicum.shoppinglist.feature.auth.network

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import ru.practicum.shoppinglist.core.data.network.StatusCodeException
import ru.practicum.shoppinglist.feature.auth.data.dto.CheckDto
import ru.practicum.shoppinglist.feature.auth.data.dto.RefreshDto
import ru.practicum.shoppinglist.feature.auth.data.dto.UserDto
import ru.practicum.shoppinglist.feature.auth.data.network.AuthApi
import ru.practicum.shoppinglist.feature.auth.data.network.RetrofitAuthApiNetworkClient
import ru.practicum.shoppinglist.testUtils.successResponse

/**
 * Unit‑tests for RetrofitAuthApiNetworkClient.
 */
class RetrofitAuthApiNetworkClientTest {

    private lateinit var authApi: AuthApi
    private lateinit var client: RetrofitAuthApiNetworkClient
    private val testUserDto = UserDto(1, "accessToken", "refreshToken")

    @Before
    fun setUp() {
        authApi = mockk()
        client = RetrofitAuthApiNetworkClient(authApi)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun registerSuccess() = runTest {
        // Giwen
        coEvery {
            authApi.register(any())
        } returns successResponse(testUserDto)

        // When
        val result = client.register(TEST_EMAIL, TEST_PASSWORD)

        // Then
        assertEquals(testUserDto, result.data)
        assertEquals("200", result.code)
        assertNull(result.error)
    }

    @Test
    fun registerException() = runTest {
        val errorString = "User already exists"
        coEvery {
            authApi.register(any())
        } throws StatusCodeException(400, errorString)

        val result = client.register(TEST_EMAIL, TEST_PASSWORD)

        assertNull(result.data)
        assertEquals("400", result.code)
        assertEquals(errorString, result.error)
    }

    @Test
    fun registerSystemError() = runTest {
        coEvery {
            authApi.register(any())
        } throws RuntimeException("Network down")

        val result = client.register(TEST_EMAIL, TEST_PASSWORD)

        assertNull(result.data)
        assertEquals("-1", result.code)
        assertEquals("System Error", result.error)
    }

    @Test
    fun loginSuccess() = runTest {
        coEvery {
            authApi.login(any())
        } returns successResponse(testUserDto)

        val result = client.login("login@example.com", "pass")

        assertEquals(testUserDto, result.data)
        assertEquals("200", result.code)
        assertNull(result.error)
    }

    @Test
    fun refreshSuccess() = runTest {
        val refreshDto = RefreshDto("accessToken", "refreshToken")
        coEvery {
            authApi.refresh(any())
        } returns successResponse(refreshDto)

        val result = client.refresh("old-token")

        assertEquals(refreshDto, result.data)
        assertEquals("200", result.code)
        assertNull(result.error)
    }

    @Test
    fun refreshException() = runTest {
        coEvery {
            authApi.refresh(any())
        } throws StatusCodeException(401, "Unauthorized")

        val result = client.refresh("bad-token")

        assertNull(result.data)
        assertEquals("401", result.code)
        assertEquals("Unauthorized", result.error)
    }

    @Test
    fun checkSuccess() = runTest {
        val checkDto = CheckDto(isValid = true, success = true)
        coEvery {
            authApi.check(any())
        } returns successResponse(checkDto)

        val result = client.check("some-token")

        assertEquals(checkDto, result.data)
        assertEquals("200", result.code)
        assertNull(result.error)
    }

    @Test
    fun recoverySuccess() = runTest {
        coEvery {
            authApi.recovery(any())
        } returns successResponse<Unit>(Unit)

        val result = client.recovery("email@example.com")

        assertEquals(Unit, result.data)
        assertEquals("200", result.code)
        assertNull(result.error)
    }

    companion object {
        const val TEST_EMAIL = "email@example.com"
        const val TEST_PASSWORD = "password"
    }
}
