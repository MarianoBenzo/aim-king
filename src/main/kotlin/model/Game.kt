package model

import service.GameWebSocketService
import java.util.*
import java.util.Collections.synchronizedList
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

class Game(private val gameWebSocketService: GameWebSocketService){
    val height = 1000
    val width = 2200
    val targets: MutableList<Target> = synchronizedList(mutableListOf())

    init {
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (targets.size < 200) {
                    addRandomTarget()
                }
            }
        }, 0, 500)
    }

    fun addRandomTarget() {
        val radius = 100
        val randomPosition = Position(
            Random.nextInt(width - (radius * 2)) + radius,
            Random.nextInt(height - (radius * 2)) + radius
        )
        val randomTarget = Target(randomPosition, radius)
        targets.add(randomTarget)
        gameWebSocketService.broadcastGame()
    }

    private fun onTarget(position: Position, target: Target): Boolean {
        val distance = sqrt((target.position.x.toDouble() - position.x.toDouble()).pow(2) + (target.position.y.toDouble() - position.y.toDouble()).pow(2))
        return distance <= target.radius
    }

    fun click(position: Position) {
        val target: Target? = targets.findLast { target -> onTarget(position, target) }
        target?.let {
            targets.remove(target)
            //addRandomTarget()
            gameWebSocketService.broadcastGame()
        }
    }
}
