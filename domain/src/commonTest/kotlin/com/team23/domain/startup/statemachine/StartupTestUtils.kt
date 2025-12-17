package com.team23.domain.startup.statemachine

import com.team23.domain.user.UserInfo
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object StartupTestUtils {

    @OptIn(ExperimentalUuidApi::class)
    fun createUserInfo(
        userId: Uuid = Uuid.parse("00000000-0000-0000-0000-000000000000"),
        username: String = "",
        isGuest: Boolean = false,
    ): UserInfo = UserInfo(
        userId = userId,
        username = username,
        isGuest = isGuest,
    )
}