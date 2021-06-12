package model

class MessageWS(val type: String, val data: String?) {
    constructor(type: ServerMessageWSType, data: String?) : this(type.name, data)
}

enum class ClientMessageWSType {
    PING,
    NEW_PLAYER,
    NEW_GAME1,
    NEW_GAME2,
    CLICK
}

enum class ServerMessageWSType {
    PONG,
    GAME,
    GAME_END
}
