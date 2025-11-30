package com.team23.ui.settings

import androidx.navigation.NavController
import com.team23.domain.admin.AdminRepository
import com.team23.domain.startup.repository.AuthRepository
import com.team23.domain.startup.repository.UserRepository
import com.team23.domain.startup.statemachine.StartupStateMachine
import com.team23.ui.debug.isDebug
import com.team23.ui.navigation.NavigationScreen
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class SettingsViewModel(
    private val adminRepository: AdminRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    dispatcher: CoroutineDispatcher,
    coroutineName: CoroutineName,
) {
    private val viewModelScope = CoroutineScope(SupervisorJob() + dispatcher + coroutineName)
    private lateinit var navController: NavController

    private val _settingsStateFlow: MutableStateFlow<SettingsUiModel> = MutableStateFlow(SettingsUiModel("", ""))
    val settingsFlow: StateFlow<SettingsUiModel> = _settingsStateFlow

    init {
        viewModelScope.launch {
            userRepository.getUserInfo().onSuccess { (userId, username) ->
                _settingsStateFlow.update { settingsState ->
                    settingsState.copy(userId = userId.toString(), username = username)
                }
            }
            if (isDebug()) {
                _settingsStateFlow.update { settingsState ->
                    settingsState.copy(baseUrl = adminRepository.getBaseUrl())
                }
                _settingsStateFlow.update { settingsState ->
                    settingsState.copy(apiVersion = adminRepository.getApiVersion())
                }
            }
        }
    }

    fun setNavController(navController: NavController) {
        this.navController = navController
    }

    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.Logout -> viewModelScope.launch {
                authRepository.logout()
                    .onSuccess {
                        withContext(Dispatchers.Main) {
                            if (!navController.clearBackStack(NavigationScreen.AuthType.name)) {
                                navController.navigate(NavigationScreen.AuthType.name)
                            }
                        }
                    }
                    .onFailure {
                        // TODO SHOW SNACKBAR
                    }
            }
        }
    }
}
