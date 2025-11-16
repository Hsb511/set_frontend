package com.team23.ui.settings

import com.team23.domain.startup.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class SettingsViewModel(
    private val userRepository: UserRepository,
    dispatcher: CoroutineDispatcher,
    coroutineName: CoroutineName,
) {
    private val viewModelScope = CoroutineScope(SupervisorJob() + dispatcher + coroutineName)

    private val _settingsStateFlow: MutableStateFlow<SettingsUiModel> = MutableStateFlow(SettingsUiModel("", ""))
    val settingsFlow: StateFlow<SettingsUiModel> = _settingsStateFlow

    init {
        viewModelScope.launch {
            userRepository.getUserId().onSuccess { userId ->
                _settingsStateFlow.value = _settingsStateFlow.value.copy(userId = userId.toString())
            }
        }
    }
}
