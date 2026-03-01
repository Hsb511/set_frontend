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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
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
    suspend fun send(action: GameWsAction)
}

@OptIn(ExperimentalUuidApi::class)
class GameWsImpl(
    private val client: HttpClient,
) : GameWs {

    private val _events = MutableSharedFlow<GameWsEvent>(extraBufferCapacity = 64)
    override val events: SharedFlow<GameWsEvent> = _events
    private val _outgoing = MutableSharedFlow<GameWsAction>(extraBufferCapacity = 64)

    private val wsEventJson = Json {
        classDiscriminator = "type"
        ignoreUnknownKeys = true
    }

    private val wsActionJson = Json {
        classDiscriminator = "action"
        ignoreUnknownKeys = true
    }
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var wsJob: Job? = null

    override suspend fun connect(sessionToken: Uuid) {
        // prevent multiple concurrent connections
        wsJob?.cancel()
        wsJob = scope.launch {
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
                // inside session
                coroutineScope {
                    launch { handleKeepalive() }
                    launch { sendLoop() }
                    launch { receiveEvents() }
                }
            }
        }
    }

    override suspend fun disconnect() {

    }

    override suspend fun send(action: GameWsAction) {
        _outgoing.emit(action)
    }

    private suspend fun handleKeepalive() {
        while (true) {
            delay(30.seconds)
            _outgoing.emit(GameWsAction.Heartbeat)
        }
    }

    private suspend fun DefaultClientWebSocketSession.sendLoop() {
        _outgoing.collect { action ->
            val payload = wsActionJson.encodeToString(action)
            Logger.d("GameWs - sending: $payload")
            send(Frame.Text(payload))
        }
    }

    private suspend fun DefaultClientWebSocketSession.receiveEvents() {
        try {
            for (frame in incoming) {
                when (frame) {
                    is Frame.Text -> {
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
                        }
                    }

                    is Frame.Close -> {
                        Logger.d("GameWs - frame close received: $frame")
                        break
                    }

                    else -> Logger.d("GameWs - something else received: $frame")
                }
            }
        } catch (throwable: Throwable) {
            Logger.d("GameWs - catching receiving events failure: $throwable")
        }
    }
}
