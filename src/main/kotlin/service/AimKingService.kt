package service

import model.*
import model.Target
import org.eclipse.jetty.websocket.api.Session
import java.util.*
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

class AimKingService(private val webSocketSenderService: WebSocketSenderService){

    var ids = AtomicLong(0)
    val players: HashMap<Session, Player> = HashMap()
    var game = Game()

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
        webSocketSenderService.broadcast(players.keys, ServerMessageWSType.GAME, game)
    }

    fun newPlayer(session: Session, name: String?) {
        val id = ids.getAndIncrement()

        var nameUnique = name ?: "Player"
        var i = 2
        while (players.values.any { player -> player.name == nameUnique }) {
            nameUnique = "$name $i"
            ++i
        }

        players[session] = Player(id, nameUnique)
        webSocketSenderService.emit(session, ServerMessageWSType.GAME, game)
    }

    fun addRandomTarget() {
        val radius = 100
        val randomPosition = Position(
            Random.nextInt(game.width - (radius * 2)) + radius,
            Random.nextInt(game.height - (radius * 2)) + radius
        )
        val randomTarget = Target(randomPosition, radius)
        game.targets.add(randomTarget)
    }

    private fun onTarget(position: Position, target: Target): Boolean {
        val distance = sqrt((target.position.x.toDouble() - position.x.toDouble()).pow(2) + (target.position.y.toDouble() - position.y.toDouble()).pow(2))
        return distance <= target.radius
    }

    fun click(position: Position?) {
        position?.let {
            val target: Target? = game.targets.findLast { target -> onTarget(it, target) }
            target?.let {
                game.targets.remove(it)
                addRandomTarget()
                webSocketSenderService.broadcast(players.keys, ServerMessageWSType.GAME, game)
            }
        }
    }
}
