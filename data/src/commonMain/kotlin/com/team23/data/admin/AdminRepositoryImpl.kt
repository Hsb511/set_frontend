package com.team23.data.admin

import com.team23.domain.admin.AdminClearMode
import com.team23.domain.admin.AdminRepository
import com.team23.data.getBaseUrl as getBaseUrlFromModule

class AdminRepositoryImpl(
    private val adminApi: AdminApi,
) : AdminRepository {

    override suspend fun clear(mode: AdminClearMode): Result<String> {
        val action = when (mode) {
            AdminClearMode.GamesOnly -> AdminClearRequest.Action.Games
            AdminClearMode.AllMemory -> AdminClearRequest.Action.AllMemory
            AdminClearMode.Database -> AdminClearRequest.Action.Db
        }
        val request = AdminClearRequest(action)
        val response = adminApi.clear(request)
        return when (response) {
            is AdminClearResponse.Success -> Result.success(response.message)
            is AdminClearResponse.Failure -> Result.failure(Exception(response.error))
        }
    }

    override suspend fun getBaseUrl(): String? {
        return getBaseUrlFromModule()
    }

    override suspend fun getApiVersion(): String? {
        return runCatching { adminApi.version() }.onFailure { println("HUGO - failure: $it") }.getOrNull()
    }
}
