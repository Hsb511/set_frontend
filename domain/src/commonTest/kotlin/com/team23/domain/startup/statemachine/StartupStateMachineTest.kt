package com.team23.domain.startup.statemachine

import com.team23.domain.startup.model.GameType
import com.team23.domain.startup.usecase.IsDeviceRegisteredUseCase
import com.team23.domain.startup.usecase.IsUserSignedInUseCase
import com.team23.domain.startup.usecase.RegisterDeviceUseCase
import com.team23.domain.startup.usecase.SignInUseCase
import com.team23.domain.startup.usecase.SignUpUseCase
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.spy
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class StartupStateMachineTest {

    private lateinit var machine: StartupStateMachine
    private lateinit var isUserSignedInUseCase: IsUserSignedInUseCase
    private lateinit var isDeviceRegisteredUseCase: IsDeviceRegisteredUseCase
    private lateinit var signInUseCase: SignInUseCase
    private lateinit var signUpUseCase: SignUpUseCase
    private lateinit var registerDeviceUseCase: RegisterDeviceUseCase

    @BeforeTest
    fun setup() {
        isUserSignedInUseCase = mock()
        isDeviceRegisteredUseCase = mock()
        signInUseCase = mock()
        signUpUseCase = mock()
        registerDeviceUseCase = mock()
        machine = StartupStateMachine(isUserSignedInUseCase, isDeviceRegisteredUseCase, signInUseCase, signUpUseCase, registerDeviceUseCase)
    }

    @Test
    fun `Init - Splash to UserSignInUp - Given Splash, Init, user not signed and device not registered, When reducing, Then returns UserSignInUp`() = runTest {
        // Given
        val state = StartupState.Splash
        val event = StartupEvent.Init
        everySuspend { isUserSignedInUseCase.invoke() } returns false
        everySuspend { isDeviceRegisteredUseCase.invoke() } returns false

        // When
        val newState = machine.reduce(state, event)

        // Then
        assertEquals(StartupState.UserSignInUp, newState)
    }

    @Test
    fun `Init - Splash to DeviceRegistration - Given Splash, Init, user signed and device not registered, When reducing, Then returns DeviceRegistration`() = runTest {
        // Given
        val state = StartupState.Splash
        val event = StartupEvent.Init
        everySuspend { isUserSignedInUseCase.invoke() } returns true
        everySuspend { isDeviceRegisteredUseCase.invoke() } returns false

        // When
        val newState = machine.reduce(state, event)

        // Then
        assertEquals(StartupState.DeviceRegistration, newState)
    }

    @Test
    fun `Init - Splash to GameTypeChoice - Given Splash, Init, user signed and device registered, When reducing, Then returns GameTypeChoice`() = runTest {
        // Given
        val state = StartupState.Splash
        val event = StartupEvent.Init
        everySuspend { isUserSignedInUseCase.invoke() } returns true
        everySuspend { isDeviceRegisteredUseCase.invoke() } returns true

        // When
        val newState = machine.reduce(state, event)

        // Then
        assertEquals(StartupState.GameTypeChoice, newState)
    }

    @Test
    fun `SignIn success - UserSignInUp to DeviceRegistration - Given UserSignInUp, SignIn and signin success, When reducing, Then returns DeviceRegistration`() = runTest {
        // Given
        val state = StartupState.UserSignInUp
        val event = StartupEvent.SignIn
        everySuspend { signInUseCase.invoke() } returns Result.success(Unit)

        // When
        val newState = machine.reduce(state, event)

        // Then
        assertEquals(StartupState.DeviceRegistration, newState)
    }

    @Test
    fun `SignIn failure - UserSignInUp to UserSignInUp - Given UserSignInUp, SignIn and signin fails, When reducing, Then returns UserSignInUp`() = runTest {
        // Given
        val state = StartupState.UserSignInUp
        val event = StartupEvent.SignIn
        everySuspend { signInUseCase.invoke() } returns Result.failure(Exception())

        // When
        val newState = machine.reduce(state, event)

        // Then
        assertEquals(StartupState.UserSignInUp, newState)
    }

    @Test
    fun `SignUp success - UserSignInUp to DeviceRegistration - Given UserSignInUp, SignUp and signup success, When reducing, Then returns DeviceRegistration`() = runTest {
        // Given
        val state = StartupState.UserSignInUp
        val event = StartupEvent.SignUp
        everySuspend { signUpUseCase.invoke() } returns Result.success(Unit)

        // When
        val newState = machine.reduce(state, event)

        // Then
        assertEquals(StartupState.DeviceRegistration, newState)
    }

    @Test
    fun `SignUp failure - UserSignInUp to UserSignInUp - Given UserSignInUp, SignUp and signup fails, When reducing, Then returns UserSignInUp`() = runTest {
        // Given
        val state = StartupState.UserSignInUp
        val event = StartupEvent.SignUp
        everySuspend { signUpUseCase.invoke() } returns Result.failure(Exception())

        // When
        val newState = machine.reduce(state, event)

        // Then
        assertEquals(StartupState.UserSignInUp, newState)
    }

    @Test
    fun `RegisterDevice success - DeviceRegistration to GameTypeChoice - Given DeviceRegistration, RegisterDevice and register success, When reducing, Then returns GameTypeChoice`() = runTest {
        // Given
        val state = StartupState.UserSignInUp
        val event = StartupEvent.SignUp
        everySuspend { signUpUseCase.invoke() } returns Result.success(Unit)

        // When
        val newState = machine.reduce(state, event)

        // Then
        assertEquals(StartupState.DeviceRegistration, newState)
    }

    @Test
    fun `RegisterDevice failure - DeviceRegistration to DeviceRegistration - Given DeviceRegistration, RegisterDevice and register fails, When reducing, Then returns DeviceRegistration`() = runTest {
        // Given
        val state = StartupState.UserSignInUp
        val event = StartupEvent.SignUp
        everySuspend { signUpUseCase.invoke() } returns Result.failure(Exception())

        // When
        val newState = machine.reduce(state, event)

        // Then
        assertEquals(StartupState.UserSignInUp, newState)
    }

    @Test
    fun `StartGameType solo - GameTypeChoice to StartGame - Given GameTypeChoice and StartGameType solo, When reducing, Then returns StartGame`() = runTest {
        // Given
        val state = StartupState.GameTypeChoice
        val event = StartupEvent.StartGameType(GameType.Solo)

        // When
        val newState = machine.reduce(state, event)

        // Then
        assertEquals(StartupState.StartGame(GameType.Solo), newState)
    }

    @Test
    fun `StartGameType multi - GameTypeChoice to StartGame - Given GameTypeChoice and StartGameType multi, When reducing, Then returns StartGame`() = runTest {
        // Given
        val state = StartupState.GameTypeChoice
        val event = StartupEvent.StartGameType(GameType.Multi)

        // When
        val newState = machine.reduce(state, event)

        // Then
        assertEquals(StartupState.StartGame(GameType.Multi), newState)
    }
}
