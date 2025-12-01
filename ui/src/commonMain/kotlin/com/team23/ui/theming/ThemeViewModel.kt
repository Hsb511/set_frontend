package com.team23.ui.theming

import com.team23.domain.settings.Preference
import com.team23.domain.startup.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ThemeViewModel(
    userRepository: UserRepository,
    dispatcher: CoroutineDispatcher,
    coroutineName: CoroutineName,
) {

    private val viewModelScope = CoroutineScope(dispatcher + coroutineName)

    val isDarkTheme: StateFlow<Boolean?> = combine(
        userRepository.getUserPreferenceAsFlow(Preference.ForceDarkMode),
        userRepository.getUserPreferenceAsFlow(Preference.ForceLightMode)
    ) { (forceDarkMode, forceLightMode) ->
        when {
            forceDarkMode == true -> true
            forceLightMode == true -> false
            else -> null
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
}
