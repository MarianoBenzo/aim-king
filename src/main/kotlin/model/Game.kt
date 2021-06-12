package model

import org.eclipse.jetty.websocket.api.Session

interface Game {
    fun disconnectPlayer(session: Session)

    fun click(session: Session, position: Position)
}
