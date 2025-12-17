package com.team23.domain.startup.statemachine

import com.team23.domain.startup.model.GameType
import com.team23.domain.startup.repository.AuthRepository
import com.team23.domain.startup.statemachine.StartupTestUtils.createUserInfo
import com.team23.domain.user.UserRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class StartupStateMachineTest {

    private lateinit var machine: StartupStateMachine
    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository

    @BeforeTest
    fun setup() {
        userRepository = mock()
        authRepository = mock()
        machine = StartupStateMachine(authRepository, userRepository)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `Init - Splash to UserSignInUp - Given Splash, Init and user not signed, When reducing, Then returns UserSignInUp`() = runTest {
        // Given
        val state = StartupState.Splash
        val event = StartupEvent.Init
        everySuspend { userRepository.getUserInfo() } returns Result.failure(NoSuchElementException())

        // When
        val newState = machine.reduce(state, event)

        // Then
        assertEquals(StartupState.UserSignInUp, newState)
    }


    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `Init - Splash to GameTypeChoice - Given Splash, Init and user signed in, When reducing, Then returns GameTypeChoice`() = runTest {
        // Given
        val state = StartupState.Splash
        val event = StartupEvent.Init
        everySuspend { userRepository.getUserInfo() } returns Result.success(createUserInfo())

        // When
        val newState = machine.reduce(state, event)

        // Then
        assertEquals(StartupState.Lobby, newState)
    }

    @Test
    fun `SignIn failure - UserSignInUp to UserSignInUp - Given UserSignInUp, SignIn and signin fails, When reducing, Then returns UserSignInUp`() = runTest {
        // Given
        val state = StartupState.UserSignInUp
        val username = "username"
        val password = "password"
        val event = StartupEvent.SignIn(username, password)
        everySuspend { authRepository.loginAndStoreUserInfo(username, password) } returns Result.failure(Exception())

        // When
        val newState = machine.reduce(state, event)

        // Then
        assertEquals(StartupState.UserSignInUp, newState)
    }

    @Test
    fun `SignUp success - UserSignInUp to GameTypeChoice - Given UserSignInUp, SignUp and signup success, When reducing, Then returns GameTypeChoice`() = runTest {
        // Given
        val state = StartupState.UserSignInUp
        val username = "username"
        val password = "password"
        val firstname = "firstname"
        val lastname = "lastname"
        val event = StartupEvent.SignUp(username, password, firstname, lastname)
        everySuspend {  authRepository.registerAndStoreUserInfo(username, password, firstname, lastname) } returns Result.success(Unit)

        // When
        val newState = machine.reduce(state, event)

        // Then
        assertEquals(StartupState.Lobby, newState)
    }

    @Test
    fun `SignUp failure - UserSignInUp to UserSignInUp - Given UserSignInUp, SignUp and signup fails, When reducing, Then returns UserSignInUp`() = runTest {
        // Given
        val state = StartupState.UserSignInUp
        val username = "username"
        val password = "password"
        val firstname = "firstname"
        val lastname = "lastname"
        val event = StartupEvent.SignUp(username, password, firstname, lastname)
        everySuspend { authRepository.registerAndStoreUserInfo(username, password, firstname, lastname) } returns Result.failure(Exception())

        // When
        val newState = machine.reduce(state, event)

        // Then
        assertEquals(StartupState.UserSignInUp, newState)
    }

    @Test
    fun `StartGameType solo - GameTypeChoice to StartGame - Given GameTypeChoice and StartGameType solo, When reducing, Then returns StartGame`() = runTest {
        // Given
        val state = StartupState.Lobby
        val event = StartupEvent.StartGameType(GameType.Solo)

        // When
        val newState = machine.reduce(state, event)

        // Then
        assertEquals(StartupState.StartGame(GameType.Solo), newState)
    }

    @Test
    fun `StartGameType multi - GameTypeChoice to StartGame - Given GameTypeChoice and StartGameType multi, When reducing, Then returns StartGame`() = runTest {
        // Given
        val state = StartupState.Lobby
        val event = StartupEvent.StartGameType(GameType.Multi)

        // When
        val newState = machine.reduce(state, event)

        // Then
        assertEquals(StartupState.StartGame(GameType.Multi), newState)
    }
}
