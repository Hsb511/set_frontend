package com.team23.ui.navigation

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

@OptIn(ExperimentalEncodingApi::class)
internal class SerializableNavType<T : Any>(
    private val serializer: KSerializer<T>,
    private val json: Json = Json,
) : NavType<T>(isNullableAllowed = false) {

    override fun put(bundle: SavedState, key: String, value: T) {
        bundle.write { putString(key, serializeAsValue(value)) }
    }

    override fun get(bundle: SavedState, key: String): T {
        val raw = bundle.read { getString(key) }
        return parseValue(raw)
    }

    override fun parseValue(value: String): T {
        val jsonPayload = Base64.UrlSafe.decode(value).decodeToString()
        return json.decodeFromString(serializer, jsonPayload)
    }

    override fun serializeAsValue(value: T): String {
        val jsonPayload = json.encodeToString(serializer, value)
        return Base64.UrlSafe.encode(jsonPayload.encodeToByteArray())
    }
}

