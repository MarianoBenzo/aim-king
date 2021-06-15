package game

import model.*
import model.Target
import org.eclipse.jetty.websocket.api.Session
import service.AimKingService
import service.WebSocketSenderService
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

abstract class GameBase(
    protected val aimKingService: AimKingService,
    protected val webSocketSenderService: WebSocketSenderService,
    protected val players: HashMap<Session, Player>
) {
    protected val targets: MutableList<Target> = Collections.synchronizedList(mutableListOf())
    protected val cursors: HashMap<Session, Cursor> = hashMapOf()
    protected var startTime: Long = 0
    protected val height = aimKingService.height
    protected val width = aimKingService.width

    abstract fun start()

    abstract fun disconnectPlayer(session: Session)

    protected fun broadcastStartGame(startIn: Long) {
        webSocketSenderService.broadcast(players.keys, ServerMessageWSType.GAME_START, startIn)
    }

    protected fun broadcastGame(session: Session, clientMessageWSType: ClientMessageWSType?) {
        when (clientMessageWSType?.name) {
            ClientMessageWSType.MOUSE_MOVE.name -> {
                players.keys.filter { it !== session }.forEach {
                    val game = Game(targets, cursors, it)
                    webSocketSenderService.emit(it, ServerMessageWSType.GAME, game)
                }
            }
            else -> {
                players.keys.forEach {
                    val game = Game(targets, cursors, it)
                    webSocketSenderService.emit(it, ServerMessageWSType.GAME, game)
                }
            }
        }
    }

    protected abstract fun broadcastEndGame(winningPlayer: Player)

    abstract fun mouseClick(session: Session, position: Position)

    fun mouseMove(session: Session, position: Position) {
        cursors[session] = Cursor(position)
        broadcastGame(session, ClientMessageWSType.MOUSE_MOVE)
    }

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
        val distance = sqrt(
            (target.position.x.toDouble() - position.x.toDouble()).pow(2) +
                    (target.position.y.toDouble() - position.y.toDouble()).pow(2)
        )
        return distance <= target.radius
    }
}
