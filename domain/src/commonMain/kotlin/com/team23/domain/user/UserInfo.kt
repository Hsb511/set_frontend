package com.team23.domain.user

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class UserInfo(
    val userId: Uuid,
    val username: String,
    val isGuest: Boolean,
)
