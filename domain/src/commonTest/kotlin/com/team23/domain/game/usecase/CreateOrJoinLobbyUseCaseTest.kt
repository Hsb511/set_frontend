package com.team23.domain.game.usecase

import com.team23.domain.game.GameTestUtils.createPlayingGame
import com.team23.domain.game.model.Card
import com.team23.domain.game.model.GameLobby
import com.team23.domain.game.model.GameMode
import com.team23.domain.game.repository.GameRepository
import com.team23.domain.user.UserInfo
import com.team23.domain.user.UserRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class CreateOrJoinLobbyUseCaseTest {

    private lateinit var gameRepository: GameRepository
    private lateinit var userRepository: UserRepository
    private lateinit var useCase: CreateOrJoinLobbyUseCase

    @BeforeTest
    fun setup() {
        gameRepository = mock()
        userRepository = mock()
        useCase = CreateOrJoinLobbyUseCase(gameRepository, userRepository)
    }

    @Test
    fun `Given publicName is null and create succeeds, When invoking use case, Then returns GameLobby with host player`() = runTest {
        // Given
        val gameMode = GameMode.Versus
        val playingGame = createPlayingGame(deck = emptyList(), table = List(12) { Card.Empty })
        val publicName = "lobby-123"
        val username = "alice"
        everySuspend { userRepository.getUserInfo() } returns Result.success(createUserInfo(username = username))
        everySuspend { gameRepository.createMultiGame(gameMode) } returns Result.success(playingGame to publicName)

        // When
        val result = useCase.invoke(publicName = null, gameMode = gameMode)

        // Then
        assertTrue(result.isSuccess)
        val lobby = result.getOrThrow()
        assertEquals(publicName, lobby.publicName)
        assertEquals(playingGame, lobby.game)
        assertEquals(gameMode, lobby.gameMode)
        assertEquals(1, lobby.players.size)
        assertEquals(GameLobby.Player(name = username, isMe = true, isHost = true), lobby.players.first())
    }

    @Test
    fun `Given publicName is null and getUserInfo fails, When invoking use case, Then returns GameLobby with empty players`() = runTest {
        // Given
        val gameMode = GameMode.TimeTrial
        val playingGame = createPlayingGame(deck = emptyList(), table = List(12) { Card.Empty })
        val publicName = "lobby-456"
        everySuspend { userRepository.getUserInfo() } returns Result.failure(NoSuchElementException())
        everySuspend { gameRepository.createMultiGame(gameMode) } returns Result.success(playingGame to publicName)

        // When
        val result = useCase.invoke(publicName = null, gameMode = gameMode)

        // Then
        assertTrue(result.isSuccess)
        val lobby = result.getOrThrow()
        assertEquals(publicName, lobby.publicName)
        assertEquals(playingGame, lobby.game)
        assertEquals(gameMode, lobby.gameMode)
        assertTrue(lobby.players.isEmpty())
    }

    @Test
    fun `Given publicName is null and createMultiGame fails, When invoking use case, Then returns failure`() = runTest {
        // Given
        val gameMode = GameMode.Versus
        val exception = RuntimeException("network error")
        everySuspend { userRepository.getUserInfo() } returns Result.success(createUserInfo(username = "alice"))
        everySuspend { gameRepository.createMultiGame(gameMode) } returns Result.failure(exception)

        // When
        val result = useCase.invoke(publicName = null, gameMode = gameMode)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `Given publicName is not null and join succeeds, When invoking use case, Then returns GameLobby with correct players`() = runTest {
        // Given
        val gameMode = GameMode.Versus
        val publicName = "lobby-789"
        val playingGame = createPlayingGame(deck = emptyList(), table = List(12) { Card.Empty })
        val username = "bob"
        val playerNames = listOf("alice", "bob")
        everySuspend { userRepository.getUserInfo() } returns Result.success(createUserInfo(username = username))
        everySuspend { gameRepository.joinMultiGame(publicName) } returns Result.success(playingGame to playerNames)

        // When
        val result = useCase.invoke(publicName = publicName, gameMode = gameMode)

        // Then
        assertTrue(result.isSuccess)
        val lobby = result.getOrThrow()
        assertEquals(publicName, lobby.publicName)
        assertEquals(playingGame, lobby.game)
        assertEquals(gameMode, lobby.gameMode)
        assertEquals(2, lobby.players.size)
        // First player is host
        assertEquals(GameLobby.Player(name = "alice", isMe = false, isHost = true), lobby.players[0])
        // Second player is me
        assertEquals(GameLobby.Player(name = "bob", isMe = true, isHost = false), lobby.players[1])
    }

    @Test
    fun `Given publicName is not null and join succeeds with me as host, When invoking use case, Then first player isMe and isHost`() = runTest {
        // Given
        val gameMode = GameMode.TimeTrial
        val publicName = "lobby-host"
        val playingGame = createPlayingGame(deck = emptyList(), table = List(12) { Card.Empty })
        val username = "alice"
        val playerNames = listOf("alice", "charlie")
        everySuspend { userRepository.getUserInfo() } returns Result.success(createUserInfo(username = username))
        everySuspend { gameRepository.joinMultiGame(publicName) } returns Result.success(playingGame to playerNames)

        // When
        val result = useCase.invoke(publicName = publicName, gameMode = gameMode)

        // Then
        assertTrue(result.isSuccess)
        val lobby = result.getOrThrow()
        assertEquals(GameLobby.Player(name = "alice", isMe = true, isHost = true), lobby.players[0])
        assertEquals(GameLobby.Player(name = "charlie", isMe = false, isHost = false), lobby.players[1])
    }

    @Test
    fun `Given publicName is not null and join fails then leave succeeds and retry succeeds, When invoking use case, Then returns GameLobby from retry`() = runTest {
        // Given
        val gameMode = GameMode.Versus
        val publicName = "lobby-retry"
        val playingGame = createPlayingGame(deck = emptyList(), table = List(12) { Card.Empty })
        val username = "bob"
        val playerNames = listOf("alice", "bob")
        everySuspend { userRepository.getUserInfo() } returns Result.success(createUserInfo(username = username))
        everySuspend { gameRepository.joinMultiGame(publicName) } returns Result.failure(RuntimeException("already in game"))
        everySuspend { gameRepository.leaveGame() } returns Result.success(Unit)
        // After leave, the second joinMultiGame call should succeed — override the stub
        everySuspend { gameRepository.joinMultiGame(publicName) } returns Result.success(playingGame to playerNames)

        // When
        val result = useCase.invoke(publicName = publicName, gameMode = gameMode)

        // Then
        assertTrue(result.isSuccess)
        val lobby = result.getOrThrow()
        assertEquals(publicName, lobby.publicName)
        assertEquals(2, lobby.players.size)
    }

    @Test
    fun `Given publicName is not null and join fails then leave fails, When invoking use case, Then returns original join failure`() = runTest {
        // Given
        val gameMode = GameMode.Versus
        val publicName = "lobby-fail"
        val joinException = RuntimeException("join failed")
        everySuspend { userRepository.getUserInfo() } returns Result.success(createUserInfo(username = "bob"))
        everySuspend { gameRepository.joinMultiGame(publicName) } returns Result.failure(joinException)
        everySuspend { gameRepository.leaveGame() } returns Result.failure(RuntimeException("leave failed"))

        // When
        val result = useCase.invoke(publicName = publicName, gameMode = gameMode)

        // Then
        assertTrue(result.isFailure)
        assertEquals(joinException, result.exceptionOrNull())
    }

    @Test
    fun `Given publicName is not null and getUserInfo fails, When invoking use case, Then players have isMe false`() = runTest {
        // Given
        val gameMode = GameMode.Versus
        val publicName = "lobby-no-user"
        val playingGame = createPlayingGame(deck = emptyList(), table = List(12) { Card.Empty })
        val playerNames = listOf("alice", "bob")
        everySuspend { userRepository.getUserInfo() } returns Result.failure(NoSuchElementException())
        everySuspend { gameRepository.joinMultiGame(publicName) } returns Result.success(playingGame to playerNames)

        // When
        val result = useCase.invoke(publicName = publicName, gameMode = gameMode)

        // Then
        assertTrue(result.isSuccess)
        val lobby = result.getOrThrow()
        assertEquals(2, lobby.players.size)
        // No player should be marked as "me" since user info failed
        assertTrue(lobby.players.none { it.isMe })
        // First player is still host
        assertTrue(lobby.players[0].isHost)
    }

    private fun createUserInfo(
        userId: Uuid = Uuid.parse("00000000-0000-0000-0000-000000000000"),
        username: String = "",
        isGuest: Boolean = false,
    ): UserInfo = UserInfo(
        userId = userId,
        username = username,
        isGuest = isGuest,
    )
}

