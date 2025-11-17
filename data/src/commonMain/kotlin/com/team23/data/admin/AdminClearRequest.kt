package com.team23.data.admin

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdminClearRequest(
    val action: Action,
) {

    @Serializable
    enum class Action {
        @SerialName( "games")
        Games,
        @SerialName( "all-memory")
        AllMemory,
        @SerialName( "db")
        Db,
    }
}
