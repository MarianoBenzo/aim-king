package service

import model.*
import org.eclipse.jetty.websocket.api.Session
import java.util.concurrent.atomic.AtomicLong
import kotlin.collections.HashMap

class AimKingService(private val webSocketSenderService: WebSocketSenderService){

    val height = 1000
    val width = 2200

    var ids = AtomicLong(0)
    val players: HashMap<String, Player> = HashMap()
    val playersOnline: HashMap<Session, Player> = HashMap()
    private val playersInLobby: HashMap<Session, Player> = HashMap()
    val games: HashMap<Session, Game> = HashMap()
    private val ranking: MutableList<Pair<Player, Long>> = mutableListOf()

    fun connectPlayer(session: Session, name: String) {
        players[name]?.let {
            playersOnline[session] = it
        } ?: run {
            val newPlayer = Player(name)
            players[name] = newPlayer
            playersOnline[session] = newPlayer
        }
    }

    fun newGame1(session: Session) {
        playersOnline[session]?.let {
            val game = Game1(this, webSocketSenderService, Pair(session, it))
            games[session] = game
            game.start()
        }
    }

    fun newGame2(session: Session) {
        playersOnline[session]?.let {
            playersInLobby[session] = it
            if (playersInLobby.size == 2) {
                val players = HashMap(playersInLobby)
                playersInLobby.clear()
                val game = Game2(this, webSocketSenderService, players)
                players.keys.forEach {
                    session ->  games[session] = game
                }
                game.start()
            }
        }
    }

    fun addNewTime(player: Player, time: Long) {
        ranking.add(Pair(player, time))
    }

    fun getTop10(): List<Pair<Player, Long>> {
        return ranking.sortedWith(compareBy({ it.second }, { it.first.name }))
    }

    fun disconnectPlayer(session: Session) {
        playersOnline.remove(session)
        playersInLobby.remove(session)
        games.remove(session)?.disconnectPlayer(session)
    }
}
