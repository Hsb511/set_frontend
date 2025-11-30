package com.team23.ui.settings

import com.team23.domain.admin.AdminRepository
import com.team23.domain.settings.GetAllPreferencesUseCase
import com.team23.domain.settings.Preference
import com.team23.domain.settings.TogglePreferenceUseCase
import com.team23.domain.startup.repository.AuthRepository
import com.team23.domain.startup.repository.UserRepository
import com.team23.ui.debug.isDebug
import com.team23.ui.navigation.NavigationManager
import com.team23.ui.navigation.NavigationScreen
import com.team23.ui.snackbar.SetSnackbarVisuals
import com.team23.ui.snackbar.SnackbarManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class SettingsViewModel(
    private val getAllPreferencesUseCase: GetAllPreferencesUseCase,
    private val togglePreferenceUseCase: TogglePreferenceUseCase,
    private val adminRepository: AdminRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    dispatcher: CoroutineDispatcher,
    coroutineName: CoroutineName,
) {
    private val viewModelScope = CoroutineScope(SupervisorJob() + dispatcher + coroutineName)

    private val _settingsStateFlow: MutableStateFlow<SettingsUiModel> = MutableStateFlow(SettingsUiModel())
    val settingsFlow: StateFlow<SettingsUiModel> = _settingsStateFlow

    init {
        viewModelScope.launch {
            userRepository.getUserInfo().onSuccess { (userId, username) ->
                _settingsStateFlow.update { settingsState ->
                    settingsState.copy(account = settingsState.account.copy(userId = userId.toString(), username = username))
                }
            }
            if (isDebug()) {
                _settingsStateFlow.update { settingsState ->
                    settingsState.copy(backend = settingsState.backend.copy(baseUrl = adminRepository.getBaseUrl()))
                }
                _settingsStateFlow.update { settingsState ->
                    settingsState.copy(backend = settingsState.backend.copy(apiVersion = adminRepository.getApiVersion()))
                }
            }
            updateSettingsWithPreferences()
        }
    }

    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.Logout -> handleLogOut()
            is SettingsAction.NavigateBack -> handleNavigateBack()
            is SettingsAction.ToggleCardOrientation -> handleTogglePreference(Preference.CardPortrait, action.currentValue)
            is SettingsAction.ToggleForceDarkMode -> handleTogglePreference(Preference.ForceDarkMode, action.currentValue)
            is SettingsAction.ToggleForceLightMode -> handleTogglePreference(Preference.ForceLightMode, action.currentValue)
        }
    }

    private fun handleLogOut() {
        viewModelScope.launch {
            authRepository.logout()
                .onSuccess {
                    NavigationManager.clearBackStackOrNavigate(NavigationScreen.AuthType)
                }
                .onFailure { failure ->
                    SnackbarManager.showMessage(SetSnackbarVisuals.LogOutError(failure.message))
                }
        }
    }

    private fun handleNavigateBack() {
        viewModelScope.launch {
            NavigationManager.popBackStack()
        }
    }

    private fun handleTogglePreference(preference: Preference, currentValue: Boolean) {
        viewModelScope.launch {
            togglePreferenceUseCase.invoke(preference, currentValue)
            updateSettingsWithPreferences()
        }
    }

    private suspend fun updateSettingsWithPreferences() {
        val preferences = getAllPreferencesUseCase.invoke()
        _settingsStateFlow.update { settingsState ->
            settingsState.copy(
                preferences = settingsState.preferences.copy(
                    cardPortrait = preferences[Preference.CardPortrait] ?: false,
                    forceDarkMode = preferences[Preference.ForceDarkMode] ?: false,
                    forceLightMode = preferences[Preference.ForceLightMode] ?: false,
                )
            )
        }
    }
}
