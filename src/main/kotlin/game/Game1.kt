package game

import model.*
import model.Target
import org.eclipse.jetty.websocket.api.Session
import service.AimKingService
import service.WebSocketSenderService
import java.util.*

class Game1 (
    aimKingService: AimKingService,
    webSocketSenderService: WebSocketSenderService,
    player: Pair<Session, Player>
) : GameBase(
    aimKingService, webSocketSenderService, hashMapOf(player)
) {
    private var score: Int = 0

    override fun start() {
        val startIn: Long = 3000
        broadcastStartGame(startIn)
        Timer().schedule(object : TimerTask() {
            override fun run() {
                startTime = System.currentTimeMillis()
                addRandomTarget()
                webSocketSenderService.broadcast(players.keys, ServerMessageWSType.GAME, Game(targets, cursors))
            }
        }, startIn)
    }

    override fun disconnectPlayer(session: Session) {}

    override fun mouseClick(session: Session, position: Position) {
        val target: Target? = targets.findLast { target -> onTarget(position, target) }
        target?.let {
            targets.remove(it)
            score += 1
            if ( score < MAX_SCORE) {
                addRandomTarget()
                broadcastGame(session, ClientMessageWSType.MOUSE_CLICK)
            } else {
                broadcastGame(session, ClientMessageWSType.MOUSE_CLICK)
                broadcastEndGame(players[session]!!)
            }
        }
    }

    override fun broadcastEndGame(winningPlayer: Player) {
        val time = System.currentTimeMillis() - startTime
        aimKingService.addNewTime(winningPlayer, time)
        webSocketSenderService.broadcast(players.keys, ServerMessageWSType.GAME_END, "Time: ${time / 1000.0} seconds")
    }

    companion object {
        private const val MAX_SCORE = 25
    }
}
