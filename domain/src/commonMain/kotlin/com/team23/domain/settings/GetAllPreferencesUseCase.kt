package com.team23.domain.settings

import com.team23.domain.startup.repository.UserRepository

class GetAllPreferencesUseCase(
    private val userRepository: UserRepository,
) {

    suspend fun invoke(): Map<Preference, Boolean?> = Preference.entries.associateWith { preference ->
        runCatching { userRepository.getUserPreference(preference) }.getOrNull()
    }
}
