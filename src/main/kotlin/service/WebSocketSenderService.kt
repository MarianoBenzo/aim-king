package service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import model.*
import org.eclipse.jetty.websocket.api.Session

class WebSocketSenderService {

    fun emit(session: Session, type: ServerMessageWSType, data: Any? = null) {
        val dataString = jacksonObjectMapper().writeValueAsString(data)
        val messageWS = MessageWS(type, dataString)
        synchronized(session) {
            session.remote.sendString(jacksonObjectMapper().writeValueAsString(messageWS))
        }
    }

    fun broadcast(sessions: Collection<Session>, type: ServerMessageWSType, data: Any?) =
        sessions.forEach { emit(it, type, data) }

    fun broadcastToOthers(
        session: Session,
        sessions: Collection<Session>,
        type: ServerMessageWSType,
        data: Any?
    ) = sessions.filter { it != session }.forEach { emit(it, type, data) }
}
