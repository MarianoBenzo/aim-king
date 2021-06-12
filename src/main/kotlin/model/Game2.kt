package model

import org.eclipse.jetty.websocket.api.Session
import service.AimKingService
import service.WebSocketSenderService
import java.util.*

class Game2(
    aimKingService: AimKingService,
    webSocketSenderService: WebSocketSenderService,
    players: HashMap<Session, Player>
) : Game(
    aimKingService, webSocketSenderService, players
) {
    private val scores: HashMap<Player, Int> = hashMapOf()

    override fun start() {
        players.values.forEach { player -> scores[player] = 0 }
        val startIn: Long = 3000
        broadcastStartGame(startIn)
        Timer().schedule(object : TimerTask() {
            override fun run() {
                startTime = System.currentTimeMillis()
                addRandomTarget()
                addRandomTarget()
                broadcastGame()
            }
        }, startIn)
    }

    override fun disconnectPlayer(session: Session) {
        players.remove(session)
    }

    override fun click(session: Session, position: Position) {
        players[session]?.let { player ->
            targets.findLast {
                    target -> onTarget(position, target)
            }?.let { target ->
                targets.remove(target)
                scores[player] = scores[player]?.plus(1) ?: 0
                if (scores[player]!! < 50) {
                    addRandomTarget()
                    broadcastGame()
                } else {
                    targets.clear()
                    broadcastGame()
                    broadcastEndGame(player)
                }
            }
        }
    }

    override fun broadcastEndGame(winningPlayer: Player) {
        val time = System.currentTimeMillis() - startTime
        webSocketSenderService.broadcast(players.keys, ServerMessageWSType.GAME_END, "Winner: ${winningPlayer.name} in ${time / 1000.0} seconds")
    }
}
