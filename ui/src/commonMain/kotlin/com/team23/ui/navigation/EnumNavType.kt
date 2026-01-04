package com.team23.ui.navigation

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write

internal class EnumNavType<T : Enum<T>>(
    private val values: Array<T>
) : NavType<T>(isNullableAllowed = false) {

    override fun put(bundle: SavedState, key: String, value: T) {
        bundle.write { putString(key, value.name) }
    }

    override fun get(bundle: SavedState, key: String): T? {
        val raw = bundle.read { getString(key) }
        return values.firstOrNull { it.name == raw }
    }

    override fun parseValue(value: String): T =
        values.first { it.name == value }

    override fun serializeAsValue(value: T): String =
        value.name
}
