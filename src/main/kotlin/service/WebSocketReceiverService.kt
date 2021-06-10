package service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import model.*
import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage
import org.eclipse.jetty.websocket.api.annotations.WebSocket

@WebSocket
class WebSocketReceiverService(
    private val webSocketSenderService: WebSocketSenderService,
    private val aimKingService: AimKingService
) {

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
                webSocketSenderService.emit(session, ServerMessageWSType.PONG)
            }
            ClientMessageWSType.NEW_PLAYER.name -> {
                aimKingService.connectPlayer(session, messageWS.data ?: "Player")
            }
            ClientMessageWSType.NEW_GAME1.name -> {
                aimKingService.newGame1(session)
            }
            ClientMessageWSType.NEW_GAME2.name -> {
                aimKingService.newGame2(session)
            }
            ClientMessageWSType.CLICK.name -> {
                val position = messageWS.data?.let { jacksonObjectMapper().readValue<Position>(it) }
                position?.let { aimKingService.games[session]?.click(it) }
            }
        }
        println("Message Received - Type: ${messageWS.type} ${messageWS.data?.let { "Data: $it" } ?: ""}")
    }

    @OnWebSocketClose
    fun onDisconnect(session: Session, code: Int, reason: String?) {
        aimKingService.disconnectPlayer(session)
        println("Session Disconnected - Code: $code Reason: $reason")
    }
}
