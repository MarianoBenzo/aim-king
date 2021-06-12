package model

import org.eclipse.jetty.websocket.api.Session
import service.AimKingService
import service.WebSocketSenderService
import java.util.*
import java.util.Collections.synchronizedList
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

class Game2(
    private val aimKingService: AimKingService,
    private val webSocketSenderService: WebSocketSenderService,
    private val players: HashMap<Session, Player>
) : Game {
    val targets: MutableList<Target> = synchronizedList(mutableListOf())
    private val height = aimKingService.height
    private val width = aimKingService.width
    private var startTime: Long = 0
    private val scores: HashMap<Player, Int> = hashMapOf()

    fun start() {
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

    fun addRandomTarget() {
        val radius = 100
        val randomPosition = Position(
            Random.nextInt(width - (radius * 2)) + radius,
            Random.nextInt(height - (radius * 2)) + radius
        )
        val randomTarget = Target(randomPosition, radius)
        targets.add(randomTarget)
    }

    private fun onTarget(position: Position, target: Target): Boolean {
        val distance = sqrt((target.position.x.toDouble() - position.x.toDouble()).pow(2) + (target.position.y.toDouble() - position.y.toDouble()).pow(2))
        return distance <= target.radius
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

    private fun broadcastEndGame(winner: Player) {
        val time = System.currentTimeMillis() - startTime
        webSocketSenderService.broadcast(players.keys, ServerMessageWSType.GAME_END, "Winner: ${winner.name} in ${time / 1000.0} seconds")
    }

    private fun broadcastGame() {
        webSocketSenderService.broadcast(players.keys, ServerMessageWSType.GAME, this)
    }

    private fun broadcastStartGame(startIn: Long) {
        webSocketSenderService.broadcast(players.keys, ServerMessageWSType.GAME_START, startIn)
    }
}
