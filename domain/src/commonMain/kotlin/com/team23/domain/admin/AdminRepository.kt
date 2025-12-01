package com.team23.domain.admin

interface AdminRepository {

    suspend fun clear(mode: AdminClearMode): Result<String>

    fun getAppVersion(): String

    suspend fun getBaseUrl(): String?

    suspend fun getApiVersion(): String?
}