package model

import org.eclipse.jetty.websocket.api.Session
import service.AimKingService
import service.WebSocketSenderService
import java.util.Collections.synchronizedList
import java.util.HashMap
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
    private val startTime: Long = System.currentTimeMillis()
    private val scores: HashMap<Session, Int> = hashMapOf()

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
        players.keys.forEach { session -> scores[session] = 0 }
        addRandomTarget()
        webSocketSenderService.broadcast(players.keys, ServerMessageWSType.GAME, this)
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
        val target: Target? = targets.findLast { target -> onTarget(position, target) }
        target?.let {
            targets.remove(it)
            addRandomTarget()
            webSocketSenderService.broadcast(players.keys, ServerMessageWSType.GAME, this)
        }
    }
}
