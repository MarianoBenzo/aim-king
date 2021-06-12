package model

import org.eclipse.jetty.websocket.api.Session
import service.AimKingService
import service.WebSocketSenderService
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
    private val startTime: Long = System.currentTimeMillis()
    private var score: Int = 0

    init {
        /*
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (game.targets.size < 200) {
                    addRandomTarget()
                }
            }
        }, 0, 500)
        */
        addRandomTarget()
        webSocketSenderService.emit(player.first, ServerMessageWSType.GAME, this)
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
                webSocketSenderService.emit(player.first, ServerMessageWSType.GAME, this)
            } else {
                webSocketSenderService.emit(player.first, ServerMessageWSType.GAME, this)
                endGame()
            }
        }
    }

    override fun disconnectPlayer(session: Session) {}

    private fun endGame() {
        val time = System.currentTimeMillis() - startTime
        aimKingService.addNewTime(player.first, time)
        webSocketSenderService.emit(player.first, ServerMessageWSType.GAME_END, "${time / 1000.0} seconds")
    }
}
