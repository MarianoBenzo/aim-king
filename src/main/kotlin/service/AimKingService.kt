package service

import model.*
import org.eclipse.jetty.websocket.api.Session
import java.util.concurrent.atomic.AtomicLong
import kotlin.collections.HashMap

class AimKingService(private val webSocketSenderService: WebSocketSenderService){

    var ids = AtomicLong(0)
    val players: HashMap<String, Player> = HashMap()
    val playersOnline: HashMap<Session, Player> = HashMap()
    val playersInLobby: HashMap<Session, Player> = HashMap()
    val games: HashMap<Session, Game> = HashMap()

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
            val game = Game(this, webSocketSenderService, hashMapOf(Pair(session, it)))
            games[session] = game
        }
    }

    fun newGame2(session: Session) {
        playersOnline[session]?.let {
            playersInLobby[session] = it
            if (playersInLobby.size == 2) {
                val players = HashMap(playersInLobby)
                playersInLobby.clear()
                val game = Game(this, webSocketSenderService, players)
                players.keys.forEach {
                    session ->  games[session] = game
                }
            }
        }
    }

    fun disconnectPlayer(session: Session) {
        playersOnline.remove(session)
        playersInLobby.remove(session)
        games.remove(session)?.let { it.disconnectPlayer(session) }
    }
}
