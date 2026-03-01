package com.team23.data.game

import co.touchlab.kermit.Logger
import com.team23.data.BuildKonfig
import com.team23.data.game.model.GameWsAction
import com.team23.data.game.model.GameWsEvent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.URLProtocol
import io.ktor.http.Url
import io.ktor.http.encodedPath
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface GameWs {
    val events: SharedFlow<GameWsEvent>

    suspend fun connect(sessionToken: Uuid)
    suspend fun disconnect()
}

@OptIn(ExperimentalUuidApi::class)
class GameWsImpl(
    private val client: HttpClient,
) : GameWs {


    private val wsEventJson = Json {
        classDiscriminator = "type"
        ignoreUnknownKeys = true
    }

    private val wsActionJson = Json {
        classDiscriminator = "action"
        ignoreUnknownKeys = true
    }

    private val _events = MutableSharedFlow<GameWsEvent>(extraBufferCapacity = 64)
    override val events: SharedFlow<GameWsEvent> = _events

    override suspend fun connect(sessionToken: Uuid) {
        val base = Url(BuildKonfig.BASE_URL)

        client.webSocket(
            request = {
                url {
                    protocol = if (base.protocol == URLProtocol.HTTPS) URLProtocol.WSS else URLProtocol.WS
                    host = base.host
                    port = base.port
                    encodedPath = "/session/$sessionToken/ws"
                }
            }
        ) {
            coroutineScope {
                launch { handleKeepalive() }

                launch { receiveEvents() }
            }
        }
    }

    private suspend fun DefaultClientWebSocketSession.handleKeepalive() {
        while (true) {
            delay(30.seconds)
            send(GameWsAction.Heartbeat)
        }
    }

    private suspend fun DefaultClientWebSocketSession.receiveEvents() {
        try {
            for (frame in incoming) {
                when (frame) {
                    is Frame.Text -> {
                        Logger.d("GameWs - frame text received: $frame")
                        val text = frame.readText()
                        Logger.d("GameWs - frame text received: $text")
                        runCatching {
                            wsEventJson.decodeFromString<GameWsEvent>(text).also {
                                Logger.d("GameWs - parsing frame text: $it")
                            }
                        }.onSuccess { event ->
                            _events.emit(event)
                        }.onFailure { throwable ->
                            Logger.d("GameWs - error while parsing frame text: $throwable")
                            // If you want, emit an Error event or just log
                            // _events.emit(GameWsEvent.Error("Failed to decode: ${err.message}", -1, Clock.System.now()))
                        }
                    }

                    is Frame.Close -> {
                        Logger.d("GameWs - frame close received: $frame")
                        break
                    }

                    else -> {
                        Logger.d("GameWs - something else received: $frame")
                    }
                }
            }
        } catch (throwable: Throwable) {
            Logger.d("GameWs - catching receiving events failure: $throwable")
        }
    }

    private suspend fun DefaultClientWebSocketSession.send(action: GameWsAction) {
        val payload = wsActionJson.encodeToString<GameWsAction>(action)
        send(Frame.Text(payload))
    }

    override suspend fun disconnect() {

    }
}
