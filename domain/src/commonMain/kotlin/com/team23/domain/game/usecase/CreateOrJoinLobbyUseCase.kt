package com.team23.domain.game.usecase

import com.team23.domain.game.model.GameLobby
import com.team23.domain.game.model.GameMode
import com.team23.domain.game.repository.GameRepository
import com.team23.domain.user.UserRepository
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class CreateOrJoinLobbyUseCase(
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository,
) {

    suspend fun invoke(publicName: String?, gameMode: GameMode): Result<GameLobby> {
        return if (publicName == null) {
            createGame(gameMode)
        } else {
            joinGame(publicName, gameMode)
        }
    }

    private suspend fun createGame(gameMode: GameMode): Result<GameLobby> {
        val currentUsername = userRepository.getUserInfo().getOrNull()?.username
        return  gameRepository.createMultiGame(gameMode).map { (game, publicName) ->
            GameLobby(
                game = game,
                publicName = publicName,
                gameMode = gameMode,
                players = currentUsername?.let { listOf(
                    GameLobby.Player(name = currentUsername, isMe = true, isHost = true)
                ) } ?: emptyList()
            )
        }
    }

    private suspend fun joinGame(publicName: String, gameMode: GameMode): Result<GameLobby> {
        val currentUsername = userRepository.getUserInfo().getOrNull()?.username
        return gameRepository.joinMultiGame(publicName).map { (game, playerNames) ->
            GameLobby(
                game = game,
                publicName = publicName,
                gameMode = gameMode,
                players = playerNames.mapIndexed { index, playerName ->
                    GameLobby.Player(name = playerName, isMe = playerName == currentUsername, isHost = index == 0)
                }
            )
        }
    }
}
