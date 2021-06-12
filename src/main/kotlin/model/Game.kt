package model

import org.eclipse.jetty.websocket.api.Session
import service.AimKingService
import service.WebSocketSenderService
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

abstract class Game(
    protected val aimKingService: AimKingService,
    protected val webSocketSenderService: WebSocketSenderService,
    protected val players: HashMap<Session, Player>
) {
    val targets: MutableList<Target> = Collections.synchronizedList(mutableListOf())
    protected var startTime: Long = 0
    protected val height = aimKingService.height
    protected val width = aimKingService.width

    abstract fun start()

    abstract fun disconnectPlayer(session: Session)

    protected fun broadcastStartGame(startIn: Long) {
        webSocketSenderService.broadcast(players.keys, ServerMessageWSType.GAME_START, startIn)
    }

    protected fun broadcastGame() {
        webSocketSenderService.broadcast(players.keys, ServerMessageWSType.GAME, this)
    }

    protected abstract fun broadcastEndGame(winningPlayer: Player)

    abstract fun click(session: Session, position: Position)

    protected fun addRandomTarget() {
        val radius = 100
        val randomPosition = Position(
            Random.nextInt(width - (radius * 2)) + radius,
            Random.nextInt(height - (radius * 2)) + radius
        )
        val randomTarget = Target(randomPosition, radius)
        targets.add(randomTarget)
    }

    protected fun onTarget(position: Position, target: Target): Boolean {
        val distance = sqrt((target.position.x.toDouble() - position.x.toDouble()).pow(2) + (target.position.y.toDouble() - position.y.toDouble()).pow(2))
        return distance <= target.radius
    }
}
