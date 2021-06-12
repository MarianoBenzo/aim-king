package model

import org.eclipse.jetty.websocket.api.Session
import service.AimKingService
import service.WebSocketSenderService
import java.util.*
import java.util.Collections.synchronizedList
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

class Game1 (
    private val aimKingService: AimKingService,
    private val webSocketSenderService: WebSocketSenderService,
    private val player: Pair<Session, Player>
) : Game {
    val targets: MutableList<Target> = synchronizedList(mutableListOf())
    private val height = aimKingService.height
    private val width = aimKingService.width
    private var startTime: Long = 0
    private var score: Int = 0

    fun start() {
        val startIn: Long = 3000
        emitStartGame(startIn)
        Timer().schedule(object : TimerTask() {
            override fun run() {
                startTime = System.currentTimeMillis()
                addRandomTarget()
                emitGame()
            }
        }, startIn)
    }

    private fun addRandomTarget() {
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
        val target: Target? = targets.findLast { target -> onTarget(position, target) }
        target?.let {
            targets.remove(it)
            score += 1
            if ( score < 3) {
                addRandomTarget()
                emitGame()
            } else {
                emitGame()
                emitEndGame()
            }
        }
    }

    override fun disconnectPlayer(session: Session) {}

    private fun emitEndGame() {
        val time = System.currentTimeMillis() - startTime
        aimKingService.addNewTime(player.first, time)
        webSocketSenderService.emit(player.first, ServerMessageWSType.GAME_END, "Time: ${time / 1000.0} seconds")
    }

    private fun emitGame() {
        webSocketSenderService.emit(player.first, ServerMessageWSType.GAME, this)
    }

    private fun emitStartGame(startIn: Long) {
        webSocketSenderService.emit(player.first, ServerMessageWSType.GAME_START, startIn)
    }
}
