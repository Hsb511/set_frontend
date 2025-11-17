package com.team23.data.admin

import com.team23.domain.admin.AdminClearMode
import com.team23.domain.admin.AdminRepository

class AdminRepositoryImpl: AdminRepository {
    override suspend fun clear(mode: AdminClearMode): Result<Unit> {
        println("HUGO - mode: $mode")
        return Result.success(Unit)
    }
}
