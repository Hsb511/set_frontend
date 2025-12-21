package com.team23.ui.lobby

import com.team23.domain.game.repository.GameRepository
import com.team23.ui.navigation.NavigationManager
import com.team23.ui.navigation.NavigationScreen
import com.team23.ui.navigation.NavigationScreen.Game.StartType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class LobbyViewModel(
    private val gameRepository: GameRepository,
    dispatcher: CoroutineDispatcher,
    coroutineName: CoroutineName,
) {
    private val job = SupervisorJob()
    private val viewModelScope = CoroutineScope(job + dispatcher + coroutineName)

    private val _lobbyUiModel: MutableStateFlow<LobbyUiModel> = MutableStateFlow(LobbyUiModel.Loading)
    val lobbyUiModel: StateFlow<LobbyUiModel> = _lobbyUiModel

    fun onStart() {
        viewModelScope.launch {
            val hasActiveSoloGame = gameRepository.hasActiveSoloGame().isSuccess
            _lobbyUiModel.value = when (val currentUiModel = _lobbyUiModel.value) {
                is LobbyUiModel.Data ->  currentUiModel.copy(hasAnOngoingSoloGame = hasActiveSoloGame)
                is LobbyUiModel.Loading -> LobbyUiModel.Data(
                    hasAnOngoingSoloGame = hasActiveSoloGame,
                    multiGames = fixedRandomUuids.map { gameId ->
                        LobbyUiModel.Data.MultiGame(
                            gameId = Uuid.parse(gameId),
                            hostName = "Guest#${gameId.take(8)}",
                            playersCount = gameId.last().digitToInt(),
                        )
                    }
                )
            }
        }
    }

    fun onAction(action: LobbyAction) {
        when (action) {
            is LobbyAction.CreateSolo -> {
                val startType = if (action.hasAnOngoingSoloGame) {
                    StartType.CreateWithActive
                } else {
                    StartType.CreateWithoutActive
                }
                startSoloGame(startType)
            }
            is LobbyAction.ContinueSolo -> startSoloGame(StartType.Continue)
            is LobbyAction.StartMulti -> TODO()
        }
    }


    private fun startSoloGame(startType: StartType) {
        viewModelScope.launch {
            NavigationManager.handle(NavigationScreen.Game(startType))
        }
    }

    /* TODO JUST FOR MOCKING */
    private val fixedRandomUuids = listOf(
        "3f2c1c8a-9a6e-4a4a-b7b7-4c1e1b7e8f01",
        "a1d4e6c9-7b9c-4b2f-9c9f-1e8c2b4a6d32",
        "6e2f8a44-2b5c-4c8d-8b41-0f7d6a9c2e11",
        "d9c7a2b4-1f8e-4f44-9a6c-3e1b2d8f0a55",
        "4b8f3d2e-6c4a-45c7-8f9b-1a2e3c4d5e66",
        "b5a7d8e2-9c3f-4b1a-a4c8-7e6d5f2a9b77",
        "8c2a9f6e-4d7b-46e3-9a5c-1b8d2f4a3c88",
        "f1e4d3c9-7a6b-4c2f-8b5e-9a2d1c6f4e99",
        "2a9c7f5e-8d4b-4c1e-9f6a-b3d2e1a4c011",
        "c6b8d1a4-9f2e-4a7c-8e3b-5d9c1f2a3112",
        "7f5a9d2c-1b4e-4f8c-9e6a-3d2b1c4a5e13",
        "9e4c8d7a-5b2f-4a1e-8c6d-f3b2a1e4c214",
        "1c6b4e9d-8a5f-4c7e-9b2d-a3f1e2c4d315",
        "e7d9a5b2-4c6f-4e8a-9d1c-3b2f1a4c5e16",
        "5a1e9c2d-7f8b-4e4a-9c6d-2b3f1a8e4172",
        "b9f4e3a2-6c1d-4a7e-8c5b-2d1f9a3e6181",
        "d3c8a9b4-2f6e-4d1a-9e5c-b7f1a2c4e719",
        "4e2f7d9c-1a8b-4c6e-9a5d-b3f2c1e4a820",
        "8d1c6a5f-4e9b-4b2c-9a7e-f3d2c1a5b921",
        "c2a4f9b7-5e1d-4e8a-9c6b-3f1a2d4c1022",
        "9b3e7a2c-4f6d-4c8a-9e1b-d5f2a1c4e122",
        "f5c2d9e4-8a6b-4b1c-9a7e-3f2d1c4a1424",
        "1a8c4f2d-6b5e-4e9a-9c7d-f3a2b1c4e155",
    )
}