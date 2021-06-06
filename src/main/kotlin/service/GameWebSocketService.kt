package service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import model.*
import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage
import org.eclipse.jetty.websocket.api.annotations.WebSocket
import java.util.concurrent.atomic.AtomicLong

@WebSocket
class GameWebSocketService {

    private var game = Game(this)
    val players: HashMap<Session, Player> = HashMap()
    private var ids = AtomicLong(0)

    @OnWebSocketConnect
    fun onConnected(session: Session) {
        session.idleTimeout = 0
        println("Session Connected")
    }

    @OnWebSocketMessage
    fun onMessage(session: Session, message: String) {
        val messageWS = jacksonObjectMapper().readValue<MessageWS>(message)

        when (messageWS.type) {
            ClientMessageWSType.PING.name -> {
                emit(session, ServerMessageWSType.PONG)
            }
            ClientMessageWSType.NEW_PLAYER.name -> {
                val id = ids.getAndIncrement()
                val name = messageWS.data ?: "Player"

                var nameUnique = name
                var i = 2
                while (players.values.any { player -> player.name == nameUnique }) {
                    nameUnique = "$name $i"
                    ++i
                }

                players[session] = Player(id, nameUnique)
                emit(session, ServerMessageWSType.GAME, game)
            }
            ClientMessageWSType.CLICK.name -> {
                messageWS.data?.let {
                    val position = jacksonObjectMapper().readValue<Position>(it)
                    game.click(position)
                }
            }
        }
        println("Message Received - Type: ${messageWS.type} ${messageWS.data?.let { "Data: $it" } ?: ""}")
    }

    @OnWebSocketClose
    fun onDisconnect(session: Session, code: Int, reason: String?) {
        println("Session Disconnected - Code: $code Reason: $reason")
    }

    fun broadcastGame() {
        broadcast(ServerMessageWSType.GAME, game)
    }

    private fun emit(session: Session, type: ServerMessageWSType, data: Any? = null) {
        val dataString = jacksonObjectMapper().writeValueAsString(data)
        val messageWS = MessageWS(type, dataString)
        synchronized(session) {
            session.remote.sendString(jacksonObjectMapper().writeValueAsString(messageWS))
        }
    }

    private fun broadcast(type: ServerMessageWSType, data: Any?) = players.forEach { emit(it.key, type, data) }

    private fun broadcastToOthers(session: Session, type: ServerMessageWSType, data: Any?) =
        players.filter { it.key != session }.forEach { emit(it.key, type, data) }
}
