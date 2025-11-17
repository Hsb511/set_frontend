package com.team23.domain.admin

interface AdminRepository {

    suspend fun clear(mode: AdminClearMode): Result<Unit>
}