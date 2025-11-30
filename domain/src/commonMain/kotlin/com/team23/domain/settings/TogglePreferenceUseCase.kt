package com.team23.domain.settings

import com.team23.domain.startup.repository.UserRepository

class TogglePreferenceUseCase(
    private val userRepository: UserRepository,
) {

    suspend fun invoke(preference: Preference) {
        val currentValue = runCatching { userRepository.getUserPreference(preference) }.getOrNull() ?: return
        userRepository.setUserPreference(preference, !currentValue)
        when {
            preference == Preference.ForceDarkMode && !currentValue ->
                userRepository.setUserPreference(Preference.ForceLightMode, false)
            preference == Preference.ForceLightMode && !currentValue ->
                userRepository.setUserPreference(Preference.ForceDarkMode, false)
        }
    }
}
