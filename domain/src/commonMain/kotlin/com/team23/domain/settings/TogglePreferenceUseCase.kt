package com.team23.domain.settings

import com.team23.domain.user.UserRepository

class TogglePreferenceUseCase(
    private val userRepository: UserRepository,
) {

    suspend fun invoke(preference: Preference, currentValue: Boolean) {
        userRepository.setUserPreference(preference, !currentValue)
        when {
            preference == Preference.ForceDarkMode && !currentValue ->
                userRepository.setUserPreference(Preference.ForceLightMode, false)
            preference == Preference.ForceLightMode && !currentValue ->
                userRepository.setUserPreference(Preference.ForceDarkMode, false)
        }
    }
}
