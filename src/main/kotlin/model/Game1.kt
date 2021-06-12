package model

import org.eclipse.jetty.websocket.api.Session
import service.AimKingService
import service.WebSocketSenderService
import java.util.*

class Game1 (
    aimKingService: AimKingService,
    webSocketSenderService: WebSocketSenderService,
    player: Pair<Session, Player>
) : Game(
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
                broadcastGame()
            }
        }, startIn)
    }

    override fun disconnectPlayer(session: Session) {}

    override fun click(session: Session, position: Position) {
        val target: Target? = targets.findLast { target -> onTarget(position, target) }
        target?.let {
            targets.remove(it)
            score += 1
            if ( score < 50) {
                addRandomTarget()
                broadcastGame()
            } else {
                broadcastGame()
                broadcastEndGame(players[session]!!)
            }
        }
    }

    override fun broadcastEndGame(winningPlayer: Player) {
        val time = System.currentTimeMillis() - startTime
        aimKingService.addNewTime(winningPlayer, time)
        webSocketSenderService.broadcast(players.keys, ServerMessageWSType.GAME_END, "Time: ${time / 1000.0} seconds")
    }
}
